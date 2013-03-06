package powercraft.weasel.lang;


import powercraft.weasel.engine.Calc;
import powercraft.weasel.engine.InstructionList;
import powercraft.weasel.engine.WeaselEngine;
import powercraft.weasel.exception.WeaselRuntimeException;
import net.minecraft.nbt.NBTTagCompound;


/**
 * Conditional jump instruction.
 * 
 * @author MightyPork
 */
public class InstructionIf extends Instruction {

	/**
	 * IF
	 * 
	 * @param expression condition
	 * @param yesLabel jump target if condition == true
	 * @param noLabel jump target if condition == false
	 */
	public InstructionIf(String expression, String yesLabel, String noLabel) {
		super(InstructionType.IF);

		this.expression = expression;
		this.yesLabel = yesLabel;
		this.noLabel = noLabel;
	}

	/**
	 * IF
	 */
	public InstructionIf() {
		super(InstructionType.IF);
	}

	private String yesLabel;
	private String noLabel;
	private String expression;

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {

		if (Calc.toBoolean(Calc.evaluate(expression, engine))) {

			if (yesLabel != null && !yesLabel.equals("")) instructionList.gotoLabel(yesLabel);

		} else {

			if (noLabel != null && !noLabel.equals("")) instructionList.gotoLabel(noLabel);

		}

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("Yes", yesLabel);
		tag.setString("No", noLabel);
		tag.setString("Expr", expression);
		return tag;
	}

	@Override
	public InstructionIf readFromNBT(NBTTagCompound tag) {
		yesLabel = tag.getString("Yes");
		noLabel = tag.getString("No");
		expression = tag.getString("Expr");
		return this;
	}

	/**
	 * @return the yesLabel
	 */
	public String getYesLabel() {
		return yesLabel;
	}

	/**
	 * @param yesLabel jump target if expression is true
	 */
	public void setYesLabel(String yesLabel) {
		this.yesLabel = yesLabel;
	}

	/**
	 * @return the noLabel
	 */
	public String getNoLabel() {
		return noLabel;
	}

	/**
	 * @param noLabel jump target if expression is false
	 */
	public void setNoLabel(String noLabel) {
		this.noLabel = noLabel;
	}

	/**
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * @param expression the condition
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	@Override
	public String toString() {
		return "IF '" + expression + "' THEN '" + yesLabel + "' ELSE '" + noLabel + "'";
	}

}
