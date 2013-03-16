package powercraft.weasel.lang;


import powercraft.weasel.engine.InstructionList;
import powercraft.weasel.engine.WeaselEngine;
import powercraft.weasel.exception.EndOfProgramException;
import powercraft.weasel.exception.WeaselRuntimeException;
import net.minecraft.nbt.NBTTagCompound;


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
