package net.minecraft.src.weasel;

import java.util.LinkedHashMap;
import java.util.Stack;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;

public class WeaselVM implements PC_INBT{

	/** List of all currently set variables */
	public WeaselVariableList variableList;
	public Stack<PC_INBT> addressStack = new Stack<PC_INBT>();
	public Stack<PC_INBT> dataStack = new Stack<PC_INBT>();
	
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		
	}
	

}
