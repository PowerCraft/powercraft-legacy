package powercraft.weasel.lang;


import powercraft.weasel.engine.InstructionList;
import powercraft.weasel.engine.WeaselEngine;
import powercraft.weasel.exception.WeaselRuntimeException;
import net.minecraft.nbt.NBTTagCompound;


/**
 * Pop instruction
 * 
 * @author MightyPork
 */
public class InstructionPop extends Instruction {

	/**
	 * POP
	 * 
	 * @param poppedVarName variable to store the result in
	 */
	public InstructionPop(String poppedVarName) {
		super(InstructionType.PAUSE);

		this.poppedVarName = poppedVarName;
	}

	/**
	 * POP
	 */
	public InstructionPop() {
		super(InstructionType.PAUSE);
	}

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
		if (poppedVarName == null || poppedVarName.equals("")) {
			engine.dataStack.pop();
		} else {
			engine.variables.setVariable(poppedVarName, engine.dataStack.pop());
		}
	}

	@Override
	public String toString() {
		return "POP -> '" + poppedVarName + "'";
	}

}
