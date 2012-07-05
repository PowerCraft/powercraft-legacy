package net.minecraft.src.weasel;


import net.minecraft.src.weasel.exception.WeaselRuntimeException;
import net.minecraft.src.weasel.obj.WeaselObject;


/**
 * Object which provides access to Weasel variables.
 * 
 * @author MightyPork
 */
public interface IVariableContainer {

	/**
	 * Set variable value. Accepts both java types and WeaselObjects, but if the
	 * types don't match, throws an exception.
	 * 
	 * @param name variable name
	 * @param value value. Can be either weasel object, or a primitive object, which should be converted to weasel object.
	 * @throws WeaselRuntimeException when trying to store unsupported object
	 *             into a variable.
	 */
	public void setVariable(String name, Object value);


	/**
	 * Get variable object for name; call getVariable(name).get() top get
	 * String, Integer, Boolean etc, which is wrapped in the WeaselObject.
	 * 
	 * @param name variable name
	 * @return variable value
	 */
	public WeaselObject getVariable(String name);

}
