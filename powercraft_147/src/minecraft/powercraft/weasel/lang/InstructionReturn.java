package powercraft.weasel.lang;


import powercraft.weasel.engine.Calc;
import powercraft.weasel.engine.InstructionList;
import powercraft.weasel.engine.WeaselEngine;
import powercraft.weasel.exception.WeaselRuntimeException;
import powercraft.weasel.obj.WeaselObject;
import net.minecraft.nbt.NBTTagCompound;


/**
 * Return instruction, end of function block.
 * 
 * @author MightyPork
 */
public class InstructionReturn extends Instruction {

	/**
	 * RETURN
	 * 
	 * @param expression returned expression.
	 */
	public InstructionReturn(String expression) {
		super(InstructionType.RETURN);

		this.expression = expression;
	}

	/**
	 * RETURN
	 */
	public InstructionReturn() {
		super(InstructionType.RETURN);
	}

	private String expression;

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {
		if (expression == null || expression.equals("")) {
			instructionList.returnFromCall(null);
			return;
		}

		instructionList.returnFromCall(WeaselObject.getWrapperForValue(Calc.evaluate(expression, engine)));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("Expr", (expression == null ? "" : expression));
		return tag;
	}

	@Override
	public InstructionReturn readFromNBT(NBTTagCompound tag) {
		expression = tag.getString("Expr");
		return this;
	}

	/**
	 * @return expression to return
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * Set expression to return
	 * 
	 * @param expression the returned expression (can contain variables and math
	 *            functions).
	 * @return this
	 */
	public InstructionReturn setReturnedVariableName(String expression) {
		this.expression = expression;
		return this;
	}

	@Override
	public String toString() {
		return "RETURN '" + expression + "'";
	}

}
