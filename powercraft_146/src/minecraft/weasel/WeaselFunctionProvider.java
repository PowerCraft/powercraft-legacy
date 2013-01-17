package weasel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import powercraft.management.PC_Struct2;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselObject;

public class WeaselFunctionProvider {

	private HashMap<String, PC_Struct2<Object, WeaselFunctionProvider>> functions = new HashMap<String, PC_Struct2<Object, WeaselFunctionProvider>>();
	
	public WeaselObject call(String name, WeaselObject...args) throws WeaselRuntimeException{
		String subNames[] = name.split("\\.");
		HashMap<String, PC_Struct2<Object, WeaselFunctionProvider>> hm = functions;
		for(int i=0; i<subNames.length-1; i++){
			if(hm==null)
				throw new WeaselRuntimeException("Function \""+name+"\" does not exist");
			hm = hm.get(subNames[i]).b.functions;
		}
		if(hm==null)
			throw new WeaselRuntimeException("Function \""+name+"\" does not exist");
		String lastName = subNames[subNames.length-1];
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
	
}
