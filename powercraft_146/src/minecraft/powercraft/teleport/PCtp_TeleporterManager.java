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

	private static HashMap<Integer, HashMap<PC_VecI, PCtp_TeleporterData>> teleoprter = new HashMap<Integer, HashMap<PC_VecI, PCtp_TeleporterData>>();
	private static boolean needSave;
	
	@Override
	public void load(NBTTagCompound nbtTag) {
		teleoprter.clear();
		int num = nbtTag.getInteger("count");
		for(int i=0; i<num; i++){
			int dimension = nbtTag.getInteger("key["+i+"]");
			NBTTagCompound nbtTag2 = nbtTag.getCompoundTag("value["+i+"]");
			int num2 = nbtTag2.getInteger("count");
			HashMap<PC_VecI, PCtp_TeleporterData> hm = new HashMap<PC_VecI, PCtp_TeleporterData>();
			for(int j=0; j<num; j++){
				PC_VecI key = new PC_VecI();
				SaveHandler.loadFromNBT(nbtTag2, "key["+j+"]", key);
				PCtp_TeleporterData td = new PCtp_TeleporterData();
				SaveHandler.loadFromNBT(nbtTag2, "value["+j+"]", td);
				hm.put(key, td);
			}
			teleoprter.put(dimension, hm);
		}
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbtTag) {
		needSave = false;
		nbtTag.setInteger("count", teleoprter.size());
		int i=0;
		for(Entry<Integer, HashMap<PC_VecI, PCtp_TeleporterData>> e:teleoprter.entrySet() ){
			nbtTag.setInteger("key["+i+"]", e.getKey());
			NBTTagCompound nbtTag2 = new NBTTagCompound();
			int j=0;
			nbtTag2.setInteger("count", e.getValue().size());
			for(Entry<PC_VecI, PCtp_TeleporterData> e2:e.getValue().entrySet()){
				SaveHandler.saveToNBT(nbtTag2, "key["+i+"]", e2.getKey());
				SaveHandler.saveToNBT(nbtTag2, "value["+i+"]", e2.getValue());
				j++;
			}
			nbtTag.setCompoundTag("value["+i+"]", nbtTag2);
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
	
	private static HashMap<PC_VecI, PCtp_TeleporterData> getMapForDimension(int dimension){
		HashMap<PC_VecI, PCtp_TeleporterData> hm;
		if(teleoprter.containsKey(dimension)){
			hm = teleoprter.get(dimension);
		}else{
			teleoprter.put(dimension, hm = new HashMap<PC_VecI, PCtp_TeleporterData>());
			needSave = true;
		}
		return hm;
	}
	
	public static void registerTeleporterData(int dimension, PC_VecI pos, PCtp_TeleporterData td){
		td.dimension = dimension;
		td.pos = pos;
		HashMap<PC_VecI, PCtp_TeleporterData> hm = getMapForDimension(dimension);
		hm.put(pos, td);
		needSave = true;
	}
	
	public static void releaseTeleporterData(int dimension, PC_VecI pos){
		if(!teleoprter.containsKey(dimension))
			return;
		HashMap<PC_VecI, PCtp_TeleporterData> hm = teleoprter.get(dimension);
		hm.remove(pos);
		if(hm.size()==0){
			teleoprter.remove(dimension);
		}
	}
	
	public static PCtp_TeleporterData getTeleporterData(int dimension, PC_VecI pos){
		if(!teleoprter.containsKey(dimension)){
			System.out.println("Dim not found");
			return null;
		}
		HashMap<PC_VecI, PCtp_TeleporterData> hm = teleoprter.get(dimension);
		if(!hm.containsKey(pos)){
			System.out.println("Pos not found");
			return null;
		}
		return hm.get(pos);
	}

	public static void teleportEntityTo(Entity entity, PC_VecI defaultTarget) {
		// TODO Auto-generated method stub
		
	}
	
	public static List<String> getTargetNames() {
		List<String> names = new ArrayList<String>();
		for(Entry<Integer, HashMap<PC_VecI, PCtp_TeleporterData>> e:teleoprter.entrySet() ){
			for(Entry<PC_VecI, PCtp_TeleporterData> e2:e.getValue().entrySet()){
				names.add(e2.getValue().name);
			}
		}
		return names;
	}

	public static boolean isNameOk(String text) {
		return !getTargetNames().contains(text);
	}

	public static PCtp_TeleporterData getTargetByName(String name) {
		for(Entry<Integer, HashMap<PC_VecI, PCtp_TeleporterData>> e:teleoprter.entrySet() ){
			for(Entry<PC_VecI, PCtp_TeleporterData> e2:e.getValue().entrySet()){
				if(name.equals(e2.getValue().name)){
					return e2.getValue();
				}
			}
		}
		return null;
	}
	
}
