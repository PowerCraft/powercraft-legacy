package powercraft.weasel.lang;


import powercraft.weasel.engine.Calc;
import powercraft.weasel.engine.InstructionList;
import powercraft.weasel.engine.WeaselEngine;
import powercraft.weasel.exception.WeaselRuntimeException;
import powercraft.weasel.obj.WeaselDouble;
import powercraft.weasel.obj.WeaselObject;
import net.minecraft.nbt.NBTTagCompound;


/**
 * Push instruction
 * 
 * @author MightyPork
 */
public class InstructionPush extends Instruction {

	/**
	 * PUSH
	 * 
	 * @param expression variable to push on stack
	 */
	public InstructionPush(String expression) {
		super(InstructionType.PUSH);

		this.pushedExpression = expression;
	}

	/**
	 * PUSH
	 */
	public InstructionPush() {
		super(InstructionType.PUSH);
	}

	private String pushedExpression;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("Name", pushedExpression);
		return tag;
	}

	@Override
	public InstructionPush readFromNBT(NBTTagCompound tag) {
		pushedExpression = tag.getString("Name");
		return this;
	}

	/**
	 * @return the pushed expression
	 */
	public String getPushedExpression() {
		return pushedExpression;
	}

	/**
	 * Set pushed expression
	 * 
	 * @param expression the pushed expression
	 * @return this
	 */
	public InstructionPush setPushedVariable(String expression) {
		this.pushedExpression = expression;
		return this;
	}

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {
		WeaselObject obj = WeaselObject.getWrapperForValue(Calc.evaluate(pushedExpression, engine));
		if (obj == null) {
			engine.dataStack.push(new WeaselDouble(0));
		} else {
			engine.dataStack.push(obj);
		}
	}

	@Override
	public String toString() {
		return "PUSH <- '" + pushedExpression + "'";
	}

}
