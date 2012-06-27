package net.minecraft.src.weasel.lang;


import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.weasel.InstructionList;
import net.minecraft.src.weasel.WeaselEngine;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;


/**
 * Label instruction, target for jumps.
 * 
 * @author MightyPork
 *
 */
public class InstructionFunction extends Instruction {

	private String functionName;
	private String[] args;

	@Override
	public void execute(WeaselEngine engine, InstructionList instructionList) throws WeaselRuntimeException {}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("Name", functionName);
		return tag;
	}

	@Override
	public InstructionFunction readFromNBT(NBTTagCompound tag) {
		functionName = tag.getString("Name");
		return this;
	}
	
	/**
	 * @return name of this label
	 */
	public String getFunctionName(){
		return functionName;
	}
	
	/**
	 * Set names of the arguments
	 * @param argnames argiment names array
	 * @return this
	 */
	public InstructionFunction setArgumentNames(String[] argnames) {
		args = argnames;
		return this;
	}
	
	/**
	 * Set label name
	 * @param functionName function name to set
	 * @return this
	 */
	public InstructionFunction setFunctionName(String functionName){
		this.functionName = functionName;
		return this;
	}

	/**
	 * Get name of an argiment variable with index i
	 * @param i arg index
	 * @return the name
	 */
	public String getArgumentName(int i) {
		if(i > 0 && i < args.length) return args[i];
		return null;
	}

}
