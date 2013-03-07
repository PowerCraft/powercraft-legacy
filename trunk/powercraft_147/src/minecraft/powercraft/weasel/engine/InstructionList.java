package powercraft.weasel.engine;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import powercraft.api.PC_INBT;
import powercraft.weasel.exception.EndOfProgramException;
import powercraft.weasel.exception.WeaselRuntimeException;
import powercraft.weasel.lang.Instruction;
import powercraft.weasel.lang.InstructionFunction;
import powercraft.weasel.lang.InstructionLabel;
import powercraft.weasel.obj.WeaselBoolean;
import powercraft.weasel.obj.WeaselDouble;
import powercraft.weasel.obj.WeaselFunctionCall;
import powercraft.weasel.obj.WeaselNull;
import powercraft.weasel.obj.WeaselObject;
import powercraft.weasel.obj.WeaselString;
import powercraft.weasel.obj.WeaselVariableMap;


/**
 * Weasel instruction list
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class InstructionList implements PC_INBT {

	/** Instruction list. Use appendInstruction to add new instructions. */
	public ArrayList<Instruction> list = new ArrayList<Instruction>();

	private Map<String, Integer> labelMap = new HashMap<String, Integer>();
	private boolean labelMapGenerated = false;

	private Map<String, Integer> functionMap = new HashMap<String, Integer>();
	private boolean functionMapGenerated = false;

	private WeaselEngine engine;
	/** program pointer, read only. */
	public int programCounter = 0;
	
	/** last called function used for error reporting in get retval. */
	public String lastFuncCall = "";

	/**
	 * Clear all from the list
	 */
	public void clear() {
		list.clear();
		programCounter = 0;
		functionMapGenerated = false;
		labelMapGenerated = false;
	}


	/**
	 * Instruction list for VM
	 * 
	 * @param engine
	 */
	public InstructionList(WeaselEngine engine) {
		this.engine = engine;
	}

	/**
	 * Append an instruction and set it's address
	 * 
	 * @param instruction the new instruction to append
	 */
	public void add(Instruction instruction) {
		list.add(instruction);
		instruction.setAddress(list.size() - 1);
	}


	/**
	 * Append all instructions from a list to the instruction list.
	 * 
	 * @param list2
	 */
	public void addAll(List<Instruction> list2) {
		for (Instruction instruction : list2) {
			add(instruction);
		}
	}

	/**
	 * Make a table of label addresses for faster program execution
	 */
	public void generateLabelMap() {

		if (labelMap == null) {
			labelMap = new HashMap<String, Integer>();
		} else {
			labelMap.clear();
		}

		for (Instruction instruction : list) {
			if (instruction instanceof InstructionLabel) {
				labelMap.put(((InstructionLabel) instruction).getLabelName(), instruction.getAddress());
			}
		}

		labelMapGenerated = true;
	}


	/**
	 * Make a table of label addresses for faster program execution
	 */
	public void generateFunctionMap() {

		if (functionMap == null) {
			functionMap = new HashMap<String, Integer>();
		} else {
			functionMap.clear();
		}

		for (Instruction instruction : list) {
			if (instruction instanceof InstructionFunction) {
				functionMap.put(((InstructionFunction) instruction).getFunctionName(), instruction.getAddress());
			}
		}

		functionMapGenerated = true;
	}

	/**
	 * Move program pointer to specified address
	 * 
	 * @param index address
	 */
	public void movePointerTo(int index) {
		if (index < 0 || index > list.size()) throw new WeaselRuntimeException("Jump out of program space.");
		programCounter = index;
	}

	/**
	 * Move program pointer to specified address, if it's within scope.
	 * 
	 * @param labelName
	 */
	public void gotoLabel(String labelName) {
		if (!labelMapGenerated) generateLabelMap();
		movePointerTo(labelMap.get(labelName));
	}

	/**
	 * Check if function called from external caller does exist
	 * 
	 * @param functionName
	 * @return can call this function externally
	 */
	public boolean canCallFunctionExternal(String functionName) {
		return hasFunctionForExternalCall(functionName) && engine.executingFunctionExternal == 0;
	}
	
	/**
	 * Check if function called from external caller does exist
	 * 
	 * @param functionName
	 * @return can call this function externally
	 */
	public boolean hasFunctionForExternalCall(String functionName) {
		if (!functionMapGenerated) generateFunctionMap();
		return functionMap.get(functionName) != null;
	}

	/**
	 * Call program function from outside the program. Return in this function
	 * will then pause the engine.
	 * 
	 * @param functionName name of a function to call
	 * @param args arguments for the call
	 */
	public void callFunctionExternal(String functionName, WeaselObject[] args) {
		engine.systemStack.push(engine.retval);
		callFunction_do(true, functionName, args);
	}

	/**
	 * Call program function from within the program.
	 * 
	 * @param functionName name of a function to call
	 * @param args arguments for the call
	 */
	public void callFunction(String functionName, WeaselObject[] args) {
		callFunction_do(false, functionName, args);
	}

	/**
	 * Stack what needs to be stacked, and move pointer to a header of the
	 * specified function
	 * 
	 * @param external flag that this call comes from an external caller, not
	 *            from the program.
	 * @param functionName name of a function
	 * @param args arguments for the call
	 */
	private void callFunction_do(boolean external, String functionName, WeaselObject[] args) {

		// engine's own functions
		if (!external && engine.doesProvideFunction(functionName)) {
			engine.setReturnValue(engine.callProvidedFunction(engine, functionName, args));
			return;
		}

		// hardware functions
		if (!external && engine.hardwareFunctionExists(functionName)) {
			engine.callHardwareFunction(functionName, args);
			if(engine.retval instanceof WeaselFunctionCall){
				WeaselFunctionCall fc = (WeaselFunctionCall)engine.retval;
				
				if(fc.lib==null){
					engine.instructionList.callFunction_do(false, fc.functionName, fc.args);
				}else{
					engine.libs.get(fc.lib).callFunction_do(false, fc.functionName, fc.args);
				}
				engine.runLib = fc.lib;
			}
			return;
		}

		// program functions

		if (!functionMapGenerated) generateFunctionMap();
		Integer address = functionMap.get(functionName);

		if (address != null) {
			InstructionFunction func = (InstructionFunction) list.get(address);

			if (func.getArgumentCount() != args.length) {
				throw new WeaselRuntimeException("Invalid argument count for function " + functionName + " (needs " + func.getArgumentCount()
						+ ", got " + args.length + ")");
			}

			engine.systemStack.push(new WeaselBoolean(external));
			engine.systemStack.push(new WeaselDouble(programCounter));
			engine.systemStack.push(engine.variables);
			if(engine.runLib==null){
				engine.systemStack.push(new WeaselNull());
			}else{
				engine.systemStack.push(new WeaselString(engine.runLib));
			}

			programCounter = func.getAddress();
			engine.variables.clear();
			int cnt = 0;
			for (WeaselObject obj : args) {
				String argname = func.getArgumentName(cnt++);

				if (argname == null)
					throw new WeaselRuntimeException("Invalid argument count for function " + functionName + " (needs " + func.getArgumentCount()
							+ ", got " + args.length + ")");

				engine.variables.setVariable(argname, obj);
			}
			
			if(!external) lastFuncCall = functionName;

			return;
		}

		throw new WeaselRuntimeException("Function \"" + functionName + "\" does not exist.");

	}

	/**
	 * Return from a program-space function.
	 * 
	 * @param retval returned value. Return null for void functions.
	 */
	public void returnFromCall(WeaselObject retval) {

		engine.setReturnValue(retval);

		WeaselObject o = engine.systemStack.pop();
		if(o==null){
			engine.runLib = null;
		}else{
			engine.runLib = ((WeaselString)o).string;
		}
		engine.variables = (WeaselVariableMap) engine.systemStack.pop();
		programCounter = ((WeaselDouble) engine.systemStack.pop()).getI();
		boolean wasExternal = ((WeaselBoolean) engine.systemStack.pop()).get();

		if (wasExternal) {
			//engine.externalCallRetval = engine.retval;
			engine.retval = engine.systemStack.pop();
			//engine.requestPause();
		}

	}

	/**
	 * Execute next instruction (the one pointed by programCounter)
	 * 
	 * @throws EndOfProgramException if end of instruction list, or end of scope
	 *             was reached
	 */
	public void executeNextInstruction() throws EndOfProgramException {
		if (programCounter >= list.size()) throw new EndOfProgramException();
		if (programCounter < 0) throw new EndOfProgramException();
		Instruction instruction = list.get(programCounter++);

		instruction.execute(engine, this);
	}


	private static final String nk_SIZE = "Size";
	private static final String nk_LIST = "List";
	private static final String nk_INDEX = "Index";
	private static final String nk_PC = "PC";

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {

		NBTTagList tags = new NBTTagList();

		int index = 0;
		int size = list.size();
		for (index = 0; index < size; index++) {
			NBTTagCompound tag1 = Instruction.saveInstructionToNBT(list.get(index), new NBTTagCompound());
			tag1.setInteger(nk_INDEX, index);
			tags.appendTag(tag1);
		}
		tag.setTag(nk_LIST, tags);
		tag.setInteger(nk_SIZE, size);

		tag.setInteger(nk_PC, programCounter);

		return tag;
		
	}

	@Override
	public InstructionList readFromNBT(NBTTagCompound tag) {

		// get list length
		int size = tag.getInteger(nk_SIZE);

		if (list == null) list = new ArrayList<Instruction>(size);

		// fill the list with nulls.
		list.clear();
		for (int i = 0; i < size; i++) {
			list.add(null);
		}

		NBTTagList tags = tag.getTagList(nk_LIST);

		//store instructions into the list
		for (int i = 0; i < tags.tagCount(); i++) {
			NBTTagCompound tag1 = (NBTTagCompound) tags.tagAt(i);
			list.set(tag1.getInteger(nk_INDEX), Instruction.loadInstructionFromNBT(tag1));
		}

		programCounter = tag.getInteger(nk_PC);
		
		return this;
	}

	/**
	 * Add an increment to program counter.Can be used to prevent further
	 * program execution by adding -1 inside an instruction body.
	 * 
	 * @param change increment
	 */
	public void movePointer(int change) {
		programCounter += change;
	}



}
