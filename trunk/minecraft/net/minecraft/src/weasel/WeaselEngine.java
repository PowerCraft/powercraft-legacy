package net.minecraft.src.weasel;


import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;
import net.minecraft.src.PC_Utils;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;
import net.minecraft.src.weasel.lang.Instruction;
import net.minecraft.src.weasel.lang.InstructionLabel;
import net.minecraft.src.weasel.obj.WeaselInteger;
import net.minecraft.src.weasel.obj.WeaselObject;
import net.minecraft.src.weasel.obj.WeaselStack;
import net.minecraft.src.weasel.obj.WeaselVariableMap;


/**
 * The WEASEL virtual machine.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class WeaselEngine implements PC_INBT {
	
	public WeaselEngine(IWeaselControlled hardware) {
		this.hardware = hardware;
	}

	/**
	 * List of variables visible in current function body / root list.
	 */
	public WeaselVariableMap variables = new WeaselVariableMap();
	
	private IWeaselControlled hardware = null;
	
	public WeaselObject returnValue = null;

	/** Stack of addresses and variable lists */
	public WeaselStack systemStack = new WeaselStack();
	
	/** Stack of user's data (PUSH and POP instructions)  */
	public WeaselStack dataStack = new WeaselStack();

	/** List of all instructions in the program */
	public InstructionList instructionList = new InstructionList(this);


	private boolean pauseRequested = false;

	/**
	 * Request pause after the current instruction is finished
	 */
	public void requestPause() {
		pauseRequested = true;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setCompoundTag("Variables", WeaselObject.saveObjectToNBT(variables, new NBTTagCompound()));
		tag.setCompoundTag("SystemStack", WeaselObject.saveObjectToNBT(systemStack, new NBTTagCompound()));
		tag.setCompoundTag("DataStack", WeaselObject.saveObjectToNBT(dataStack, new NBTTagCompound()));
		tag.setCompoundTag("InstructionList", instructionList.writeToNBT(new NBTTagCompound()));
		tag.setCompoundTag("ReturnValue", WeaselObject.saveObjectToNBT(returnValue, new NBTTagCompound()));
		return tag;
	}

	@Override
	public WeaselEngine readFromNBT(NBTTagCompound tag) {		
		variables = (WeaselVariableMap) WeaselObject.loadObjectFromNBT(tag.getCompoundTag("Variables"));
		systemStack = (WeaselStack) WeaselObject.loadObjectFromNBT(tag.getCompoundTag("SystemStack"));
		dataStack = (WeaselStack) WeaselObject.loadObjectFromNBT(tag.getCompoundTag("DataStack"));
		instructionList = (InstructionList) new InstructionList(this).readFromNBT(tag.getCompoundTag("InstructionList"));	
		returnValue = WeaselObject.loadObjectFromNBT(tag.getCompoundTag("ReturnValue"));
		return this;
	}
	
	public void setReturnValue(WeaselObject object) {
		returnValue = object;
	}

	/**
	 * Check if a native function exists
	 * @param functionName native function name
	 * @return exists
	 */
	public boolean nativeFunctionExists(String functionName) {
		if(hardware == null) return false;
		return hardware.hasFunction(functionName);
	}
	
	/**
	 * Call native function
	 * @param functionName native function name
	 * @param args argument list
	 */
	public void callNativeFunction(String functionName, WeaselObject[] args) {
		if(hardware == null) throw new WeaselRuntimeException("No hardware is connected.");
		hardware.callFunction(this, functionName, args);
	}

}
