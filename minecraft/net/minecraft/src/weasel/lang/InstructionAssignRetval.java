package net.minecraft.src.weasel.lang;


import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.weasel.InstructionList;
import net.minecraft.src.weasel.WeaselEngine;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;
import net.minecraft.src.weasel.obj.WeaselObject;


/**
 * Assignment instruction, assigning result of a function call.
 * 
 * @author MightyPork
 */
public class InstructionAssignRetval extends Instruction {
	
	/**
	 * ASSIGN_RETVAL
	 * @param varName name of the variable to store returned value into
	 */
	public InstructionAssignRetval(String varName) {
		this.lhsVarName = varName;
	}

	/**
	 * ASSIGN_RETVAL
	 */
	public InstructionAssignRetval() {}
	
	private String lhsVarName;

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {
		
		Object result = engine.retval;
		
		if(result == null) {
			throw new WeaselRuntimeException("ASSIGN_RETVAL - returned value is NULL.");
		}
		
		if(lhsVarName != null && !lhsVarName.equals("")) {
			engine.setVariable(lhsVarName, WeaselObject.getWrapperForValue(result));
		}
		
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("LHS", lhsVarName);
		return tag;
	}

	@Override
	public InstructionAssignRetval readFromNBT(NBTTagCompound tag) {
		lhsVarName = tag.getString("LHS");
		return this;
	}

	/**
	 * Get LHS variable name.
	 * @return the name of Left Hand Side variable (result is stored in it)
	 */
	public String getVarName() {
		return lhsVarName;
	}

	/**
	 * Set LHS variable name.
	 * @param lhsVarName the name of Left Hand Side variable (result is stored in it)
	 */
	public void setVarName(String lhsVarName) {
		this.lhsVarName = lhsVarName;
	}
	
	@Override
	public String toString() {
		return "ASSIGN_RETVAL -> '"+lhsVarName+"'";
	}

}
