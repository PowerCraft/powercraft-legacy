package cpw.mods.fml.common;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;

import net.minecraft.src.CallableMinecraftVersion;

import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.Multisets;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets.SetView;
import com.google.common.collect.TreeMultimap;

import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.discovery.ModDiscoverer;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLLoadEvent;
import cpw.mods.fml.common.functions.ModIdFunction;
import cpw.mods.fml.common.modloader.BaseModProxy;
import cpw.mods.fml.common.toposort.ModSorter;
import cpw.mods.fml.common.toposort.ModSortingException;
import cpw.mods.fml.common.toposort.TopologicalSort;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;

public class Loader
{
    private static final Splitter DEPENDENCYPARTSPLITTER = Splitter.on(":").omitEmptyStrings().trimResults();
    private static final Splitter DEPENDENCYSPLITTER = Splitter.on(";").omitEmptyStrings().trimResults();

    private static Loader instance;

    private static String major;
    private static String minor;
    private static String rev;
    private static String build;
    private static String mccversion;
    private static String mcpversion;

    private ModClassLoader modClassLoader;

    private List<ModContainer> mods;

    private Map<String, ModContainer> namedMods;

    private File canonicalConfigDir;

    private File canonicalMinecraftDir;

    private Exception capturedError;
    private File canonicalModsDir;
    private LoadController modController;
    private MinecraftDummyContainer minecraft;
    private MCPDummyContainer mcp;

    private static File minecraftDir;
    private static List<String> injectedContainers;

    public static Loader instance()
    {
        if (instance == null)
        {
            instance = new Loader();
        }

        return instance;
    }

    public static void injectData(Object... data)
    {
        major = (String) data[0];
        minor = (String) data[1];
        rev = (String) data[2];
        build = (String) data[3];
        mccversion = (String) data[4];
        mcpversion = (String) data[5];
        minecraftDir = (File) data[6];
        injectedContainers = (List<String>)data[7];
    }

    private Loader()
    {
        modClassLoader = new ModClassLoader(getClass().getClassLoader());
        String actualMCVersion = new CallableMinecraftVersion(null).minecraftVersion();

        if (!mccversion.equals(actualMCVersion))
        {
            FMLLog.severe("This version of FML is built for Minecraft %s, we have detected Minecraft %s in your minecraft jar file", mccversion, actualMCVersion);
            throw new LoaderException();
        }

        minecraft = new MinecraftDummyContainer(actualMCVersion);
        mcp = new MCPDummyContainer(MetadataCollection.from(getClass().getResourceAsStream("/mcpmod.info"), "MCP").getMetadataForId("mcp", null));
    }

    private void sortModList()
    {
        FMLLog.fine("Verifying mod requirements are satisfied");

        try
        {
            BiMap<String, ArtifactVersion> modVersions = HashBiMap.create();

            for (ModContainer mod : getActiveModList())
            {
                modVersions.put(mod.getModId(), mod.getProcessedVersion());
            }

            for (ModContainer mod : getActiveModList())
            {
                if (!mod.acceptableMinecraftVersionRange().containsVersion(minecraft.getProcessedVersion()))
                {
                    FMLLog.severe("The mod %s does not wish to run in Minecraft version %s. You will have to remove it to play.", mod.getModId(), getMCVersionString());
                    throw new WrongMinecraftVersionException(mod);
                }

                Map<String, ArtifactVersion> names = Maps.uniqueIndex(mod.getRequirements(), new Function<ArtifactVersion, String>()
                {
                    public String apply(ArtifactVersion v)
                    {
                        return v.getLabel();
                    }
                });
                Set<ArtifactVersion> versionMissingMods = Sets.newHashSet();
                Set<String> missingMods = Sets.difference(names.keySet(), modVersions.keySet());

                if (!missingMods.isEmpty())
                {
                    FMLLog.severe("The mod %s (%s) requires mods %s to be available", mod.getModId(), mod.getName(), missingMods);

                    for (String modid : missingMods)
                    {
                        versionMissingMods.add(names.get(modid));
                    }

                    throw new MissingModsException(versionMissingMods);
                }

                ImmutableList<ArtifactVersion> allDeps = ImmutableList.<ArtifactVersion>builder().addAll(mod.getDependants()).addAll(mod.getDependencies()).build();

                for (ArtifactVersion v : allDeps)
                {
                    if (modVersions.containsKey(v.getLabel()))
                    {
                        if (!v.containsVersion(modVersions.get(v.getLabel())))
                        {
                            versionMissingMods.add(v);
                        }
                    }
                }

                if (!versionMissingMods.isEmpty())
                {
                    FMLLog.severe("The mod %s (%s) requires mod versions %s to be available", mod.getModId(), mod.getName(), versionMissingMods);
                    throw new MissingModsException(versionMissingMods);
                }
            }

            FMLLog.fine("All mod requirements are satisfied");
            ModSorter sorter = new ModSorter(getActiveModList(), namedMods);

            try
            {
                FMLLog.fine("Sorting mods into an ordered list");
                List<ModContainer> sortedMods = sorter.sort();
                modController.getActiveModList().clear();
                modController.getActiveModList().addAll(sortedMods);
                mods.removeAll(sortedMods);
                sortedMods.addAll(mods);
                mods = sortedMods;
                FMLLog.fine("Mod sorting completed successfully");
            }
            catch (ModSortingException sortException)
            {
                FMLLog.severe("A dependency cycle was detected in the input mod set so an ordering cannot be determined");
                FMLLog.severe("The visited mod list is %s", sortException.getExceptionData().getVisitedNodes());
                FMLLog.severe("The first mod in the cycle is %s", sortException.getExceptionData().getFirstBadNode());
                FMLLog.log(Level.SEVERE, sortException, "The full error");
                throw new LoaderException(sortException);
            }
        }
        finally
        {
            FMLLog.fine("Mod sorting data:");

            for (ModContainer mod : getActiveModList())
            {
                if (!mod.isImmutable())
                {
                    FMLLog.fine("\t%s(%s:%s): %s (%s)", mod.getModId(), mod.getName(), mod.getVersion(), mod.getSource().getName(), mod.getSortingRules());
                }
            }

            if (mods.size() == 0)
            {
                FMLLog.fine("No mods found to sort");
            }
        }
    }

    private ModDiscoverer identifyMods()
    {
        FMLLog.fine("Building injected Mod Containers %s", injectedContainers);
        mods.add(new InjectedModContainer(mcp, new File("minecraft.jar")));
        File coremod = new File(minecraftDir, "coremods");

        for (String cont : injectedContainers)
        {
            ModContainer mc;

            try
            {
                mc = (ModContainer) Class.forName(cont, true, modClassLoader).newInstance();
            }
            catch (Exception e)
            {
                FMLLog.log(Level.SEVERE, e, "A problem occured instantiating the injected mod container %s", cont);
                throw new LoaderException(e);
            }

            mods.add(new InjectedModContainer(mc, coremod));
        }

        ModDiscoverer discoverer = new ModDiscoverer();
        FMLLog.fine("Attempting to load mods contained in the minecraft jar file and associated classes");
        discoverer.findClasspathMods(modClassLoader);
        FMLLog.fine("Minecraft jar mods loaded successfully");
        FMLLog.info("Searching %s for mods", canonicalModsDir.getAbsolutePath());
        discoverer.findModDirMods(canonicalModsDir);
        mods.addAll(discoverer.identifyMods());
        identifyDuplicates(mods);
        namedMods = Maps.uniqueIndex(mods, new ModIdFunction());
        FMLLog.info("Forge Mod Loader has identified %d mod%s to load", mods.size(), mods.size() != 1 ? "s" : "");
        return discoverer;
    }

    private class ModIdComparator implements Comparator<ModContainer>
    {
        @Override
        public int compare(ModContainer o1, ModContainer o2)
        {
            return o1.getModId().compareTo(o2.getModId());
        }
    }

    private void identifyDuplicates(List<ModContainer> mods)
    {
        TreeMultimap<ModContainer, File> dupsearch = TreeMultimap.create(new ModIdComparator(), Ordering.arbitrary());

        for (ModContainer mc : mods)
        {
            if (mc.getSource() != null)
            {
                dupsearch.put(mc, mc.getSource());
            }
        }

        ImmutableMultiset<ModContainer> duplist = Multisets.copyHighestCountFirst(dupsearch.keys());
        SetMultimap<ModContainer, File> dupes = LinkedHashMultimap.create();

        for (Entry<ModContainer> e : duplist.entrySet())
        {
            if (e.getCount() > 1)
            {
                FMLLog.severe("Found a duplicate mod %s at %s", e.getElement().getModId(), dupsearch.get(e.getElement()));
                dupes.putAll(e.getElement(), dupsearch.get(e.getElement()));
            }
        }

        if (!dupes.isEmpty())
        {
            throw new DuplicateModsFoundException(dupes);
        }
    }

    private void initializeLoader()
    {
        File modsDir = new File(minecraftDir, "mods");
        File configDir = new File(minecraftDir, "config");
        String canonicalModsPath;
        String canonicalConfigPath;

        try
        {
            canonicalMinecraftDir = minecraftDir.getCanonicalFile();
            canonicalModsPath = modsDir.getCanonicalPath();
            canonicalConfigPath = configDir.getCanonicalPath();
            canonicalConfigDir = configDir.getCanonicalFile();
            canonicalModsDir = modsDir.getCanonicalFile();
        }
        catch (IOException ioe)
        {
            FMLLog.log(Level.SEVERE, ioe, "Failed to resolve loader directories: mods : %s ; config %s", canonicalModsDir.getAbsolutePath(),
                    configDir.getAbsolutePath());
            throw new LoaderException(ioe);
        }

        if (!canonicalModsDir.exists())
        {
            FMLLog.info("No mod directory found, creating one: %s", canonicalModsPath);
            boolean dirMade = canonicalModsDir.mkdir();

            if (!dirMade)
            {
                FMLLog.severe("Unable to create the mod directory %s", canonicalModsPath);
                throw new LoaderException();
            }

            FMLLog.info("Mod directory created successfully");
        }

        if (!canonicalConfigDir.exists())
        {
            FMLLog.fine("No config directory found, creating one: %s", canonicalConfigPath);
            boolean dirMade = canonicalConfigDir.mkdir();

            if (!dirMade)
            {
                FMLLog.severe("Unable to create the config directory %s", canonicalConfigPath);
                throw new LoaderException();
            }

            FMLLog.info("Config directory created successfully");
        }

        if (!canonicalModsDir.isDirectory())
        {
            FMLLog.severe("Attempting to load mods from %s, which is not a directory", canonicalModsPath);
            throw new LoaderException();
        }

        if (!configDir.isDirectory())
        {
            FMLLog.severe("Attempting to load configuration from %s, which is not a directory", canonicalConfigPath);
            throw new LoaderException();
        }
    }

    public List<ModContainer> getModList()
    {
        return instance().mods != null ? ImmutableList.copyOf(instance().mods) : ImmutableList.<ModContainer>of();
    }

    public void loadMods()
    {
        initializeLoader();
        mods = Lists.newArrayList();
        namedMods = Maps.newHashMap();
        modController = new LoadController(this);
        modController.transition(LoaderState.LOADING);
        ModDiscoverer disc = identifyMods();
        disableRequestedMods();
        modController.distributeStateMessage(FMLLoadEvent.class);
        sortModList();
        mods = ImmutableList.copyOf(mods);

        for (File nonMod : disc.getNonModLibs())
        {
            if (nonMod.isFile())
            {
                FMLLog.info("FML has found a non-mod file %s in your mods directory. It will now be injected into your classpath. This could severe stability issues, it should be removed if possible.", nonMod.getName());

                try
                {
                    modClassLoader.addFile(nonMod);
                }
                catch (MalformedURLException e)
                {
                    FMLLog.log(Level.SEVERE, e, "Encountered a weird problem with non-mod file injection : %s", nonMod.getName());
                }
            }
        }

        modController.transition(LoaderState.CONSTRUCTING);
        modController.distributeStateMessage(LoaderState.CONSTRUCTING, modClassLoader, disc.getASMTable());
        modController.transition(LoaderState.PREINITIALIZATION);
        modController.distributeStateMessage(LoaderState.PREINITIALIZATION, disc.getASMTable(), canonicalConfigDir);
        modController.transition(LoaderState.INITIALIZATION);
    }

    private void disableRequestedMods()
    {
        String forcedModList = System.getProperty("fml.modStates", "");
        FMLLog.fine("Received a system property request \'%s\'", forcedModList);
        Map<String, String> sysPropertyStateList = Splitter.on(CharMatcher.anyOf(";:"))
                .omitEmptyStrings().trimResults().withKeyValueSeparator("=")
                .split(forcedModList);
        FMLLog.fine("System property request managing the state of %d mods", sysPropertyStateList.size());
        Map<String, String> modStates = Maps.newHashMap();
        File forcedModFile = new File(canonicalConfigDir, "fmlModState.properties");
        Properties forcedModListProperties = new Properties();

        if (forcedModFile.exists() && forcedModFile.isFile())
        {
            FMLLog.fine("Found a mod state file %s", forcedModFile.getName());

            try
            {
                forcedModListProperties.load(new FileReader(forcedModFile));
                FMLLog.fine("Loaded states for %d mods from file", forcedModListProperties.size());
            }
            catch (Exception e)
            {
                FMLLog.log(Level.INFO, e, "An error occurred reading the fmlModState.properties file");
            }
        }

        modStates.putAll(Maps.fromProperties(forcedModListProperties));
        modStates.putAll(sysPropertyStateList);
        FMLLog.fine("After merging, found state information for %d mods", modStates.size());
        Map<String, Boolean> isEnabled = Maps.transformValues(modStates, new Function<String, Boolean>()
        {
            public Boolean apply(String input)
            {
                return Boolean.parseBoolean(input);
            }
        });

        for (Map.Entry<String, Boolean> entry : isEnabled.entrySet())
        {
            if (namedMods.containsKey(entry.getKey()))
            {
                FMLLog.info("Setting mod %s to enabled state %b", entry.getKey(), entry.getValue());
                namedMods.get(entry.getKey()).setEnabledState(entry.getValue());
            }
        }
    }

    public static boolean isModLoaded(String modname)
    {
        return instance().namedMods.containsKey(modname) && instance().modController.getModState(instance.namedMods.get(modname)) != ModState.DISABLED;
    }

    public File getConfigDir()
    {
        return canonicalConfigDir;
    }

    public String getCrashInformation()
    {
        StringBuilder ret = new StringBuilder();
        List<String> branding = FMLCommonHandler.instance().getBrandings();
        Joiner.on(' ').skipNulls().appendTo(ret, branding.subList(1, branding.size()));

        if (modController != null)
        {
            modController.printModStates(ret);
        }

        return ret.toString();
    }

    public String getFMLVersionString()
    {
        return String.format("%s.%s.%s.%s", major, minor, rev, build);
    }

    public ClassLoader getModClassLoader()
    {
        return modClassLoader;
    }

    public void computeDependencies(String dependencyString, Set<ArtifactVersion> requirements, List<ArtifactVersion> dependencies, List<ArtifactVersion> dependants)
    {
        if (dependencyString == null || dependencyString.length() == 0)
        {
            return;
        }

        boolean parseFailure = false;

        for (String dep : DEPENDENCYSPLITTER.split(dependencyString))
        {
            List<String> depparts = Lists.newArrayList(DEPENDENCYPARTSPLITTER.split(dep));

            if (depparts.size() != 2)
            {
                parseFailure = true;
                continue;
            }

            String instruction = depparts.get(0);
            String target = depparts.get(1);
            boolean targetIsAll = target.startsWith("*");

            if (targetIsAll && target.length() > 1)
            {
                parseFailure = true;
                continue;
            }

            if ("required-before".equals(instruction) || "required-after".equals(instruction))
            {
                if (!targetIsAll)
                {
                    requirements.add(VersionParser.parseVersionReference(target));
                }
                else
                {
                    parseFailure = true;
                    continue;
                }
            }

            if (targetIsAll && target.indexOf('@') > -1)
            {
                parseFailure = true;
                continue;
            }

            if ("required-before".equals(instruction) || "before".equals(instruction))
            {
                dependants.add(VersionParser.parseVersionReference(target));
            }
            else if ("required-after".equals(instruction) || "after".equals(instruction))
            {
                dependencies.add(VersionParser.parseVersionReference(target));
            }
            else
            {
                parseFailure = true;
            }
        }

        if (parseFailure)
        {
            FMLLog.log(Level.WARNING, "Unable to parse dependency string %s", dependencyString);
            throw new LoaderException();
        }
    }

    public Map<String, ModContainer> getIndexedModList()
    {
        return ImmutableMap.copyOf(namedMods);
    }

    public void initializeMods()
    {
        modController.distributeStateMessage(LoaderState.INITIALIZATION);
        modController.transition(LoaderState.POSTINITIALIZATION);
        modController.distributeStateMessage(FMLInterModComms.IMCEvent.class);
        modController.distributeStateMessage(LoaderState.POSTINITIALIZATION);
        modController.transition(LoaderState.AVAILABLE);
        modController.distributeStateMessage(LoaderState.AVAILABLE);
        FMLLog.info("Forge Mod Loader has successfully loaded %d mod%s", mods.size(), mods.size() == 1 ? "" : "s");
    }

    public ICrashCallable getCallableCrashInformation()
    {
        return new ICrashCallable()
        {
            @Override
            public String call() throws Exception
            {
                return getCrashInformation();
            }
            @Override
            public String getLabel()
            {
                return "FML";
            }
        };
    }

    public List<ModContainer> getActiveModList()
    {
        return modController != null ? modController.getActiveModList() : ImmutableList.<ModContainer>of();
    }

    public ModState getModState(ModContainer selectedMod)
    {
        return modController.getModState(selectedMod);
    }

    public String getMCVersionString()
    {
        return "Minecraft " + mccversion;
    }

    public void serverStarting(Object server)
    {
        modController.distributeStateMessage(LoaderState.SERVER_STARTING, server);
        modController.transition(LoaderState.SERVER_STARTING);
    }

    public void serverStarted()
    {
        modController.distributeStateMessage(LoaderState.SERVER_STARTED);
        modController.transition(LoaderState.SERVER_STARTED);
    }

    public void serverStopping()
    {
        modController.distributeStateMessage(LoaderState.SERVER_STOPPING);
        modController.transition(LoaderState.SERVER_STOPPING);
        modController.transition(LoaderState.AVAILABLE);
    }

    public BiMap<ModContainer, Object> getModObjectList()
    {
        return modController.getModObjectList();
    }

    public BiMap<Object, ModContainer> getReversedModObjectList()
    {
        return getModObjectList().inverse();
    }

    public ModContainer activeModContainer()
    {
        return modController.activeContainer();
    }

    public boolean isInState(LoaderState state)
    {
        return modController.isInState(state);
    }

    public MinecraftDummyContainer getMinecraftModContainer()
    {
        return minecraft;
    }

    public boolean hasReachedState(LoaderState state)
    {
        return modController.hasReachedState(state);
    }

    public String getMCPVersionString()
    {
        return String.format("MCP v%s", mcpversion);
    }
}
