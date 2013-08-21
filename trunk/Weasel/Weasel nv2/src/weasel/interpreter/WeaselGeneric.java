package weasel.interpreter;

import java.util.Arrays;

public class WeaselGeneric<T> {

	public T t;
	
	public WeaselClass[] generics;

	protected final WeaselInterpreter interpreter;
	
	public WeaselGeneric(WeaselInterpreter interpreter, T t, WeaselClass[] generics){
		this.t = t;
		this.generics = generics;
		this.interpreter = interpreter;
	}
	
	protected WeaselGenericClass getGenericClassesFor(WeaselGenericClassInfo gci){
		WeaselClass[] classes = new WeaselClass[gci.generics.length];
		for(int i=0; i<classes.length; i++){
			classes[i] = gci.generics[i].getWeaselClass(interpreter, generics);
		}
		return new WeaselGenericClass(interpreter, gci.getWeaselClass(interpreter, generics), classes);
	}

	@Override
	public String toString() {
		return "WeaselGeneric [t=" + t + ", generics="
				+ Arrays.toString(generics) + "]";
	}
	
	
	
}
