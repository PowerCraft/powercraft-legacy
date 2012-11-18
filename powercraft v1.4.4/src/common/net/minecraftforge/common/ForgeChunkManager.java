package net.minecraftforge.common;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeMultiset;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.World;
import net.minecraft.src.WorldServer;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class ForgeChunkManager
{
    private static int defaultMaxCount;
    private static int defaultMaxChunks;
    private static boolean overridesEnabled;

    private static Map<World, Multimap<String, Ticket>> tickets = new MapMaker().weakKeys().makeMap();
    private static Map<String, Integer> ticketConstraints = Maps.newHashMap();
    private static Map<String, Integer> chunkConstraints = Maps.newHashMap();

    private static SetMultimap<String, Ticket> playerTickets = HashMultimap.create();

    private static Map<String, LoadingCallback> callbacks = Maps.newHashMap();

    private static Map<World, ImmutableSetMultimap<ChunkCoordIntPair, Ticket>> forcedChunks = new MapMaker().weakKeys().makeMap();
    private static BiMap<UUID, Ticket> pendingEntities = HashBiMap.create();

    private static Map<World, Cache<Long, Chunk>> dormantChunkCache = new MapMaker().weakKeys().makeMap();

    private static File cfgFile;
    private static Configuration config;
    private static int playerTicketLength;
    private static int dormantChunkCacheSize;

    public interface LoadingCallback
    {
        public void ticketsLoaded(List<Ticket> tickets, World world);
    }

    public interface OrderedLoadingCallback extends LoadingCallback
    {
        public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount);
    }
    public enum Type
    {
        NORMAL,

        ENTITY
    }
    public static class Ticket
    {
        private String modId;
        private Type ticketType;
        private LinkedHashSet<ChunkCoordIntPair> requestedChunks;
        private NBTTagCompound modData;
        private World world;
        private int maxDepth;
        private String entityClazz;
        private int entityChunkX;
        private int entityChunkZ;
        private Entity entity;
        private String player;

        Ticket(String modId, Type type, World world)
        {
            this.modId = modId;
            this.ticketType = type;
            this.world = world;
            this.maxDepth = getMaxChunkDepthFor(modId);
            this.requestedChunks = Sets.newLinkedHashSet();
        }

        Ticket(String modId, Type type, World world, EntityPlayer player)
        {
            this(modId, type, world);

            if (player != null)
            {
                this.player = player.getEntityName();
            }
            else
            {
                FMLLog.log(Level.SEVERE, "Attempt to create a player ticket without a valid player");
                throw new RuntimeException();
            }
        }

        public void setChunkListDepth(int depth)
        {
            if (depth > getMaxChunkDepthFor(modId) || (depth <= 0 && getMaxChunkDepthFor(modId) > 0))
            {
                FMLLog.warning("The mod %s tried to modify the chunk ticket depth to: %d, its allowed maximum is: %d", modId, depth, getMaxChunkDepthFor(modId));
            }
            else
            {
                this.maxDepth = depth;
            }
        }

        public int getChunkListDepth()
        {
            return maxDepth;
        }

        public int getMaxChunkListDepth()
        {
            return getMaxChunkDepthFor(modId);
        }

        public void bindEntity(Entity entity)
        {
            if (ticketType != Type.ENTITY)
            {
                throw new RuntimeException("Cannot bind an entity to a non-entity ticket");
            }

            this.entity = entity;
        }

        public NBTTagCompound getModData()
        {
            if (this.modData == null)
            {
                this.modData = new NBTTagCompound();
            }

            return modData;
        }

        public Entity getEntity()
        {
            return entity;
        }

        public boolean isPlayerTicket()
        {
            return player != null;
        }

        public String getPlayerName()
        {
            return player;
        }

        public String getModId()
        {
            return modId;
        }

        public Type getType()
        {
            return ticketType;
        }

        public ImmutableSet getChunkList()
        {
            return ImmutableSet.copyOf(requestedChunks);
        }
    }

    static void loadWorld(World world)
    {
        ArrayListMultimap<String, Ticket> newTickets = ArrayListMultimap.<String, Ticket>create();
        tickets.put(world, newTickets);
        forcedChunks.put(world, ImmutableSetMultimap.<ChunkCoordIntPair, Ticket>of());

        if (!(world instanceof WorldServer))
        {
            return;
        }

        dormantChunkCache.put(world, CacheBuilder.newBuilder().maximumSize(dormantChunkCacheSize).<Long, Chunk>build());
        WorldServer worldServer = (WorldServer) world;
        File chunkDir = worldServer.getChunkSaveLocation();
        File chunkLoaderData = new File(chunkDir, "forcedchunks.dat");

        if (chunkLoaderData.exists() && chunkLoaderData.isFile())
        {
            ArrayListMultimap<String, Ticket> loadedTickets = ArrayListMultimap.<String, Ticket>create();
            ArrayListMultimap<String, Ticket> playerLoadedTickets = ArrayListMultimap.<String, Ticket>create();
            NBTTagCompound forcedChunkData;

            try
            {
                forcedChunkData = CompressedStreamTools.read(chunkLoaderData);
            }
            catch (IOException e)
            {
                FMLLog.log(Level.WARNING, e, "Unable to read forced chunk data at %s - it will be ignored", chunkLoaderData.getAbsolutePath());
                return;
            }

            NBTTagList ticketList = forcedChunkData.getTagList("TicketList");

            for (int i = 0; i < ticketList.tagCount(); i++)
            {
                NBTTagCompound ticketHolder = (NBTTagCompound) ticketList.tagAt(i);
                String modId = ticketHolder.getString("Owner");
                boolean isPlayer = "Forge".equals(modId);

                if (!isPlayer && !Loader.isModLoaded(modId))
                {
                    FMLLog.warning("Found chunkloading data for mod %s which is currently not available or active - it will be removed from the world save", modId);
                    continue;
                }

                if (!isPlayer && !callbacks.containsKey(modId))
                {
                    FMLLog.warning("The mod %s has registered persistent chunkloading data but doesn't seem to want to be called back with it - it will be removed from the world save", modId);
                    continue;
                }

                NBTTagList tickets = ticketHolder.getTagList("Tickets");

                for (int j = 0; j < tickets.tagCount(); j++)
                {
                    NBTTagCompound ticket = (NBTTagCompound) tickets.tagAt(j);
                    modId = ticket.hasKey("ModId") ? ticket.getString("ModId") : modId;
                    Type type = Type.values()[ticket.getByte("Type")];
                    byte ticketChunkDepth = ticket.getByte("ChunkListDepth");
                    Ticket tick = new Ticket(modId, type, world);

                    if (ticket.hasKey("ModData"))
                    {
                        tick.modData = ticket.getCompoundTag("ModData");
                    }

                    if (ticket.hasKey("Player"))
                    {
                        tick.player = ticket.getString("Player");
                        playerLoadedTickets.put(tick.modId, tick);
                        playerTickets.put(tick.player, tick);
                    }
                    else
                    {
                        loadedTickets.put(modId, tick);
                    }

                    if (type == Type.ENTITY)
                    {
                        tick.entityChunkX = ticket.getInteger("chunkX");
                        tick.entityChunkZ = ticket.getInteger("chunkZ");
                        UUID uuid = new UUID(ticket.getLong("PersistentIDMSB"), ticket.getLong("PersistentIDLSB"));
                        pendingEntities.put(uuid, tick);
                    }
                }
            }

            for (Ticket tick : ImmutableSet.copyOf(pendingEntities.values()))
            {
                if (tick.ticketType == Type.ENTITY && tick.entity == null)
                {
                    world.getChunkFromChunkCoords(tick.entityChunkX, tick.entityChunkZ);
                }
            }

            for (Ticket tick : ImmutableSet.copyOf(pendingEntities.values()))
            {
                if (tick.ticketType == Type.ENTITY && tick.entity == null)
                {
                    FMLLog.warning("Failed to load persistent chunkloading entity %s from store.", pendingEntities.inverse().get(tick));
                    loadedTickets.remove(tick.modId, tick);
                }
            }

            pendingEntities.clear();

            for (String modId : loadedTickets.keySet())
            {
                LoadingCallback loadingCallback = callbacks.get(modId);
                int maxTicketLength = getMaxTicketLengthFor(modId);
                List<Ticket> tickets = loadedTickets.get(modId);

                if (loadingCallback instanceof OrderedLoadingCallback)
                {
                    OrderedLoadingCallback orderedLoadingCallback = (OrderedLoadingCallback) loadingCallback;
                    tickets = orderedLoadingCallback.ticketsLoaded(ImmutableList.copyOf(tickets), world, maxTicketLength);
                }

                if (tickets.size() > maxTicketLength)
                {
                    FMLLog.warning("The mod %s has too many open chunkloading tickets %d. Excess will be dropped", modId, tickets.size());
                    tickets.subList(maxTicketLength, tickets.size()).clear();
                }

                ForgeChunkManager.tickets.get(world).putAll(modId, tickets);
                loadingCallback.ticketsLoaded(ImmutableList.copyOf(tickets), world);
            }

            for (String modId : playerLoadedTickets.keySet())
            {
                LoadingCallback loadingCallback = callbacks.get(modId);
                List<Ticket> tickets = playerLoadedTickets.get(modId);
                ForgeChunkManager.tickets.get(world).putAll("Forge", tickets);
                loadingCallback.ticketsLoaded(ImmutableList.copyOf(tickets), world);
            }
        }
    }

    public static void setForcedChunkLoadingCallback(Object mod, LoadingCallback callback)
    {
        ModContainer container = getContainer(mod);

        if (container == null)
        {
            FMLLog.warning("Unable to register a callback for an unknown mod %s (%s : %x)", mod, mod.getClass().getName(), System.identityHashCode(mod));
            return;
        }

        callbacks.put(container.getModId(), callback);
    }

    public static int ticketCountAvailableFor(Object mod, World world)
    {
        ModContainer container = getContainer(mod);

        if (container != null)
        {
            String modId = container.getModId();
            int allowedCount = getMaxTicketLengthFor(modId);
            return allowedCount - tickets.get(world).get(modId).size();
        }
        else
        {
            return 0;
        }
    }

    private static ModContainer getContainer(Object mod)
    {
        ModContainer container = Loader.instance().getModObjectList().inverse().get(mod);
        return container;
    }

    private static int getMaxTicketLengthFor(String modId)
    {
        int allowedCount = ticketConstraints.containsKey(modId) && overridesEnabled ? ticketConstraints.get(modId) : defaultMaxCount;
        return allowedCount;
    }

    private static int getMaxChunkDepthFor(String modId)
    {
        int allowedCount = chunkConstraints.containsKey(modId) && overridesEnabled ? chunkConstraints.get(modId) : defaultMaxChunks;
        return allowedCount;
    }

    public static Ticket requestPlayerTicket(Object mod, EntityPlayer player, World world, Type type)
    {
        ModContainer mc = getContainer(mod);

        if (mc == null)
        {
            FMLLog.log(Level.SEVERE, "Failed to locate the container for mod instance %s (%s : %x)", mod, mod.getClass().getName(), System.identityHashCode(mod));
            return null;
        }

        if (playerTickets.get(player.getEntityName()).size() > playerTicketLength)
        {
            FMLLog.warning("Unable to assign further chunkloading tickets to player %s (on behalf of mod %s)", player.getEntityName(), mc.getModId());
            return null;
        }

        Ticket ticket = new Ticket(mc.getModId(), type, world, player);
        playerTickets.put(player.getEntityName(), ticket);
        tickets.get(world).put("Forge", ticket);
        return ticket;
    }

    public static Ticket requestTicket(Object mod, World world, Type type)
    {
        ModContainer container = getContainer(mod);

        if (container == null)
        {
            FMLLog.log(Level.SEVERE, "Failed to locate the container for mod instance %s (%s : %x)", mod, mod.getClass().getName(), System.identityHashCode(mod));
            return null;
        }

        String modId = container.getModId();

        if (!callbacks.containsKey(modId))
        {
            FMLLog.severe("The mod %s has attempted to request a ticket without a listener in place", modId);
            throw new RuntimeException("Invalid ticket request");
        }

        int allowedCount = ticketConstraints.containsKey(modId) ? ticketConstraints.get(modId) : defaultMaxCount;

        if (tickets.get(world).get(modId).size() >= allowedCount)
        {
            FMLLog.info("The mod %s has attempted to allocate a chunkloading ticket beyond it's currently allocated maximum : %d", modId, allowedCount);
            return null;
        }

        Ticket ticket = new Ticket(modId, type, world);
        tickets.get(world).put(modId, ticket);
        return ticket;
    }

    public static void releaseTicket(Ticket ticket)
    {
        if (ticket == null)
        {
            return;
        }

        if (ticket.isPlayerTicket() ? !playerTickets.containsValue(ticket) : !tickets.get(ticket.world).containsEntry(ticket.modId, ticket))
        {
            return;
        }

        if (ticket.requestedChunks != null)
        {
            for (ChunkCoordIntPair chunk : ImmutableSet.copyOf(ticket.requestedChunks))
            {
                unforceChunk(ticket, chunk);
            }
        }

        if (ticket.isPlayerTicket())
        {
            playerTickets.remove(ticket.player, ticket);
            tickets.get(ticket.world).remove("Forge", ticket);
        }
        else
        {
            tickets.get(ticket.world).remove(ticket.modId, ticket);
        }
    }

    public static void forceChunk(Ticket ticket, ChunkCoordIntPair chunk)
    {
        if (ticket == null || chunk == null)
        {
            return;
        }

        if (ticket.ticketType == Type.ENTITY && ticket.entity == null)
        {
            throw new RuntimeException("Attempted to use an entity ticket to force a chunk, without an entity");
        }

        if (ticket.isPlayerTicket() ? !playerTickets.containsValue(ticket) : !tickets.get(ticket.world).containsEntry(ticket.modId, ticket))
        {
            FMLLog.severe("The mod %s attempted to force load a chunk with an invalid ticket. This is not permitted.", ticket.modId);
            return;
        }

        ticket.requestedChunks.add(chunk);
        ImmutableSetMultimap<ChunkCoordIntPair, Ticket> newMap = ImmutableSetMultimap.<ChunkCoordIntPair, Ticket>builder().putAll(forcedChunks.get(ticket.world)).put(chunk, ticket).build();
        forcedChunks.put(ticket.world, newMap);

        if (ticket.maxDepth > 0 && ticket.requestedChunks.size() > ticket.maxDepth)
        {
            ChunkCoordIntPair removed = ticket.requestedChunks.iterator().next();
            unforceChunk(ticket, removed);
        }
    }

    public static void reorderChunk(Ticket ticket, ChunkCoordIntPair chunk)
    {
        if (ticket == null || chunk == null || !ticket.requestedChunks.contains(chunk))
        {
            return;
        }

        ticket.requestedChunks.remove(chunk);
        ticket.requestedChunks.add(chunk);
    }

    public static void unforceChunk(Ticket ticket, ChunkCoordIntPair chunk)
    {
        if (ticket == null || chunk == null)
        {
            return;
        }

        ticket.requestedChunks.remove(chunk);
        LinkedHashMultimap<ChunkCoordIntPair, Ticket> copy = LinkedHashMultimap.create(forcedChunks.get(ticket.world));
        copy.remove(chunk, ticket);
        ImmutableSetMultimap<ChunkCoordIntPair, Ticket> newMap = ImmutableSetMultimap.copyOf(copy);
        forcedChunks.put(ticket.world, newMap);
    }

    static void loadConfiguration()
    {
        for (String mod : config.categories.keySet())
        {
            if (mod.equals("Forge") || mod.equals("defaults"))
            {
                continue;
            }

            Property modTC = config.get(mod, "maximumTicketCount", 200);
            Property modCPT = config.get(mod, "maximumChunksPerTicket", 25);
            ticketConstraints.put(mod, modTC.getInt(200));
            chunkConstraints.put(mod, modCPT.getInt(25));
        }

        config.save();
    }

    public static ImmutableSetMultimap<ChunkCoordIntPair, Ticket> getPersistentChunksFor(World world)
    {
        return forcedChunks.containsKey(world) ? forcedChunks.get(world) : ImmutableSetMultimap.<ChunkCoordIntPair, Ticket>of();
    }

    static void saveWorld(World world)
    {
        if (!(world instanceof WorldServer))
        {
            return;
        }

        WorldServer worldServer = (WorldServer) world;
        File chunkDir = worldServer.getChunkSaveLocation();
        File chunkLoaderData = new File(chunkDir, "forcedchunks.dat");
        NBTTagCompound forcedChunkData = new NBTTagCompound();
        NBTTagList ticketList = new NBTTagList();
        forcedChunkData.setTag("TicketList", ticketList);
        Multimap<String, Ticket> ticketSet = tickets.get(worldServer);

        for (String modId : ticketSet.keySet())
        {
            NBTTagCompound ticketHolder = new NBTTagCompound();
            ticketList.appendTag(ticketHolder);
            ticketHolder.setString("Owner", modId);
            NBTTagList tickets = new NBTTagList();
            ticketHolder.setTag("Tickets", tickets);

            for (Ticket tick : ticketSet.get(modId))
            {
                NBTTagCompound ticket = new NBTTagCompound();
                ticket.setByte("Type", (byte) tick.ticketType.ordinal());
                ticket.setByte("ChunkListDepth", (byte) tick.maxDepth);

                if (tick.isPlayerTicket())
                {
                    ticket.setString("ModId", tick.modId);
                    ticket.setString("Player", tick.player);
                }

                if (tick.modData != null)
                {
                    ticket.setCompoundTag("ModData", tick.modData);
                }

                if (tick.ticketType == Type.ENTITY && tick.entity != null)
                {
                    ticket.setInteger("chunkX", MathHelper.floor_double(tick.entity.chunkCoordX));
                    ticket.setInteger("chunkZ", MathHelper.floor_double(tick.entity.chunkCoordZ));
                    ticket.setLong("PersistentIDMSB", tick.entity.getPersistentID().getMostSignificantBits());
                    ticket.setLong("PersistentIDLSB", tick.entity.getPersistentID().getLeastSignificantBits());
                    tickets.appendTag(ticket);
                }
                else if (tick.ticketType != Type.ENTITY)
                {
                    tickets.appendTag(ticket);
                }
            }
        }

        try
        {
            CompressedStreamTools.write(forcedChunkData, chunkLoaderData);
        }
        catch (IOException e)
        {
            FMLLog.log(Level.WARNING, e, "Unable to write forced chunk data to %s - chunkloading won't work", chunkLoaderData.getAbsolutePath());
            return;
        }
    }

    static void loadEntity(Entity entity)
    {
        UUID id = entity.getPersistentID();
        Ticket tick = pendingEntities.get(id);

        if (tick != null)
        {
            tick.bindEntity(entity);
            pendingEntities.remove(id);
        }
    }

    public static void putDormantChunk(long coords, Chunk chunk)
    {
        Cache<Long, Chunk> cache = dormantChunkCache.get(chunk.worldObj);

        if (cache != null)
        {
            cache.put(coords, chunk);
        }
    }

    public static Chunk fetchDormantChunk(long coords, World world)
    {
        Cache<Long, Chunk> cache = dormantChunkCache.get(world);
        return cache == null ? null : cache.getIfPresent(coords);
    }

    static void captureConfig(File configDir)
    {
        cfgFile = new File(configDir, "forgeChunkLoading.cfg");
        config = new Configuration(cfgFile, true);
        config.categories.clear();

        try
        {
            config.load();
        }
        catch (Exception e)
        {
            File dest = new File(cfgFile.getParentFile(), "forgeChunkLoading.cfg.bak");

            if (dest.exists())
            {
                dest.delete();
            }

            cfgFile.renameTo(dest);
            FMLLog.log(Level.SEVERE, e, "A critical error occured reading the forgeChunkLoading.cfg file, defaults will be used - the invalid file is backed up at forgeChunkLoading.cfg.bak");
        }

        config.addCustomCategoryComment("defaults", "Default configuration for forge chunk loading control");
        Property maxTicketCount = config.get("defaults", "maximumTicketCount", 200);
        maxTicketCount.comment = "The default maximum ticket count for a mod which does not have an override\n" +
                "in this file. This is the number of chunk loading requests a mod is allowed to make.";
        defaultMaxCount = maxTicketCount.getInt(200);
        Property maxChunks = config.get("defaults", "maximumChunksPerTicket", 25);
        maxChunks.comment = "The default maximum number of chunks a mod can force, per ticket, \n" +
                "for a mod without an override. This is the maximum number of chunks a single ticket can force.";
        defaultMaxChunks = maxChunks.getInt(25);
        Property playerTicketCount = config.get("defaults", "playetTicketCount", 500);
        playerTicketCount.comment = "The number of tickets a player can be assigned instead of a mod. This is shared across all mods and it is up to the mods to use it.";
        playerTicketLength = playerTicketCount.getInt(500);
        Property dormantChunkCacheSizeProperty = config.get("defaults", "dormantChunkCacheSize", 0);
        dormantChunkCacheSizeProperty.comment = "Unloaded chunks can first be kept in a dormant cache for quicker\n" +
                "loading times. Specify the size of that cache here";
        dormantChunkCacheSize = dormantChunkCacheSizeProperty.getInt(0);
        FMLLog.info("Configured a dormant chunk cache size of %d", dormantChunkCacheSizeProperty.getInt(0));
        Property modOverridesEnabled = config.get("defaults", "enabled", true);
        modOverridesEnabled.comment = "Are mod overrides enabled?";
        overridesEnabled = modOverridesEnabled.getBoolean(true);
        config.addCustomCategoryComment("Forge", "Sample mod specific control section.\n" +
                "Copy this section and rename the with the modid for the mod you wish to override.\n" +
                "A value of zero in either entry effectively disables any chunkloading capabilities\n" +
                "for that mod");
        Property sampleTC = config.get("Forge", "maximumTicketCount", 200);
        sampleTC.comment = "Maximum ticket count for the mod. Zero disables chunkloading capabilities.";
        sampleTC = config.get("Forge", "maximumChunksPerTicket", 25);
        sampleTC.comment = "Maximum chunks per ticket for the mod.";

        for (String mod : config.categories.keySet())
        {
            if (mod.equals("Forge") || mod.equals("defaults"))
            {
                continue;
            }

            Property modTC = config.get(mod, "maximumTicketCount", 200);
            Property modCPT = config.get(mod, "maximumChunksPerTicket", 25);
        }
    }

    public static Map<String, Property> getConfigMapFor(Object mod)
    {
        ModContainer container = getContainer(mod);

        if (container != null)
        {
            Map<String, Property> map = config.categories.get(container.getModId());

            if (map == null)
            {
                map = Maps.newHashMap();
                config.categories.put(container.getModId(), map);
            }

            return map;
        }

        return null;
    }

    public static void addConfigProperty(Object mod, String propertyName, String value, Property.Type type)
    {
        ModContainer container = getContainer(mod);

        if (container != null)
        {
            Map<String, Property> props = config.categories.get(container.getModId());
            props.put(propertyName, new Property(propertyName, value, type));
        }
    }
}
