package powercraft.api;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.registry.PC_MSGRegistry;

public class PC_ClientPacketHandler extends PC_PacketHandler {

	@Override
	protected void handleIncomingPacketHandlerPacket(ObjectInputStream input, EntityPlayer player) throws IOException, ClassNotFoundException{
		String name = (String)input.readObject();
		Object[] o = (Object[])input.readObject();
		PC_IPacketHandler ph = packetHandler.get(name);
		if(ph!=null){
			ph.handleIncomingPacket(player, o);
		}
	}
	
	@Override
	protected void handleIncomingGuiPacket(ObjectInputStream input, EntityPlayer player) throws ClassNotFoundException, IOException{
		PC_GresRegistry.openGres("", player, null, new Object[]{input});
    }

	@Override
	protected void handleIncomingIDPacket(ObjectInputStream input, EntityPlayer player) throws ClassNotFoundException, IOException{
		byte[] b = (byte[])input.readObject();
		PC_IDResolver.loadIDFromTagCompound(CompressedStreamTools.decompress(b), false);
		PC_GlobalVariables.oldConsts = (HashMap<String, Object>) PC_GlobalVariables.consts.clone();
		PC_GlobalVariables.consts.putAll((HashMap<String, Object>)input.readObject());
		PC_MSGRegistry.callAllMSG(PC_MSGRegistry.MSG_LOAD_WORLD);
	}
	
}
