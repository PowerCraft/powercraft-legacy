package net.minecraft.src.weasel.lang;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;
import net.minecraft.src.weasel.InstructionList;
import net.minecraft.src.weasel.WeaselEngine;
import net.minecraft.src.weasel.exception.PauseRequestedException;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;
import net.minecraft.src.weasel.obj.WeaselObject;

/**
 * Push instruction
 * 
 * @author MightyPork
 *
 */
public class InstructionPush extends Instruction {
	
	private String pushedVariableName;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("Name", pushedVariableName);
		return tag;
	}

	@Override
	public InstructionPush readFromNBT(NBTTagCompound tag) {
		pushedVariableName = tag.getString("Name");
		return this;
	}
	
	/**
	 * @return name of this label
	 */
	public String getLabelName(){
		return pushedVariableName;
	}
	
	/**
	 * Set label name
	 * @param labelName label name to set
	 * @return this
	 */
	public InstructionPush setLabelName(String labelName){
		this.pushedVariableName = labelName;
		return this;
	}

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws PauseRequestedException, WeaselRuntimeException {
		WeaselObject obj = engine.variables.get(pushedVariableName);
		if(obj == null) throw new WeaselRuntimeException("Variable "+pushedVariableName+" does not exist in this scope.");
		engine.dataStack.push(obj);
	}

}
