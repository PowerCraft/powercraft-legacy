package net.minecraft.src.weasel.lang;


import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.weasel.InstructionList;
import net.minecraft.src.weasel.WeaselEngine;
import net.minecraft.src.weasel.exception.EndOfProgramException;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;


/**
 * Terminate the program
 * 
 * @author MightyPork
 */
public class InstructionEnd extends Instruction {

	/**
	 * END
	 */
	public InstructionEnd() {}

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

}
