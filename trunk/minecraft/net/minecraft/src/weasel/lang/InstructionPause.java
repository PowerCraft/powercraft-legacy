package net.minecraft.src.weasel.lang;


import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.weasel.InstructionList;
import net.minecraft.src.weasel.WeaselEngine;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;


/**
 * Pause the program
 * 
 * @author MightyPork
 */
public class InstructionPause extends Instruction {

	/**
	 * PAUSE
	 */
	public InstructionPause() {}

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {
		engine.requestPause();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		return tag;
	}

	@Override
	public InstructionPause readFromNBT(NBTTagCompound tag) {
		return this;
	}

}
