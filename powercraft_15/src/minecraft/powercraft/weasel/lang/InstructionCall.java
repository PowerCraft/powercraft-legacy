package powercraft.weasel.lang;


import java.util.ArrayList;

import powercraft.weasel.engine.Calc;
import powercraft.weasel.engine.InstructionList;
import powercraft.weasel.engine.WeaselEngine;
import powercraft.weasel.exception.WeaselRuntimeException;
import powercraft.weasel.obj.WeaselObject;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;


/**
 * Function call.
 * 
 * @author MightyPork
 */
public class InstructionCall extends Instruction {

	/**
	 * CALL
	 * 
	 * @param functionName function to call
	 * @param parameterExpressions array of expressions to pass
	 */
	public InstructionCall(String functionName, String... parameterExpressions) {
		super(InstructionType.CALL);
		this.targetFunctionName = functionName;
		this.parameterExpressions = parameterExpressions;
	}

	/**
	 * CALL
	 */
	public InstructionCall() {
		super(InstructionType.CALL);
	}

	private String targetFunctionName;

	/**
	 * Array of arguments passed to the functions. Can be math expressions,
	 * direct values or simple variable names. Each of these will be processed
	 * by the Calculator before passing to the function.
	 */
	private String[] parameterExpressions;

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {

		WeaselObject[] paramValues = new WeaselObject[parameterExpressions.length];

		for (int i = 0; i < parameterExpressions.length; i++) {
			try {
				paramValues[i] = WeaselObject.getWrapperForValue(Calc.evaluate(parameterExpressions[i], engine));
			} catch (NullPointerException npe) {
				throw new WeaselRuntimeException("Can not pass NULL to a function " + targetFunctionName);
			}

		}

		instructionList.callFunction(targetFunctionName, paramValues);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("TargetName", targetFunctionName);

		NBTTagList tags = new NBTTagList();
		for (String argExpr : parameterExpressions) {
			NBTTagCompound tag1 = new NBTTagCompound();
			tag1.setString("Expr", argExpr);
			tags.appendTag(tag1);
		}
		tag.setTag("Params", tags);

		return tag;
	}

	@Override
	public InstructionCall readFromNBT(NBTTagCompound tag) {
		targetFunctionName = tag.getString("TargetName");

		ArrayList<String> list = new ArrayList<String>();

		NBTTagList tags = tag.getTagList("Params");
		for (int i = 0; i < tags.tagCount(); i++) {
			NBTTagCompound tag1 = (NBTTagCompound) tags.tagAt(i);
			list.add(tag1.getString("Expr"));
		}

		parameterExpressions = list.toArray(new String[list.size()]);

		return this;
	}

	/**
	 * @return name of this label
	 */
	public String getTargetFunctionName() {
		return targetFunctionName;
	}

	/**
	 * Set expressions to pass to the function
	 * 
	 * @param argnames array of expressions to pass to the function
	 * @return this
	 */
	public InstructionCall setparameterExpressions(String[] argnames) {
		parameterExpressions = argnames;
		return this;
	}

	/**
	 * Set target function name
	 * 
	 * @param functionName target func
	 * @return this
	 */
	public InstructionCall setFunctionName(String functionName) {
		this.targetFunctionName = functionName;
		return this;
	}

	/**
	 * Get n-th expression passed to the func
	 * 
	 * @param i arg index
	 * @return the name
	 */
	public String getParameterExpression(int i) {
		if (i > 0 && i < parameterExpressions.length) return parameterExpressions[i];
		return null;
	}

	/**
	 * @return number of parameters passed to the function.
	 */
	public int getParameterCount() {
		return parameterExpressions.length;
	}

	@Override
	public String toString() {
		String a = "CALL " + targetFunctionName + " (";
		boolean first = true;
		for (String str : parameterExpressions) {
			if (!first) a += ", ";
			first = false;
			a += "'" + str + "'";
		}
		a += ")";
		return a;
	}

}
