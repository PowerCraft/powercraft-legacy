package powercraft.weasel.lang;


import powercraft.weasel.engine.InstructionList;
import powercraft.weasel.engine.WeaselEngine;
import powercraft.weasel.exception.WeaselRuntimeException;
import net.minecraft.nbt.NBTTagCompound;


/**
 * Label instruction, target for jumps.
 * 
 * @author MightyPork
 */
public class InstructionLabel extends Instruction {


	/**
	 * LABEL
	 * 
	 * @param labelName label name
	 */
	public InstructionLabel(String labelName) {
		super(InstructionType.LABEL);

		this.labelName = labelName;
	}

	/**
	 * LABEL
	 */
	public InstructionLabel() {
		super(InstructionType.LABEL);
	}

	private String labelName = "";

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
	public String getLabelName() {
		return labelName;
	}

	/**
	 * Set label name
	 * 
	 * @param labelName label name to set
	 * @return this
	 */
	public InstructionLabel setLabelName(String labelName) {
		this.labelName = labelName;
		return this;
	}

	@Override
	public String toString() {
		return "LABEL '" + labelName + "'";
	}

}
