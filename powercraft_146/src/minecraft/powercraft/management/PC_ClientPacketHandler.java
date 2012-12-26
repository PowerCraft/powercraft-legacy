package powercraft.management;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.SaveHandler;

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
		Gres.openGres("", player, new Object[]{input});
    }

	@Override
	protected void handleIncomingIDPacket(ObjectInputStream input, EntityPlayer player) throws ClassNotFoundException, IOException{
		byte[] b = (byte[])input.readObject();
		SaveHandler.loadIDFromTagCompound(CompressedStreamTools.decompress(b));
		PC_GlobalVariables.oldConsts = (HashMap<String, Object>) PC_GlobalVariables.consts.clone();
		PC_GlobalVariables.consts.putAll((HashMap<String, Object>)input.readObject());
	}
	
}
