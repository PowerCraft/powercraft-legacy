package weasel.lang;


import net.minecraft.src.NBTTagCompound;
import weasel.InstructionList;
import weasel.WeaselEngine;
import weasel.exception.EndOfProgramException;
import weasel.exception.WeaselRuntimeException;


/**
 * Restart the program, clear all but global variables
 * 
 * @author MightyPork
 */
public class InstructionRestart extends Instruction {

	/**
	 * RESTART
	 */
	public InstructionRestart() {
		super(InstructionType.RESTART);
	}

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {
//		if (engine.executingFunctionExternal > 0) {
//			engine.restartProgram();
//			engine.requestPause();
//			engine.isProgramFinished = false;
//		} else {
			engine.scheduleRestart();
			throw new EndOfProgramException();
//		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		return tag;
	}

	@Override
	public InstructionRestart readFromNBT(NBTTagCompound tag) {
		return this;
	}

	@Override
	public String toString() {
		return "RESTART";
	}



}
