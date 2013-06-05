package powercraft.api.structure;

import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.api.interfaces.PC_IDataHandler;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

public class PC_CableNetworkManager implements PC_IDataHandler {

	private static TreeMap<Integer, PC_CableNetworkChunk> networks = new TreeMap<Integer, PC_CableNetworkChunk>();
	
	public static PC_CableNetworkChunk getNetwork(int network){
		return networks.get(network);
	}
	
	public static int getNetworkID(PC_CableNetworkChunk network){
		for(Entry<Integer, PC_CableNetworkChunk>e:networks.entrySet()){
			if(e.getValue()==network){
				return e.getKey();
			}
		}
		Set<Integer> keys = networks.keySet();
		int newKey=1;
		while(keys.contains(newKey)){
			newKey++;
		}
		networks.put(newKey, network);
		return newKey;
	}

	public static void addRef(int network, PC_VecI pos){
		getNetwork(network).addRef();
	}
	
	public static void release(int network, PC_VecI pos){
		if(networks.containsKey(network)){
			getNetwork(network).removeIO(pos);
			if(getNetwork(network).release()){
				networks.remove(network);
			}
		}
	}
	
	@Override
	public String getName() {
		return "PC_CableNetworks";
	}

	@Override
	public void load(NBTTagCompound nbtTag) {
		int count = nbtTag.getInteger("count");
		for(int i=0; i<count; i++){
			int key = nbtTag.getInteger("key["+i+"]");
			PC_CableNetworkChunk net = PC_Utils.loadFromNBT(nbtTag, "value["+i+"]", new PC_CableNetworkChunk());
			networks.put(key, net);
		}
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbtTag) {
		nbtTag.setInteger("count", networks.size());
		int i=0;
		for(Entry<Integer, PC_CableNetworkChunk>e:networks.entrySet()){
			nbtTag.setInteger("key["+i+"]", e.getKey());
			PC_Utils.saveToNBT(nbtTag, "value["+i+"]", e.getValue());
			i++;
		}
		return nbtTag;
	}

	@Override
	public boolean needSave() {
		return true;
	}

	@Override
	public void reset() {
		networks.clear();
	}
	
}
