package powercraft.weasel;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import powercraft.management.PC_Color;
import powercraft.management.PC_INBT;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.SaveHandler;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;
import weasel.Calc;
import weasel.IFunctionProvider;
import weasel.IVariableProvider;
import weasel.WeaselEngine;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;

public abstract class PCws_WeaselPlugin implements PC_INBT<PCws_WeaselPlugin>, IVariableProvider, IFunctionProvider {

	private int id;
	private int networkID;
	private String name;
	private int dimension;
	private PC_VecI pos = new PC_VecI();
	private boolean[] weaselOutport = { false, false, false, false, false, false };
	private boolean[] weaselInport = { false, false, false, false, false, false };
	
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
			needSave = true;
			this.name = name;
		}
	}
	
	public void connectToNetowrk(PCws_WeaselNetwork network){
		PCws_WeaselNetwork oldNetwork = getNetwork();
		if(oldNetwork!=null)
			oldNetwork.removeMember(this);
		network.registerMember(this);
	}
	
	public void setNetwork(int networkID) {
		this.networkID = networkID;
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
		return false;
	}

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		return null;
	}

	@Override
	public List<String> getProvidedFunctionNames() {
		return null;
	}

	@Override
	public void setVariable(String name, Object value) {}

	@Override
	public WeaselObject getVariable(String name) {
		return null;
	}

	@Override
	public List<String> getProvidedVariableNames() {
		return null;
	}
	
	public boolean doesProvideFunctionOnEngine(String functionName) {
		return false;
	}
	
	public void callFunctionOnEngine(String functionName, WeaselObject... args) {}
	
	public void setOutport(int port, boolean state){
		if(weaselOutport[port] != state){
			weaselOutport[port] = state;
			ValueWriting.hugeUpdate(getWorld(), pos.x, pos.y, pos.z);
			
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
					weaselInport[i] = newWeaselInport[i];
					if(weaselNetwork!=null){
						if(!weaselNetwork.callFunctionOnEngine("onPortChange."+name+"."+numToPort(i), new WeaselBoolean(weaselInport[i])))
							anyNoCall = true;
					}
				}
			}
		}
		if(weaselNetwork!=null && anyChang && anyNoCall){
			if(!weaselNetwork.callFunctionOnEngine("onPortChange."+name)){
				weaselNetwork.callFunctionOnEngine("onPortChange", new WeaselString(name));
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
			return "B";
		case 1:
			return "L";
		case 2:
			return "R";
		case 3:
			return "F";
		case 4:
			return "U";
		case 5:
			return "D";
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
		return this;
	}
	
	public abstract void update();

}
