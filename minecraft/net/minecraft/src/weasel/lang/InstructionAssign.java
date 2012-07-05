package net.minecraft.src.weasel.lang;


import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.weasel.Calculator;
import net.minecraft.src.weasel.InstructionList;
import net.minecraft.src.weasel.WeaselEngine;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;
import net.minecraft.src.weasel.obj.WeaselObject;


/**
 * Assignment instruction, with expression support.
 * 
 * @author MightyPork
 */
public class InstructionAssign extends Instruction {
	
	/**
	 * ASSIGN
	 * @param varName left variable name (assigned)
	 * @param expression expression to evaluate
	 */
	public InstructionAssign(String varName, String expression) {
		lhsVarName = varName;
		this.expression = expression;
	}
	
	/**
	 * ASSIGN
	 */
	public InstructionAssign() {}

	private String lhsVarName;
	private String expression;

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {
		
		Object result = Calculator.evaluate(expression, engine);
		
		if(lhsVarName != null && !lhsVarName.equals("")) {
			engine.setVariable(lhsVarName, WeaselObject.getWrapperForValue(result));
		}
		
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("LHS", lhsVarName);
		tag.setString("Expr", expression);
		return tag;
	}

	@Override
	public InstructionAssign readFromNBT(NBTTagCompound tag) {
		lhsVarName = tag.getString("LHS");
		expression = tag.getString("Expr");
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

	/**
	 * Get the expression.
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * Set the expression.
	 * @param expression the expression to set
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

}
