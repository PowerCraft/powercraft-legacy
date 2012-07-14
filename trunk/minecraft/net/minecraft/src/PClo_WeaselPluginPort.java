package net.minecraft.src;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import weasel.Calc;
import weasel.WeaselEngine;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselObject;

/**
 * 
 * 
 * @author MightyPork
 *
 */
public class PClo_WeaselPluginPort extends PClo_WeaselPlugin {
	
	/** Flag that this port is sending signal. */
	public boolean sendingSignal = false;
	private boolean inputRedstoneState = false;

	/**
	 * @param tew tile entity weasel
	 */
	public PClo_WeaselPluginPort(PClo_TileEntityWeasel tew) {
		super(tew);
	}

	@Override
	public int getType() {
		return PClo_WeaselType.PORT;
	}

	@Override
	public void updateTick() {
	}

	@Override
	public void onNeighborBlockChanged() {
		inputRedstoneState = PClo_BlockWeasel.powered_from_input(world(), coord(), 3);
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
		sendingSignal = tag.getBoolean("Out");
		inputRedstoneState = tag.getBoolean("In");
		return this;
	}

	@Override
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag) {
		tag.setBoolean("Out", sendingSignal);
		tag.setBoolean("In", inputRedstoneState);
		return tag;
	}

	@Override
	public void onDeviceDestroyed(){}

	@Override
	public boolean doesProvideFunction(String functionName) {
		return false;
	}

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		return null;
	}

	@Override
	public WeaselObject getVariable(String name) {
		if(name.equals(getMemberName())) return new WeaselBoolean(sendingSignal || inputRedstoneState);
		return null;
	}

	@Override
	public void setVariable(String name, Object object) {
		if(name.equals(getMemberName())) {
			sendingSignal = Calc.toBoolean(object);
			notifyBlockChange();
		}
	}

	@Override
	public List<String> getProvidedFunctionNames() {
		return new ArrayList<String>(0);
	}

	@Override
	public List<String> getProvidedVariableNames() {
		List<String> list = new ArrayList<String>(1);
		list.add(getMemberName());
		return list;
	}

	@Override
	public boolean isMaster() {
		return false;
	}

	@Override
	protected void onNetworkChanged() {}

}
