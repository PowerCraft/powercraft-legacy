package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.SaveHandler;
import weasel.WeaselFunctionManager;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselVariableMap;

public class PCws_WeaselManager implements PC_IDataHandler, PC_IMSG {

	/**
	 * Globally shared variable pool
	 */
	private static WeaselVariableMap globalHeap = new WeaselVariableMap();
	
	private static TreeMap<Integer, PCws_WeaselNetwork> networks = new TreeMap<Integer, PCws_WeaselNetwork>();
	
	private static TreeMap<Integer, PCws_WeaselPlugin> plugins = new TreeMap<Integer, PCws_WeaselPlugin>();
	
	private static TreeMap<Integer, PCws_WeaselPluginInfo> pluginInfo = new TreeMap<Integer, PCws_WeaselPluginInfo>();
	
	private static WeaselFunctionManager globalFunctions;
	
	private static boolean needSave=false;
	
	@Override
	public void load(NBTTagCompound nbtTag) {
		needSave = false;
		globalHeap.clear();
		networks.clear();
		plugins.clear();
		
		SaveHandler.loadFromNBT(nbtTag, "globalHeap", globalHeap);
		
		NBTTagCompound nbtNetworks = nbtTag.getCompoundTag("networks");
		int num = nbtNetworks.getInteger("count");
		for(int i=0; i<num; i++){
			PCws_WeaselNetwork network = new PCws_WeaselNetwork();
			SaveHandler.loadFromNBT(nbtNetworks, "value["+i+"]", network);
		}
		
		NBTTagCompound nbtPlugins = nbtTag.getCompoundTag("plugins");
		num = nbtPlugins.getInteger("count");
		for(int i=0; i<num; i++){
			PCws_WeaselPlugin plugin = createPlugin(nbtPlugins.getInteger("type["+i+"]"));
			SaveHandler.loadFromNBT(nbtPlugins, "value["+i+"]", plugin);
		}
		
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbtTag) {
		needSave = false;
		SaveHandler.saveToNBT(nbtTag, "globalHeap", globalHeap);
		
		NBTTagCompound nbtNetworks = new NBTTagCompound();
		nbtNetworks.setInteger("count", networks.size());
		int i=0;
		for(Entry<Integer, PCws_WeaselNetwork> network:networks.entrySet()){
			SaveHandler.saveToNBT(nbtNetworks, "value["+i+"]", network.getValue());
			i++;
		}
		nbtTag.setCompoundTag("networks", nbtNetworks);
		
		NBTTagCompound nbtPlugins = new NBTTagCompound();
		nbtPlugins.setInteger("count", plugins.size());
		i=0;
		for(Entry<Integer, PCws_WeaselPlugin> plugin:plugins.entrySet()){
			int type = -1;
			for(Entry<Integer, PCws_WeaselPluginInfo> e:pluginInfo.entrySet()){
				if(e.getValue().getPluginClass().equals(plugin.getValue().getClass())){
					type = e.getKey();
				}
			}
			nbtPlugins.setInteger("type["+i+"]", type);
			SaveHandler.saveToNBT(nbtPlugins, "value["+i+"]", plugin.getValue());
			i++;
		}
		nbtTag.setCompoundTag("plugins", nbtPlugins);
		
		return nbtTag;
	}

	@Override
	public boolean needSave() {
		if(needSave)
			return true;
		for(PCws_WeaselNetwork net:networks.values()){
			if(net.needSave())
				return true;
		}
		for(PCws_WeaselPlugin plugin:plugins.values()){
			if(plugin.needSave())
				return true;
		}
		return false;
	}

	@Override
	public void reset() {
		needSave = false;
		globalHeap.clear();
		networks.clear();
		plugins.clear();
	}
	
	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_TICK_EVENT:
			update();
			return true;
		}
		return null;
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
	public static void setGlobalVariable(String name, WeaselObject value) throws WeaselRuntimeException {
		globalHeap.setVariableForce(name, value);
		needSave = true;
	}

	/**
	 * Get state of a weasel variable.
	 * 
	 * @param name variable name
	 * @return variable value.
	 */
	public static WeaselObject getGlobalVariable(String name) {
		if(globalHeap.getVariable(name) == null) throw new WeaselRuntimeException("Global network does't contain variable "+name);
		return globalHeap.getVariable(name);
	}

	/**
	 * Get if globvar exists
	 * 
	 * @param name variable name
	 * @return variable value.
	 */
	public static boolean hasGlobalVariable(String name) {
		return globalHeap.getVariable(name) != null;
	}
	
	public static int registerNetwork(PCws_WeaselNetwork weaselNetwork) {
		if(networks.containsValue(weaselNetwork))
			return -1;
		int i=1;
		while(networks.containsKey(i))
			i++;
		networks.put(i, weaselNetwork);
		needSave = true;
		return i;
	}

	public static void registerNetwork(PCws_WeaselNetwork weaselNetwork, int id) {
		if(!(networks.containsKey(id)||networks.containsValue(weaselNetwork))){
			networks.put(id, weaselNetwork);
			needSave = true;
		}
	}
	
	public static PCws_WeaselNetwork getNetwork(int id) {
		return networks.get(id); 
	}

	public static PCws_WeaselNetwork getNetwork(String name) {
		for(PCws_WeaselNetwork weaselNetwork:networks.values()){
			if(weaselNetwork.getName() != null && weaselNetwork.getName().equals(name)){
				return weaselNetwork;
			}
		}
		return null;
	}
	
	public static void removeNetwork(PCws_WeaselNetwork weaselNetwork) {
		weaselNetwork.remove();
		networks.remove(weaselNetwork.getID());
		needSave = true;
	}
	
	
	public static int registerPlugin(PCws_WeaselPlugin weaselPlugin) {
		if(plugins.containsValue(weaselPlugin))
			return -1;
		int i=1;
		while(plugins.containsKey(i))
			i++;
		plugins.put(i, weaselPlugin);
		needSave = true;
		return i;
	}
	
	public static void registerPlugin(PCws_WeaselPlugin weaselPlugin, int id) {
		if(!(plugins.containsKey(id)||plugins.containsValue(weaselPlugin))){
			plugins.put(id, weaselPlugin);
			needSave = true;
		}
	}
	
	public static PCws_WeaselPlugin getPlugin(int i) {
		return plugins.get(i);
	}
	
	public static PCws_WeaselPlugin getPlugin(String name) {
		for(PCws_WeaselPlugin weaselPlugin:plugins.values()){
			if(weaselPlugin.getName() != null && weaselPlugin.getName().equals(name)){
				return weaselPlugin;
			}
		}
		return null;
	}
	
	public static PCws_WeaselPlugin getPlugin(World world, int x, int y, int z) {
		for(PCws_WeaselPlugin weaselPlugin:plugins.values()){
			if(weaselPlugin.isOnPlace(world, x, y, z)){
				return weaselPlugin;
			}
		}
		return null;
	}
	
	public static void removePlugin(PCws_WeaselPlugin weaselPlugin) {
		if(weaselPlugin.getNetwork()!=null){
			weaselPlugin.getNetwork().removeMember(weaselPlugin);
		}
		plugins.remove(weaselPlugin.getID());
		needSave = true;
	}
	
	public static void registerPluginInfo(PCws_WeaselPluginInfo info, int id){
		if(!pluginInfo.containsKey(id))
			pluginInfo.put(id, info);
	}
	
	public static PCws_WeaselPlugin createPlugin(int id){
		return pluginInfo.get(id).createPlugin();
	}
	
	public static PCws_WeaselPluginInfo getPluginInfo(int id){
		return pluginInfo.get(id);
	}
	
	public static TreeMap<Integer, PCws_WeaselPluginInfo> getPluginInfoMap(){
		return pluginInfo;
	}
	
	public static void update(){
		if(globalFunctions==null){
			globalFunctions = new WeaselFunctionManager();
			List<PC_IMSG> msgs = ModuleInfo.getMSGObjects();
			for(PC_IMSG msg:msgs){
				Object o = msg.msg(PC_Utils.MSG_GET_PROVIDET_GLOBAL_FUNCTIONS, new ArrayList<PC_Struct3<String, String, Object>>());
				if(o instanceof List){
					List<PC_Struct3<String, String, Object>> l = (List<PC_Struct3<String, String, Object>>)o;
					for(PC_Struct3<String, String, Object>s:l){
						globalFunctions.registerMethod(s.a, s.b, s.c);
					}
				}
			}
		}
		for(PCws_WeaselPlugin weaselPlugin:plugins.values()){
			weaselPlugin.update();
		}
	}

	public static List<String> getAllPluginNames() {
		List<String> l = new ArrayList<String>();
		for(PCws_WeaselPlugin weaselPlugin:plugins.values()){
			l.add(weaselPlugin.getName());
		}
		return l;
	}
	
	public static List<String> getAllNetworkNames() {
		List<String> l = new ArrayList<String>();
		for(PCws_WeaselNetwork weaselNetwork:networks.values()){
			l.add(weaselNetwork.getName());
		}
		return l;
	}

	public static WeaselFunctionManager getGlobalFunctionManager() {
		return globalFunctions;
	}
	
}
