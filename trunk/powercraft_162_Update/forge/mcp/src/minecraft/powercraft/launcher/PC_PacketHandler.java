package powercraft.launcher;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PC_PacketHandler implements IPacketHandler{

	public static PC_PacketHandler handler;
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if(handler!=null){
			handler.onPacketData(manager, packet, player);
		}
	}

}
