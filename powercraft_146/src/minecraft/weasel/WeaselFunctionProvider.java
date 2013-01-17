package weasel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import powercraft.management.PC_Struct2;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselObject;

public class WeaselFunctionProvider implements IWeaselHardware {

	private HashMap<String, PC_Struct2<Object, WeaselFunctionProvider>> functions = new HashMap<String, PC_Struct2<Object, WeaselFunctionProvider>>();
	
	public WeaselObject call(String name, WeaselObject...args) throws WeaselRuntimeException{
		String subNames[] = name.split("\\.");
		HashMap<String, PC_Struct2<Object, WeaselFunctionProvider>> hm = functions;
		for(int i=0; i<subNames.length-1; i++){
			if(hm==null || !hm.containsKey(subNames[i]))
				throw new WeaselRuntimeException("Function \""+name+"\" does not exist");
			WeaselFunctionProvider fp = hm.get(subNames[i]).b;
			if(fp == null)
				throw new WeaselRuntimeException("Function \""+name+"\" does not exist");
			hm = fp.functions;
		}
		String lastName = subNames[subNames.length-1];
		if(hm==null || !hm.containsKey(lastName))
			throw new WeaselRuntimeException("Function \""+name+"\" does not exist");
		return call(hm.get(lastName).a, lastName, args);
	}
	
	private static WeaselObject call(Object obj, String name, WeaselObject[] args) throws WeaselRuntimeException{
		if(obj==null)
			throw new WeaselRuntimeException("Function \""+name+"\" does not exist");
		Class<?> c = obj.getClass();
		Object[] param = new Object[args.length];
		Class<?>[] paramClass = new Class<?>[args.length];
		for(int i=0; i<args.length; i++){
			param[i] = args[i].get();
			paramClass[i] = param[i].getClass();
		}
		Method m;
		try {
			m = c.getDeclaredMethod(name, paramClass);
		} catch (Exception e) {
			throw new WeaselRuntimeException("Function \""+name+"\" does not exist");
		} 
		Object ret;
		try {
			ret = m.invoke(obj, param);
		} catch (WeaselRuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new WeaselRuntimeException("Function \""+name+"\" does not exist");
		}
		return WeaselObject.getWrapperForValue(ret);
	}
	
	public boolean registerMethod(String name, Object obj){
		String subNames[] = name.split("\\.", 2);
		PC_Struct2<Object, WeaselFunctionProvider> e;
		if(functions.containsKey(subNames[0])){
			e = functions.get(subNames[0]);
		}else{
			functions.put(subNames[0], e = new PC_Struct2<Object, WeaselFunctionProvider>(null, null));
		}
		if(subNames.length==1){
			if(e.a==null)
				e.a=obj;
			else
				return false;
			return true;
		}
		if(e.b!=null)
			e.b = new WeaselFunctionProvider();
		return e.b.registerMethod(name, obj);
	}
	
	public boolean registerFunctionProvider(String name, WeaselFunctionProvider functionProvider){
		String subNames[] = name.split("\\.", 2);
		PC_Struct2<Object, WeaselFunctionProvider> e;
		if(functions.containsKey(subNames[0])){
			e = functions.get(subNames[0]);
		}else{
			functions.put(subNames[0], e = new PC_Struct2<Object, WeaselFunctionProvider>(null, null));
		}
		if(subNames.length==1){
			if(e.b==null)
				e.b=functionProvider;
			else
				return false;
			return true;
		}
		if(e.b!=null)
			e.b = new WeaselFunctionProvider();
		return e.b.registerFunctionProvider(name, functionProvider);
	}

	@Override
	public boolean doesProvideFunction(String name) {
		String subNames[] = name.split("\\.");
		HashMap<String, PC_Struct2<Object, WeaselFunctionProvider>> hm = functions;
		for(int i=0; i<subNames.length-1; i++){
			if(hm==null || !hm.containsKey(subNames[i]))
				return false;
			WeaselFunctionProvider fp = hm.get(subNames[i]).b;
			if(fp == null)
				return false;
			hm = fp.functions;
		}
		String lastName = subNames[subNames.length-1];
		if(hm==null || !hm.containsKey(lastName))
			return false;
		return hm.get(lastName).a!=null;
	}

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		return call(functionName, args);
	}

	@Override
	public WeaselObject getVariable(String name) {
		return null;
	}

	@Override
	public void setVariable(String name, Object object) {
	}

	@Override
	public List<String> getProvidedFunctionNames() {
		List<String> list = new ArrayList<String>();
		for(Entry<String, PC_Struct2<Object, WeaselFunctionProvider>> e:functions.entrySet()){
			String key = e.getKey();
			if(e.getValue().a!=null)
				list.add(key);
			if(e.getValue().b!=null){
				List<String> list2 = e.getValue().b.getProvidedFunctionNames();
				for(String s:list2){
					list.add(key + "."+s);
				}
			}
		}
		return list;
	}

	@Override
	public List<String> getProvidedVariableNames() {
		return new ArrayList<String>();
	}
	
}
