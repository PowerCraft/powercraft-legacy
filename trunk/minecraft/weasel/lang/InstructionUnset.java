package weasel.lang;


import net.minecraft.src.NBTTagCompound;
import weasel.InstructionList;
import weasel.WeaselEngine;
import weasel.exception.WeaselRuntimeException;


/**
 * Unset a variable
 * 
 * @author MightyPork
 */
public class InstructionUnset extends Instruction {

	/**
	 * UNSET
	 * 
	 * @param unsetVarName variable name to unset
	 */
	public InstructionUnset(String unsetVarName) {
		super(InstructionType.UNSET);

		this.unsetVarName = unsetVarName;
	}

	/**
	 * POP
	 */
	public InstructionUnset() {
		super(InstructionType.UNSET);
	}

	private String unsetVarName;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("Name", unsetVarName);
		return tag;
	}

	@Override
	public InstructionUnset readFromNBT(NBTTagCompound tag) {
		unsetVarName = tag.getString("Name");
		return this;
	}

	/**
	 * @return name of variable to unset
	 */
	public String getVariableName() {
		return unsetVarName;
	}

	/**
	 * Set variable name to unset
	 * 
	 * @param variableName name of the unset variable
	 * @return this
	 */
	public InstructionUnset setVariableName(String variableName) {
		this.unsetVarName = variableName;
		return this;
	}

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {
		if (unsetVarName == null || unsetVarName.equals("")) {

		} else {
			engine.variables.unsetVariable(unsetVarName);
		}
	}

	@Override
	public String toString() {
		return "UNSET '" + unsetVarName + "'";
	}

}
