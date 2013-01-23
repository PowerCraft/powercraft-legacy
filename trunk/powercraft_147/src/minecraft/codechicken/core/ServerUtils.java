package codechicken.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.block.Block;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.command.CommandHandler;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.command.ICommand;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class ServerUtils extends CommonUtils
{
	public static MinecraftServer mc()
	{
		return MinecraftServer.getServer();
	}

	public static EntityPlayerMP getPlayer(String playername)
	{
		return mc().getConfigurationManager().getPlayerForUsername(playername);
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<EntityPlayer> getAllPlayers()
	{
		return new ArrayList<EntityPlayer>(mc().getConfigurationManager().playerEntityList);
	}

	public static ArrayList<EntityPlayer> getPlayersInDimension(int dimension)
	{
		ArrayList<EntityPlayer> allplayers = getAllPlayers();
		for(Iterator<EntityPlayer> iterator = allplayers.iterator(); iterator.hasNext();)
		{
			if(iterator.next().dimension != dimension)
			{
				iterator.remove();
			}
		}
		return allplayers;
	}

	public static double getBlockReachDistance(EntityPlayerMP entityplayer)
	{
		return entityplayer.theItemInWorldManager.getBlockReachDistance();
	}

	public static MovingObjectPosition retraceBlock(World world, EntityPlayer entityplayer, int x, int y, int z)
    {
        Vec3 headVec = Vec3.createVectorHelper(entityplayer.posX, (entityplayer.posY + 1.62) - (double)entityplayer.yOffset, entityplayer.posZ);
        Vec3 lookVec = entityplayer.getLook(1.0F);
        double reach = getBlockReachDistance((EntityPlayerMP) entityplayer);
        Vec3 endVec = headVec.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
        return Block.blocksList[world.getBlockId(x, y, z)].collisionRayTrace(world, x, y, z, headVec, endVec);
    }
	
	@SuppressWarnings("unchecked")
	public static void sendPacketTo(EntityPlayer player, Packet packet)
	{
		if(player == null)
		{
			for(EntityPlayerMP player2 : (List<EntityPlayerMP>)mc().getConfigurationManager().playerEntityList)
            {
				player2.playerNetServerHandler.sendPacketToPlayer(packet);
            }
		}
		else
		{
			((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(packet);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void sendPacketToAllExcept(Packet packet, EntityPlayer player)
	{
		for(EntityPlayerMP player2 : (List<EntityPlayerMP>)mc().getConfigurationManager().playerEntityList)
        {
			if(player2 == player)
				continue;
			
            sendPacketTo(player2, packet);
        }
	}

	@SuppressWarnings("unchecked")
	public static void sendPacketToPostion(int i, int j, Packet packet)
	{
		ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i >> 4, j >> 4);
        List<EntityPlayerMP> list = mc().getConfigurationManager().playerEntityList;

        for (EntityPlayerMP entityplayermp : list)
        {
            if (!entityplayermp.loadedChunks.contains(chunkcoordintpair))
            {
                entityplayermp.playerNetServerHandler.sendPacketToPlayer(packet);
            }
        }
	}

	public static void sendPacketToAll(Packet packet)
	{
		sendPacketTo(null, packet);
	}

	@SuppressWarnings("unchecked")
	public static void sendChatToOps(String message)
	{
		List<String> lines = formatMessage(message);
		for(EntityPlayerMP player : (List<EntityPlayerMP>)mc().getConfigurationManager().playerEntityList)
		{
			if(!isPlayerOP(player.username))
				continue;
			
			for(String s : lines)
			{
				sendPacketTo(player, new Packet3Chat(s));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void sendChatToAll(String message)
	{
		List<String> lines = formatMessage(message);
		for(EntityPlayerMP player : (List<EntityPlayerMP>)mc().getConfigurationManager().playerEntityList)
		{			
			for(String s : lines)
			{
				sendPacketTo(player, new Packet3Chat(s));
			}
		}		
	}

	public static void sendChatTo(EntityPlayerMP player, String message)
	{
		List<String> lines = formatMessage(message);
		for(String s : lines)
		{
			sendPacketTo(player, new Packet3Chat(s));
		}
	}
	
	public static void openSMPContainer(EntityPlayerMP player, Container container, IGuiPacketSender packetSender)
	{
		player.incrementWindowID();
		player.closeInventory();
		packetSender.sendPacket(player, player.currentWindowId);		
		player.openContainer = container; 
		player.openContainer.windowId = player.currentWindowId;		
		player.openContainer.addCraftingToCrafters(player);
	}

	public static boolean isPlayerOP(String username)
	{
		return mc().getConfigurationManager().areCommandsAllowed(username);
	}
	
	public static boolean isPlayerOwner(String username)
	{
		return mc().isSinglePlayer() && mc().getServerOwner().equalsIgnoreCase(username);
	}
	
	public static void registerCommand(ICommand command)
	{
		((CommandHandler)mc().getCommandManager()).registerCommand(command);
	}
}
