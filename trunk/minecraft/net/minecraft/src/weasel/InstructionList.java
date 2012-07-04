package net.minecraft.src.weasel;


import java.util.ArrayList;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.PC_INBT;
import net.minecraft.src.weasel.exception.EndOfProgramException;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;
import net.minecraft.src.weasel.lang.Instruction;
import net.minecraft.src.weasel.lang.InstructionFunction;
import net.minecraft.src.weasel.lang.InstructionLabel;
import net.minecraft.src.weasel.obj.WeaselInteger;
import net.minecraft.src.weasel.obj.WeaselObject;
import net.minecraft.src.weasel.obj.WeaselVariableMap;


/**
 * Weasel instruction list
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class InstructionList implements PC_INBT {

	/** Instruction list. Use appendInstruction to add new instructions. */
	public ArrayList<Instruction> list = new ArrayList<Instruction>();
	private WeaselEngine engine;
	private int programCounter = 0;


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
	public void appendInstruction(Instruction instruction) {
		list.add(instruction);
		instruction.setAddress(list.size() - 1);
	}

	/**
	 * Move program pointer to specified address
	 * 
	 * @param index address
	 */
	public void movePointerTo(int index) {
		if (index < 0 || index > list.size()) throw new WeaselRuntimeException("INSTRL goto - jump out of program space.");
		programCounter = index;
	}

	/**
	 * Move program pointer to specified address, if it's within scope.
	 * 
	 * @param labelName
	 */
	public void gotoLabel(String labelName) {
		for (Instruction instruction : list) {
			if (instruction instanceof InstructionLabel) {
				if (((InstructionLabel) instruction).getLabelName().equals(labelName)) {
					movePointerTo(instruction.getAddress());
				}
			}
		}
	}

	/**
	 * Stack what needs to be stacked, and move pointer to a header of the
	 * specified function
	 * 
	 * @param functionName name of a function
	 * @param args arguments for the call
	 */
	public void callFunction(String functionName, WeaselObject[] args) {
		for (Instruction instruction : list) {
			if (instruction instanceof InstructionFunction) {
				
				InstructionFunction func = (InstructionFunction) instruction;
				if (func.getFunctionName().equals(functionName)) {
					
					if(func.getArgumentCount() != args.length) {
						throw new WeaselRuntimeException("INSTRL call - invalid argument count for function "+functionName);
					}
					
					engine.systemStack.push(new WeaselInteger(programCounter));
					engine.systemStack.push(engine.variables);

					programCounter = func.getAddress();
					engine.variables.clear();
					int cnt = 0;
					for (WeaselObject obj : args) {
						String argname = func.getArgumentName(cnt++);
						
						if(argname == null) throw new WeaselRuntimeException("INSTRL call - invalid argument count for function "+functionName);
						
						engine.variables.setVariable(argname, obj);
					}

					return;
					
				}
				
			}
		}

		// if the function was not found in the program space, try hardware.
		if (engine.nativeFunctionExists(functionName)) {
			engine.callNativeFunction(functionName, args);
		} else {
			throw new WeaselRuntimeException("INSTRL Call - function " + functionName + " does not exist.");
		}

	}

	/**
	 * Return from a program-space function.
	 * 
	 * @param retval returned value. Return null for void functions.
	 */
	public void returnFromCall(WeaselObject retval) {
		engine.setReturnValue(retval);

		engine.variables = (WeaselVariableMap) engine.systemStack.pop();
		programCounter = ((WeaselInteger) engine.systemStack.pop()).get();
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

		return tag;

	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {

		// get list length
		int size = tag.getInteger(nk_SIZE);

		if (list == null) list = new ArrayList<Instruction>();

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

		return this;

	}

}
