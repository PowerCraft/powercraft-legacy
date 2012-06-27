package net.minecraft.src.weasel;


import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;
import net.minecraft.src.weasel.obj.WeaselStack;
import net.minecraft.src.weasel.obj.WeaselVariableMap;


/**
 * The WEASEL virtual machine.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class WeaselEngine implements PC_INBT {

	/**
	 * List of variables visible in current function body / root list.
	 */
	public WeaselVariableMap variables = new WeaselVariableMap();

	/** Stack of addresses and variable lists */
	public WeaselStack systemStack = new WeaselStack();
	
	/** Stack of user's data (PUSH and POP instructions)  */
	public WeaselStack dataStack = new WeaselStack();

	public InstructionList instructionList = new InstructionList(this);

	/** Address of first instruction in current scope */
	public int scopeStart;

	/** Address of last instruction in current scope */
	public int scopeEnd;




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
