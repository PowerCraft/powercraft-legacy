package powercraft.weasel.lang;


import powercraft.weasel.engine.InstructionList;
import powercraft.weasel.engine.WeaselEngine;
import powercraft.weasel.exception.WeaselRuntimeException;
import net.minecraft.nbt.NBTTagCompound;


/**
 * GOTO instruction, jumps to label.
 * 
 * @author MightyPork
 */
public class InstructionGoto extends Instruction {

	/**
	 * GOTO
	 * 
	 * @param targetLabel target label name
	 */
	public InstructionGoto(String targetLabel) {
		super(InstructionType.GOTO);
		this.targetLabelName = targetLabel;
	}

	/**
	 * GOTO
	 */
	public InstructionGoto() {
		super(InstructionType.GOTO);
	}

	private String targetLabelName;

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {
		instructionList.gotoLabel(targetLabelName);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("Target", targetLabelName);
		return tag;
	}

	@Override
	public InstructionGoto readFromNBT(NBTTagCompound tag) {
		targetLabelName = tag.getString("Target");
		return this;
	}

	/**
	 * @return name of target label
	 */
	public String getTargetLabelName() {
		return targetLabelName;
	}

	/**
	 * Set target label name
	 * 
	 * @param labelName target label name to set
	 * @return this
	 */
	public InstructionGoto setTargetLabelName(String labelName) {
		this.targetLabelName = labelName;
		return this;
	}

	@Override
	public String toString() {
		return "GOTO '" + targetLabelName + "'";
	}
}
