package powercraftCombi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselNativeException;

public class Test {
	boolean isMinecraft=false;
	public void loader() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		//TODO nur Mod-Files 
		s;
		ClassLoader cl = Test.class.getClassLoader();
		Field f = ClassLoader.class.getDeclaredField("classes");
		f.setAccessible(true);
		@SuppressWarnings("unchecked")
		Vector<Class<?>> classes = (Vector<Class<?>>)f.get(cl);
		for(Class<?> c:classes){
			WeaselClassList wc = c.getAnnotation(WeaselClassList.class);
			if(wc!=null){
				for(Class<?> clazz:wc.classes()){
					WeaselNativeSourceManager.registerMethodsInClass(clazz);
				}
			}
		}
		WeaselNativeSourceManager.finished=true;
	}
	
	public static class WeaselNativeSourceManager{
		private static List<WeaselNativeMethodAccessor> methods = new ArrayList<WeaselNativeMethodAccessor>();
		public static boolean finished=false;
		
		public static void registerMethodsInClass(Class<?> c){
			WeaselNamedMethod named;
			for(Method m:c.getMethods()){
				if((named=m.getAnnotation(WeaselNamedMethod.class))!=null){
					if(((m.getModifiers()&Modifier.STATIC)!=Modifier.STATIC))
						throw new WeaselNativeException("Only static Methods can be loaded");
					for(String namespace:named.nameSpaces()){
						for(String weaselName:named.weaselNames()){
							methods.add(new WeaselNativeMethodAccessor(namespace, weaselName, m));
						}
					}
				}
			}
		}
		
		public static boolean registerNativeMethodsInWeasel(WeaselInterpreter wi){
			WeaselNativeMethodAccessor tmp;
			while(methods.size()>0){
				tmp = methods.remove(0);
				wi.registerNativeMethod(tmp.getName(), tmp);
			}
			return finished;
		}
	}
}
