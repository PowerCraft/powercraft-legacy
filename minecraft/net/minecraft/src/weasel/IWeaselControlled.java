package net.minecraft.src.weasel;

import net.minecraft.src.weasel.obj.WeaselObject;
import net.minecraft.src.weasel.obj.WeaselVariableMap;

/**
 * Weasel engine controlled hardware
 * 
 * @author MightyPork
 *
 */
public interface IWeaselControlled {

	/**
	 * Check if this hardware supports given function
	 * @param functionName hardware function name
	 * @return supports function
	 */
	public boolean hasFunction(String functionName);

	/**
	 * Call a hardware function
	 * @param engine weasel engine calling the hardware
	 * @param functionName hardware function name
	 * @param args function argument list
	 * @return return value
	 */
	public WeaselObject callFunction(WeaselEngine engine, String functionName, WeaselObject[] args);

	/**
	 * Get a variable for name.<br>
	 * This is commonly used to retrieve hardware state, inputs etc.
	 * 
	 * @param name variable name
	 * @return the variable, or null if not available.
	 */
	public WeaselObject getVariable(String name);

}
