package net.minecraft.src.weasel.lang;


import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.weasel.InstructionList;
import net.minecraft.src.weasel.WeaselEngine;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;


/**
 * GOTO instruction, jumps to label.
 * 
 * @author MightyPork
 */
public class InstructionGoto extends Instruction {
	
	/**
	 * GOTO
	 * @param targetLabel target label name
	 */
	public InstructionGoto(String targetLabel) {
		this.targetLabelName = targetLabel;
	}

	/**
	 * GOTO
	 */
	public InstructionGoto() {}

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
		return "GOTO '"+targetLabelName+"'";
	}
}
