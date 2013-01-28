package weasel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import powercraft.management.PC_Struct2;
import powercraft.management.PC_Struct3;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselDouble;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;

public class WeaselFunctionManager implements IWeaselHardware {

	private HashMap<String, PC_Struct3<PC_Struct2<String , Object>, PC_Struct2<String , Object>, WeaselFunctionManager>> functions = new HashMap<String, PC_Struct3<PC_Struct2<String , Object>, PC_Struct2<String , Object>, WeaselFunctionManager>>();
	
	public WeaselObject call(WeaselEngine engine, String name, boolean var, WeaselObject...args) throws WeaselRuntimeException{
		String subNames[] = name.split("\\.", 2);
		if(functions==null || !functions.containsKey(subNames[0])){
			throw new WeaselRuntimeException("Function \""+name+"\" does not exist");
		}
		PC_Struct3<PC_Struct2<String , Object>, PC_Struct2<String , Object>, WeaselFunctionManager> value = functions.get(subNames[0]);
		if(subNames.length==1){
			Object o;
			if(var){
				o = value.b;
			}else{
				o = value.a;
			}
			return call(engine, o, subNames[0], args);
		}
		WeaselFunctionManager fp = value.c;
		if(fp == null)
			throw new WeaselRuntimeException("Function \""+name+"\" does not exist");
		return fp.call(engine, subNames[1], var, args);
	}
	
	private static Class<?> toWrapper(Class<?>c){
		if(c.isPrimitive()){
			if(c == boolean.class){
				c = Boolean.class;
			}else if(c == byte.class){
				c = Byte.class;
			}else if(c == char.class){
				c = Character.class;
			}else if(c == short.class){
				c = Short.class;
			}else if(c == int.class){
				c = Integer.class;
			}else if(c == long.class){
				c = Long.class;
			}else if(c == float.class){
				c = Float.class;
			}else if(c == double.class){
				c = Double.class;
			}
		}
		return c;
	}
	
	private static boolean canCastTo(Class<?> c1, Class<?> c2){
		c1 = toWrapper(c1);
		c2 = toWrapper(c2);
		if(WeaselObject.class.isAssignableFrom(c1)){
			if(c2 == Boolean.class || c2 == Byte.class || c2 == Character.class || c2 == Short.class
					|| c2 == Integer.class || c2 == Long.class || c2 == Float.class || c2 == Double.class
					|| c2 == String.class){
				return true;
			}
		}
		return c2.isAssignableFrom(c1);
	}
	
	private static int rate(Method method, Class<?>[] expect){
		Class<?>[] param = method.getParameterTypes();
		Class<?>c = null;
		int rate=0;
		for(int i=0; i<expect.length; i++){
			Class<?> ce = expect[i];
			if(i<param.length)
				c = param[i];
			if(WeaselObject.class.isAssignableFrom(ce)){
				if(c == Boolean.class){
					if(WeaselBoolean.class.isAssignableFrom(ce))
						rate++;
				}else if(c == Byte.class || c == Character.class || c == Short.class || c == Integer.class || c == Long.class || c == Float.class || c == Double.class){
					if(WeaselDouble.class.isAssignableFrom(ce))
						rate++;
				}else if(c == String.class){
					if(WeaselString.class.isAssignableFrom(ce))
						rate++;
				}
			}else if(!canCastTo(ce, c))
				return -1;
		}
		return rate;
	}
	
	private static Method rate(Method newMethod, Method oldMethod, Class<?>[] expect){
		Class<?>[] param1 = newMethod.getParameterTypes();
		if(param1.length>expect.length)
			return oldMethod;
		if(expect.length==0){
			if(param1.length==0)
				return newMethod;
			return oldMethod;
		}
		if(param1.length==0)
			return oldMethod;
		Class<?>last1 = param1[param1.length-1];
		if(param1.length<expect.length && !last1.isArray()){
			return oldMethod;
		}
		if(oldMethod!=null){
			Class<?>[] param2 = oldMethod.getParameterTypes();
			if(param1.length<param2.length){
				return oldMethod;
			}
		}
		int r1 = -1;
		if(oldMethod!=null)
			r1 = rate(oldMethod, expect);
		int r2 = rate(newMethod, expect);
		if(r2==-1)
			return oldMethod;
		if(r2>r1)
			return newMethod;
		return oldMethod;
	}
	
	private static Object weaselObject2Class(Class<?> c, WeaselObject obj){
		c = toWrapper(c);
		if(c == Boolean.class){
			return Calc.toBoolean(obj);
		}else if(c == Byte.class){
			return (byte)Calc.toInteger(obj);
		}else if(c == Character.class){
			return (char)Calc.toInteger(obj);
		}else if(c == Short.class){
			return (short)Calc.toInteger(obj);
		}else if(c == Integer.class){
			return Calc.toInteger(obj);
		}else if(c == Long.class){
			return (long)Calc.toInteger(obj);
		}else if(c == Float.class){
			return (float)Calc.toDouble(obj);
		}else if(c == Double.class){
			return Calc.toDouble(obj);
		}else if(c == String.class){
			return Calc.toString(obj);
		}
		return null;
	}
	
	private static Object[] makeParam(Object[] args, Class<?>[] expect){
		Object[] obj = new Object[expect.length];
		int dif = args.length - expect.length;
		int max = expect.length-(dif<=0?0:1);
		for(int i=0; i<max; i++){
			obj[i] = args[i];
			if(obj[i] instanceof WeaselObject){
				obj[i] = weaselObject2Class(expect[i], (WeaselObject)obj[i]);
			}
		}
		if(dif>0){
			Object[] o = new Object[dif];
			obj[obj.length-1] = o;
			for(int i=0; i<dif; i++){
				o[i] = args[obj.length-1+i];
				if(o[i] instanceof WeaselObject){
					o[i] = weaselObject2Class(expect[i], (WeaselObject)o[i]);
				}
			}
		}
		return obj;
	}
	
	private static Method getBestMethod(Method[] methods, String name, Class<?>[] expect){
		Method method = null;
		for(Method m:methods){
			if(m.getName().equals(name)){
				method = rate(m, method, expect);
			}
		}
		return method;
	}
	
	private static WeaselObject call(Object obj, String name, Object[] args){
		Class<?> c = obj.getClass();
		Class<?>[] expect = new Class<?>[args.length];
		for(int i=0; i<args.length; i++){
			expect[i] = args[i].getClass();
		}
		Method[] methods = c.getDeclaredMethods();
		Method method = getBestMethod(methods, name, expect);
		while(method==null){
			c = c.getSuperclass();
			if(c==null)
				throw new WeaselRuntimeException("Function \""+name+"\" does not exist");
			methods = c.getDeclaredMethods();
			method = getBestMethod(methods, name, expect);
		}
		Object[] param = makeParam(args, method.getParameterTypes());
		try {
			return WeaselObject.getWrapperForValue(method.invoke(obj, param));
		} catch (WeaselRuntimeException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WeaselRuntimeException("Function \""+name+"\" does not exist");
		} 
	}
	
	private static WeaselObject call(WeaselEngine engine, Object obj, String name, WeaselObject[] args) throws WeaselRuntimeException{
		if(obj instanceof PC_Struct2){
			name = ((PC_Struct2<String, Object>)obj).a;
			obj = ((PC_Struct2<String, Object>)obj).b;
		}
		if(obj==null)
			throw new WeaselRuntimeException("Function \""+name+"\" does not exist");
		if(engine==null){
			return call(obj, name, (Object[])args);
		}
		Object[] param = new Object[args.length+1];
		param[0] = engine;
		for(int i=0; i<args.length; i++){
			param[i+1] = args[i];
		}
		try{
			return call(obj, name, param);
		}catch (Exception e) {
			return call(obj, name, (Object[])args);
		}
	}
	
	public boolean registerMethod(String name, Object obj){
		return registerMethod(name, name, obj);
	}
	
	public boolean registerMethod(String name, String realName, Object obj){
		String subNames[] = name.split("\\.", 2);
		PC_Struct3<PC_Struct2<String , Object>, PC_Struct2<String , Object>, WeaselFunctionManager> e;
		if(functions.containsKey(subNames[0])){
			e = functions.get(subNames[0]);
		}else{
			functions.put(subNames[0], e = new PC_Struct3<PC_Struct2<String , Object>, PC_Struct2<String , Object>, WeaselFunctionManager>(null, null, null));
		}
		if(subNames.length==1){
			if(e.a==null)
				e.a=new PC_Struct2<String , Object>(realName, obj);
			else
				return false;
			return true;
		}
		if(e.c==null)
			e.c = new WeaselFunctionManager();
		return e.c.registerMethod(subNames[1], realName, obj);
	}
	
	public boolean registerVariable(String name, Object obj) {
		return registerVariable(name, name, obj);
	}
	
	public boolean registerVariable(String name, String realName, Object obj) {
		String subNames[] = name.split("\\.", 2);
		PC_Struct3<PC_Struct2<String , Object>, PC_Struct2<String , Object>, WeaselFunctionManager> e;
		if(functions.containsKey(subNames[0])){
			e = functions.get(subNames[0]);
		}else{
			functions.put(subNames[0], e = new PC_Struct3<PC_Struct2<String , Object>, PC_Struct2<String , Object>, WeaselFunctionManager>(null, null, null));
		}
		if(subNames.length==1){
			if(e.b==null)
				e.b=new PC_Struct2<String , Object>(realName, obj);
			else
				return false;
			return true;
		}
		if(e.c==null)
			e.c = new WeaselFunctionManager();
		return e.c.registerVariable(subNames[1], realName, obj);
	}
	
	public boolean registerFunctionProvider(String name, WeaselFunctionManager functionProvider){
		String subNames[] = name.split("\\.", 2);
		PC_Struct3<PC_Struct2<String , Object>, PC_Struct2<String , Object>, WeaselFunctionManager> e;
		if(functions.containsKey(subNames[0])){
			e = functions.get(subNames[0]);
		}else{
			functions.put(subNames[0], e = new PC_Struct3<PC_Struct2<String , Object>, PC_Struct2<String , Object>, WeaselFunctionManager>(null, null, null));
		}
		if(subNames.length==1){
			if(e.c==null)
				e.c=functionProvider;
			else
				return false;
			return true;
		}
		if(e.c==null)
			e.c = new WeaselFunctionManager();
		return e.c.registerFunctionProvider(subNames[1], functionProvider);
	}
	
	public boolean removeFunctionProvider(String name){
		if(functions.containsKey(name)){
			functions.remove(name);
		}
		return true;
	}

	@Override
	public boolean doesProvideFunction(String name) {
		String subNames[] = name.split("\\.", 2);
		if(functions==null || !functions.containsKey(subNames[0])){
			return false;
		}
		PC_Struct3<PC_Struct2<String , Object>, PC_Struct2<String , Object>, WeaselFunctionManager> value = functions.get(subNames[0]);
		if(subNames.length==1){
			return value.a!=null;
		}
		WeaselFunctionManager fp = value.c;
		if(fp == null)
			return false;
		return fp.doesProvideFunction(subNames[1]);
	}

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		return call(engine, functionName, false, args);
	}
	
	@Override
	public WeaselObject getVariable(String name) {
		try{
			return call(null, name, true);
		}catch(WeaselRuntimeException e){
			return null;
		}
	}

	@Override
	public void setVariable(String name, WeaselObject object) {
		try{
			call(null, name, true, object);
		}catch(WeaselRuntimeException e){}
	}

	@Override
	public List<String> getProvidedFunctionNames() {
		List<String> list = new ArrayList<String>();
		for(Entry<String, PC_Struct3<PC_Struct2<String , Object>, PC_Struct2<String , Object>, WeaselFunctionManager>> e:functions.entrySet()){
			String key = e.getKey();
			if(e.getValue().a!=null)
				list.add(key);
			if(e.getValue().c!=null){
				List<String> list2 = e.getValue().c.getProvidedFunctionNames();
				for(String s:list2){
					list.add(key + "."+s);
				}
			}
		}
		return list;
	}

	@Override
	public List<String> getProvidedVariableNames() {
		List<String> list = new ArrayList<String>();
		for(Entry<String, PC_Struct3<PC_Struct2<String , Object>, PC_Struct2<String , Object>, WeaselFunctionManager>> e:functions.entrySet()){
			String key = e.getKey();
			if(e.getValue().b!=null)
				list.add(key);
			if(e.getValue().c!=null){
				List<String> list2 = e.getValue().c.getProvidedVariableNames();
				for(String s:list2){
					list.add(key + "."+s);
				}
			}
		}
		return list;
	}
	
}
