package powercraft.management;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import powercraft.management.PC_Utils.SaveHandler;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetLoginHandler;
import net.minecraft.src.Packet1Login;
import net.minecraft.src.Packet250CustomPayload;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;

public class PC_ConnectionHandler implements IConnectionHandler {

	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {
		
	}

	@Override
	public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) {
		System.out.println("connectionReceived");
		ByteArrayOutputStream data = new ByteArrayOutputStream();
        ObjectOutputStream sendData;

        
        
        try
        {
            sendData = new ObjectOutputStream(data);
            sendData.writeInt(PC_PacketHandler.PACKETIDS);
            sendData.writeObject(CompressedStreamTools.compress(SaveHandler.makeIDTagCompound()));
            sendData.writeInt(PC_PacketHandler.PACKETIDS);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet =  new Packet250CustomPayload("PowerCraft", data.toByteArray());
		manager.addToSendQueue(packet);
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {
	}

	@Override
	public void connectionClosed(INetworkManager manager) {
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {
	}

}
