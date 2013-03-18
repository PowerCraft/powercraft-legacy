package codechicken.core;

import java.net.Socket;
import java.util.List;

import codechicken.core.internal.ClientTickHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.MemoryConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.storage.SaveFormatComparator;
import net.minecraft.network.TcpConnection;
import net.minecraft.world.World;
import net.minecraft.client.multiplayer.WorldClient;

public class ClientUtils extends CommonUtils
{	
	public static Minecraft mc()
	{
		return Minecraft.getMinecraft();
	}
	
	public static World getWorld()
	{
		return mc().theWorld;
	}

	public static EntityPlayer getPlayer(String playername)
	{
		return playername == mc().thePlayer.username || playername == null ? mc().thePlayer : null;
	}

	public static boolean isClient(World world)
	{
		return world instanceof WorldClient;
	}
	
	public static boolean inWorld()
	{
		return mc().getNetHandler() != null;
	}
	
	@Deprecated
	public static void sendPacket(Packet packet)
	{
		mc().getNetHandler().addToSendQueue(packet);
	}

	public static void openSMPGui(int windowId, GuiScreen gui)
	{
		mc().displayGuiScreen(gui);
        if(windowId != 0)
        	mc().thePlayer.openContainer.windowId = windowId;
	}

	public static float getRenderFrame()
	{
		return mc().timer.renderPartialTicks;
	}
	
	public static double getRenderTime()
	{
	    return ClientTickHandler.renderTime + getRenderFrame();
	}	

	public static String getServerIP() throws NetworkClosedException
	{
		try
		{
			INetworkManager networkManager = mc().getNetHandler().netManager;
			if(networkManager instanceof MemoryConnection)
				return "memory";
			
			Socket socket = ((TcpConnection)networkManager).getSocket();
			if(socket == null)
				throw new NetworkClosedException();
			return socket.getInetAddress().getHostAddress()+":"+socket.getPort();
		}
		catch(Exception e)
		{
			FMLCommonHandler.instance().raiseException(e, "Code Chicken Core", true);
			return null;
		}
	}

	public static boolean isLocal() 
	{
		try
		{
			return getServerIP().equals("memory");
		}
		catch(NetworkClosedException e)
		{
			return true;
		}
	}

	@SideOnly(Side.CLIENT)
	public static String getWorldSaveName(String worldName)
	{
	    if(!isLocal())
	        return null;
	    return MinecraftServer.getServer().getFolderName();
	}
	
    public static MovingObjectPosition retraceBlock(World world, EntityPlayer entityplayer, int x, int y, int z)
    {
        Vec3 headVec = Vec3.createVectorHelper(entityplayer.posX, (entityplayer.posY + 1.62) - (double)entityplayer.yOffset, entityplayer.posZ);
        Vec3 lookVec = entityplayer.getLook(1.0F);
        double reach = mc().playerController.getBlockReachDistance();
        Vec3 endVec = headVec.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
        return Block.blocksList[world.getBlockId(x, y, z)].collisionRayTrace(world, x, y, z, headVec, endVec);
    }
}
