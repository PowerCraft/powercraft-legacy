package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public final class ModLoader
{
    private static final Map blockModels = new HashMap();
    private static final Map blockSpecialInv = new HashMap();
    private static final File cfgdir = new File(Minecraft.getMinecraftDir(), "/config/");
    private static final File cfgfile = new File(cfgdir, "ModLoader.cfg");
    public static Level cfgLoggingLevel = Level.FINER;
    private static Map classMap = null;
    private static long clock = 0L;
    private static Field field_armorList = null;
    private static Field field_modifiers = null;
    private static Field field_TileEntityRenderers = null;
    private static boolean hasInit = false;
    private static final Map inGameHooks = new HashMap();
    private static final Map inGUIHooks = new HashMap();
    private static Minecraft instance = null;
    private static final Map keyList = new HashMap();
    private static String langPack = null;
    private static Map localizedStrings = new HashMap();
    private static final File logfile = new File(Minecraft.getMinecraftDir(), "ModLoader.txt");
    private static final Logger logger = Logger.getLogger("ModLoader");
    private static FileHandler logHandler = null;
    private static Method method_RegisterEntityID = null;
    private static Method method_RegisterTileEntity = null;
    private static final File modDir = new File(Minecraft.getMinecraftDir(), "/mods/");
    private static final LinkedList modList = new LinkedList();
    private static int nextBlockModelID = 1000;
    private static final Map overrides = new HashMap();
    private static final Map packetChannels = new HashMap();
    public static final Properties props = new Properties();
    private static BiomeGenBase[] standardBiomes;
    public static final String VERSION = "ModLoader 1.5.2";
    private static NetClientHandler clientHandler = null;
    private static final List commandList = new LinkedList();
    private static final Map tradeItems = new HashMap();
    private static final Map containerGUIs = new HashMap();
    private static final Map trackers = new HashMap();
    private static final Map dispenserBehaviors = new HashMap();
    private static final Map customTextures = new HashMap();
    private static SoundPool soundPoolSounds;
    private static SoundPool soundPoolStreaming;
    private static SoundPool soundPoolMusic;

    public static void addAchievementDesc(Achievement var0, String var1, String var2)
    {
        try
        {
            if (var0.getName().contains("."))
            {
                String[] var3 = var0.getName().split("\\.");

                if (var3.length == 2)
                {
                    String var4 = var3[1];
                    addLocalization("achievement." + var4, var1);
                    addLocalization("achievement." + var4 + ".desc", var2);
                    setPrivateValue(StatBase.class, var0, 1, StatCollector.translateToLocal("achievement." + var4));
                    setPrivateValue(Achievement.class, var0, 3, StatCollector.translateToLocal("achievement." + var4 + ".desc"));
                }
                else
                {
                    setPrivateValue(StatBase.class, var0, 1, var1);
                    setPrivateValue(Achievement.class, var0, 3, var2);
                }
            }
            else
            {
                setPrivateValue(StatBase.class, var0, 1, var1);
                setPrivateValue(Achievement.class, var0, 3, var2);
            }
        }
        catch (IllegalArgumentException var5)
        {
            logger.throwing("ModLoader", "AddAchievementDesc", var5);
            throwException(var5);
        }
        catch (SecurityException var6)
        {
            logger.throwing("ModLoader", "AddAchievementDesc", var6);
            throwException(var6);
        }
        catch (NoSuchFieldException var7)
        {
            logger.throwing("ModLoader", "AddAchievementDesc", var7);
            throwException(var7);
        }
    }

    public static void addEntityTracker(BaseMod var0, Class var1, int var2, int var3, int var4, boolean var5)
    {
        if (var1 == null)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            if (!Entity.class.isAssignableFrom(var1))
            {
                Exception var6 = new Exception(var1.getCanonicalName() + " is not an entity.");
                logger.throwing("ModLoader", "addEntityTracker", var6);
                throwException(var6);
            }

            trackers.put(var1, new EntityTrackerNonliving(var0, var1, var2, var3, var4, var5));
        }
    }

    public static Map getTrackers()
    {
        return Collections.unmodifiableMap(trackers);
    }

    public static int addAllFuel(int var0, int var1)
    {
        logger.finest("Finding fuel for " + var0);
        int var2 = 0;

        for (Iterator var3 = modList.iterator(); var3.hasNext() && var2 == 0; var2 = ((BaseMod)var3.next()).addFuel(var0, var1))
        {
            ;
        }

        if (var2 != 0)
        {
            logger.finest("Returned " + var2);
        }

        return var2;
    }

    public static void addAllRenderers(Map var0)
    {
        if (!hasInit)
        {
            init();
            logger.fine("Initialized");
        }

        Iterator var1 = modList.iterator();

        while (var1.hasNext())
        {
            BaseMod var2 = (BaseMod)var1.next();
            var2.addRenderer(var0);
        }
    }

    public static int addArmor(String var0)
    {
        try
        {
            String[] var1 = (String[])((String[])field_armorList.get((Object)null));
            List var2 = Arrays.asList(var1);
            ArrayList var3 = new ArrayList();
            var3.addAll(var2);

            if (!var3.contains(var0))
            {
                var3.add(var0);
            }

            int var4 = var3.indexOf(var0);
            field_armorList.set((Object)null, var3.toArray(new String[0]));
            return var4;
        }
        catch (IllegalArgumentException var5)
        {
            logger.throwing("ModLoader", "AddArmor", var5);
            throwException("An impossible error has occured!", var5);
        }
        catch (IllegalAccessException var6)
        {
            logger.throwing("ModLoader", "AddArmor", var6);
            throwException("An impossible error has occured!", var6);
        }

        return -1;
    }

    public static void addBiome(BiomeGenBase var0)
    {
        BiomeGenBase[] var1 = GenLayerBiome.biomeArray;
        List var2 = Arrays.asList(var1);
        ArrayList var3 = new ArrayList();
        var3.addAll(var2);

        if (!var3.contains(var0))
        {
            var3.add(var0);
        }

        GenLayerBiome.biomeArray = (BiomeGenBase[])((BiomeGenBase[])var3.toArray(new BiomeGenBase[0]));
    }

    public static void addCommand(ICommand var0)
    {
        commandList.add(var0);
    }

    public static void addDispenserBehavior(Item var0, IBehaviorDispenseItem var1)
    {
        dispenserBehaviors.put(var0, var1);
    }

    public static void registerServer(MinecraftServer var0)
    {
        ICommandManager var1 = var0.getCommandManager();

        if (var1 instanceof CommandHandler)
        {
            CommandHandler var2 = (CommandHandler)var1;
            Iterator var3 = commandList.iterator();

            while (var3.hasNext())
            {
                ICommand var4 = (ICommand)var3.next();
                var2.registerCommand(var4);
            }

            var3 = dispenserBehaviors.entrySet().iterator();

            while (var3.hasNext())
            {
                Entry var5 = (Entry)var3.next();
                BlockDispenser.dispenseBehaviorRegistry.putObject(var5.getKey(), var5.getValue());
            }
        }
    }

    public static void addLocalization(String var0, String var1)
    {
        addLocalization(var0, "en_US", var1);
    }

    public static void addLocalization(String var0, String var1, String var2)
    {
        Object var3;

        if (localizedStrings.containsKey(var1))
        {
            var3 = (Map)localizedStrings.get(var1);
        }
        else
        {
            var3 = new HashMap();
            localizedStrings.put(var1, var3);
        }

        ((Map)var3).put(var0, var2);
    }

    public static void addTrade(int var0, TradeEntry var1)
    {
        Object var2 = null;

        if (tradeItems.containsKey(Integer.valueOf(var0)))
        {
            var2 = (List)tradeItems.get(Integer.valueOf(var0));
        }
        else
        {
            var2 = new LinkedList();
            tradeItems.put(Integer.valueOf(var0), var2);
        }

        ((List)var2).add(var1);
    }

    public static List getTrades(int var0)
    {
        if (var0 != -1)
        {
            return tradeItems.containsKey(Integer.valueOf(var0)) ? Collections.unmodifiableList((List)tradeItems.get(Integer.valueOf(var0))) : null;
        }
        else
        {
            LinkedList var1 = new LinkedList();
            Iterator var2 = tradeItems.values().iterator();

            while (var2.hasNext())
            {
                List var3 = (List)var2.next();
                var1.addAll(var3);
            }

            return var1;
        }
    }

    private static void addMod(ClassLoader var0, String var1)
    {
        try
        {
            String var2 = var1.split("\\.")[0];

            if (var2.contains("$"))
            {
                return;
            }

            if (props.containsKey(var2) && (props.getProperty(var2).equalsIgnoreCase("no") || props.getProperty(var2).equalsIgnoreCase("off")))
            {
                return;
            }

            Package var3 = ModLoader.class.getPackage();

            if (var3 != null)
            {
                var2 = var3.getName() + "." + var2;
            }

            Class var4 = var0.loadClass(var2);

            if (!BaseMod.class.isAssignableFrom(var4))
            {
                return;
            }

            setupProperties(var4);
            BaseMod var5 = (BaseMod)var4.newInstance();

            if (var5 != null)
            {
                modList.add(var5);
                logger.fine("Mod Initialized: \"" + var5.toString() + "\" from " + var1);
                System.out.println("Mod Initialized: " + var5.toString());
            }
        }
        catch (Throwable var6)
        {
            logger.fine("Failed to load mod from \"" + var1 + "\"");
            System.out.println("Failed to load mod from \"" + var1 + "\"");
            logger.throwing("ModLoader", "addMod", var6);
            throwException(var6);
        }
    }

    public static void addName(Object var0, String var1)
    {
        addName(var0, "en_US", var1);
    }

    public static void addName(Object var0, String var1, String var2)
    {
        String var3 = null;
        Exception var8;

        if (var0 instanceof Item)
        {
            Item var4 = (Item)var0;

            if (var4.getUnlocalizedName() != null)
            {
                var3 = var4.getUnlocalizedName() + ".name";
            }
        }
        else if (var0 instanceof Block)
        {
            Block var6 = (Block)var0;

            if (var6.getUnlocalizedName() != null)
            {
                var3 = var6.getUnlocalizedName() + ".name";
            }
        }
        else if (var0 instanceof ItemStack)
        {
            ItemStack var7 = (ItemStack)var0;
            String var5 = Item.itemsList[var7.itemID].getUnlocalizedName(var7);

            if (var5 != null)
            {
                var3 = var5 + ".name";
            }
        }
        else
        {
            var8 = new Exception(var0.getClass().getName() + " cannot have name attached to it!");
            logger.throwing("ModLoader", "AddName", var8);
            throwException(var8);
        }

        if (var3 != null)
        {
            addLocalization(var3, var1, var2);
        }
        else
        {
            var8 = new Exception(var0 + " is missing name tag!");
            logger.throwing("ModLoader", "AddName", var8);
            throwException(var8);
        }
    }

    public static void addRecipe(ItemStack var0, Object ... var1)
    {
        CraftingManager.getInstance().addRecipe(var0, var1);
    }

    public static void addShapelessRecipe(ItemStack var0, Object ... var1)
    {
        CraftingManager.getInstance().addShapelessRecipe(var0, var1);
    }

    public static void addSmelting(int var0, ItemStack var1, float var2)
    {
        FurnaceRecipes.smelting().addSmelting(var0, var1, var2);
    }

    public static void addSpawn(Class var0, int var1, int var2, int var3, EnumCreatureType var4)
    {
        addSpawn(var0, var1, var2, var3, var4, (BiomeGenBase[])null);
    }

    public static void addSpawn(Class var0, int var1, int var2, int var3, EnumCreatureType var4, BiomeGenBase[] var5)
    {
        if (var0 == null)
        {
            throw new IllegalArgumentException("entityClass cannot be null");
        }
        else if (var4 == null)
        {
            throw new IllegalArgumentException("spawnList cannot be null");
        }
        else
        {
            if (var5 == null)
            {
                var5 = standardBiomes;
            }

            for (int var6 = 0; var6 < var5.length; ++var6)
            {
                List var7 = var5[var6].getSpawnableList(var4);

                if (var7 != null)
                {
                    boolean var8 = false;
                    Iterator var9 = var7.iterator();

                    while (var9.hasNext())
                    {
                        SpawnListEntry var10 = (SpawnListEntry)var9.next();

                        if (var10.entityClass == var0)
                        {
                            var10.itemWeight = var1;
                            var10.minGroupCount = var2;
                            var10.maxGroupCount = var3;
                            var8 = true;
                            break;
                        }
                    }

                    if (!var8)
                    {
                        var7.add(new SpawnListEntry(var0, var1, var2, var3));
                    }
                }
            }
        }
    }

    public static void addSpawn(String var0, int var1, int var2, int var3, EnumCreatureType var4)
    {
        addSpawn(var0, var1, var2, var3, var4, (BiomeGenBase[])null);
    }

    public static void addSpawn(String var0, int var1, int var2, int var3, EnumCreatureType var4, BiomeGenBase[] var5)
    {
        Class var6 = (Class)classMap.get(var0);

        if (var6 != null && EntityLiving.class.isAssignableFrom(var6))
        {
            addSpawn(var6, var1, var2, var3, var4, var5);
        }
    }

    public static void genericContainerRemoval(World var0, int var1, int var2, int var3)
    {
        IInventory var4 = (IInventory)var0.getBlockTileEntity(var1, var2, var3);

        if (var4 != null)
        {
            for (int var5 = 0; var5 < var4.getSizeInventory(); ++var5)
            {
                ItemStack var6 = var4.getStackInSlot(var5);

                if (var6 != null)
                {
                    double var7 = var0.rand.nextDouble() * 0.8D + 0.1D;
                    double var9 = var0.rand.nextDouble() * 0.8D + 0.1D;
                    EntityItem var14;

                    for (double var11 = var0.rand.nextDouble() * 0.8D + 0.1D; var6.stackSize > 0; var0.spawnEntityInWorld(var14))
                    {
                        int var13 = var0.rand.nextInt(21) + 10;

                        if (var13 > var6.stackSize)
                        {
                            var13 = var6.stackSize;
                        }

                        var6.stackSize -= var13;
                        var14 = new EntityItem(var0, (double)var1 + var7, (double)var2 + var9, (double)var3 + var11, new ItemStack(var6.itemID, var13, var6.getItemDamage()));
                        double var15 = 0.05D;
                        var14.motionX = var0.rand.nextGaussian() * var15;
                        var14.motionY = var0.rand.nextGaussian() * var15 + 0.2D;
                        var14.motionZ = var0.rand.nextGaussian() * var15;

                        if (var6.hasTagCompound())
                        {
                            var14.getEntityItem().setTagCompound((NBTTagCompound)var6.getTagCompound().copy());
                        }
                    }

                    var4.setInventorySlotContents(var5, (ItemStack)null);
                }
            }
        }
    }

    public static List getLoadedMods()
    {
        return Collections.unmodifiableList(modList);
    }

    public static Logger getLogger()
    {
        return logger;
    }

    public static Minecraft getMinecraftInstance()
    {
        if (instance == null)
        {
            try
            {
                ThreadGroup var0 = Thread.currentThread().getThreadGroup();
                int var1 = var0.activeCount();
                Thread[] var2 = new Thread[var1];
                var0.enumerate(var2);
                int var3;

                for (var3 = 0; var3 < var2.length; ++var3)
                {
                    System.out.println(var2[var3].getName());
                }

                for (var3 = 0; var3 < var2.length; ++var3)
                {
                    if (var2[var3].getName().equals("Minecraft main thread"))
                    {
                        instance = (Minecraft)getPrivateValue(Thread.class, var2[var3], "target");
                        break;
                    }
                }
            }
            catch (SecurityException var4)
            {
                logger.throwing("ModLoader", "getMinecraftInstance", var4);
                throw new RuntimeException(var4);
            }
            catch (NoSuchFieldException var5)
            {
                logger.throwing("ModLoader", "getMinecraftInstance", var5);
                throw new RuntimeException(var5);
            }
        }

        return instance;
    }

    public static Object getPrivateValue(Class var0, Object var1, int var2) throws IllegalArgumentException, SecurityException, NoSuchFieldException
    {
        try
        {
            Field var3 = var0.getDeclaredFields()[var2];
            var3.setAccessible(true);
            return var3.get(var1);
        }
        catch (IllegalAccessException var4)
        {
            logger.throwing("ModLoader", "getPrivateValue", var4);
            throwException("An impossible error has occured!", var4);
            return null;
        }
    }

    public static Object getPrivateValue(Class var0, Object var1, String var2) throws IllegalArgumentException, SecurityException, NoSuchFieldException
    {
        try
        {
            Field var3 = var0.getDeclaredField(var2);
            var3.setAccessible(true);
            return var3.get(var1);
        }
        catch (IllegalAccessException var4)
        {
            logger.throwing("ModLoader", "getPrivateValue", var4);
            throwException("An impossible error has occured!", var4);
            return null;
        }
    }

    public static int getUniqueBlockModelID(BaseMod var0, boolean var1)
    {
        int var2 = nextBlockModelID++;
        blockModels.put(Integer.valueOf(var2), var0);
        blockSpecialInv.put(Integer.valueOf(var2), Boolean.valueOf(var1));
        return var2;
    }

    private static void init()
    {
        hasInit = true;

        try
        {
            instance = Minecraft.getMinecraft();
            instance.entityRenderer = new EntityRendererProxy(instance);
            classMap = (Map)getPrivateValue(EntityList.class, (Object)null, 0);
            soundPoolSounds = (SoundPool)getPrivateValue(SoundManager.class, instance.sndManager, 1);
            soundPoolStreaming = (SoundPool)getPrivateValue(SoundManager.class, instance.sndManager, 2);
            soundPoolMusic = (SoundPool)getPrivateValue(SoundManager.class, instance.sndManager, 3);
            field_modifiers = Field.class.getDeclaredField("modifiers");
            field_modifiers.setAccessible(true);
            field_TileEntityRenderers = TileEntityRenderer.class.getDeclaredFields()[0];
            field_TileEntityRenderers.setAccessible(true);
            field_armorList = RenderPlayer.class.getDeclaredFields()[3];
            field_modifiers.setInt(field_armorList, field_armorList.getModifiers() & -17);
            field_armorList.setAccessible(true);
            Field[] var0 = BiomeGenBase.class.getDeclaredFields();
            LinkedList var1 = new LinkedList();

            for (int var2 = 0; var2 < var0.length; ++var2)
            {
                Class var3 = var0[var2].getType();

                if ((var0[var2].getModifiers() & 8) != 0 && var3.isAssignableFrom(BiomeGenBase.class))
                {
                    BiomeGenBase var4 = (BiomeGenBase)var0[var2].get((Object)null);

                    if (!(var4 instanceof BiomeGenHell) && !(var4 instanceof BiomeGenEnd))
                    {
                        var1.add(var4);
                    }
                }
            }

            standardBiomes = (BiomeGenBase[])((BiomeGenBase[])var1.toArray(new BiomeGenBase[0]));

            try
            {
                method_RegisterTileEntity = TileEntity.class.getDeclaredMethod("a", new Class[] {Class.class, String.class});
            }
            catch (NoSuchMethodException var6)
            {
                method_RegisterTileEntity = TileEntity.class.getDeclaredMethod("addMapping", new Class[] {Class.class, String.class});
            }

            method_RegisterTileEntity.setAccessible(true);

            try
            {
                method_RegisterEntityID = EntityList.class.getDeclaredMethod("a", new Class[] {Class.class, String.class, Integer.TYPE});
            }
            catch (NoSuchMethodException var5)
            {
                method_RegisterEntityID = EntityList.class.getDeclaredMethod("addMapping", new Class[] {Class.class, String.class, Integer.TYPE});
            }

            method_RegisterEntityID.setAccessible(true);
        }
        catch (SecurityException var8)
        {
            logger.throwing("ModLoader", "init", var8);
            throwException(var8);
            throw new RuntimeException(var8);
        }
        catch (NoSuchFieldException var9)
        {
            logger.throwing("ModLoader", "init", var9);
            throwException(var9);
            throw new RuntimeException(var9);
        }
        catch (NoSuchMethodException var10)
        {
            logger.throwing("ModLoader", "init", var10);
            throwException(var10);
            throw new RuntimeException(var10);
        }
        catch (IllegalArgumentException var11)
        {
            logger.throwing("ModLoader", "init", var11);
            throwException(var11);
            throw new RuntimeException(var11);
        }
        catch (IllegalAccessException var12)
        {
            logger.throwing("ModLoader", "init", var12);
            throwException(var12);
            throw new RuntimeException(var12);
        }

        try
        {
            loadConfig();

            if (props.containsKey("loggingLevel"))
            {
                cfgLoggingLevel = Level.parse(props.getProperty("loggingLevel"));
            }

            if (props.containsKey("grassFix"))
            {
                RenderBlocks.cfgGrassFix = Boolean.parseBoolean(props.getProperty("grassFix"));
            }

            logger.setLevel(cfgLoggingLevel);

            if ((logfile.exists() || logfile.createNewFile()) && logfile.canWrite() && logHandler == null)
            {
                logHandler = new FileHandler(logfile.getPath());
                logHandler.setFormatter(new SimpleFormatter());
                logger.addHandler(logHandler);
            }

            logger.fine("ModLoader 1.5.2 Initializing...");
            System.out.println("ModLoader 1.5.2 Initializing...");
            File var13 = new File(ModLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            modDir.mkdirs();
            readFromClassPath(var13);
            readFromModFolder(modDir);
            sortModList();
            Iterator var14 = modList.iterator();

            while (var14.hasNext())
            {
                BaseMod var16 = (BaseMod)var14.next();
                var16.load();
                logger.fine("Mod Loaded: \"" + var16.toString() + "\"");
                System.out.println("Mod Loaded: " + var16.toString());

                if (!props.containsKey(var16.getClass().getSimpleName()))
                {
                    props.setProperty(var16.getClass().getSimpleName(), "on");
                }
            }

            Iterator var17 = modList.iterator();

            while (var17.hasNext())
            {
                BaseMod var15 = (BaseMod)var17.next();
                var15.modsLoaded();
            }

            System.out.println("Done.");
            props.setProperty("loggingLevel", cfgLoggingLevel.getName());
            props.setProperty("grassFix", Boolean.toString(RenderBlocks.cfgGrassFix));
            instance.gameSettings.keyBindings = registerAllKeys(instance.gameSettings.keyBindings);
            instance.gameSettings.loadOptions();
            initStats();
            saveConfig();
        }
        catch (Throwable var7)
        {
            logger.throwing("ModLoader", "init", var7);
            throwException("ModLoader has failed to initialize.", var7);

            if (logHandler != null)
            {
                logHandler.close();
            }

            throw new RuntimeException(var7);
        }
    }

    private static void initStats()
    {
        int var0;
        String var1;

        for (var0 = 0; var0 < Block.blocksList.length; ++var0)
        {
            if (!StatList.oneShotStats.containsKey(Integer.valueOf(16777216 + var0)) && Block.blocksList[var0] != null && Block.blocksList[var0].getEnableStats())
            {
                var1 = StatCollector.translateToLocalFormatted("stat.mineBlock", new Object[] {Block.blocksList[var0].getLocalizedName()});
                StatList.mineBlockStatArray[var0] = (new StatCrafting(16777216 + var0, var1, var0)).registerStat();
                StatList.objectMineStats.add(StatList.mineBlockStatArray[var0]);
            }
        }

        for (var0 = 0; var0 < Item.itemsList.length; ++var0)
        {
            if (!StatList.oneShotStats.containsKey(Integer.valueOf(16908288 + var0)) && Item.itemsList[var0] != null)
            {
                var1 = StatCollector.translateToLocalFormatted("stat.useItem", new Object[] {Item.itemsList[var0].getStatName()});
                StatList.objectUseStats[var0] = (new StatCrafting(16908288 + var0, var1, var0)).registerStat();

                if (var0 >= Block.blocksList.length)
                {
                    StatList.itemStats.add(StatList.objectUseStats[var0]);
                }
            }

            if (!StatList.oneShotStats.containsKey(Integer.valueOf(16973824 + var0)) && Item.itemsList[var0] != null && Item.itemsList[var0].isDamageable())
            {
                var1 = StatCollector.translateToLocalFormatted("stat.breakItem", new Object[] {Item.itemsList[var0].getStatName()});
                StatList.objectBreakStats[var0] = (new StatCrafting(16973824 + var0, var1, var0)).registerStat();
            }
        }

        HashSet var5 = new HashSet();
        Iterator var6 = CraftingManager.getInstance().getRecipeList().iterator();

        while (var6.hasNext())
        {
            IRecipe var2 = (IRecipe)var6.next();

            if (var2.getRecipeOutput() != null)
            {
                var5.add(Integer.valueOf(var2.getRecipeOutput().itemID));
            }
        }

        var6 = FurnaceRecipes.smelting().getSmeltingList().values().iterator();

        while (var6.hasNext())
        {
            ItemStack var7 = (ItemStack)var6.next();
            var5.add(Integer.valueOf(var7.itemID));
        }

        Iterator var8 = var5.iterator();

        while (var8.hasNext())
        {
            int var3 = ((Integer)var8.next()).intValue();

            if (!StatList.oneShotStats.containsKey(Integer.valueOf(16842752 + var3)) && Item.itemsList[var3] != null)
            {
                String var4 = StatCollector.translateToLocalFormatted("stat.craftItem", new Object[] {Item.itemsList[var3].getStatName()});
                StatList.objectCraftStats[var3] = (new StatCrafting(16842752 + var3, var4, var3)).registerStat();
            }
        }
    }

    public static boolean isGUIOpen(Class var0)
    {
        Minecraft var1 = getMinecraftInstance();
        return var0 == null ? var1.currentScreen == null : (var1.currentScreen == null && var0 != null ? false : var0.isInstance(var1.currentScreen));
    }

    public static boolean isModLoaded(String var0)
    {
        Iterator var1 = modList.iterator();
        BaseMod var2;

        do
        {
            if (!var1.hasNext())
            {
                return false;
            }

            var2 = (BaseMod)var1.next();
        }
        while (!var0.contentEquals(var2.getName()));

        return true;
    }

    public static void loadConfig() throws IOException
    {
        cfgdir.mkdir();

        if (cfgfile.exists() || cfgfile.createNewFile())
        {
            if (cfgfile.canRead())
            {
                FileInputStream var0 = new FileInputStream(cfgfile);
                props.load(var0);
                var0.close();
            }
        }
    }

    public static BufferedImage loadImage(RenderEngine var0, String var1) throws Exception
    {
        TexturePackList var2 = (TexturePackList)getPrivateValue(RenderEngine.class, var0, 10);
        InputStream var3 = var2.getSelectedTexturePack().getResourceAsStream(var1);

        if (var3 == null)
        {
            throw new Exception("Image not found: " + var1);
        }
        else
        {
            BufferedImage var4 = ImageIO.read(var3);

            if (var4 == null)
            {
                throw new Exception("Image corrupted: " + var1);
            }
            else
            {
                return var4;
            }
        }
    }

    public static void onItemPickup(EntityPlayer var0, ItemStack var1)
    {
        Iterator var3 = modList.iterator();

        while (var3.hasNext())
        {
            BaseMod var2 = (BaseMod)var3.next();
            var2.onItemPickup(var0, var1);
        }
    }

    public static void onTick(float var0, Minecraft var1)
    {
        var1.mcProfiler.endSection();
        var1.mcProfiler.endSection();
        var1.mcProfiler.startSection("modtick");

        if (!hasInit)
        {
            init();
            logger.fine("Initialized");
        }

        if (langPack == null || StringTranslate.getInstance().getCurrentLanguage() != langPack)
        {
            Properties var2 = null;

            try
            {
                var2 = (Properties)getPrivateValue(StringTranslate.class, StringTranslate.getInstance(), 1);
            }
            catch (SecurityException var12)
            {
                logger.throwing("ModLoader", "AddLocalization", var12);
                throwException(var12);
            }
            catch (NoSuchFieldException var13)
            {
                logger.throwing("ModLoader", "AddLocalization", var13);
                throwException(var13);
            }

            langPack = StringTranslate.getInstance().getCurrentLanguage();

            if (var2 != null)
            {
                if (localizedStrings.containsKey("en_US"))
                {
                    var2.putAll((Map)localizedStrings.get("en_US"));
                }

                if (!langPack.contentEquals("en_US") && localizedStrings.containsKey(langPack))
                {
                    var2.putAll((Map)localizedStrings.get(langPack));
                }
            }
        }

        long var14 = 0L;
        Iterator var4;
        Entry var5;

        if (var1.thePlayer != null && var1.thePlayer.worldObj != null)
        {
            var14 = var1.thePlayer.worldObj.getWorldTime();
            var4 = inGameHooks.entrySet().iterator();

            while (var4.hasNext())
            {
                var5 = (Entry)var4.next();

                if ((clock != var14 || !((Boolean)var5.getValue()).booleanValue()) && !((BaseMod)var5.getKey()).onTickInGame(var0, var1))
                {
                    var4.remove();
                }
            }
        }

        if (var1.currentScreen != null)
        {
            var4 = inGUIHooks.entrySet().iterator();

            while (var4.hasNext())
            {
                var5 = (Entry)var4.next();

                if ((clock != var14 || !((Boolean)var5.getValue()).booleanValue() || var1.thePlayer == null || var1.thePlayer.worldObj == null) && !((BaseMod)var5.getKey()).onTickInGUI(var0, var1, var1.currentScreen))
                {
                    var4.remove();
                }
            }
        }

        if (clock != var14)
        {
            var4 = keyList.entrySet().iterator();

            while (var4.hasNext())
            {
                var5 = (Entry)var4.next();
                Iterator var6 = ((Map)var5.getValue()).entrySet().iterator();

                while (var6.hasNext())
                {
                    Entry var7 = (Entry)var6.next();
                    int var8 = ((KeyBinding)var7.getKey()).keyCode;
                    boolean var9;

                    if (var8 < 0)
                    {
                        var8 += 100;
                        var9 = Mouse.isButtonDown(var8);
                    }
                    else
                    {
                        var9 = Keyboard.isKeyDown(var8);
                    }

                    boolean[] var10 = (boolean[])((boolean[])var7.getValue());
                    boolean var11 = var10[1];
                    var10[1] = var9;

                    if (var9 && (!var11 || var10[0]))
                    {
                        ((BaseMod)var5.getKey()).keyboardEvent((KeyBinding)var7.getKey());
                    }
                }
            }
        }

        clock = var14;
        var1.mcProfiler.endSection();
        var1.mcProfiler.startSection("render");
        var1.mcProfiler.startSection("gameRenderer");
    }

    public static void openGUI(EntityPlayer var0, GuiScreen var1)
    {
        if (!hasInit)
        {
            init();
            logger.fine("Initialized");
        }

        Minecraft var2 = getMinecraftInstance();

        if (var2.thePlayer == var0)
        {
            if (var1 != null)
            {
                var2.displayGuiScreen(var1);
            }
        }
    }

    public static void populateChunk(IChunkProvider var0, int var1, int var2, World var3)
    {
        if (!hasInit)
        {
            init();
            logger.fine("Initialized");
        }

        Random var4 = new Random(var3.getSeed());
        long var5 = var4.nextLong() / 2L * 2L + 1L;
        long var7 = var4.nextLong() / 2L * 2L + 1L;
        var4.setSeed((long)var1 * var5 + (long)var2 * var7 ^ var3.getSeed());
        Iterator var9 = modList.iterator();

        while (var9.hasNext())
        {
            BaseMod var10 = (BaseMod)var9.next();

            if (var3.provider.isSurfaceWorld())
            {
                var10.generateSurface(var3, var4, var1 << 4, var2 << 4);
            }
            else if (var3.provider.isHellWorld)
            {
                var10.generateNether(var3, var4, var1 << 4, var2 << 4);
            }
        }
    }

    private static void readFromClassPath(File var0) throws FileNotFoundException, IOException
    {
        logger.finer("Adding mods from " + var0.getCanonicalPath());
        ClassLoader var1 = ModLoader.class.getClassLoader();
        String var5;

        if (var0.isFile() && (var0.getName().endsWith(".jar") || var0.getName().endsWith(".zip")))
        {
            logger.finer("Zip found.");
            URL var9 = var0.toURI().toURL();
            FileInputStream var11 = new FileInputStream(var0);
            ZipInputStream var12 = new ZipInputStream(var11);
            var5 = null;

            while (true)
            {
                ZipEntry var6 = var12.getNextEntry();

                if (var6 == null)
                {
                    var11.close();
                    break;
                }

                String var7 = var6.getName();

                if (!var6.isDirectory())
                {
                    if (var7.startsWith("mod_") && var7.endsWith(".class"))
                    {
                        addMod(var1, var7);
                    }
                    else
                    {
                        String var8;

                        if (var7.startsWith("resources/streaming/"))
                        {
                            var8 = var7.substring(20);
                            soundPoolStreaming.addSound(var8, new URL(String.format("jar:%s!/%s", new Object[] {var9, var7})));
                        }
                        else if (var7.startsWith("resources/music/"))
                        {
                            var8 = var7.substring(16);
                            soundPoolMusic.addSound(var8, new URL(String.format("jar:%s!/%s", new Object[] {var9, var7})));
                        }
                        else if (var7.startsWith("resources/sound/"))
                        {
                            var8 = var7.substring(16);
                            soundPoolSounds.addSound(var8, new URL(String.format("jar:%s!/%s", new Object[] {var9, var7})));
                        }
                    }
                }
            }
        }
        else if (var0.isDirectory())
        {
            Package var2 = ModLoader.class.getPackage();

            if (var2 != null)
            {
                String var3 = var2.getName().replace('.', File.separatorChar);
                var0 = new File(var0, var3);
            }

            logger.finer("Directory found.");
            File[] var10 = var0.listFiles();

            if (var10 != null)
            {
                for (int var4 = 0; var4 < var10.length; ++var4)
                {
                    var5 = var10[var4].getName();

                    if (var10[var4].isFile() && var5.startsWith("mod_") && var5.endsWith(".class"))
                    {
                        addMod(var1, var5);
                    }
                }
            }
        }
    }

    private static void readFromModFolder(File var0) throws IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException
    {
        ClassLoader var1 = Minecraft.class.getClassLoader();
        Method var2 = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] {URL.class});
        var2.setAccessible(true);

        if (!var0.isDirectory())
        {
            throw new IllegalArgumentException("folder must be a Directory.");
        }
        else
        {
            File[] var3 = var0.listFiles();
            Arrays.sort(var3);
            int var4;
            File var5;

            if (var1 instanceof URLClassLoader)
            {
                for (var4 = 0; var4 < var3.length; ++var4)
                {
                    var5 = var3[var4];

                    if (var5.isDirectory() || var5.isFile() && (var5.getName().endsWith(".jar") || var5.getName().endsWith(".zip")))
                    {
                        var2.invoke(var1, new Object[] {var5.toURI().toURL()});
                    }
                }
            }

            for (var4 = 0; var4 < var3.length; ++var4)
            {
                var5 = var3[var4];

                if (var5.isDirectory() || var5.isFile() && (var5.getName().endsWith(".jar") || var5.getName().endsWith(".zip")))
                {
                    logger.finer("Adding mods from " + var5.getCanonicalPath());
                    String var9;

                    if (var5.isFile())
                    {
                        logger.finer("Zip found.");
                        URL var6 = var5.toURI().toURL();
                        FileInputStream var7 = new FileInputStream(var5);
                        ZipInputStream var8 = new ZipInputStream(var7);
                        var9 = null;

                        while (true)
                        {
                            ZipEntry var10 = var8.getNextEntry();

                            if (var10 == null)
                            {
                                var8.close();
                                var7.close();
                                break;
                            }

                            String var11 = var10.getName();

                            if (!var10.isDirectory())
                            {
                                if (var11.startsWith("mod_") && var11.endsWith(".class"))
                                {
                                    addMod(var1, var11);
                                }
                                else
                                {
                                    String var12;

                                    if (var11.startsWith("resources/streaming/"))
                                    {
                                        var12 = var11.substring(20);
                                        soundPoolStreaming.addSound(var12, new URL(String.format("jar:%s!/%s", new Object[] {var6, var11})));
                                    }
                                    else if (var11.startsWith("resources/music/"))
                                    {
                                        var12 = var11.substring(16);
                                        soundPoolMusic.addSound(var12, new URL(String.format("jar:%s!/%s", new Object[] {var6, var11})));
                                    }
                                    else if (var11.startsWith("resources/sound/"))
                                    {
                                        var12 = var11.substring(16);
                                        soundPoolSounds.addSound(var12, new URL(String.format("jar:%s!/%s", new Object[] {var6, var11})));
                                    }
                                }
                            }
                        }
                    }
                    else if (var5.isDirectory())
                    {
                        Package var13 = ModLoader.class.getPackage();

                        if (var13 != null)
                        {
                            String var14 = var13.getName().replace('.', File.separatorChar);
                            var5 = new File(var5, var14);
                        }

                        logger.finer("Directory found.");
                        File[] var15 = var5.listFiles();

                        if (var15 != null)
                        {
                            for (int var16 = 0; var16 < var15.length; ++var16)
                            {
                                var9 = var15[var16].getName();

                                if (var15[var16].isFile() && var9.startsWith("mod_") && var9.endsWith(".class"))
                                {
                                    addMod(var1, var9);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void clientCustomPayload(Packet250CustomPayload var0)
    {
        if (var0.channel.equals("ML|OpenTE"))
        {
            try
            {
                DataInputStream var1 = new DataInputStream(new ByteArrayInputStream(var0.data));
                int var2 = var1.read();
                int var3 = var1.readInt();
                int var4 = var1.readInt();
                int var5 = var1.readInt();
                int var6 = var1.readInt();
                byte var7 = (byte)var1.read();
                EntityClientPlayerMP var8 = instance.thePlayer;

                if (var8.dimension != var7)
                {
                    return;
                }

                if (containerGUIs.containsKey(Integer.valueOf(var3)))
                {
                    BaseMod var9 = (BaseMod)containerGUIs.get(Integer.valueOf(var3));

                    if (var9 != null)
                    {
                        GuiContainer var10 = var9.getContainerGUI(var8, var3, var4, var5, var6);

                        if (var10 == null)
                        {
                            return;
                        }

                        instance.displayGuiScreen(var10);
                        var8.openContainer.windowId = var2;
                    }
                }
            }
            catch (IOException var11)
            {
                var11.printStackTrace();
            }
        }
        else if (packetChannels.containsKey(var0.channel))
        {
            BaseMod var12 = (BaseMod)packetChannels.get(var0.channel);

            if (var12 != null)
            {
                var12.clientCustomPayload(clientHandler, var0);
            }
        }
    }

    public static void serverCustomPayload(NetServerHandler var0, Packet250CustomPayload var1)
    {
        if (packetChannels.containsKey(var1.channel))
        {
            BaseMod var2 = (BaseMod)packetChannels.get(var1.channel);

            if (var2 != null)
            {
                var2.serverCustomPayload(var0, var1);
            }
        }
    }

    public static void registerContainerID(BaseMod var0, int var1)
    {
        containerGUIs.put(Integer.valueOf(var1), var0);
    }

    public static void clientOpenWindow(Packet100OpenWindow var0) {}

    public static void serverOpenWindow(EntityPlayerMP var0, Container var1, int var2, int var3, int var4, int var5)
    {
        try
        {
            Field var6 = EntityPlayerMP.class.getDeclaredFields()[16];
            var6.setAccessible(true);
            int var7 = var6.getInt(var0);
            var7 = var7 % 100 + 1;
            var6.setInt(var0, var7);
            ByteArrayOutputStream var8 = new ByteArrayOutputStream();
            DataOutputStream var9 = new DataOutputStream(var8);
            var9.write(var7);
            var9.writeInt(var2);
            var9.writeInt(var3);
            var9.writeInt(var4);
            var9.writeInt(var5);
            var9.write(var0.dimension);
            var0.playerNetServerHandler.sendPacketToPlayer(new Packet250CustomPayload("ML|OpenTE", var8.toByteArray()));
            var0.openContainer = var1;
            var0.openContainer.windowId = var7;
            var0.openContainer.addCraftingToCrafters(var0);
        }
        catch (Exception var10)
        {
            var10.printStackTrace();
        }
    }

    public static KeyBinding[] registerAllKeys(KeyBinding[] var0)
    {
        LinkedList var1 = new LinkedList();
        var1.addAll(Arrays.asList(var0));
        Iterator var3 = keyList.values().iterator();

        while (var3.hasNext())
        {
            Map var2 = (Map)var3.next();
            var1.addAll(var2.keySet());
        }

        return (KeyBinding[])((KeyBinding[])var1.toArray(new KeyBinding[0]));
    }

    public static void registerBlock(Block var0)
    {
        registerBlock(var0, (Class)null);
    }

    public static void registerBlock(Block var0, Class var1)
    {
        try
        {
            if (var0 == null)
            {
                throw new IllegalArgumentException("block parameter cannot be null.");
            }

            int var2 = var0.blockID;
            ItemBlock var3 = null;

            if (var1 != null)
            {
                var3 = (ItemBlock)var1.getConstructor(new Class[] {Integer.TYPE}).newInstance(new Object[] {Integer.valueOf(var2 - 256)});
            }
            else
            {
                var3 = new ItemBlock(var2 - 256);
            }

            if (Block.blocksList[var2] != null && Item.itemsList[var2] == null)
            {
                Item.itemsList[var2] = var3;
            }
        }
        catch (IllegalArgumentException var4)
        {
            logger.throwing("ModLoader", "RegisterBlock", var4);
            throwException(var4);
        }
        catch (IllegalAccessException var5)
        {
            logger.throwing("ModLoader", "RegisterBlock", var5);
            throwException(var5);
        }
        catch (SecurityException var6)
        {
            logger.throwing("ModLoader", "RegisterBlock", var6);
            throwException(var6);
        }
        catch (InstantiationException var7)
        {
            logger.throwing("ModLoader", "RegisterBlock", var7);
            throwException(var7);
        }
        catch (InvocationTargetException var8)
        {
            logger.throwing("ModLoader", "RegisterBlock", var8);
            throwException(var8);
        }
        catch (NoSuchMethodException var9)
        {
            logger.throwing("ModLoader", "RegisterBlock", var9);
            throwException(var9);
        }
    }

    public static void registerEntityID(Class var0, String var1, int var2)
    {
        try
        {
            method_RegisterEntityID.invoke((Object)null, new Object[] {var0, var1, Integer.valueOf(var2)});
        }
        catch (IllegalArgumentException var4)
        {
            logger.throwing("ModLoader", "RegisterEntityID", var4);
            throwException(var4);
        }
        catch (IllegalAccessException var5)
        {
            logger.throwing("ModLoader", "RegisterEntityID", var5);
            throwException(var5);
        }
        catch (InvocationTargetException var6)
        {
            logger.throwing("ModLoader", "RegisterEntityID", var6);
            throwException(var6);
        }
    }

    public static void registerEntityID(Class var0, String var1, int var2, int var3, int var4)
    {
        registerEntityID(var0, var1, var2);
        EntityList.entityEggs.put(Integer.valueOf(var2), new EntityEggInfo(var2, var3, var4));
    }

    public static void registerKey(BaseMod var0, KeyBinding var1, boolean var2)
    {
        Object var3 = (Map)keyList.get(var0);

        if (var3 == null)
        {
            var3 = new HashMap();
        }

        boolean[] var4 = new boolean[] {var2, false};
        ((Map)var3).put(var1, var4);
        keyList.put(var0, var3);
    }

    public static void registerPacketChannel(BaseMod var0, String var1)
    {
        if (var1.length() < 16)
        {
            packetChannels.put(var1, var0);
        }
        else
        {
            throw new RuntimeException(String.format("Invalid channel name: %s. Must be less than 16 characters.", new Object[] {var1}));
        }
    }

    public static void registerTileEntity(Class var0, String var1)
    {
        registerTileEntity(var0, var1, (TileEntitySpecialRenderer)null);
    }

    public static void registerTileEntity(Class var0, String var1, TileEntitySpecialRenderer var2)
    {
        try
        {
            method_RegisterTileEntity.invoke((Object)null, new Object[] {var0, var1});

            if (var2 != null)
            {
                TileEntityRenderer var3 = TileEntityRenderer.instance;
                Map var4 = (Map)field_TileEntityRenderers.get(var3);
                var4.put(var0, var2);
                var2.setTileEntityRenderer(var3);
            }
        }
        catch (IllegalArgumentException var5)
        {
            logger.throwing("ModLoader", "RegisterTileEntity", var5);
            throwException(var5);
        }
        catch (IllegalAccessException var6)
        {
            logger.throwing("ModLoader", "RegisterTileEntity", var6);
            throwException(var6);
        }
        catch (InvocationTargetException var7)
        {
            logger.throwing("ModLoader", "RegisterTileEntity", var7);
            throwException(var7);
        }
    }

    public static void removeBiome(BiomeGenBase var0)
    {
        BiomeGenBase[] var1 = GenLayerBiome.biomeArray;
        List var2 = Arrays.asList(var1);
        ArrayList var3 = new ArrayList();
        var3.addAll(var2);

        if (var3.contains(var0))
        {
            var3.remove(var0);
        }

        GenLayerBiome.biomeArray = (BiomeGenBase[])((BiomeGenBase[])var3.toArray(new BiomeGenBase[0]));
    }

    public static void removeSpawn(Class var0, EnumCreatureType var1)
    {
        removeSpawn(var0, var1, (BiomeGenBase[])null);
    }

    public static void removeSpawn(Class var0, EnumCreatureType var1, BiomeGenBase[] var2)
    {
        if (var0 == null)
        {
            throw new IllegalArgumentException("entityClass cannot be null");
        }
        else if (var1 == null)
        {
            throw new IllegalArgumentException("spawnList cannot be null");
        }
        else
        {
            if (var2 == null)
            {
                var2 = standardBiomes;
            }

            for (int var3 = 0; var3 < var2.length; ++var3)
            {
                List var4 = var2[var3].getSpawnableList(var1);

                if (var4 != null)
                {
                    Iterator var5 = var4.iterator();

                    while (var5.hasNext())
                    {
                        SpawnListEntry var6 = (SpawnListEntry)var5.next();

                        if (var6.entityClass == var0)
                        {
                            var5.remove();
                        }
                    }
                }
            }
        }
    }

    public static void removeSpawn(String var0, EnumCreatureType var1)
    {
        removeSpawn(var0, var1, (BiomeGenBase[])null);
    }

    public static void removeSpawn(String var0, EnumCreatureType var1, BiomeGenBase[] var2)
    {
        Class var3 = (Class)classMap.get(var0);

        if (var3 != null && EntityLiving.class.isAssignableFrom(var3))
        {
            removeSpawn(var3, var1, var2);
        }
    }

    public static boolean renderBlockIsItemFull3D(int var0)
    {
        return !blockSpecialInv.containsKey(Integer.valueOf(var0)) ? var0 == 35 : ((Boolean)blockSpecialInv.get(Integer.valueOf(var0))).booleanValue();
    }

    public static void renderInvBlock(RenderBlocks var0, Block var1, int var2, int var3)
    {
        BaseMod var4 = (BaseMod)blockModels.get(Integer.valueOf(var3));

        if (var4 != null)
        {
            var4.renderInvBlock(var0, var1, var2, var3);
        }
    }

    public static boolean renderWorldBlock(RenderBlocks var0, IBlockAccess var1, int var2, int var3, int var4, Block var5, int var6)
    {
        BaseMod var7 = (BaseMod)blockModels.get(Integer.valueOf(var6));
        return var7 == null ? false : var7.renderWorldBlock(var0, var1, var2, var3, var4, var5, var6);
    }

    public static void saveConfig() throws IOException
    {
        cfgdir.mkdir();

        if (cfgfile.exists() || cfgfile.createNewFile())
        {
            if (cfgfile.canWrite())
            {
                FileOutputStream var0 = new FileOutputStream(cfgfile);
                props.store(var0, "ModLoader Config");
                var0.close();
            }
        }
    }

    public static void clientChat(String var0)
    {
        Iterator var2 = modList.iterator();

        while (var2.hasNext())
        {
            BaseMod var3 = (BaseMod)var2.next();
            var3.clientChat(var0);
        }
    }

    public static void serverChat(NetServerHandler var0, String var1)
    {
        Iterator var3 = modList.iterator();

        while (var3.hasNext())
        {
            BaseMod var4 = (BaseMod)var3.next();
            var4.serverChat(var0, var1);
        }
    }

    public static void clientConnect(NetClientHandler var0, Packet1Login var1)
    {
        clientHandler = var0;

        if (packetChannels.size() > 0)
        {
            Packet250CustomPayload var2 = new Packet250CustomPayload();
            var2.channel = "REGISTER";
            StringBuilder var3 = new StringBuilder();
            Iterator var4 = packetChannels.keySet().iterator();
            var3.append((String)var4.next());

            while (var4.hasNext())
            {
                var3.append("\u0000");
                var3.append((String)var4.next());
            }

            var2.data = var3.toString().getBytes(Charset.forName("UTF8"));
            var2.length = var2.data.length;
            clientSendPacket(var2);
        }

        Iterator var5 = modList.iterator();

        while (var5.hasNext())
        {
            BaseMod var6 = (BaseMod)var5.next();
            var6.clientConnect(var0);
        }
    }

    public static void clientDisconnect()
    {
        Iterator var0 = modList.iterator();

        while (var0.hasNext())
        {
            BaseMod var1 = (BaseMod)var0.next();
            var1.clientDisconnect(clientHandler);
        }

        clientHandler = null;
    }

    public static void clientSendPacket(Packet var0)
    {
        if (clientHandler != null)
        {
            clientHandler.addToSendQueue(var0);
        }
    }

    public static void serverSendPacket(NetServerHandler var0, Packet var1)
    {
        if (var0 != null)
        {
            var0.sendPacketToPlayer(var1);
        }
    }

    public static void setInGameHook(BaseMod var0, boolean var1, boolean var2)
    {
        if (var1)
        {
            inGameHooks.put(var0, Boolean.valueOf(var2));
        }
        else
        {
            inGameHooks.remove(var0);
        }
    }

    public static void setInGUIHook(BaseMod var0, boolean var1, boolean var2)
    {
        if (var1)
        {
            inGUIHooks.put(var0, Boolean.valueOf(var2));
        }
        else
        {
            inGUIHooks.remove(var0);
        }
    }

    public static void setPrivateValue(Class var0, Object var1, int var2, Object var3) throws IllegalArgumentException, SecurityException, NoSuchFieldException
    {
        try
        {
            Field var4 = var0.getDeclaredFields()[var2];
            var4.setAccessible(true);
            int var5 = field_modifiers.getInt(var4);

            if ((var5 & 16) != 0)
            {
                field_modifiers.setInt(var4, var5 & -17);
            }

            var4.set(var1, var3);
        }
        catch (IllegalAccessException var6)
        {
            logger.throwing("ModLoader", "setPrivateValue", var6);
            throwException("An impossible error has occured!", var6);
        }
    }

    public static void setPrivateValue(Class var0, Object var1, String var2, Object var3) throws IllegalArgumentException, SecurityException, NoSuchFieldException
    {
        try
        {
            Field var4 = var0.getDeclaredField(var2);
            int var5 = field_modifiers.getInt(var4);

            if ((var5 & 16) != 0)
            {
                field_modifiers.setInt(var4, var5 & -17);
            }

            var4.setAccessible(true);
            var4.set(var1, var3);
        }
        catch (IllegalAccessException var6)
        {
            logger.throwing("ModLoader", "setPrivateValue", var6);
            throwException("An impossible error has occured!", var6);
        }
    }

    private static void setupProperties(Class var0) throws IllegalArgumentException, IllegalAccessException, IOException, SecurityException, NoSuchFieldException, NoSuchAlgorithmException, DigestException
    {
        LinkedList var1 = new LinkedList();
        Properties var2 = new Properties();
        int var3 = 0;
        int var4 = 0;
        File var5 = new File(cfgdir, var0.getSimpleName() + ".cfg");

        if (var5.exists() && var5.canRead())
        {
            var2.load(new FileInputStream(var5));
        }

        if (var2.containsKey("checksum"))
        {
            var4 = Integer.parseInt(var2.getProperty("checksum"), 36);
        }

        Field[] var6;
        int var7 = (var6 = var0.getDeclaredFields()).length;

        for (int var8 = 0; var8 < var7; ++var8)
        {
            Field var9 = var6[var8];

            if ((var9.getModifiers() & 8) != 0 && var9.isAnnotationPresent(MLProp.class))
            {
                var1.add(var9);
                Object var10 = var9.get((Object)null);
                var3 += var10.hashCode();
            }
        }

        StringBuilder var21 = new StringBuilder();
        Iterator var22 = var1.iterator();

        while (var22.hasNext())
        {
            Field var23 = (Field)var22.next();

            if ((var23.getModifiers() & 8) != 0 && var23.isAnnotationPresent(MLProp.class))
            {
                Class var11 = var23.getType();
                MLProp var12 = (MLProp)var23.getAnnotation(MLProp.class);
                String var13 = var12.name().length() != 0 ? var12.name() : var23.getName();
                Object var14 = var23.get((Object)null);
                StringBuilder var15 = new StringBuilder();

                if (var12.min() != Double.NEGATIVE_INFINITY)
                {
                    var15.append(String.format(",>=%.1f", new Object[] {Double.valueOf(var12.min())}));
                }

                if (var12.max() != Double.POSITIVE_INFINITY)
                {
                    var15.append(String.format(",<=%.1f", new Object[] {Double.valueOf(var12.max())}));
                }

                StringBuilder var16 = new StringBuilder();

                if (var12.info().length() > 0)
                {
                    var16.append(" -- ");
                    var16.append(var12.info());
                }

                var21.append(String.format("%s (%s:%s%s)%s\n", new Object[] {var13, var11.getName(), var14, var15, var16}));

                if (var4 == var3 && var2.containsKey(var13))
                {
                    String var17 = var2.getProperty(var13);
                    Object var18 = null;

                    if (var11.isAssignableFrom(String.class))
                    {
                        var18 = var17;
                    }
                    else if (var11.isAssignableFrom(Integer.TYPE))
                    {
                        var18 = Integer.valueOf(Integer.parseInt(var17));
                    }
                    else if (var11.isAssignableFrom(Short.TYPE))
                    {
                        var18 = Short.valueOf(Short.parseShort(var17));
                    }
                    else if (var11.isAssignableFrom(Byte.TYPE))
                    {
                        var18 = Byte.valueOf(Byte.parseByte(var17));
                    }
                    else if (var11.isAssignableFrom(Boolean.TYPE))
                    {
                        var18 = Boolean.valueOf(Boolean.parseBoolean(var17));
                    }
                    else if (var11.isAssignableFrom(Float.TYPE))
                    {
                        var18 = Float.valueOf(Float.parseFloat(var17));
                    }
                    else if (var11.isAssignableFrom(Double.TYPE))
                    {
                        var18 = Double.valueOf(Double.parseDouble(var17));
                    }

                    if (var18 != null)
                    {
                        if (var18 instanceof Number)
                        {
                            double var19 = ((Number)var18).doubleValue();

                            if (var12.min() != Double.NEGATIVE_INFINITY && var19 < var12.min() || var12.max() != Double.POSITIVE_INFINITY && var19 > var12.max())
                            {
                                continue;
                            }
                        }

                        logger.finer(var13 + " set to " + var18);

                        if (!var18.equals(var14))
                        {
                            var23.set((Object)null, var18);
                        }
                    }
                }
                else
                {
                    logger.finer(var13 + " not in config, using default: " + var14);
                    var2.setProperty(var13, var14.toString());
                }
            }
        }

        var2.put("checksum", Integer.toString(var3, 36));

        if (!var2.isEmpty() && (var5.exists() || var5.createNewFile()) && var5.canWrite())
        {
            var2.store(new FileOutputStream(var5), var21.toString());
        }
    }

    private static void sortModList() throws Exception
    {
        HashMap var0 = new HashMap();
        Iterator var2 = getLoadedMods().iterator();

        while (var2.hasNext())
        {
            BaseMod var1 = (BaseMod)var2.next();
            var0.put(var1.getClass().getSimpleName(), var1);
        }

        LinkedList var18 = new LinkedList();

        for (int var3 = 0; var18.size() != modList.size() && var3 <= 10; ++var3)
        {
            Iterator var4 = modList.iterator();

            while (var4.hasNext())
            {
                BaseMod var5 = (BaseMod)var4.next();

                if (!var18.contains(var5))
                {
                    String var6 = var5.getPriorities();

                    if (var6 != null && var6.length() != 0 && var6.indexOf(58) != -1)
                    {
                        if (var3 > 0)
                        {
                            int var7 = -1;
                            int var8 = Integer.MIN_VALUE;
                            int var9 = Integer.MAX_VALUE;
                            String[] var10;

                            if (var6.indexOf(59) > 0)
                            {
                                var10 = var6.split(";");
                            }
                            else
                            {
                                var10 = new String[] {var6};
                            }

                            int var11 = 0;

                            while (true)
                            {
                                if (var11 < var10.length)
                                {
                                    label143:
                                    {
                                        String var12 = var10[var11];

                                        if (var12.indexOf(58) != -1)
                                        {
                                            String[] var13 = var12.split(":");
                                            String var14 = var13[0];
                                            String var15 = var13[1];

                                            if (var14.contentEquals("required-before") || var14.contentEquals("before") || var14.contentEquals("after") || var14.contentEquals("required-after"))
                                            {
                                                if (var15.contentEquals("*"))
                                                {
                                                    if (!var14.contentEquals("required-before") && !var14.contentEquals("before"))
                                                    {
                                                        if (var14.contentEquals("required-after") || var14.contentEquals("after"))
                                                        {
                                                            var7 = var18.size();
                                                        }
                                                    }
                                                    else
                                                    {
                                                        var7 = 0;
                                                    }

                                                    break label143;
                                                }

                                                if ((var14.contentEquals("required-before") || var14.contentEquals("required-after")) && !var0.containsKey(var15))
                                                {
                                                    throw new Exception(String.format("%s is missing dependency: %s", new Object[] {var5, var15}));
                                                }

                                                BaseMod var16 = (BaseMod)var0.get(var15);

                                                if (!var18.contains(var16))
                                                {
                                                    break;
                                                }

                                                int var17 = var18.indexOf(var16);

                                                if (!var14.contentEquals("required-before") && !var14.contentEquals("before"))
                                                {
                                                    if (var14.contentEquals("required-after") || var14.contentEquals("after"))
                                                    {
                                                        var7 = var17 + 1;

                                                        if (var7 > var8)
                                                        {
                                                            var8 = var7;
                                                        }
                                                        else
                                                        {
                                                            var7 = var8;
                                                        }
                                                    }
                                                }
                                                else
                                                {
                                                    var7 = var17;

                                                    if (var17 < var9)
                                                    {
                                                        var9 = var17;
                                                    }
                                                    else
                                                    {
                                                        var7 = var9;
                                                    }
                                                }
                                            }
                                        }

                                        ++var11;
                                        continue;
                                    }
                                }

                                if (var7 != -1)
                                {
                                    var18.add(var7, var5);
                                }

                                break;
                            }
                        }
                    }
                    else
                    {
                        var18.add(var5);
                    }
                }
            }
        }

        modList.clear();
        modList.addAll(var18);
    }

    public static void takenFromCrafting(EntityPlayer var0, ItemStack var1, IInventory var2)
    {
        Iterator var4 = modList.iterator();

        while (var4.hasNext())
        {
            BaseMod var3 = (BaseMod)var4.next();
            var3.takenFromCrafting(var0, var1, var2);
        }
    }

    public static void takenFromFurnace(EntityPlayer var0, ItemStack var1)
    {
        Iterator var3 = modList.iterator();

        while (var3.hasNext())
        {
            BaseMod var2 = (BaseMod)var3.next();
            var2.takenFromFurnace(var0, var1);
        }
    }

    public static void throwException(String var0, Throwable var1)
    {
        Minecraft var2 = getMinecraftInstance();

        if (var2 != null)
        {
            var2.displayCrashReport(new CrashReport(var0, var1));
        }
        else
        {
            throw new RuntimeException(var1);
        }
    }

    private static void throwException(Throwable var0)
    {
        throwException("Exception occured in ModLoader", var0);
    }

    public static String getCrashReport()
    {
        StringBuilder var0 = new StringBuilder();
        var0.append("Mods loaded: ");
        var0.append(getLoadedMods().size() + 1);
        var0.append('\n');
        var0.append("ModLoader 1.5.2");
        var0.append('\n');
        Iterator var1 = getLoadedMods().iterator();

        while (var1.hasNext())
        {
            BaseMod var2 = (BaseMod)var1.next();
            var0.append(var2.getName());
            var0.append(' ');
            var0.append(var2.getVersion());
            var0.append('\n');
        }

        return var0.toString();
    }

    public static void addCustomAnimationLogic(String var0, TextureStitched var1)
    {
        customTextures.put(var0, var1);
    }

    public static TextureStitched getCustomAnimationLogic(String var0)
    {
        return !customTextures.containsKey(var0) ? null : (TextureStitched)customTextures.get(var0);
    }
}
