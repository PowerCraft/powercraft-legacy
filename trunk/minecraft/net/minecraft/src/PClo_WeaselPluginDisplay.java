package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

import weasel.Calc;
import weasel.WeaselEngine;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;

public class PClo_WeaselPluginDisplay extends PClo_WeaselPlugin {

	String text="";
	
	public PClo_WeaselPluginDisplay(PClo_TileEntityWeasel tew) {
		super(tew);
	}

	@Override
	public boolean doesProvideFunction(String functionName) {
		return false;
	}

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine,
			String functionName, WeaselObject[] args) {
		return null;
	}

	@Override
	public WeaselObject getVariable(String name) {
		if (name.equals(getName())) return new WeaselString(text);
		return null;
	}

	@Override
	public void setVariable(String name, Object object) {
		if (name.equals(getName())){
			if(object instanceof String)
				text = (String)object;
			else if(object instanceof WeaselString)
				text = ((WeaselString)object).string;
		}
	}

	@Override
	public List<String> getProvidedFunctionNames() {
		List<String> list = new ArrayList<String>(0);
		return list;
	}

	@Override
	public List<String> getProvidedVariableNames() {
		List<String> list = new ArrayList<String>(1);
		list.add(getName());
		return list;
	}

	@Override
	public int getType() {
		return PClo_WeaselType.DISPLAY;
	}

	@Override
	protected void updateTick() {
	}

	@Override
	public void onRedstoneSignalChanged() {
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
	public WeaselEngine getWeaselEngine() {
		return null;
	}

	@Override
	public boolean isMaster() {
		return false;
	}

	@Override
	protected void onNetworkChanged() {
	}

	@Override
	protected void onDeviceDestroyed() {
	}

	@Override
	public Object callFunctionExternalDelegated(String function, Object... args) {
		return null;
	}

	@Override
	protected PClo_WeaselPlugin readPluginFromNBT(NBTTagCompound tag) {
		text = tag.getString("text");
		return this;
	}

	@Override
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag) {
		tag.setString("text", text);
		return tag;
	}

}
