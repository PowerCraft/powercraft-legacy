package powercraftCombi;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class WeaselNativeClass {
	
	String name;
	ArrayList<Method> methods = new ArrayList<Method>();
	
	public WeaselNativeClass(String name){
		this.name = name;
	}
	
	public void registerNewMethod(Method m){
		if(!methods.contains(m)){
			methods.add(m);
		}
	}
}
