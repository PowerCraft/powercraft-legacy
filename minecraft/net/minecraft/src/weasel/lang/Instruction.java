package net.minecraft.src.weasel.lang;


import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;
import net.minecraft.src.weasel.InstructionList;
import net.minecraft.src.weasel.WeaselEngine;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;
import net.minecraft.src.weasel.exception.PauseRequestedException;
import net.minecraft.src.weasel.obj.WeaselObjectType;


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
	 * @throws PauseRequestedException thrown at the end of instruction if
	 *             Weasel Engine should pause program and wait for external
	 *             resume call.
	 * @throws WeaselRuntimeException thrown if execution of this instruction
	 *             failed.
	 */
	public abstract void execute(WeaselEngine engine, InstructionList instructionList) throws PauseRequestedException, WeaselRuntimeException;

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
				return (Instruction) new InstructionLabel().readFromNBT(tag);
			case PUSH:
				return (Instruction) new InstructionPush().readFromNBT(tag);
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

		LABEL(1), CALL(2), FUNCTION(3), SET(4), PUSH(5), POP(6), IF(7), CALL_HW(8), END(9), PAUSE(10);

		private InstructionType(int i) {
			index = i;
		}

		/**
		 * Get enum type for type index
		 * 
		 * @param index type index
		 * @return corresponding enum type
		 */
		public static InstructionType getTypeFromIndex(int index) {
			switch (index) {
				case 1:
					return LABEL;
				case 2:
					return CALL;
				case 3:
					return FUNCTION;
				case 4:
					return SET;
				case 5:
					return PUSH;
				case 6:
					return POP;
				case 7:
					return IF;
				case 8:
					return CALL_HW;
				case 9:
					return END;
				case 10:
					return PAUSE;
				default:
					return null;
			}
		}

		/** enum index */
		public int index;
	}

}
