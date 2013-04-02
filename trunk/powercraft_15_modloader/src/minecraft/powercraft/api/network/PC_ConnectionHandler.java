package powercraft.api.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetLoginHandler;
import net.minecraft.src.Packet1Login;
import net.minecraft.src.Packet250CustomPayload;
import powercraft.api.PC_IDResolver;
import powercraft.api.utils.PC_GlobalVariables;

public class PC_ConnectionHandler {
	
	public void playerLoggedIn(EntityPlayer player, NetHandler netHandler, INetworkManager manager) {
		
	}
	
	public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		ObjectOutputStream sendData;
		
		try {
			sendData = new ObjectOutputStream(data);
			sendData.writeInt(PC_PacketHandler.PACKETIDS);
			sendData.writeObject(CompressedStreamTools.compress(PC_IDResolver.makeIDTagCompound()));
			sendData.writeObject(PC_GlobalVariables.consts);
			sendData.writeInt(PC_PacketHandler.PACKETIDS);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload("PowerCraft", data.toByteArray());
		manager.addToSendQueue(packet);
		return null;
	}
	
	public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {
	}
	
	public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {
	}
	
	public void connectionClosed(INetworkManager manager) {
	}
	
	public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {
	}
	
}
