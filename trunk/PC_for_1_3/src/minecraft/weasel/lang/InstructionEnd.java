package weasel.lang;


import net.minecraft.src.NBTTagCompound;
import weasel.InstructionList;
import weasel.WeaselEngine;
import weasel.exception.EndOfProgramException;
import weasel.exception.WeaselRuntimeException;


/**
 * Terminate the program
 * 
 * @author MightyPork
 */
public class InstructionEnd extends Instruction {

	/**
	 * END
	 */
	public InstructionEnd() {
		super(InstructionType.END);
	}

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {
		instructionList.movePointer(-1);
		throw new EndOfProgramException();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		return tag;
	}

	@Override
	public InstructionEnd readFromNBT(NBTTagCompound tag) {
		return this;
	}

	@Override
	public String toString() {
		return "END";
	}



}
