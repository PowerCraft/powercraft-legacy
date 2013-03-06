package powercraft.weasel.lang;


import powercraft.weasel.engine.Calc;
import powercraft.weasel.engine.InstructionList;
import powercraft.weasel.engine.WeaselEngine;
import powercraft.weasel.exception.WeaselRuntimeException;
import powercraft.weasel.obj.WeaselObject;
import net.minecraft.nbt.NBTTagCompound;


/**
 * Assignment instruction, with expression support.
 * 
 * @author MightyPork
 */
public class InstructionAssign extends Instruction {

	private boolean global;


	/**
	 * ASSIGN
	 * 
	 * @param global variable is global
	 * @param varName left variable name (assigned)
	 * @param expression expression to evaluate
	 */
	public InstructionAssign(boolean global, String varName, String expression) {
		super(InstructionType.ASSIGN);
		this.lhsVarName = varName;
		this.global = global;
		this.expression = expression;
	}

	/**
	 * ASSIGN
	 */
	public InstructionAssign() {
		super(InstructionType.ASSIGN);
	}

	private String lhsVarName;
	private String expression;

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {

		Object result = Calc.evaluate(expression, engine);

		if (lhsVarName != null && !lhsVarName.equals("")) {
			if (global) {
				engine.setVariableGlobal(lhsVarName, WeaselObject.getWrapperForValue(result));
			} else {
				engine.setVariable(lhsVarName, WeaselObject.getWrapperForValue(result));
			}
		}

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("LHS", lhsVarName);
		tag.setString("Expr", expression);
		tag.setBoolean("Global", global);
		return tag;
	}

	@Override
	public InstructionAssign readFromNBT(NBTTagCompound tag) {
		lhsVarName = tag.getString("LHS");
		expression = tag.getString("Expr");
		global = tag.getBoolean("Global");
		return this;
	}


	/**
	 * @return is variable global?
	 */
	public boolean isGlobal() {
		return global;
	}

	/**
	 * Set global flag
	 * 
	 * @param global is variable global
	 */
	public void setGlobal(boolean global) {
		this.global = global;
	}

	/**
	 * Get LHS variable name.
	 * 
	 * @return the name of Left Hand Side variable (result is stored in it)
	 */
	public String getVarName() {
		return lhsVarName;
	}

	/**
	 * Set LHS variable name.
	 * 
	 * @param lhsVarName the name of Left Hand Side variable (result is stored
	 *            in it)
	 */
	public void setVarName(String lhsVarName) {
		this.lhsVarName = lhsVarName;
	}

	/**
	 * Get the expression.
	 * 
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * Set the expression.
	 * 
	 * @param expression the expression to set
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	@Override
	public String toString() {
		return "ASSIGN " + (global ? "GLOBAL" : "") + " '" + lhsVarName + "' = '" + expression + "'";
	}

}
