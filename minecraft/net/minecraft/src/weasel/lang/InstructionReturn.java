package net.minecraft.src.weasel.lang;


import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.weasel.Calculator;
import net.minecraft.src.weasel.InstructionList;
import net.minecraft.src.weasel.WeaselEngine;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;
import net.minecraft.src.weasel.obj.WeaselObject;


/**
 * Return instruction, end of function block.
 * 
 * @author MightyPork
 */
public class InstructionReturn extends Instruction {
	
	/**
	 * RETURN
	 * @param expression returned expression.
	 */
	public InstructionReturn(String expression) {
		this.expression = expression;
	}
	
	/**
	 * RETURN
	 */
	public InstructionReturn() {}
	
	private String expression;

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {
		
		if(expression == null || expression.equals("")) {
			instructionList.returnFromCall(null);
		}
		
		instructionList.returnFromCall(WeaselObject.getWrapperForValue(Calculator.evaluate(expression, engine)));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("Expr", (expression==null?"":expression));
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
	 * @param expression the returned expression (can contain variables and math functions).
	 * @return this
	 */
	public InstructionReturn setReturnedVariableName(String expression) {
		this.expression = expression;
		return this;
	}

}
