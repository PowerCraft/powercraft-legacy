package codechicken.core;

import java.net.Socket;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.MemoryConnection;
import net.minecraft.network.packet.Packet;
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
		return mc().getSendQueue() != null;
	}

	public static void sendPacket(Packet packet)
	{
		mc().getSendQueue().addToSendQueue(packet);
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

	public static String getServerIP() throws NetworkClosedException
	{
		try
		{
			INetworkManager networkManager = mc().getSendQueue().netManager;
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

	@SuppressWarnings("unchecked")
	public static String getWorldSaveName(String worldName)
	{
		List<SaveFormatComparator> savelist = mc().getSaveLoader().getSaveList();
		for(SaveFormatComparator comp : savelist)
		{
			if(comp.getDisplayName().equals(worldName))
				return comp.getFileName();
		}
		System.err.println("Unable to find save location for: "+worldName);
		return worldName;
	}
}
