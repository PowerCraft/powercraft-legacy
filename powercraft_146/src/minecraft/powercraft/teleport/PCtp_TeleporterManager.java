package powercraft.teleport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.SaveHandler;
import powercraft.management.PC_VecI;

public class PCtp_TeleporterManager implements PC_IDataHandler, PC_IPacketHandler {

	private static List<PCtp_TeleporterData> teleoprter = new ArrayList<PCtp_TeleporterData>();
	private static boolean needSave;
	
	@Override
	public void load(NBTTagCompound nbtTag) {
		teleoprter.clear();
		int num = nbtTag.getInteger("count");
		for(int i=0; i<num; i++){
			PCtp_TeleporterData td = new PCtp_TeleporterData();
			SaveHandler.loadFromNBT(nbtTag, "value["+i+"]", td);
			teleoprter.add(td);
		}
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbtTag) {
		needSave = false;
		nbtTag.setInteger("count", teleoprter.size());
		int i=0;
		for(PCtp_TeleporterData td:teleoprter){
			SaveHandler.saveToNBT(nbtTag, "value["+i+"]", td);
			i++;
		}
		return nbtTag;
	}

	@Override
	public boolean needSave() {
		return needSave;
	}
	
	@Override
	public boolean handleIncomingPacket(EntityPlayer player, Object[] o) {
		PCtp_TeleporterData td = (PCtp_TeleporterData)o[0];
		String target = (String)o[1];
		td.defaultTarget = getTargetByName(target).pos;
		PCtp_TeleporterData td2 = getTeleporterData(td.dimension, td.pos);
		td2.setTo(td);
		return false;
	}
	
	public static void openGui(EntityPlayer entityPlayer, int x, int y, int z){
		int dimension = entityPlayer.dimension;
		openGui(entityPlayer, getTeleporterData(dimension, new PC_VecI(x, y, z)));
	}
	
	public static void openGui(EntityPlayer entityPlayer, PCtp_TeleporterData td) {
		List<String> names = getTargetNames();
		Gres.openGres("Teleporter", entityPlayer, td, names);
	}
	
	public static void registerTeleporterData(int dimension, PC_VecI pos, PCtp_TeleporterData td){
		releaseTeleporterData(dimension, pos);
		td.dimension = dimension;
		td.pos = pos;
		teleoprter.add(td);
		needSave = true;
	}
	
	public static void releaseTeleporterData(int dimension, PC_VecI pos){
		teleoprter.remove(getTeleporterData(dimension, pos));
	}
	
	public static PCtp_TeleporterData getTeleporterData(int dimension, PC_VecI pos){
		for(PCtp_TeleporterData td:teleoprter){
			if(td.dimension == dimension && pos.equals(td.pos))
				return td;
		}
		return null;
	}

	public static void teleportEntityTo(Entity entity, PC_VecI defaultTarget) {
		// TODO Auto-generated method stub
		
	}
	
	public static List<String> getTargetNames() {
		List<String> names = new ArrayList<String>();
		for(PCtp_TeleporterData td:teleoprter){
			if(td.name!=null && !td.name.equals(""))
				names.add(td.name);
		}
		return names;
	}

	public static boolean isNameOk(String text) {
		return !getTargetNames().contains(text);
	}

	public static PCtp_TeleporterData getTargetByName(String name) {
		for(PCtp_TeleporterData td:teleoprter){
			if(name.equals(td.name)){
				return td;
			}
		}
		return null;
	}
	
}
