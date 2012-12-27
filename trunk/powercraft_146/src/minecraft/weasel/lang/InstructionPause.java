package weasel.lang;


import net.minecraft.nbt.NBTTagCompound;
import weasel.InstructionList;
import weasel.WeaselEngine;
import weasel.exception.WeaselRuntimeException;


/**
 * Pause the program
 * 
 * @author MightyPork
 */
public class InstructionPause extends Instruction {

	/**
	 * PAUSE
	 */
	public InstructionPause() {
		super(InstructionType.PAUSE);
	}

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

	@Override
	public String toString() {
		return "PAUSE";
	}



}
