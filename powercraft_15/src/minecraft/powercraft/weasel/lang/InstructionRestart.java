package powercraft.weasel.lang;


import powercraft.weasel.engine.InstructionList;
import powercraft.weasel.engine.WeaselEngine;
import powercraft.weasel.exception.EndOfProgramException;
import powercraft.weasel.exception.WeaselRuntimeException;
import net.minecraft.nbt.NBTTagCompound;


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
