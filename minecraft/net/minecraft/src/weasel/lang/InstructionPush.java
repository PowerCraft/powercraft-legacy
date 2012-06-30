package net.minecraft.src.weasel.lang;


import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.weasel.InstructionList;
import net.minecraft.src.weasel.WeaselEngine;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;
import net.minecraft.src.weasel.obj.WeaselObject;


/**
 * Push instruction
 * 
 * @author MightyPork
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
	 * @return name of pushed variable
	 */
	public String getVariableName() {
		return pushedVariableName;
	}

	/**
	 * Set pushed variable name
	 * 
	 * @param variableName name of the pushed variable
	 * @return this
	 */
	public InstructionPush setVariableName(String variableName) {
		this.pushedVariableName = variableName;
		return this;
	}

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {
		WeaselObject obj = engine.variables.getVariable(pushedVariableName);
		if (obj == null) throw new WeaselRuntimeException("Variable " + pushedVariableName + " does not exist in this scope.");
		engine.dataStack.push(obj);
	}

}
