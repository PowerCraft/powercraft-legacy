package powercraftCombi;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class WeaselNamespace {
	
	String name;
	Map<MethodDescriptor, Method> methods = new HashMap<MethodDescriptor, Method>();
	
	public WeaselNamespace(String name){
		this.name = name;
	}
	
	public void registerNewMethod(Method m){
		if(!methods.contains(m)){
			methods.add(m);
		}
	}
}
