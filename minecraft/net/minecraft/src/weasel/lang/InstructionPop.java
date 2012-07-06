package net.minecraft.src.weasel.lang;


import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.weasel.InstructionList;
import net.minecraft.src.weasel.WeaselEngine;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;


/**
 * Pop instruction
 * 
 * @author MightyPork
 */
public class InstructionPop extends Instruction {
	
	/**
	 * POP
	 * @param poppedVarName variable to store the result in
	 */
	public InstructionPop(String poppedVarName) {
		this.poppedVarName = poppedVarName;
	}
	
	/**
	 * POP
	 */
	public InstructionPop() {}

	private String poppedVarName;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("Name", poppedVarName);
		return tag;
	}

	@Override
	public InstructionPop readFromNBT(NBTTagCompound tag) {
		poppedVarName = tag.getString("Name");
		return this;
	}

	/**
	 * @return name of popped variable
	 */
	public String getVariableName() {
		return poppedVarName;
	}

	/**
	 * Set popped variable name
	 * 
	 * @param variableName name of the popped variable
	 * @return this
	 */
	public InstructionPop setVariableName(String variableName) {
		this.poppedVarName = variableName;
		return this;
	}

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {
		engine.variables.setVariable(poppedVarName, engine.dataStack.pop());
	}
	
	@Override
	public String toString() {
		return "POP -> '"+poppedVarName+"'";
	}

}
