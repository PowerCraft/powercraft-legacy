package net.minecraft.src.weasel.lang;


import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.weasel.Calculator;
import net.minecraft.src.weasel.InstructionList;
import net.minecraft.src.weasel.WeaselEngine;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;


/**
 * Conditional jump instruction.
 * 
 * @author MightyPork
 */
public class InstructionIf extends Instruction {
	
	/**
	 * IF
	 * @param expression condition
	 * @param yesLabel jump target if condition == true
	 * @param noLabel jump target if condition == false
	 */
	public InstructionIf(String expression, String yesLabel, String noLabel) {
		this.expression = expression;
		this.yesLabel = yesLabel;
		this.noLabel = noLabel;
	}
	
	/**
	 * IF
	 */
	public InstructionIf() {}

	private String yesLabel;
	private String noLabel;
	private String expression;

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {
		
		if(Calculator.toBoolean(Calculator.evaluate(expression, engine))) {		
			instructionList.gotoLabel(yesLabel);
		}else {
			instructionList.gotoLabel(noLabel);
		}
		
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("Yes", yesLabel);
		tag.setString("No", noLabel);
		return tag;
	}

	@Override
	public InstructionIf readFromNBT(NBTTagCompound tag) {
		yesLabel = tag.getString("Yes");
		noLabel = tag.getString("No");
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
		return "IF '"+expression+"' THEN '"+yesLabel+"' ELSE '"+noLabel+"'";
	}

}
