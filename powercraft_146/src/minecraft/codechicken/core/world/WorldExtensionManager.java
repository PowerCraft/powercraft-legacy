package codechicken.core.world;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

import codechicken.core.PacketCustom;
import codechicken.core.PacketCustom.ICustomPacketHandler.IClientPacketHandler;


import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.client.Minecraft;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.world.ChunkWatchEvent.UnWatch;
import net.minecraftforge.event.world.ChunkWatchEvent.Watch;

public class WorldExtensionManager
{
    private static final String channel = "CCWEM";
    
    public static class WorldExtensionEventHandler
    {
        private Chunk dataLoad;
        private Chunk dataUnload;

        @ForgeSubscribe
        public void onChunkDataLoad(ChunkDataEvent.Load event)
        {
            if(!worldMap.containsKey(event.world))
                WorldExtensionManager.onWorldLoad(event.world);
            
            createChunkExtension(event.world, event.getChunk());

            for(WorldExtension extension : worldMap.get(event.world))
                extension.loadChunkData(event.getChunk(), event.getData());
            
            dataLoad = event.getChunk();
        }

        @ForgeSubscribe
        public void onChunkDataSave(ChunkDataEvent.Save event)
        {
            for(WorldExtension extension : worldMap.get(event.world))
                extension.saveChunkData(event.getChunk(), event.getData());
            
            if(dataUnload == event.getChunk())
                removeChunk(event.world, event.getChunk());
        }
        
        @ForgeSubscribe
        public void onChunkLoad(ChunkEvent.Load event)
        {
            if(!worldMap.containsKey(event.world))
                WorldExtensionManager.onWorldLoad(event.world);
            
            if(dataLoad != event.getChunk())
                createChunkExtension(event.world, event.getChunk());
            
            for(WorldExtension extension : worldMap.get(event.world))
                extension.loadChunk(event.getChunk());
        }

        @ForgeSubscribe
        public void onChunkUnLoad(ChunkEvent.Unload event)
        {
            for(WorldExtension extension : worldMap.get(event.world))
                extension.unloadChunk(event.getChunk());
            
            if(event.world.isRemote)
                removeChunk(event.world, event.getChunk());
            else
                dataUnload = event.getChunk();
        }

        @ForgeSubscribe
        public void onWorldSave(WorldEvent.Save event)
        {
            for(WorldExtension extension : worldMap.get(event.world))
                extension.save();
        }

        @ForgeSubscribe
        public void onWorldLoad(WorldEvent.Load event)
        {
            if(!worldMap.containsKey(event.world))
                WorldExtensionManager.onWorldLoad(event.world);
        }

        @ForgeSubscribe
        public void onWorldUnLoad(WorldEvent.Unload event)
        {
            for(WorldExtension extension : worldMap.remove(event.world))
                extension.unload();
        }

        @ForgeSubscribe
        public void onChunkWatch(Watch event)
        {
            PacketCustom packet = new PacketCustom(channel, 1);
            packet.writeInt(event.chunk.chunkXPos);
            packet.writeInt(event.chunk.chunkZPos);
            packet.sendToPlayer(event.player);
            
            for(WorldExtension extension : worldMap.get(event.player.worldObj))
                extension.watchChunk(event.player.worldObj.getChunkFromChunkCoords(event.chunk.chunkXPos, event.chunk.chunkZPos), event.player);
        }

        @ForgeSubscribe
        public void onChunkUnWatch(UnWatch event)
        {
            for(WorldExtension extension : worldMap.get(event.player.worldObj))
                extension.unwatchChunk(event.player.worldObj.getChunkFromChunkCoords(event.chunk.chunkXPos, event.chunk.chunkZPos), event.player);
        }
    }
    
    public static class WorldExtensionClientTickHandler implements ITickHandler
    {
        @Override
        public void tickStart(EnumSet<TickType> type, Object... tickData)
        {
            if(type.contains(TickType.CLIENT))
            {
                World world = Minecraft.getMinecraft().theWorld;
                if(worldMap.containsKey(world))
                    preTick(world);
            }
        }

        @Override
        public void tickEnd(EnumSet<TickType> type, Object... tickData)
        {
            if(type.contains(TickType.CLIENT))
            {
                World world = Minecraft.getMinecraft().theWorld;
                if(worldMap.containsKey(world))
                    postTick(world);
            }
        }

        @Override
        public EnumSet<TickType> ticks()
        {
            return EnumSet.of(TickType.CLIENT);
        }

        @Override
        public String getLabel()
        {
            return "WorldExtenstions";
        }
    }
    
    public static class WorldExtensionServerTickHandler implements ITickHandler
    {
        @Override
        public void tickStart(EnumSet<TickType> type, Object... tickData)
        {
            if(type.contains(TickType.WORLD))
            {
                preTick((World)tickData[0]);
            }
        }

        @Override
        public void tickEnd(EnumSet<TickType> type, Object... tickData)
        {
            if(type.contains(TickType.WORLD))
            {
                postTick((World)tickData[0]);
            }
        }

        @Override
        public EnumSet<TickType> ticks()
        {
            return EnumSet.of(TickType.WORLD, TickType.CLIENT);
        }

        @Override
        public String getLabel()
        {
            return "WorldExtenstions";
        }
    }
    
    public static class WorldExtensionManagerPacketHandler implements IClientPacketHandler
    {
        @Override
        public void handlePacket(PacketCustom packetCustom, NetClientHandler nethandler, Minecraft mc)
        {
            switch(packetCustom.getType())
            {
                case 1:
                    World world = mc.theWorld;
                    Chunk chunk = world.getChunkFromChunkCoords(packetCustom.readInt(), packetCustom.readInt());
                    createChunkExtension(world, chunk);
                    
                    for(WorldExtension extension : worldMap.get(world))
                        extension.loadChunk(chunk);
            }
        }        
    }
    
    private static boolean initialised;
    private static ArrayList<WorldExtensionInstantiator> extensionIntialisers = new ArrayList<WorldExtensionInstantiator>();
    
    public static void registerWorldExtension(WorldExtensionInstantiator init)
    {
        if(!initialised)
            init();
        
        init.instantiatorID = extensionIntialisers.size();
        extensionIntialisers.add(init);
    }

    private static void init()
    {
        initialised = true;
        MinecraftForge.EVENT_BUS.register(new WorldExtensionEventHandler());
        TickRegistry.registerTickHandler(new WorldExtensionServerTickHandler(), Side.SERVER);
        if(FMLCommonHandler.instance().getSide().isClient())
        {
            TickRegistry.registerTickHandler(new WorldExtensionClientTickHandler(), Side.CLIENT);
            PacketCustom.assignHandler(channel, 0, 255, new WorldExtensionManagerPacketHandler());
        }
    }

    private static HashMap<World, WorldExtension[]> worldMap = new HashMap<World, WorldExtension[]>();
    
    private static void onWorldLoad(World world)
    {
        WorldExtension[] extensions = new WorldExtension[extensionIntialisers.size()];
        for(int i = 0; i < extensions.length; i++)
            extensions[i] = extensionIntialisers.get(i).createWorldExtension(world);
        
        worldMap.put(world, extensions);
        
        for(WorldExtension extension : extensions)
            extension.load();
    }

    private static void createChunkExtension(World world, Chunk chunk)
    {
        WorldExtension[] extensions = worldMap.get(world);
        for(int i = 0; i < extensionIntialisers.size(); i++)
            extensions[i].addChunk(extensionIntialisers.get(i).createChunkExtension(chunk, extensions[i]));
    }
    
    private static void removeChunk(World world, Chunk chunk)
    {
        for(WorldExtension extension : worldMap.get(world))
            extension.remChunk(chunk);
    }
    
    private static void preTick(World world)
    {
        for(WorldExtension extension : worldMap.get(world))
            extension.preTick();
    }
    
    private static void postTick(World world)
    {
        for(WorldExtension extension : worldMap.get(world))
            extension.postTick();
    }

    public static WorldExtension getWorldExtension(World world, int instantiatorID)
    {
        return worldMap.get(world)[instantiatorID];
    }
}
