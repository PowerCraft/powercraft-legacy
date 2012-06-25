package net.minecraft.src.weasel;

import java.util.LinkedHashMap;
import java.util.Stack;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;

public class WeaselVM implements PC_INBT{

	/** List of all currently set variables */
	public WeaselVariableMap memory = new WeaselVariableMap();
	
	/** Stack of addresses */
	public WeaselStack stack = new WeaselStack();
	
	
	
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	

}
