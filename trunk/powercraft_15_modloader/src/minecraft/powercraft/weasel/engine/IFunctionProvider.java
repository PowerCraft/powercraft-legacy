package powercraft.weasel.engine;


import java.util.List;

import powercraft.weasel.obj.WeaselObject;




/**
 * Object providing functions to weasel's program
 * 
 * @author MightyPork
 */
public interface IFunctionProvider {

	/**
	 * Check if this provider supports given function.<br>
	 * Note that provider functions have higher priority than program functions,
	 * thus they can't be overridden within Weasel program.
	 * 
	 * @param functionName provider function name
	 * @return supports function
	 */
	public boolean doesProvideFunction(String functionName);

	/**
	 * Call a provider function. If you want, you can ask to pause the program:
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
	 * @param engine weasel engine calling the provider
	 * @param functionName provider function name
	 * @param args function argument list
	 * @return return value
	 */
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args);

	/**
	 * Get function names (keywords) for syntax highlighting text editor.
	 * 
	 * @return list of available function names
	 */
	public List<String> getProvidedFunctionNames();

}
