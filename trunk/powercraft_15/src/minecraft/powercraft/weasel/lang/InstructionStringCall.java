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
 * Function call with function name from 1st argument.
 * 
 * @author MightyPork
 */
public class InstructionStringCall extends Instruction {

	/**
	 * _CALL(fnname,arg,arg,arg)
	 * 
	 * @param parameterExpressions array of expressions to pass
	 */
	public InstructionStringCall(String... parameterExpressions) {
		super(InstructionType._CALL);
		this.parameterExpressions = parameterExpressions;
	}

	/**
	 * CALL
	 */
	public InstructionStringCall() {
		super(InstructionType._CALL);
	}

	/**
	 * Array of arguments passed to the functions. Can be math expressions,
	 * direct values or simple variable names. Each of these will be processed
	 * by the Calculator before passing to the function.
	 */
	private String[] parameterExpressions;

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {
		
		String funcname = (String) Calc.evaluate(parameterExpressions[0], engine);

		WeaselObject[] paramValues = new WeaselObject[parameterExpressions.length-1];

		for (int i = 1; i < parameterExpressions.length; i++) {
			try {
				paramValues[i-1] = WeaselObject.getWrapperForValue(Calc.evaluate(parameterExpressions[i], engine));
			} catch (NullPointerException npe) {
				throw new WeaselRuntimeException("Can not pass NULL to a function " + funcname);
			}
		}

		instructionList.callFunction(funcname, paramValues);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {

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
	public InstructionStringCall readFromNBT(NBTTagCompound tag) {

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
	 * Set expressions to pass to the function
	 * 
	 * @param argnames array of expressions to pass to the function
	 * @return this
	 */
	public InstructionStringCall setParameterExpressions(String[] argnames) {
		parameterExpressions = argnames;
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
		String a = "_CALL (";
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
