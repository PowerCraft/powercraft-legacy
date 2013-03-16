package powercraft.weasel.lang;


import powercraft.weasel.engine.InstructionList;
import powercraft.weasel.engine.WeaselEngine;
import powercraft.weasel.exception.WeaselRuntimeException;
import powercraft.weasel.obj.WeaselObject;
import net.minecraft.nbt.NBTTagCompound;


/**
 * Assignment instruction, assigning result of a function call.
 * 
 * @author MightyPork
 */
public class InstructionAssignRetval extends Instruction {

	private boolean global;

	/**
	 * ASSIGN_RETVAL
	 * 
	 * @param global variable is global (only important if it is the first use
	 *            of given variable)
	 * @param varName name of the variable to store returned value into
	 */
	public InstructionAssignRetval(boolean global, String varName) {
		super(InstructionType.ASSIGN_RETVAL);
		this.lhsVarName = varName;
		this.global = global;
	}

	/**
	 * ASSIGN_RETVAL
	 */
	public InstructionAssignRetval() {
		super(InstructionType.ASSIGN_RETVAL);
	}

	private String lhsVarName;

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {

		Object result = engine.retval;

		if (result == null) {
			throw new WeaselRuntimeException("Cannot assign var, function "+instructionList.lastFuncCall+" returned nothing.");
		}

		if (lhsVarName != null && !lhsVarName.equals("")) {
			if (global) {
				engine.setVariableGlobal(lhsVarName, WeaselObject.getWrapperForValue(result));
			} else {
				engine.setVariable(lhsVarName, WeaselObject.getWrapperForValue(result));
			}
		}

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("LHS", lhsVarName);
		tag.setBoolean("Global", global);
		return tag;
	}

	@Override
	public InstructionAssignRetval readFromNBT(NBTTagCompound tag) {
		lhsVarName = tag.getString("LHS");
		global = tag.getBoolean("Global");
		return this;
	}

	/**
	 * Get LHS variable name.
	 * 
	 * @return the name of Left Hand Side variable (result is stored in it)
	 */
	public String getVarName() {
		return lhsVarName;
	}

	/**
	 * Set LHS variable name.
	 * 
	 * @param lhsVarName the name of Left Hand Side variable (result is stored
	 *            in it)
	 */
	public void setVarName(String lhsVarName) {
		this.lhsVarName = lhsVarName;
	}

	@Override
	public String toString() {
		return "ASSIGN_RETVAL -> '" + lhsVarName + "'";
	}

}
