package net.minecraft.src.weasel;


import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;
import net.minecraft.src.weasel.exception.EndOfProgramException;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;
import net.minecraft.src.weasel.obj.WeaselBoolean;
import net.minecraft.src.weasel.obj.WeaselInteger;
import net.minecraft.src.weasel.obj.WeaselObject;
import net.minecraft.src.weasel.obj.WeaselStack;
import net.minecraft.src.weasel.obj.WeaselString;
import net.minecraft.src.weasel.obj.WeaselVariableMap;


/**
 * The WEASEL virtual machine.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class WeaselEngine implements PC_INBT, IVariableContainer {

	/**
	 * Weasel Engine
	 * 
	 * @param hardware hardware the engine is controlling
	 */
	public WeaselEngine(IWeaselHardware hardware) {
		this.hardware = hardware;
	}

	/**
	 * List of variables visible in current function body / root list.
	 */
	public WeaselVariableMap variables = new WeaselVariableMap();

	private IWeaselHardware hardware = null;

	/** Function retval */
	public WeaselObject returnValue = null;

	/** Stack of addresses and variable lists */
	public WeaselStack systemStack = new WeaselStack();

	/** Stack of user's data (PUSH and POP instructions) */
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
	
	private static final String nk_VARIABLE_MAP = "VAR";
	private static final String nk_STACK_SYSTEM = "STSYS";
	private static final String nk_STACK_DATA = "STUSR";
	private static final String nk_INSTRUCTION_LIST = "INSTRL";
	private static final String nk_RETURN_VALUE = "RETVAL";

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setCompoundTag(nk_VARIABLE_MAP, WeaselObject.saveObjectToNBT(variables, new NBTTagCompound()));
		tag.setCompoundTag(nk_STACK_SYSTEM, WeaselObject.saveObjectToNBT(systemStack, new NBTTagCompound()));
		tag.setCompoundTag(nk_STACK_DATA, WeaselObject.saveObjectToNBT(dataStack, new NBTTagCompound()));
		tag.setCompoundTag(nk_INSTRUCTION_LIST, instructionList.writeToNBT(new NBTTagCompound()));
		tag.setCompoundTag(nk_RETURN_VALUE, WeaselObject.saveObjectToNBT(returnValue, new NBTTagCompound()));
		return tag;
	}

	@Override
	public WeaselEngine readFromNBT(NBTTagCompound tag) {
		variables = (WeaselVariableMap) WeaselObject.loadObjectFromNBT(tag.getCompoundTag(nk_VARIABLE_MAP));
		systemStack = (WeaselStack) WeaselObject.loadObjectFromNBT(tag.getCompoundTag(nk_STACK_SYSTEM));
		dataStack = (WeaselStack) WeaselObject.loadObjectFromNBT(tag.getCompoundTag(nk_STACK_DATA));
		instructionList = (InstructionList) new InstructionList(this).readFromNBT(tag.getCompoundTag(nk_INSTRUCTION_LIST));
		returnValue = WeaselObject.loadObjectFromNBT(tag.getCompoundTag(nk_RETURN_VALUE));
		return this;
	}

	/**
	 * Set return value of a function. Since return value can't be assigned
	 * directly, SET_RETVAL must be called right after function call.
	 * 
	 * @param object return value (WeaselObject)
	 */
	public void setReturnValue(WeaselObject object) {
		returnValue = object;
	}

	/**
	 * Execute at most given number of statements
	 * 
	 * @param statementsMax max number of statements to execute
	 * @return true = all executed, false = PAUSE required
	 * @throws WeaselRuntimeException when something goes wrong.
	 */
	public boolean run(int statementsMax) {

		for (; statementsMax > 0; statementsMax--) {
			try {
				instructionList.executeNextInstruction();
			} catch (EndOfProgramException eose) {
				return true;
			} catch (WeaselRuntimeException wre) {
				throw wre;
			}

			if (pauseRequested) return false;
		}
		return true;
	}

	/**
	 * Check if a native function exists
	 * 
	 * @param functionName native function name
	 * @return exists
	 */
	public boolean nativeFunctionExists(String functionName) {
		if (hardware == null) return false;
		return hardware.hasFunction(functionName);
	}

	/**
	 * Call native function
	 * 
	 * @param functionName native function name
	 * @param args argument list
	 */
	public void callNativeFunction(String functionName, WeaselObject[] args) {
		if (hardware == null) throw new WeaselRuntimeException("ENGINE CallNative() - no hardware is connected.");
		hardware.callFunction(this, functionName, args);
	}


	@Override
	public WeaselObject getVariable(String name) {
		WeaselObject obj;
		if ((obj = hardware.getVariable(name)) != null) {
			return obj;
		}
		return variables.getVariable(name);
	}


	@Override
	public void setVariable(String name, Object value) {
		
		if (name == null) throw new WeaselRuntimeException("ENGINE Set() - variable name == null. @ " + name + " = " + value);
		if (value == null) throw new WeaselRuntimeException("ENGINE Set() - variable value == null. @ " + name + " = " + value);
		
		if (hardware.getVariable(name) != null) {
			hardware.setVariable(name, value);
		}

		variables.setVariable(name, value);
	}


}
