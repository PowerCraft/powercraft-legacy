package weasel;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_Color;
import net.minecraft.src.PC_INBT;
import weasel.exception.EndOfProgramException;
import weasel.exception.SyntaxError;
import weasel.exception.WeaselRuntimeException;
import weasel.lang.Instruction;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselInteger;
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
				wre.printStackTrace();
				throw wre;
			} catch (Exception e) {
				e.printStackTrace();
				throw new WeaselRuntimeException(e.getMessage());
			} catch (Throwable t) {
				t.printStackTrace();
				throw new WeaselRuntimeException(t.getMessage());
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

		isProgramFinished = false;
		instructionList.movePointerTo(0);

		systemStack.clear();
		dataStack.clear();

		retval = null;
		externalCallRetval = null;
		variables.clear();

		// not globals! they are preserved!
	}

	/**
	 * Restart program, clear system and data stacks and all variables.
	 */
	public void restartProgramClearGlobals() {
		restartProgram();
		globals.clear();
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
			e.printStackTrace();
			throw new WeaselRuntimeException("Not enough arguments for function " + functionName);
		} catch (WeaselRuntimeException w) {
			throw new WeaselRuntimeException(w.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
			throw new WeaselRuntimeException(t.getMessage());
		}
	}


	/**
	 * Get variable for weasel. First HW, then globals, at last locals.
	 */
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
			return;
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
		return getProvidedFunctionNames().contains(functionName);
	}

	/**
	 * Call engine's own function.
	 */
	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {

		if (functionName.equals("isset")) {

			if (args.length != 1) throw new WeaselRuntimeException("Isset() requires 1 argument, got " + args.length);
			if (args[0] instanceof WeaselString) {
				String varname = (String) args[0].get();
//				System.out.println("## ISSET? "+varname+" -> "+getVariable(varname));
				return new WeaselBoolean(getVariable(varname) != null);
			} else {
				throw new WeaselRuntimeException("Isset() requires String argument, got " + args[0].get().getClass().getSimpleName());
			}

		}

		if (functionName.equals("_get")) {

			if (args.length != 1) throw new WeaselRuntimeException("_get() requires 1 argument, got " + args.length);
			if (args[0] instanceof WeaselString) {
				String varname = (String) args[0].get();
				return getVariable(varname);
			} else {
				throw new WeaselRuntimeException("_get() requires String argument, got " + args[0].get().getClass().getSimpleName());
			}

		}

		if (functionName.equals("_set")) {

			if (args.length != 2) throw new WeaselRuntimeException("_set() requires 2 arguments, got " + args.length);
			if (args[0] instanceof WeaselString) {
				String varname = (String) args[0].get();
				setVariable(varname, args[1]);
			} else {
				throw new WeaselRuntimeException("_set() requires String as 1st argument, got " + args[0].get().getClass().getSimpleName());
			}

		}

		if (functionName.equals("color")) {
			if (args.length == 1) {
				if (args[0] instanceof WeaselInteger) {
					return new WeaselInteger(((Integer) args[0].get()) & 0xffffff);
				}
				if (args[0] instanceof WeaselString) {
					return new WeaselInteger(PC_Color.getHexColorForName(args[0].get()));
				}

				throw new WeaselRuntimeException("color() can't work with " + args[0].get().getClass().getSimpleName());
			} else if (args.length == 3) {
				if (args[0] instanceof WeaselInteger)
					if (args[1] instanceof WeaselInteger)
						if (args[2] instanceof WeaselInteger)
							return new WeaselInteger(clr((Integer) args[0].get(), (Integer) args[1].get(), (Integer) args[2].get()));

				throw new WeaselRuntimeException("Invalid arguments for color().");
			} else {
				throw new WeaselRuntimeException("color() needs 1 or 3 arguments: name or r,g,b.");
			}

		}

		if (functionName.equals("bound")) {
			if (args.length == 3) {
				if (args[0] instanceof WeaselInteger) if (args[1] instanceof WeaselInteger) if (args[2] instanceof WeaselInteger) {
					return new WeaselInteger(Math.min((Integer) args[2].get(), Math.max((Integer) args[1].get(), (Integer) args[0].get())));
				}

				throw new WeaselRuntimeException("Invalid arguments for bound().");
			} else {
				throw new WeaselRuntimeException("bound() needs 3 arguments: num,min,max.");
			}
		}

		if (functionName.equals("bound_c")) {
			if (args.length == 3) {
				if (args[0] instanceof WeaselInteger) if (args[1] instanceof WeaselInteger) if (args[2] instanceof WeaselInteger) {
					int min = (Integer) args[1].get();
					int max = (Integer) args[2].get();
					int val = (Integer) args[0].get();
					while (val > max)
						val = val - (max - min);
					while (val < min)
						val = val + (max - min);
					return new WeaselInteger(val);
				}

				throw new WeaselRuntimeException("Invalid arguments for bound().");
			} else {
				throw new WeaselRuntimeException("bound() needs 3 arguments: num,min,max.");
			}
		}

		return null;
	}

	private int clr(int r, int g, int b) {
		return Math.round(Math.min(255, Math.max(0, r))) << 16 | Math.round(Math.min(255, Math.max(0, g))) << 8
				| Math.round(Math.min(255, Math.max(0, b)));
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
	 * @return true if function was called, false if is missing.
	 * @throws WeaselRuntimeException if the user function has bad number of
	 *             parameters declared.
	 */
	public boolean callFunctionExternal(String funcName, Object... args) throws WeaselRuntimeException {
		if (args == null) args = new Object[] {};
		if (instructionList.canCallFunctionExternal(funcName)) {
			isProgramFinished = false;
			instructionList.callFunctionExternal(funcName, Calc.s2w(args));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Number of currently running external functions. This isn't tested, but
	 * they should be "stackable".
	 */
	public int executingFunctionExternal = 0;

	/**
	 * Get value returned from the last function called from outside
	 * 
	 * @return the output value, simple object
	 */
	public Object getRetvalExternal() {
		return externalCallRetval == null ? null : externalCallRetval.get();
	}

	/**
	 * Get names of functions the engine provides directly
	 */
	@Override
	public List<String> getProvidedFunctionNames() {
		List<String> list = new ArrayList<String>(0);
		list.add("isset");
		list.add("_set");
		list.add("_get");
		list.add("color");
		list.add("bound");
		list.add("bound_c");
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
