package powercraftCombi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Vector;

import powercraftCombi.WeaselClassMarker.WeaselClassList;

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
				WeaselNativeSourceManager.registerNewClass(c);
			}
		}
	}
	
	public static class WeaselNativeSourceManager{
		public static HashMap<String, WeaselNamespace> classes = new HashMap<String, WeaselNamespace>();
		public static HashMap<String, Object> objects = new HashMap<String, Object>();
		
		public static void init(){
			registerNewClass("byte", byte.class);
			registerNewClass("int", int.class);
			registerNewClass("float", float.class);
			registerNewClass("double", double.class);
			registerNewClass("String", String.class);
		}
		
		public static void registerNewClass(Class<?> c){
			WeaselClassMarker wc = (WeaselClassMarker) c.getAnnotation(WeaselClassMarker.class);
			if(wc!=null){
				registerNewClass(wc.weaselName(), wc.getClass());
			}
		}
		
		public static void registerNewClass(String name, Class<?> c){
			classes.put(name, c);
		}
		
		public static void registerNewNamespace(String name){
			
		}
		
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
