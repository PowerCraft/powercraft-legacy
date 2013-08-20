package powercraftCombi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class WeaselNamespace {
	
	String name;
	ArrayList<Field> fields = new ArrayList<Field>();
	ArrayList<Method> methods = new ArrayList<Method>();
	
	public WeaselNamespace(String name){
		this.name = name;
	}
	
	public void registerNewField(Field f){
		if(!fields.contains(f)){
			fields.add(f);
		}
	}
	
	public void registerNewMethod(Method m){
		if(!methods.contains(m)){
			methods.add(m);
		}
	}
}
