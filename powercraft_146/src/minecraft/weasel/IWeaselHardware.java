package weasel;


import java.util.HashMap;
import java.util.List;

import powercraft.management.PC_Struct2;

import weasel.obj.WeaselObject;


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
	public void setVariable(String name, Object object);

	@Override
	public HashMap<String, PC_Struct2<Boolean, HashMap>> getProvidedFunctionNames();

	@Override
	public List<String> getProvidedVariableNames();

}
