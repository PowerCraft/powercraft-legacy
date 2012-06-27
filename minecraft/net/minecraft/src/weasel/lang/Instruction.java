package net.minecraft.src.weasel.lang;


import java.util.ArrayList;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;
import net.minecraft.src.weasel.InstructionList;
import net.minecraft.src.weasel.WeaselEngine;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;


/**
 * Weasel instruction
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public abstract class Instruction implements PC_INBT {

	private int address = -1;

	/**
	 * Execute the instruction
	 * 
	 * @param engine the weasel engine
	 * @param instructionList the instruction list the instruction is in
	 * @throws WeaselRuntimeException thrown if execution of this instruction failed.
	 */
	public abstract void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException;

	/**
	 * Set instruction address in {@link InstructionList}
	 * 
	 * @param address address
	 * @return this
	 */
	public final Instruction setAddress(int address) {
		this.address = address;
		return this;
	}

	/**
	 * Get {@link InstructionList} address in {@link InstructionList}
	 * 
	 * @return address
	 */
	public final int getAddress() {
		return this.address;
	}

	private InstructionType type;

	/**
	 * Get instruction type
	 * 
	 * @return type
	 */
	public final InstructionType getType() {
		return type;
	}

	/**
	 * Set instruction type
	 * 
	 * @param type
	 * @return this
	 */
	public final Instruction setType(InstructionType type) {
		this.type = type;
		return this;
	}



	/**
	 * Save a given instruction to {@link NBTTagCompound}
	 * @param instruction instruction to save
	 * @param tag compound tag to save into
	 * @return the tag
	 */
	public static final NBTTagCompound saveInstructionToNBT(Instruction instruction, NBTTagCompound tag) {
		tag.setInteger("type", instruction.getType().index);
		instruction.writeToNBT(tag);
		return tag;
	}

	/**
	 * Load a correct subtype of {@link Instruction} from a
	 * {@link NBTTagCompound}
	 * 
	 * @param tag the compound tag
	 * @return instruction loaded
	 */
	public static final Instruction loadInstructionFromNBT(NBTTagCompound tag) {
		switch (InstructionType.getTypeFromIndex(tag.getInteger("type"))) {
			case LABEL:
				return new InstructionLabel().readFromNBT(tag);
			case GOTO:
				return new InstructionGoto().readFromNBT(tag);
			case PUSH:
				return new InstructionPush().readFromNBT(tag);
			case POP:
				return new InstructionPop().readFromNBT(tag);
			default:
				return null;
		}
	}


	/**
	 * Type of an instruction
	 * 
	 * @author MightyPork
	 */
	@SuppressWarnings("javadoc")
	protected enum InstructionType {

		LABEL, GOTO, CALL, FUNCTION, SET, SET_RETVAL, PUSH, POP, IF, END, PAUSE;

		private InstructionType() {
			setup();
		}
		
		private static int counter = 1;
		private static ArrayList<InstructionType> members;

		static {
			members = new ArrayList<Instruction.InstructionType>();
			members.add(null);
		}
		
		private void setup() {			
			index = counter++;
			members.add(this);
		}
		
		/**
		 * Get enum type for type index
		 * 
		 * @param index type index
		 * @return corresponding enum type
		 */
		public static InstructionType getTypeFromIndex(int index) {
			return members.get(index);
		}

		/** enum index */
		public int index;
	}

}
