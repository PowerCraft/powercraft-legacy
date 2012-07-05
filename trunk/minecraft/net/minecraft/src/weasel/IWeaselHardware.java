package net.minecraft.src.weasel;


import net.minecraft.src.weasel.obj.WeaselObject;


/**
 * Weasel-controlled hardware
 * 
 * @author MightyPork
 */
public interface IWeaselHardware extends IVariableContainer {

	/**
	 * Check if this hardware supports given function.<br>
	 * Note that hardware functions have higher priority than program functions,
	 * thus they can't be overridden within Weasel program.
	 * 
	 * @param functionName hardware function name
	 * @return supports function
	 */
	public boolean hasFunction(String functionName);
	
	/**
	 * Get number of parameters required by a given function.
	 * @param functionName name of the checked function
	 * @return number of parameters
	 */
	public int getFunctionArgumentCount(String functionName);

	/**
	 * Call a hardware function. If you want, you can ask to pause the program:
	 * 
	 * <pre>
	 * engine.requestPause();
	 * </pre>
	 * 
	 * This will interrupt program execution after this call is finished, and
	 * you can then perform the time consuming operations before the program
	 * starts running again. It is especially useful if you want to perform some
	 * movement, crafting or smelting, which takes multiple update ticks.
	 * 
	 * @param engine weasel engine calling the hardware
	 * @param functionName hardware function name
	 * @param args function argument list
	 * @return return value
	 */
	public WeaselObject callFunction(WeaselEngine engine, String functionName, WeaselObject[] args);

	/**
	 * Get a variable for name.<br>
	 * This is commonly used to retrieve hardware state, inputs etc.<br>
	 * Unlike in callFunction, you are not allowed to ask for pause within this
	 * call.
	 * 
	 * @param name variable name
	 * @return the variable, or null if not available.
	 */
	@Override
	public WeaselObject getVariable(String name);

	/**
	 * Set variable value.<br>
	 * It is up to the controlled machine whether this call will be accepted or
	 * not.<br>
	 * Unlike in callFunction, you are not allowed to ask for pause within this
	 * call.
	 * 
	 * @param name variable name
	 * @param object variable value. Can be either weasel object, or a primitive value.
	 */
	@Override
	public void setVariable(String name, Object object);

}
