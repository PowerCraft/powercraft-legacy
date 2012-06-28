package net.minecraft.src.weasel.lang;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.weasel.InstructionList;
import net.minecraft.src.weasel.WeaselEngine;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;

/**
 * Pop instruction
 * 
 * @author MightyPork
 *
 */
public class InstructionPop extends Instruction {
	
	private String poppedVariableName;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("Name", poppedVariableName);
		return tag;
	}

	@Override
	public InstructionPop readFromNBT(NBTTagCompound tag) {
		poppedVariableName = tag.getString("Name");
		return this;
	}
	
	/**
	 * @return name of popped variable
	 */
	public String getVariableName(){
		return poppedVariableName;
	}
	
	/**
	 * Set popped variable name
	 * @param variableName name of the popped variable
	 * @return this
	 */
	public InstructionPop setVariableName(String variableName){
		this.poppedVariableName = variableName;
		return this;
	}

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {
		engine.variables.setVariable(poppedVariableName,engine.dataStack.pop());
	}

}
