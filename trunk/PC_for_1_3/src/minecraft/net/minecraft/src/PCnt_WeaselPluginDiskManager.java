package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weasel.WeaselEngine;
import weasel.obj.WeaselObject;

public class PCnt_WeaselPluginDiskManager extends PCnt_WeaselPlugin {

	/**
	 * A digital workbench
	 */
	public PCnt_WeaselPluginDiskManager() {
		super();
	}
	
	/**
	 * A digital workbench
	 * 
	 * @param id
	 */
	public PCnt_WeaselPluginDiskManager(int id) {
		super(id);
	}


	@Override
	public boolean onClick(EntityPlayer player) {
		//TODO PC_Utils.openGres(player, new PCnt_GuiWeaselDiskManager());
		return true;
	}

	@Override
	public boolean doesProvideFunction(String functionName) {
		return getProvidedFunctionNames().contains(functionName);
	}

	@Override
	public List<String> getProvidedFunctionNames() {
		List<String> list = new ArrayList<String>(0);
		return list;
	}

	@Override
	public List<String> getProvidedVariableNames() {
		List<String> list = new ArrayList<String>(1);
		return list;
	}

	@Override
	public int getType() {
		return PCnt_WeaselType.DISK_MANAGER;
	}


	@Override
	public float[] getBounds() {
		return new float[] { 0, 0, 0, 1, 1 - 2 * 0.0625F, 1 };
	}

}
