package net.minecraft.src.weasel.lang;


import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.weasel.InstructionList;
import net.minecraft.src.weasel.WeaselEngine;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;


/**
 * Label instruction, target for jumps.
 * 
 * @author MightyPork
 *
 */
public class InstructionLabel extends Instruction {


	private String labelName;

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("Name", labelName);
		return tag;
	}

	@Override
	public InstructionLabel readFromNBT(NBTTagCompound tag) {
		labelName = tag.getString("Name");
		return this;
	}
	
	/**
	 * @return name of this label
	 */
	public String getLabelName(){
		return labelName;
	}
	
	/**
	 * Set label name
	 * @param labelName label name to set
	 * @return this
	 */
	public InstructionLabel setLabelName(String labelName){
		this.labelName = labelName;
		return this;
	}

}
