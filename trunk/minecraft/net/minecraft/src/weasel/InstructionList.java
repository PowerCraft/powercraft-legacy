package net.minecraft.src.weasel;


import java.util.ArrayList;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.PC_INBT;
import net.minecraft.src.weasel.exception.EndOfScopeException;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;
import net.minecraft.src.weasel.lang.Instruction;
import net.minecraft.src.weasel.lang.InstructionLabel;


/**
 * Weasel instruction list
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class InstructionList implements PC_INBT {

	private ArrayList<Instruction> list = new ArrayList<Instruction>();
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
	 * @param instruction the new instruction to append
	 */
	public void appendInstruction(Instruction instruction) {
		list.add(instruction);
		instruction.setAddress(list.size() - 1);
	}

	/**
	 * Move program pointer to specified address
	 * @param index address
	 */
	public void movePointerTo(int index) {
		if (!Utils.isInRange(index, engine.scopeStart, engine.scopeEnd)) throw new WeaselRuntimeException("Jump target out of scope range.");
		programCounter = index;
	}

	/**
	 * Move program pointer to specified address, if it's within scope.
	 * @param labelName
	 */
	public void gotoLabel(String labelName) {
		for (Instruction instruction : list) {
			if (instruction instanceof InstructionLabel) {
				if(((InstructionLabel)instruction).getLabelName().equals(labelName)) {
					movePointerTo(instruction.getAddress());
				}
			}
		}
	}

	/**
	 * Execute next instruction (the one pointed by programCounter)
	 * @throws EndOfScopeException if end of instruction list, or end of scope was reached
	 */
	public void executeNextInstruction() throws EndOfScopeException {
		if (programCounter >= list.size()) throw new EndOfScopeException();
		if (programCounter > engine.scopeEnd) throw new EndOfScopeException();
		Instruction instruction = list.get(programCounter++);
		instruction.execute(engine, this);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {

		NBTTagList tags = new NBTTagList();

		int index = 0;
		int size = list.size();
		for (index = 0; index < size; index++) {
			NBTTagCompound tag1 = Instruction.saveInstructionToNBT(list.get(index), new NBTTagCompound());
			tag1.setInteger("Index", index);
			tags.appendTag(tag1);
		}
		tag.setTag("List", tags);
		tag.setInteger("Size", size);

		return tag;

	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {

		int size = tag.getInteger("Size");
		list.clear();
		for (int i = 0; i < size; i++)
			list.add(null);

		NBTTagList tags = tag.getTagList("List");

		for (int i = 0; i < tags.tagCount(); i++) {
			NBTTagCompound tag1 = (NBTTagCompound) tags.tagAt(i);
			list.set(tag1.getInteger("Index"), Instruction.loadInstructionFromNBT(tag1));
		}

		return this;

	}

}
