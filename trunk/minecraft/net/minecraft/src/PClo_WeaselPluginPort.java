package net.minecraft.src;


import java.util.ArrayList;
import java.util.List;

import weasel.Calc;
import weasel.WeaselEngine;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselObject;


/**
 * @author MightyPork
 */
public class PClo_WeaselPluginPort extends PClo_WeaselPlugin {

	/**
	 * @param tew tile entity weasel
	 */
	public PClo_WeaselPluginPort(PClo_TileEntityWeasel tew) {
		super(tew);
	}
	
	public boolean renderAsActive() {
		boolean flag = false;
		flag |= getOutport("F") == true;
		flag |= getOutport("L") == true;
		flag |= getOutport("R") == true;
		flag |= getOutport("B") == true;
		flag |= getOutport("U") == true;
		flag |= getOutport("D") == true;
		return flag;
	}

	@Override
	public int getType() {
		return PClo_WeaselType.PORT;
	}

	@Override
	public void updateTick() {}

	@Override
	public void onRedstoneSignalChanged() {
		((PClo_WeaselPlugin) getNetwork().getMember("CORE")).callFunctionExternalDelegated("update");
	}

	@Override
	public String getError() {
		return null;
	}

	@Override
	public boolean hasError() {
		return false;
	}

	@Override
	protected PClo_WeaselPlugin readPluginFromNBT(NBTTagCompound tag) {
		return this;
	}

	@Override
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag) {
		return tag;
	}

	@Override
	public void onDeviceDestroyed() {}

	@Override
	public boolean doesProvideFunction(String functionName) {
		return getProvidedFunctionNames().contains(functionName);
	}

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		if (functionName.equals(getName()+".empty")) {

			return this.chestEmptyTest(args);

		} else if (functionName.equals(getName()+".full")) {

			return this.chestFullTest(args);

		}
		return null;
	}

	@Override
	public WeaselObject getVariable(String name) {
		if (name.equals(getName())) return new WeaselBoolean(getInport("F"));
		if(name.startsWith(getName()+".") && name.length() == getName().length() + 2) {
			String port = name.substring(name.length()-1);
			return new WeaselBoolean(getInport(port));
		}
		return null;
	}

	@Override
	public void setVariable(String name, Object object) {
		if (name.equals(getName())) {
			setOutport("F", Calc.toBoolean(object));
			
		}else		
		if(name.startsWith(getName()+".") && name.length() == getName().length() + 2) {
			String port = name.substring(name.length()-1);
			setOutport(port, Calc.toBoolean(object));
		}
	}

	@Override
	public List<String> getProvidedFunctionNames() {
		List<String> list = new ArrayList<String>(0);

		list.add(getName()+".empty");
		list.add(getName()+".full");
		
		return list;
	}

	@Override
	public List<String> getProvidedVariableNames() {
		List<String> list = new ArrayList<String>(7);
		list.add(getName());
		list.add(getName() + ".F");
		list.add(getName() + ".L");
		list.add(getName() + ".R");
		list.add(getName() + ".B");
		list.add(getName() + ".U");
		list.add(getName() + ".D");
		return list;
	}

	@Override
	public boolean isMaster() {
		return false;
	}

	@Override
	protected void onNetworkChanged() {}

	@Override
	public WeaselEngine getWeaselEngine() {
		return null;
	}

	@Override
	public Object callFunctionExternalDelegated(String function, Object... args) {
		return null;
	}

}
