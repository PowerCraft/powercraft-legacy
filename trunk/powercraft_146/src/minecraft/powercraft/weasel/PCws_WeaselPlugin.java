package powercraft.weasel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_Color;
import powercraft.management.PC_INBT;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.SaveHandler;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;
import weasel.Calc;
import weasel.IWeaselHardware;
import weasel.WeaselEngine;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselInteger;
import weasel.obj.WeaselNull;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;

public abstract class PCws_WeaselPlugin implements PC_INBT<PCws_WeaselPlugin>, IWeaselHardware {

	private int id;
	private int networkID;
	private String name;
	private int dimension;
	private PC_VecI pos = new PC_VecI();
	private boolean[] weaselOutport = { false, false, false, false, false, false };
	private boolean[] weaselInport = { false, false, false, false, false, false };
	private String error;
	
	private boolean needSave;
	
	protected PCws_WeaselPlugin(){
		id = PCws_WeaselManager.registerPlugin(this);
		dimension = 0;
		networkID = -1;
		String shouldName = Calc.generateUniqueName();
		while(PCws_WeaselManager.getPlugin(shouldName)!=null)
			shouldName = Calc.generateUniqueName();
		name = shouldName;
		needSave = true;
	}
	
	@Override
	public final PCws_WeaselPlugin readFromNBT(NBTTagCompound nbttag) {
		needSave = false;
		PCws_WeaselManager.removePlugin(this);
		id = nbttag.getInteger("id");
		PCws_WeaselManager.registerPlugin(this, id);
		networkID = nbttag.getInteger("networkID");
		name = nbttag.getString("name");
		dimension = nbttag.getInteger("dimension");
		SaveHandler.loadFromNBT(nbttag, "pos", pos);
		if(nbttag.hasKey("plugin")){
			readPluginFromNBT(nbttag.getCompoundTag("plugin"));
		}
		error = null;
		if(nbttag.hasKey("error"))
			error = nbttag.getString("error");
		for(int i=0; i<6; i++){
			weaselOutport[i] = nbttag.getBoolean("weaselOutport["+i+"]");
			weaselInport[i] = nbttag.getBoolean("weaselInport["+i+"]");
		}
		return this;
	}

	protected PCws_WeaselPlugin readPluginFromNBT(NBTTagCompound tag){
		return this;
	}
	
	@Override
	public final NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
		needSave = false;
		nbttag.setInteger("id", id);
		nbttag.setInteger("networkID", networkID);
		nbttag.setString("name", name);
		dimension = nbttag.getInteger("dimension");
		SaveHandler.saveToNBT(nbttag, "pos", pos);
		NBTTagCompound nbtPlugin = writePluginToNBT(new NBTTagCompound());
		if(nbtPlugin!=null)
			nbttag.setCompoundTag("plugin", nbtPlugin);
		if(error!=null)
			nbttag.setString("error", error);
		for(int i=0; i<6; i++){
			nbttag.setBoolean("weaselOutport["+i+"]", weaselOutport[i]);
			nbttag.setBoolean("weaselInport["+i+"]", weaselInport[i]);
		}
		return nbttag;
	}
	
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag){
		return null;
	}
	
	public boolean needSave(){
		return needSave;
	}
	
	protected void needsSave(){
		needSave = true;
	}
	
	public final int getID() {
		return id;
	}
	
	public final String getName() {
		return name;
	}
	
	public final void setName(String name) {
		if(PCws_WeaselManager.getPlugin(name)==null){
			needsSave();
			this.name = name;
			setData("deviceName", name);
		}
	}
	
	public void removeFromNetwork(){
		PCws_WeaselNetwork oldNetwork = getNetwork();
		if(oldNetwork!=null){
			oldNetwork.removeMember(this);
			setData("color", getColor());
			setData("networkName", "");
		}
	}
	
	public void connectToNetwork(PCws_WeaselNetwork network){
		removeFromNetwork();
		network.registerMember(this);
		setData("color", getColor());
		setData("networkName", network.getName());
		needsSave();
	}
	
	public void setNetwork(int networkID) {
		this.networkID = networkID;
		needsSave();
	}
	
	public int getNetworkID() {
		return networkID;
	}
	
	public PCws_WeaselNetwork getNetwork() {
		if(networkID==-1)
			return null;
		return PCws_WeaselManager.getNetwork(networkID);
	}

	@Override
	public boolean doesProvideFunction(String functionName) {
		List<String> l = getProvidedFunctionNames();
		if(l==null)
			return false;
		return l.contains(functionName);
	}

	@Override
	public List<String> getProvidedFunctionNames() {
		List<String> l = new ArrayList<String>();
		l.addAll(getProvidedPluginFunctionNames());
		l.add("reset");
		l.add("restart");
		if(getNetwork()!=null){
			for(PCws_WeaselPlugin plugin:getNetwork()){
				List<String> l2 = plugin.getProvidedPluginFunctionNames();
				l2.add("reset");
				l2.add("restart");
				for(String s:l2){
					l.add(plugin.name + "." + s);
				}
			}
		}
		return l;
	}

	protected abstract List<String> getProvidedPluginFunctionNames();
	
	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		if(functionName.equals("reset") || functionName.equals("restart")){
			restartDevice();
			return new WeaselNull();
		}else if(getProvidedPluginFunctionNames().contains(functionName)){
			return callProvidedPluginFunction(engine, functionName, args);
		}
		String s[] = functionName.split("\\.", 2);
		if(s.length>1){
			if(s[0].equals(name)){
				if(getProvidedPluginFunctionNames().contains(s[1])){
					return callProvidedPluginFunction(engine, s[1], args);
				}
			}else if(getNetwork()!=null){
				for(PCws_WeaselPlugin plugin:getNetwork()){
					if(plugin.getName().equals(s[0])){
						return plugin.callProvidedFunction(engine, s[1], args);
					}
				}
			}
		}
		throw new WeaselRuntimeException("Invalid call of function " + functionName);
	}

	protected abstract WeaselObject callProvidedPluginFunction(WeaselEngine engine, String functionName, WeaselObject[] args);
	
	@Override
	public List<String> getProvidedVariableNames() {
		List<String> l = new ArrayList<String>();
		l.addAll(getProvidedPluginVariableNames());
		if(getNetwork()!=null){
			for(PCws_WeaselPlugin plugin:getNetwork()){
				List<String> l2 = plugin.getProvidedPluginVariableNames();
				for(String s:l2){
					if(s.equals("this")){
						l.add(plugin.name);
					}
					l.add(plugin.name + "." + s);
				}
			}
		}
		return l;
	}
	
	protected abstract List<String> getProvidedPluginVariableNames();
	
	@Override
	public void setVariable(String name, Object value) {
		if(getProvidedPluginVariableNames().contains(name)){
			setPluginVariable(name, value);
			return;
		}
		String s[] = name.split("\\.", 2);
		if(s.length>1){
			if(s[0].equals(this.name)){
				if(getProvidedPluginVariableNames().contains(s[1])){
					setPluginVariable(s[1], value);
				}
			}else if(getNetwork()!=null){
				for(PCws_WeaselPlugin plugin:getNetwork()){
					if(plugin.getName().equals(s[0])){
						plugin.setVariable(s[1], value);
						break;
					}
				}
			}
		}else if(getNetwork()!=null){
			PCws_WeaselPlugin plugin = getNetwork().getMember(name);
			if(plugin!=null){
				plugin.setVariable("this", value);
			}
		}
	}

	protected abstract void setPluginVariable(String name, Object value);
	
	@Override
	public WeaselObject getVariable(String name) {
		if(getProvidedPluginVariableNames().contains(name)){
			return getPluginVariable(name);
		}
		String s[] = name.split("\\.", 2);
		if(s.length>1){
			if(s[0].equals(this.name)){
				if(getProvidedPluginVariableNames().contains(s[1])){
					return getPluginVariable(s[1]);
				}
			}else if(getNetwork()!=null){
				for(PCws_WeaselPlugin plugin:getNetwork()){
					if(plugin.getName().equals(s[0])){
						return plugin.getVariable(s[1]);
					}
				}
			}
		}else if(getNetwork()!=null){
			PCws_WeaselPlugin plugin = getNetwork().getMember(name);
			if(plugin!=null){
				return plugin.getVariable("this");
			}
		}
		return null;
	}
	
	protected abstract WeaselObject getPluginVariable(String name);
	
	public boolean doesProvideFunctionOnEngine(String functionName) {
		return false;
	}
	
	public void callFunctionOnEngine(String functionName, WeaselObject... args) {}
	
	
	public void setOutport(int port, boolean state){
		if(weaselOutport[port] != state){
			weaselOutport[port] = state;
			ValueWriting.hugeUpdate(getWorld(), pos.x, pos.y, pos.z);
			needsSave();
		}
	}
	
	public void refreshInport(){
		boolean newWeaselInport[] = PCws_BlockWeasel.getWeaselInputStates(getWorld(), pos);
		PCws_WeaselNetwork weaselNetwork = getNetwork();
		boolean anyChang = false;
		boolean anyNoCall = false;
		if(newWeaselInport!=null){
			for(int i=0; i<6; i++){
				if(weaselInport[i] != newWeaselInport[i]){
					anyChang = true;
					needsSave();
					weaselInport[i] = newWeaselInport[i];
					if(weaselNetwork!=null){
						if(!weaselNetwork.callFunctionOnEngine("onPortChange."+name+"."+numToPort(i), new WeaselBoolean(weaselInport[i])))
							anyNoCall = true;
					}else{
						if(doesProvideFunctionOnEngine("onPortChange."+name+"."+numToPort(i))){
							callFunctionOnEngine("onPortChange."+name+"."+numToPort(i), new WeaselBoolean(weaselInport[i]));
						}else{
							anyNoCall = true;
						}
					}
				}
			}
		}
		if(weaselNetwork!=null && anyChang && anyNoCall){
			if(!weaselNetwork.callFunctionOnEngine("onPortChange."+name)){
				weaselNetwork.callFunctionOnEngine("onPortChange", new WeaselString(name));
			}
		}else if(anyChang && anyNoCall){
			if(doesProvideFunctionOnEngine("onPortChange."+name)){
				callFunctionOnEngine("onPortChange."+name);
			}else if(doesProvideFunctionOnEngine("onPortChange")){
				callFunctionOnEngine("onPortChange",  new WeaselString(name));
			}
		}
	}
	
	public boolean getOutport(int port){
		return weaselOutport[port];
	}
	
	public boolean getInport(int port){
		return weaselInport[port];
	}
	
	public static String numToPort(int num){
		switch(num){
		case 0:
			return "b";
		case 1:
			return "l";
		case 2:
			return "r";
		case 3:
			return "f";
		case 4:
			return "u";
		case 5:
			return "d";
		}
		return null;
	}
	
	public static int portToNum(String port){
		if(port.equalsIgnoreCase("b") || port.equalsIgnoreCase("back")){
			return 0;
		}else if(port.equalsIgnoreCase("l") || port.equalsIgnoreCase("left")){
			return 1;
		}else if(port.equalsIgnoreCase("r") || port.equalsIgnoreCase("right")){
			return 2;
		}else if(port.equalsIgnoreCase("f") || port.equalsIgnoreCase("front")){
			return 3;
		}else if(port.equalsIgnoreCase("u") || port.equalsIgnoreCase("up") || port.equalsIgnoreCase("top")){
			return 4;
		}else if(port.equalsIgnoreCase("d") || port.equalsIgnoreCase("down") || port.equalsIgnoreCase("bottom")){
			return 5;
		}
		return -1;
	}
	
	public World getWorld(){
		return GameInfo.mcs().worldServerForDimension(dimension);
	}

	public PCws_TileEntityWeasel getTE(){
		return GameInfo.getTE(getWorld(), pos);
	}
	
	public PC_Color getColor() {
		PCws_WeaselNetwork network = getNetwork();
		if(network!=null)
			return network.getColor();
		return new PC_Color(0.3f, 0.3f, 0.3f);
	}
	
	public boolean isOnPlace(World world, int x, int y, int z) {
		int dimension = world.getWorldInfo().getDimension();
		if(this.dimension != dimension)
			return false;
		if(pos.x!=x)
			return false;
		if(pos.y!=y)
			return false;
		if(pos.z!=z)
			return false;
		return true;
	}
	
	public PCws_WeaselPlugin setPlace(World world, int x, int y, int z) {
		dimension = world.getWorldInfo().getDimension();
		pos.setTo(x, y, z);
		needsSave();
		return this;
	}
	
	public PC_VecI getPos() {
		return pos.copy();
	}
	
	public abstract void update();

	public abstract void syncWithClient(PCws_TileEntityWeasel tileEntityWeasel);
	
	public void sync(PCws_TileEntityWeasel tileEntityWeasel) {
		tileEntityWeasel.setData("color", getColor());
		tileEntityWeasel.setData("deviceName", name);
		PCws_WeaselNetwork netkork = getNetwork();
		if(netkork==null)
			tileEntityWeasel.setData("networkName", "");
		else
			tileEntityWeasel.setData("networkName", netkork.getName());
		tileEntityWeasel.setData("error", error);
		syncWithClient(tileEntityWeasel);
	}

	public void getClientMsg(String msg, Object obj) {
		if(msg.equalsIgnoreCase("deviceRename")){
			setName((String)obj);
		}else if(msg.equalsIgnoreCase("networkJoin")){
			if(((String) obj).equals("")){
				removeFromNetwork();
			}else{
				connectToNetwork(PCws_WeaselManager.getNetwork((String) obj));
			}
		}else if(msg.equalsIgnoreCase("networkRename")){
			if(getNetwork()==null){
				connectToNetwork(new PCws_WeaselNetwork());
			}
			getNetwork().setName((String) obj);
		}else if(msg.equalsIgnoreCase("networkNew")){
			connectToNetwork(new PCws_WeaselNetwork());
			getNetwork().setName((String) obj);
		}
	}

	public void reciveData(String var, Object obj) {
		if(var.equalsIgnoreCase("color")){
			PCws_WeaselNetwork network = getNetwork();
			if(network!=null){
				network.setColor((PC_Color)obj);
				needsSave();
			}
		}
	}

	protected abstract void openPluginGui(EntityPlayer player);
	
	public void openGui(EntityPlayer player){
		setData("deviceNames", PCws_WeaselManager.getAllPluginNames());
		setData("networkNames", PCws_WeaselManager.getAllNetworkNames());
		openPluginGui(player);
	}
	
	protected void setError(String message) {
		error = message;
		setData("error", error);
		needsSave();
	}
	
	public boolean hasError(){
		return error != null;
	}
	
	public void restartDevice(){
		needsSave();
		for(int i=0; i<6; i++){
			weaselOutport[i] = false;
		}
		ValueWriting.hugeUpdate(getWorld(), pos.x, pos.y, pos.z);
		restart();
		error = null;
		PCws_TileEntityWeasel te;
		if((te = getTE())!=null){
			sync(te);
		}
	}
	
	public abstract void restart();
	
	protected void setData(String key, Object value){
		PCws_TileEntityWeasel te = getTE();
		if(te!=null)
			te.setData(key, value);
	}
	
}
