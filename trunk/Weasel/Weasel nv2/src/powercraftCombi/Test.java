package powercraftCombi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Vector;

import powercraftCombi.WeaselClassMarker.WeaselClassList;
import weasel.interpreter.WeaselNativeException;

public class Test {

	/*public void test(){
		
		WeaselClass.WeaselClassMarker wcm;																
		for(ModContainer mc:Loader.instance().getModList()){
			wcm = mc.getMod().getClass().getAnnotation(WeaselClass.WeaselClassMarker.class);
			if(wcm!=null){
				for(Class<?> c:wcm.classes()){
					WeaselNativeSourceManager.registerNewClass(c);
				}
			}
		}
	}*/
	
	public void loaderV2() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		ClassLoader cl = Test.class.getClassLoader();
		Field f = ClassLoader.class.getDeclaredField("classes");
		f.setAccessible(true);
		@SuppressWarnings("unchecked")
		Vector<Class<?>> classes = (Vector<Class<?>>)f.get(cl);
		for(Class<?> c:classes){
			WeaselClassList wc = c.getAnnotation(WeaselClassList.class);
			if(wc!=null){
				WeaselNativeSourceManager.registerMethodsInClass(c);
			}
		}
	}
	
	public static class WeaselNativeSourceManager{
		public static HashMap<String, WeaselNamespace> classes = new HashMap<String, WeaselNamespace>();
		
		public static void registerMethodsInClass(Class<?> c){
			Named named;
			WeaselNamespace wn;
			for(Method m:c.getMethods()){
				if((named=m.getAnnotation(Named.class))!=null){
					for(String namespace:named.nameSpaces()){
						if((wn=classes.get(namespace))==null){
							classes.put(namespace, wn=new WeaselNamespace(namespace));
						}
						wn.registerNewMethod(m);
					}
				}
			}
		}
		
		@SuppressWarnings("unchecked")
		public static <T> Object callFunc(String obj, String func, Object... params){
			try{
				T o;
				if((o=(T) objects.get(obj))!=null){
					Class<?> types[] = new Class[params.length];
					for(int i=0; i<params.length; i++){
						types[i] = params[i].getClass();
					}
					Method m = o.getClass().getDeclaredMethod(func, types);
					if(m!=null && !m.isAnnotationPresent(WeaselClassMarker.Invisible.class)){
						return m.invoke(o, params);
					}
				}
				
				return null;
			}catch(Exception e){
				
			}
			return null;
		}
	}
}
