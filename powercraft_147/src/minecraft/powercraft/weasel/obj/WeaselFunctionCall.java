package powercraft.weasel.obj;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_INBT;

public class WeaselFunctionCall extends WeaselObject {

	public String lib;
	public String functionName;
	public WeaselObject[]args;
	
	
	public WeaselFunctionCall(String lib, String functionName, WeaselObject...args) {
		super(WeaselObjectType.FUNCTIONCALL);
		this.lib = lib;
		this.functionName = functionName;
		this.args = args;
	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound nbttag) {
		return null;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
		return null;
	}

	@Override
	public WeaselObject copy() {
		return null;
	}

	@Override
	public Object get() {
		return null;
	}

	@Override
	public void set(Object obj) {
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}

}
