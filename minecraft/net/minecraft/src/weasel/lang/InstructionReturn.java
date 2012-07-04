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
public class InstructionReturn extends Instruction {
	
	private String retvarName;

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {
		
		if(retvarName == null || retvarName.equals("")) {
			instructionList.returnFromCall(null);
		}
		
		instructionList.returnFromCall(engine.getVariable(retvarName));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("ReturnVar", (retvarName==null?"":retvarName));
		return tag;
	}

	@Override
	public InstructionReturn readFromNBT(NBTTagCompound tag) {
		retvarName = tag.getString("ReturnVar");
		return this;
	}

	/**
	 * @return name of target label
	 */
	public String getReturnedVariableName() {
		return retvarName;
	}

	/**
	 * Set target label name
	 * 
	 * @param retvarName name of the returned variable. Use null or "" for void.
	 * @return this
	 */
	public InstructionReturn setReturnedVariableName(String retvarName) {
		this.retvarName = retvarName;
		return this;
	}

}
