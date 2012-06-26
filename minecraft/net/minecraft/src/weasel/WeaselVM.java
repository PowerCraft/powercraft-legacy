package net.minecraft.src.weasel;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;

/**
 * The WEASEL virtual machine.
 * 
 * @author MightyPork
 * @copy (c) 2012
 *
 */
public class WeaselVM implements PC_INBT{

	/**
	 *  List of variables visible in current function body / root list.
	 */
	public WeaselVariableMap localVars = new WeaselVariableMap();
	/** 
	 * Globally visible variables
	 */
	public WeaselVariableMap globalVars = new WeaselVariableMap();
	
	/** Stack of addresses */
	public WeaselStack systemStack = new WeaselStack();
	
	
	
	
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
