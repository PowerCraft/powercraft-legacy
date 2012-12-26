package powercraft.management;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import powercraft.management.PC_Utils.SaveHandler;
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
            sendData.writeObject(PC_GlobalVariables.consts);
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
