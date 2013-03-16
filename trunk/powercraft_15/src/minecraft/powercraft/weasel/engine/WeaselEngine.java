package powercraft.weasel.engine;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.api.PC_Color;
import powercraft.api.PC_INBT;
import powercraft.weasel.exception.EndOfProgramException;
import powercraft.weasel.exception.SyntaxError;
import powercraft.weasel.exception.WeaselRuntimeException;
import powercraft.weasel.lang.Instruction;
import powercraft.weasel.obj.WeaselBoolean;
import powercraft.weasel.obj.WeaselDouble;
import powercraft.weasel.obj.WeaselFunctionCall;
import powercraft.weasel.obj.WeaselObject;
import powercraft.weasel.obj.WeaselStack;
import powercraft.weasel.obj.WeaselString;
import powercraft.weasel.obj.WeaselVariableMap;


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
	public TreeMap<String, InstructionList> libs = new TreeMap<String, InstructionList>();
	
	public String runLib;
	
	// only temporary
	/** Flag that last instruction called requires pause */
	private boolean pauseRequested = false;


	/** Flag that program is finished */
	public boolean isProgramFinished = false;

	/** Number of restarts scheduled */
	private int restartsScheduled = 0;

	/** Value returned form an external call of a function */
	public WeaselObject externalCallRetval = null;

	private int statementsMax;
	
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
	private static final String nk_INSTRUCTION_LIST_LIBS = "LIB_INSTRUCTIONS";
	private static final String nk_RUN_INSTRUCTION = "RUNINSTRUCTIONS";
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
		NBTTagCompound libTag = new NBTTagCompound();
		
		libTag.setInteger("count", libs.size());
		int i=0;
		for(Entry<String, InstructionList>e:libs.entrySet()){
			libTag.setString("key["+i+"]", e.getKey());
			libTag.setCompoundTag("value["+i+"]", e.getValue().writeToNBT(new NBTTagCompound()));
		}
		
		tag.setCompoundTag(nk_INSTRUCTION_LIST_LIBS, libTag);
		if(runLib!=null){
			tag.setString(nk_RUN_INSTRUCTION, runLib);
		}
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
		
		NBTTagCompound libTag = tag.getCompoundTag(nk_INSTRUCTION_LIST_LIBS);
		
		libs.clear();
		
		int num = libTag.getInteger("count");
		for(int i=0; i<num; i++){
			String key = libTag.getString("key["+i+"]");
			InstructionList inst = (InstructionList) new InstructionList(this).readFromNBT(libTag.getCompoundTag("value["+i+"]"));
			libs.put(key, inst);
		}
		
		retval = WeaselObject.loadObjectFromNBT(tag.getCompoundTag(nk_RETURN_VALUE));
		
		if(tag.hasKey(nk_RUN_INSTRUCTION)){
			runLib = tag.getString(nk_RUN_INSTRUCTION);
		}else{
			runLib = null;
		}
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

	public void setStatementsToRun(int statementsMax){
		this.statementsMax = statementsMax;
	}
	
	public int getStatementsToRun(){
		return statementsMax;
	}
	
	/**
	 * Execute at most given number of statements
	 * 
	 * @param statementsMax max number of statements to execute
	 * @return true = all executed, false = PAUSE required
	 * @throws WeaselRuntimeException when something goes wrong.
	 */
	public boolean run() {

		//if (isProgramFinished) return true;

		for (; statementsMax > 0; statementsMax--) {
			try {
				if(runLib==null){
					instructionList.executeNextInstruction();
				}else if(libs.containsKey(runLib)){
					libs.get(runLib).executeNextInstruction();
				}else{
					throw new WeaselRuntimeException("Libary \""+runLib+"\" not found");
				}
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
		executingFunctionExternal = 0;

		systemStack.clear();
		dataStack.clear();

		retval = null;
		externalCallRetval = null;
		variables.clear();
		libs.clear();
		runLib = null;
		// not globals! they are preserved!
	}

	/**
	 * Restart program, clear system and data stacks and all variables.
	 */
	public void restartProgramClearGlobals() {
		restartProgram();
		globals.clear();
		libs.clear();
		runLib = null;
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
		libs.clear();
		runLib = null;
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
	 * Clear all variables, insert compiled program into instruction list and
	 * get ready for it's execution.
	 * 
	 * @param instructions the new program
	 */
	public void insertNewLibary(String name, List<Instruction> instructions) {
		if(!libs.containsKey(name)){
			InstructionList il = new InstructionList(this);
			il.addAll(instructions);
			libs.put(name, il);
		}
	}
	
	public void freeLibary(String name) {
		if(libs.containsKey(name)){
			libs.remove(name);
		}
	}
	
	/**
	 * Compile given program to a list of instruction.
	 * 
	 * @param program source code
	 * @return the list of instructions
	 * @throws SyntaxError when there's an error in the source code
	 */
	public static List<Instruction> compileProgram(String program) throws SyntaxError {
		return (new Compiler()).compile(program, false);
	}
	
	/**
	 * Compile given library source to a list of instruction.
	 * 
	 * @param program source code
	 * @return the list of instructions
	 * @throws SyntaxError when there's an error in the source code
	 */
	public static List<Instruction> compileLibrary(String program) throws SyntaxError {
		return (new Compiler()).compile(program, true);
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
			throw w;
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

		if (hw != null && (obj = hw.getVariable(name)) != null) {
			return obj;
		}

		if ((obj = globals.getVariable(name)) != null) {
			return obj;
		}

		return variables.getVariable(name);

	}


	@Override
	public void setVariable(String name, WeaselObject value) {

		if (name == null) throw new WeaselRuntimeException("Variable name cannot be null at " + name + " = " + value);
		if (value == null) throw new WeaselRuntimeException("Variable value cannot be null at " + name + " = " + value);

		if (hw != null && hw.getVariable(name) != null) {
			hw.setVariable(name, value);
			return;
		}

		if (globals.getVariable(name) != null) {
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
				String varname = Calc.toString(args[0]);				
				return new WeaselBoolean(getVariable(varname) != null);
			} else {
				throw new WeaselRuntimeException("Isset() requires String argument, got " + args[0].get().getClass().getSimpleName());
			}

		}
		
		if (functionName.equals("unset")) {

			if (args.length != 1) throw new WeaselRuntimeException("Unset() requires 1 argument, got " + args.length);
			if (args[0] instanceof WeaselString) {
				String varname = Calc.toString(args[0]);
				variables.unsetVariable(varname);
				globals.unsetVariable(varname);
				return null;
			} else {
				throw new WeaselRuntimeException("Unset() requires String argument, got " + args[0].get().getClass().getSimpleName());
			}

		}

		if (functionName.equals("get")) {

			if (args.length != 1) throw new WeaselRuntimeException("get() requires 1 argument, got " + args.length);
			if (args[0] instanceof WeaselString) {				
				String varname = Calc.toString(args[0]);				
				return getVariable(varname);
			} else {
				throw new WeaselRuntimeException("get() requires String argument, got " + args[0].get().getClass().getSimpleName());
			}

		}

		if (functionName.equals("set")) {

			if (args.length != 2 && args.length != 3) throw new WeaselRuntimeException("set() requires 2 or 3 arguments, got " + args.length);
			if (args[0] instanceof WeaselString) {
				String varname = Calc.toString(args[0]);
				boolean global = args.length==3?Calc.toBoolean(args[2]):false;
				if(global) {
					setVariableGlobal(varname, args[1]);
				}else {
					setVariable(varname, args[1]);
				}
			} else {
				throw new WeaselRuntimeException("set() requires String as 1st argument, got " + args[0].get().getClass().getSimpleName());
			}

		}

		if (functionName.equals("color")) {
			if (args.length == 1) {
				if (args[0] instanceof WeaselDouble) {
					return new WeaselDouble(Calc.toInteger(args[0]) & 0xffffff);
				}
				if (args[0] instanceof WeaselString) {
					return new WeaselDouble(PC_Color.getHexColorForName(Calc.toString(args[0])));
				}

				throw new WeaselRuntimeException("color() can't work with " + args[0].get().getClass().getSimpleName());
			} else if (args.length == 3) {
				if (args[0] instanceof WeaselDouble)
					if (args[1] instanceof WeaselDouble)
						if (args[2] instanceof WeaselDouble)
							return new WeaselDouble(clr(Calc.toInteger(args[0]), Calc.toInteger(args[1]), Calc.toInteger(args[2])));

				throw new WeaselRuntimeException("Invalid arguments for color().");
			} else {
				throw new WeaselRuntimeException("color() needs 1 or 3 arguments: name or r,g,b.");
			}

		}

		if (functionName.equals("bound")) {
			if (args.length == 3) {
				if (args[0] instanceof WeaselDouble) if (args[1] instanceof WeaselDouble) if (args[2] instanceof WeaselDouble) {
					return new WeaselDouble(Math.min(Calc.toInteger(args[2]), Math.max(Calc.toInteger(args[1]), Calc.toInteger(args[0]))));
				}

				throw new WeaselRuntimeException("Invalid arguments for bound().");
			} else {
				throw new WeaselRuntimeException("bound() needs 3 arguments: num,min,max.");
			}
		}

		if (functionName.equals("bound_c")) {
			if (args.length == 3) {
				if (args[0] instanceof WeaselDouble) if (args[1] instanceof WeaselDouble) if (args[2] instanceof WeaselDouble) {
					int min = Calc.toInteger(args[1]);
					int max = Calc.toInteger(args[2]);
					int val = Calc.toInteger(args[0]);
					while (val > max)
						val = val - (max - min);
					while (val < min)
						val = val + (max - min);
					return new WeaselDouble(val);
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
	 * @return -1 missing, 0 you must wait, 1 function called.
	 * @throws WeaselRuntimeException if the user function has bad number of
	 *             parameters declared.
	 */
	public int callFunctionExternal(String funcName, Object... args) throws WeaselRuntimeException {
		if(!instructionList.hasFunctionForExternalCall(funcName)) return -1;
		if (args == null) args = new Object[] {};
		if (instructionList.canCallFunctionExternal(funcName)) {
			isProgramFinished = false;
			instructionList.callFunctionExternal(funcName, Calc.s2w(args));
			return 1;
		} else {
			return 0;
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
		list.add("unset");
		list.add("set");
		list.add("get");
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
