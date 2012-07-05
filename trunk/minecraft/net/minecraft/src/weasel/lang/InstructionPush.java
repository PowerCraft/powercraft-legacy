package net.minecraft.src.weasel.lang;


import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.weasel.Calculator;
import net.minecraft.src.weasel.InstructionList;
import net.minecraft.src.weasel.WeaselEngine;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;
import net.minecraft.src.weasel.obj.WeaselObject;


/**
 * Push instruction
 * 
 * @author MightyPork
 */
public class InstructionPush extends Instruction {
	
	/**
	 * PUSH
	 * @param expression variable to push on stack
	 */
	public InstructionPush(String expression) {
		this.pushedExpression = expression;
	}
	
	/**
	 * PUSH
	 */
	public InstructionPush() {}

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
		engine.dataStack.push(WeaselObject.getWrapperForValue(Calculator.evaluate(pushedExpression, engine)));
	}

}
