package powercraft.teleport;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_Utils.SaveHandler;
import powercraft.management.PC_VecI;

public class PCtp_TeleporterManager implements PC_IDataHandler {

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

	public static void registerTeleporterData(int dimension, PC_VecI pos, PCtp_TeleporterData td){
		
	}
	
	@Override
	public boolean needSave() {
		return needSave;
	}
	
}
