package net.minecraft.src.weasel;

import net.minecraft.src.weasel.obj.WeaselObject;

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
	boolean hasFunction(String functionName);

	/**
	 * Call a hardware function
	 * @param engine weasel engine calling the hardware
	 * @param functionName hardware function name
	 * @param args function argument list
	 * @return return value
	 */
	WeaselObject callFunction(WeaselEngine engine, String functionName, WeaselObject[] args);

}
