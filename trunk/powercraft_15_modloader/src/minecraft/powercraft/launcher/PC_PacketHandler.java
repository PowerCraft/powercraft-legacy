package powercraft.launcher;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Packet250CustomPayload;

public class PC_PacketHandler{

	public static PC_PacketHandler handler;
	
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, EntityPlayer player) {
		if(handler!=null){
			handler.onPacketData(manager, packet, player);
		}
	}

}
