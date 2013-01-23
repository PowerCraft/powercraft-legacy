package powercraft.management;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.NetLoginHandler;
import net.minecraft.src.Packet250CustomPayload;
import powercraft.management.PC_Utils.SaveHandler;

public class PC_ConnectionHandler {

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
	
}
