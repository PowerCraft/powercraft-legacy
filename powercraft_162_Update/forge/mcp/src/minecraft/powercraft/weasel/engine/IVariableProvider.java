package powercraft.weasel.engine;


import java.util.List;

import powercraft.weasel.exception.WeaselRuntimeException;
import powercraft.weasel.obj.WeaselObject;



/**
 * Object which provides variables to weasel's program.
 * 
 * @author MightyPork
 */
public interface IVariableProvider {

	/**
	 * Set variable value. Accepts both java types and WeaselObjects, but if the
	 * types don't match, throws an exception. This does not have to be
	 * implemented if variables are read only.
	 * 
	 * @param name variable name
	 * @param value value. Can be either weasel object, or a primitive object,
	 *            which should be converted to weasel object.
	 * @throws WeaselRuntimeException when trying to store unsupported object
	 *             into a variable.
	 */
	public void setVariable(String name, WeaselObject value);


	/**
	 * Get variable object for name; call getVariable(name).get() top get
	 * String, Integer, Boolean etc, which is wrapped in the WeaselObject.
	 * 
	 * @param name variable name
	 * @return variable value
	 */
	public WeaselObject getVariable(String name);

	/**
	 * Get variable names (keywords) for syntax highlighting text editor.
	 * 
	 * @return list of available variables. In case of WeaselEngine, does not
	 *         have to be implemented, as it is useless.
	 */
	public List<String> getProvidedVariableNames();

}
