package powercraft.weasel.engine;


import java.util.List;

import powercraft.weasel.obj.WeaselObject;




/**
 * Weasel-controlled hardware
 * 
 * @author MightyPork
 */
public interface IWeaselHardware extends IVariableProvider, IFunctionProvider {

	@Override
	public boolean doesProvideFunction(String functionName);

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args);

	@Override
	public WeaselObject getVariable(String name);

	@Override
	public void setVariable(String name, WeaselObject object);

	@Override
	public List<String> getProvidedFunctionNames();

	@Override
	public List<String> getProvidedVariableNames();

}
