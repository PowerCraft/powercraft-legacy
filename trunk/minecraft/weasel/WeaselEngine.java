package weasel;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;
import weasel.exception.EndOfProgramException;
import weasel.exception.SyntaxError;
import weasel.exception.WeaselRuntimeException;
import weasel.lang.Instruction;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselStack;
import weasel.obj.WeaselString;
import weasel.obj.WeaselVariableMap;


/**
 * The WEASEL virtual machine.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
/**
 * @author MightyPork
 */
public class WeaselEngine implements PC_INBT, IVariableProvider, IFunctionProvider {

	/**
	 * Weasel Engine
	 * 
	 * @param hardware hardware the engine is controlling
	 */
	public WeaselEngine(IWeaselHardware hardware) {
		this.hw = hardware;
	}

	/**
	 * List of variables visible in current function body / root list.
	 */
	public WeaselVariableMap variables = new WeaselVariableMap();

	/**
	 * List of variables visible globally.
	 */
	public WeaselVariableMap globals = new WeaselVariableMap();


	private IWeaselHardware hw = null;

	/** Function retval */
	public WeaselObject retval = null;

	/** Stack of addresses and variable lists */
	public WeaselStack systemStack = new WeaselStack();

	/** Stack of user's data (PUSH and POP instructions) */
	public WeaselStack dataStack = new WeaselStack();

	/** List of all instructions in the program */
	public InstructionList instructionList = new InstructionList(this);


	// only temporary
	/** Flag that last instruction called requires pause */
	private boolean pauseRequested = false;


	/** Flag that program is finished */
	public boolean isProgramFinished = false;

	/** Number of restarts scheduled */
	private int restartsScheduled = 0;

	/** Value returned form an external call of a function */
	public WeaselObject externalCallRetval = null;

	/**
	 * Request pause after the current instruction is finished
	 */
	public void requestPause() {
		pauseRequested = true;
	}

	private static final String nk_VARIABLE_MAP = "LOCAL_VAR";
	private static final String nk_GLOBAL_VARIABLE_MAP = "GLOBAL_VAR";
	private static final String nk_STACK_SYSTEM = "STACK_SYSTEM";
	private static final String nk_STACK_DATA = "STACK_DATA";
	private static final String nk_INSTRUCTION_LIST = "INSTRUCTIONS";
	private static final String nk_RETURN_VALUE = "RETVAL";
	private static final String nk_IS_FINISHED = "FINISHED";
	private static final String nk_RESTARTS_SCHEDULED = "RESTARTS_SCH";

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setCompoundTag(nk_VARIABLE_MAP, WeaselObject.saveObjectToNBT(variables, new NBTTagCompound()));
		tag.setCompoundTag(nk_GLOBAL_VARIABLE_MAP, WeaselObject.saveObjectToNBT(globals, new NBTTagCompound()));
		tag.setCompoundTag(nk_STACK_SYSTEM, WeaselObject.saveObjectToNBT(systemStack, new NBTTagCompound()));
		tag.setCompoundTag(nk_STACK_DATA, WeaselObject.saveObjectToNBT(dataStack, new NBTTagCompound()));
		tag.setCompoundTag(nk_INSTRUCTION_LIST, instructionList.writeToNBT(new NBTTagCompound()));
		tag.setCompoundTag(nk_RETURN_VALUE, WeaselObject.saveObjectToNBT(retval, new NBTTagCompound()));
		tag.setInteger(nk_RESTARTS_SCHEDULED, restartsScheduled);
		tag.setBoolean(nk_IS_FINISHED, isProgramFinished);
		return tag;
	}

	@Override
	public WeaselEngine readFromNBT(NBTTagCompound tag) {
		variables = (WeaselVariableMap) WeaselObject.loadObjectFromNBT(tag.getCompoundTag(nk_VARIABLE_MAP));
		globals = (WeaselVariableMap) WeaselObject.loadObjectFromNBT(tag.getCompoundTag(nk_GLOBAL_VARIABLE_MAP));
		systemStack = (WeaselStack) WeaselObject.loadObjectFromNBT(tag.getCompoundTag(nk_STACK_SYSTEM));
		dataStack = (WeaselStack) WeaselObject.loadObjectFromNBT(tag.getCompoundTag(nk_STACK_DATA));
		instructionList = (InstructionList) new InstructionList(this).readFromNBT(tag.getCompoundTag(nk_INSTRUCTION_LIST));
		retval = WeaselObject.loadObjectFromNBT(tag.getCompoundTag(nk_RETURN_VALUE));
		restartsScheduled = tag.getInteger(nk_RESTARTS_SCHEDULED);
		isProgramFinished = tag.getBoolean(nk_IS_FINISHED);
		return this;
	}

	/**
	 * Set return value of a function. Since return value can't be assigned
	 * directly, SET_RETVAL must be called right after function call.
	 * 
	 * @param object return value (WeaselObject)
	 */
	public void setReturnValue(WeaselObject object) {
		retval = object;
	}

	/**
	 * Schedule program restart. When the program pointer reaches either end of
	 * program, or an END instruction, program won't end, but will restant.
	 */
	public void scheduleRestart() {
		restartsScheduled++;
	}

	/**
	 * Execute at most given number of statements
	 * 
	 * @param statementsMax max number of statements to execute
	 * @return true = all executed, false = PAUSE required
	 * @throws WeaselRuntimeException when something goes wrong.
	 */
	public boolean run(int statementsMax) {

		//if (isProgramFinished) return true;

		for (; statementsMax > 0; statementsMax--) {
			try {
				instructionList.executeNextInstruction();
			} catch (EndOfProgramException eope) {
				isProgramFinished = true;

				if (restartsScheduled > 0) {
					restartsScheduled--;
					restartProgram();
				} else {
					boolean notpaused = !pauseRequested;
					pauseRequested = false;
					return notpaused;
				}

			} catch (WeaselRuntimeException wre) {
				throw wre;
			}

			if (pauseRequested) {
				pauseRequested = false;
				return false;
			}
		}
		boolean notpaused = !pauseRequested;
		pauseRequested = false;
		return notpaused;
	}

	/**
	 * Restart program, clear system and data stacks and local variable map,
	 * preserving globals.
	 */
	public void restartProgram() {

		instructionList.movePointerTo(0);

		systemStack.clear();
		dataStack.clear();

		retval = null;
		externalCallRetval = null;
		variables.clear();

		// not globals! they are preserved!
	}

	/**
	 * Clear everything and prepare for execution of a new program code
	 */
	public void clear() {
		restartProgram();
		instructionList.clear();
		globals.clear();
		restartsScheduled = 0;
		isProgramFinished = false;
		pauseRequested = false;
	}


	/**
	 * Clear all variables, insert compiled program into instruction list and
	 * get ready for it's execution.
	 * 
	 * @param instructions the new program
	 */
	public void insertNewProgram(List<Instruction> instructions) {
		clear();
		this.instructionList.addAll(instructions);
	}

	/**
	 * Compile given program to a list of instruction.
	 * 
	 * @param program source code
	 * @return the list of instructions
	 * @throws SyntaxError when there's an error in the source code
	 */
	public static List<Instruction> compileProgram(String program) throws SyntaxError {
		return (new Compiler()).compile(program);
	}



	/**
	 * Check if a native function exists
	 * 
	 * @param functionName native function name
	 * @return exists
	 */
	public boolean hardwareFunctionExists(String functionName) {
		if (hw == null) return false;
		return hw.doesProvideFunction(functionName);
	}

	/**
	 * Call native function
	 * 
	 * @param functionName native function name
	 * @param args argument list
	 */
	public void callHardwareFunction(String functionName, WeaselObject[] args) {
		if (hw == null) throw new WeaselRuntimeException("No hardware is connected.");

		try {
			retval = hw.callProvidedFunction(this, functionName, args);
		} catch (ClassCastException e) {
			throw new WeaselRuntimeException("Invalid arguments for function " + functionName);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new WeaselRuntimeException("Not enough arguments for function " + functionName);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new WeaselRuntimeException("Hardware call error - " + t.getMessage());
		}
	}


	@Override
	public WeaselObject getVariable(String name) {

		WeaselObject obj;

		if ((obj = hw.getVariable(name)) != null) {
			return obj;
		}

		if ((obj = globals.getVariable(name)) != null) {
			return obj;
		}

		return variables.getVariable(name);

	}


	@Override
	public void setVariable(String name, Object value) {

		if (name == null) throw new WeaselRuntimeException("Variable name cannot be null at " + name + " = " + value);
		if (value == null) throw new WeaselRuntimeException("Variable value cannot be null at " + name + " = " + value);

		if (hw.getVariable(name) != null) {
			hw.setVariable(name, value);
		}

		if (globals.getVariable(name) != null) {
//			System.out.println("## setting global because global with this name exists. "+name+" = "+value);
			globals.setVariable(name, value);
		} else {
			variables.setVariable(name, value);
		}
	}

	/**
	 * Set a global variable, visible in all scopes and preserved through
	 * restart.
	 * 
	 * @param name variable name
	 * @param value value
	 */
	public void setVariableGlobal(String name, WeaselObject value) {
		if (variables.getVariable(name) != null) {
//			System.out.println("## setting global, removing local because local with this name exists. "+name+" = "+value);

			variables.unsetVariable(name);
		}

		globals.setVariable(name, value);
	}

	/**
	 * Check if the Engine does provide given function (itself, not from
	 * hardware or program).<br>
	 * This may be used for engine functions that can return values.<br>
	 * Function like RETURN, PUSH, POP, PAUSE are void, and are implemented
	 * directly by Instructions.
	 */
	@Override
	public boolean doesProvideFunction(String functionName) {
		if (functionName.equals("isset")) return true;

		return false;
	}

	/**
	 * Call engine's own function.
	 */
	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {

		if (functionName.equals("isset")) {

			if (args.length != 1) throw new WeaselRuntimeException("Isset() requires exactly 1 argument, got " + args.length);
			if (args[0] instanceof WeaselString) {
				String varname = (String) args[0].get();
//				System.out.println("## ISSET? "+varname+" -> "+getVariable(varname));
				return new WeaselBoolean(getVariable(varname) != null);
			} else {
				throw new WeaselRuntimeException("Isset() requires String argument, got " + args[0].get().getClass().getSimpleName());
			}

		}

		return null;
	}

	/**
	 * Call program function from outside, without affecting the program.<br>
	 * This call must be followed by calling run() with sufficient number of
	 * instructions to execute.<br>
	 * <br>
	 * run() returns false if the program was paused, and when externally called
	 * function ends, it is paused, so you can use this to check whether it was
	 * already fully executed.<br>
	 * You can then get the returned value by calling
	 * getReturnedValueFromExternalCall()
	 * 
	 * @param funcName function name
	 * @param args function arguments, array of simple objects
	 */
	public void callFunctionExternal(String funcName, Object... args) {
		if (args == null) args = new Object[] {};
		instructionList.callFunctionExternal(funcName, Calc.s2w(args));
	}

	public int executingFunctionExternal = 0;

	/**
	 * Get value returned from the last function called from outside
	 * 
	 * @return the output value, simple object
	 */
	public Object getRetvalExternal() {
		return externalCallRetval.get();
	}

	/**
	 * Get names of functions the engine provides directly
	 */
	@Override
	public List<String> getProvidedFunctionNames() {
		List<String> list = new ArrayList<String>(0);
		list.add("isset");
		return list;
	}

	/**
	 * Get all variables provided directly by this engine.
	 */
	@Override
	public List<String> getProvidedVariableNames() {
		List<String> list = new ArrayList<String>(0);
		return list;
	}

}
