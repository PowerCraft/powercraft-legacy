package weasel.lang;


import java.util.HashMap;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;
import weasel.InstructionList;
import weasel.WeaselEngine;
import weasel.exception.WeaselRuntimeException;


/**
 * Weasel instruction
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public abstract class Instruction implements PC_INBT {

	/**
	 * Instruction
	 * 
	 * @param type instruction type (enum)
	 */
	protected Instruction(InstructionType type) {
		setType(type);
	}

	private int address = -1;

	/**
	 * Execute the instruction
	 * 
	 * @param engine the weasel engine
	 * @param instructionList the instruction list the instruction is in
	 * @throws WeaselRuntimeException thrown if execution of this instruction
	 *             failed.
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
	 * 
	 * @param instruction instruction to save
	 * @param tag compound tag to save into
	 * @return the tag
	 */
	public static final NBTTagCompound saveInstructionToNBT(Instruction instruction, NBTTagCompound tag) {
		tag.setString("type", instruction.getType().toString());
		tag.setInteger("address", instruction.address);
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
		Instruction read = null;
		switch (InstructionType.getTypeFromName(tag.getString("type"))) {
			case LABEL:
				read = new InstructionLabel().readFromNBT(tag);
				break;

			case GOTO:
				read = new InstructionGoto().readFromNBT(tag);
				break;

			case CALL:
				read = new InstructionCall().readFromNBT(tag);
				break;

			case _CALL:
				read = new InstructionStringCall().readFromNBT(tag);
				break;

			case FUNCTION:
				read = new InstructionFunction().readFromNBT(tag);
				break;

			case RETURN:
				read = new InstructionReturn().readFromNBT(tag);
				break;

			case ASSIGN:
				read = new InstructionAssign().readFromNBT(tag);
				break;

			case ASSIGN_RETVAL:
				read = new InstructionAssignRetval().readFromNBT(tag);
				break;

			case PUSH:
				read = new InstructionPush().readFromNBT(tag);
				break;

			case POP:
				read = new InstructionPop().readFromNBT(tag);
				break;

			case IF:
				read = new InstructionIf().readFromNBT(tag);
				break;

			case END:
				read = new InstructionEnd().readFromNBT(tag);
				break;

			case PAUSE:
				read = new InstructionPause().readFromNBT(tag);
				break;

			case RESTART:
				read = new InstructionRestart().readFromNBT(tag);
				break;

			default:
				return null;
		}

		read.address = tag.getInteger("address");

		return read;
	}

	/**
	 * Type of an instruction
	 * 
	 * @author MightyPork
	 */
	@SuppressWarnings("javadoc")
	protected static enum InstructionType {

		LABEL, GOTO, CALL, _CALL, FUNCTION, RETURN, ASSIGN, ASSIGN_RETVAL, UNSET, PUSH, POP, IF, END, PAUSE, RESTART;

		private static HashMap<String, InstructionType> members = new HashMap<String, InstructionType>();

		/**
		 * Get enum type for type name
		 * 
		 * @param index type name
		 * @return corresponding enum type
		 */
		public static InstructionType getTypeFromName(String name) {
			return members.get(name);
		}

		static {
			for (InstructionType type : InstructionType.values()) {
				members.put(type.toString(), type);
			}
		}
	}

	@Override
	public abstract String toString();

}
