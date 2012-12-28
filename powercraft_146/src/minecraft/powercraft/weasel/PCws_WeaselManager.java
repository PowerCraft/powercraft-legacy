package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_Utils.SaveHandler;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselVariableMap;

public class PCws_WeaselManager implements PC_IDataHandler {

	/**
	 * Globally shared variable pool
	 */
	private static WeaselVariableMap globalHeap = new WeaselVariableMap();
	
	private static TreeMap<Integer, PCws_WeaselNetwork> networks = new TreeMap<Integer, PCws_WeaselNetwork>();
	
	private static TreeMap<Integer, PCws_WeaselPlugin> plugins = new TreeMap<Integer, PCws_WeaselPlugin>();
	
	private static boolean needSave=false;
	
	@Override
	public void load(NBTTagCompound nbtTag) {
		needSave = false;
		globalHeap.clear();
		SaveHandler.loadFromNBT(nbtTag, "globalHeap", globalHeap);
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbtTag) {
		needSave = false;
		SaveHandler.saveToNBT(nbtTag, "globalHeap", globalHeap);
		return nbtTag;
	}

	@Override
	public boolean needSave() {
		return needSave;
	}

	@Override
	public void reset() {
		needSave = false;
		globalHeap.clear();
	}

	/**
	 * Set a weasel variable into the global weasel bus. You should use some
	 * prefixes in order to prevent cross-system conflicts.
	 * 
	 * @param name variable key - name
	 * @param value variable value
	 * @throws WeaselRuntimeException if you are trying to store incompatible
	 *             variable type
	 */
	public void setGlobalVariable(String name, WeaselObject value) throws WeaselRuntimeException {
		globalHeap.setVariableForce(name, value);
		needSave = true;
	}

	/**
	 * Get state of a weasel variable.
	 * 
	 * @param name variable name
	 * @return variable value.
	 */
	public WeaselObject getGlobalVariable(String name) {
		if(globalHeap.getVariable(name) == null) throw new WeaselRuntimeException("Global network does't contain variable "+name);
		return globalHeap.getVariable(name);
	}

	/**
	 * Get if globvar exists
	 * 
	 * @param name variable name
	 * @return variable value.
	 */
	public boolean hasGlobalVariable(String name) {
		return globalHeap.getVariable(name) != null;
	}
	
	public static int registerNetwork(PCws_WeaselNetwork weaselNetwork) {
		if(networks.containsValue(weaselNetwork))
			return -1;
		int i=1;
		while(networks.containsKey(i))
			i++;
		networks.put(i, weaselNetwork);
		return i;
	}

	public static void registerNetwork(PCws_WeaselNetwork weaselNetwork, int id) {
		if(!(networks.containsKey(id)||networks.containsValue(weaselNetwork))){
			networks.put(id, weaselNetwork);
		}
	}
	
	public static PCws_WeaselNetwork getNetwork(int id) {
		return networks.get(id); 
	}

	public static void removeNetwork(PCws_WeaselNetwork weaselNetwork) {
		weaselNetwork.remove();
		networks.remove(weaselNetwork);
	}
	
	
	public static int registerPlugin(PCws_WeaselPlugin weaselPlugin) {
		if(plugins.containsValue(weaselPlugin))
			return -1;
		int i=1;
		while(plugins.containsKey(i))
			i++;
		plugins.put(i, weaselPlugin);
		return i;
	}
	
	public static void registerPlugin(PCws_WeaselPlugin weaselPlugin, int id) {
		if(!(plugins.containsKey(id)||plugins.containsValue(weaselPlugin))){
			plugins.put(id, weaselPlugin);
		}
	}
	
	public static PCws_WeaselPlugin getPlugin(int i) {
		return plugins.get(i);
	}
	
	public static void removePlugin(PCws_WeaselPlugin weaselPlugin) {
		plugins.remove(weaselPlugin.getID());
	}
	
}
