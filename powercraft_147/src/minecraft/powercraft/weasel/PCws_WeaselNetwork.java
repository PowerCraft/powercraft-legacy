package powercraft.weasel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_Color;
import powercraft.management.PC_INBT;
import powercraft.management.PC_Utils.SaveHandler;
import weasel.Calc;
import weasel.WeaselFunctionManager;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselVariableMap;

public final class PCws_WeaselNetwork implements Iterable<PCws_IWeaselNetworkDevice>, PC_INBT<PCws_WeaselNetwork> {

	private int id;
	private String name = "";
	private PC_Color color = new PC_Color();
	private List<Integer> members = new ArrayList<Integer>();
	/** Local shared variable pool */
	private WeaselVariableMap localHeap = new WeaselVariableMap();
	private WeaselFunctionManager functionMap = createWeaselFunctionManager();
	private boolean needSave = false;
	
	public PCws_WeaselNetwork(){
		id = PCws_WeaselManager.registerNetwork(this);
		String shouldName = Calc.generateUniqueName();
		while(PCws_WeaselManager.getNetwork(shouldName)!=null)
			shouldName = Calc.generateUniqueName();
		name = shouldName;
		color = PC_Color.randomColor();
		needSave = true;
	}
	
	@Override
	public PCws_WeaselNetwork readFromNBT(NBTTagCompound nbttag) {
		needSave = false;
		PCws_WeaselManager.removeNetwork(this);
		id = nbttag.getInteger("id");
		PCws_WeaselManager.registerNetwork(this, id);
		name = nbttag.getString("name");
		SaveHandler.loadFromNBT(nbttag, "color", color);
		int num = nbttag.getInteger("count");
		members.clear();
		for(int i=0; i<num; i++){
			members.add(nbttag.getInteger("value["+i+"]"));
		}
		SaveHandler.loadFromNBT(nbttag, "localHeap", localHeap);
		return this;
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
		needSave = false;
		nbttag.setInteger("id", id);
		nbttag.setString("name", name);
		SaveHandler.saveToNBT(nbttag, "color", color);
		nbttag.setInteger("count", members.size());
		int i=0;
		for(Integer mem:members){
			nbttag.setInteger("value["+i+"]", mem);
			i++;
		}
		SaveHandler.saveToNBT(nbttag, "localHeap", localHeap);
		return nbttag;
	}
	
	public boolean needSave(){
		return needSave;
	}
	
	public int getID(){
		return id;
	}
	
	public void setName(String name) {
		if(PCws_WeaselManager.getNetwork(name) == null){
			needSave = true;
			this.name = name;
			for(PCws_IWeaselNetworkDevice weaselPlugin:this){
				weaselPlugin.setNetworkName(name);
			}
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void setColor(PC_Color color) {
		needSave = true;
		this.color.setTo(color);
		for(PCws_IWeaselNetworkDevice weaselPlugin:this){
			weaselPlugin.setNetworkColor(color.copy());
		}
	}
	
	public PC_Color getColor() {
		return color;
	}
	
	public void updateAll(){
		functionMap = createWeaselFunctionManager();
		for(PCws_IWeaselNetworkDevice member:this){
			functionMap.registerFunctionProvider(member.getName(), member.makePluginProvider());
		}
	}
	
	public void remove() {
		for(Integer i:members){
			PCws_IWeaselNetworkDevice member = PCws_WeaselManager.getPlugin(i);
			if(member!=null){
				member.setNetwork(-1);
			}
		}
	}
	
	public PCws_IWeaselNetworkDevice getMember(String name){
		for(Integer i:members){
			PCws_IWeaselNetworkDevice member = PCws_WeaselManager.getPlugin(i);
			if(member!=null){
				if(name.equals(member.getName())){
					return member;
				}
			}
		}
		return null;
	}
	
	public void registerMember(PCws_IWeaselNetworkDevice member){
		if(!members.contains(member.getID())){
			needSave = true;
			members.add(member.getID());
			functionMap.registerFunctionProvider(member.getName(), member.makePluginProvider());
		}
		member.setNetwork(id);
	}
	
	public void updateMemberFunctionProvider(PCws_IWeaselNetworkDevice member){
		if(members.contains(member.getID())){
			functionMap.removeFunctionProvider(member.getName());
			functionMap.registerFunctionProvider(member.getName(), member.makePluginProvider());
		}
	}
	
	public void renameMember(PCws_IWeaselNetworkDevice member, String newName){
		if(members.contains(member.getID())){
			functionMap.removeFunctionProvider(member.getName());
			functionMap.registerFunctionProvider(newName, member.makePluginProvider());
		}
	}
	
	public void removeMember(PCws_IWeaselNetworkDevice member){
		int id = member.getID();
		if(members.contains(id)){
			needSave = true;
			member.setNetwork(-1);
			members.remove((Integer)id);
			functionMap.removeFunctionProvider(member.getName());
		}
		if(members.size()==0)
			PCws_WeaselManager.removeNetwork(this);
	}
	
	@Override
	public Iterator<PCws_IWeaselNetworkDevice> iterator() {
		return new WeaselNetworkIterator();
	}
	
	private class WeaselNetworkIterator implements Iterator<PCws_IWeaselNetworkDevice>{

		private int i=0;
		
		@Override
		public boolean hasNext() {
			return members.size()>i;
		}

		@Override
		public PCws_IWeaselNetworkDevice next() {
			PCws_IWeaselNetworkDevice wp = PCws_WeaselManager.getPlugin(members.get(i));
			i++;
			return wp;
		}

		@Override
		public void remove() {
			i--;
			needSave = true;
			members.remove(i);
		}
		
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
	public void setLocalVariable(String name, WeaselObject value) throws WeaselRuntimeException {
		localHeap.setVariableForce(name, value);
		needSave = true;
	}

	/**
	 * Get state of a weasel variable.
	 * 
	 * @param name variable name
	 * @return variable value.
	 */
	public WeaselObject getLocalVariable(String name) {
		if(localHeap.getVariable(name) == null) throw new WeaselRuntimeException("Local network does't contain variable "+name);
		return localHeap.getVariable(name);
	}

	/**
	 * Get if globvar exists
	 * 
	 * @param name variable name
	 * @return variable value.
	 */
	public boolean hasLocalVariable(String name) {
		return localHeap.getVariable(name) != null;
	}

	public boolean isConnected(String name) {
		return getMember(name)!=null;
	}
	
	public boolean callFunctionOnEngine(String functionName, WeaselObject... args) {
		boolean call = false;
		for(PCws_IWeaselNetworkDevice weaselPlugin:this){
			if(weaselPlugin.doesProvideFunctionOnEngine(functionName)){
				weaselPlugin.callFunctionOnEngine(functionName, args);
				call = true;
			}
		}
		return call;
	}

	public int size() {
		return members.size();
	}
	
	public WeaselFunctionManager getFunctionHandler(){
		return functionMap;
	}
	
	private WeaselFunctionManager createWeaselFunctionManager(){
		WeaselFunctionManager wfm = new WeaselFunctionManager();
		wfm.registerMethod("network.set", "setLocalVariable", this);
		wfm.registerMethod("network.get", "getLocalVariable", this);
		wfm.registerMethod("network.has", "hasLocalVariable", this);
		wfm.registerMethod("network.isConnected", "isConnected", this);
		return wfm;
	}
	
}
