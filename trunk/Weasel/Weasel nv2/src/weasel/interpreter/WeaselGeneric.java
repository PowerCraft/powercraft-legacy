package weasel.interpreter;

public class WeaselGeneric<T> {

	public T t;
	
	public WeaselClass[] generics;
	
	public WeaselGeneric(T t, WeaselClass[] generics){
		this.t = t;
		this.generics = generics;
	}
	
	protected WeaselClass[] getGenericClassesFor(WeaselGenericClassInfo gci){
		if(gci==null)
			return new WeaselClass[0];
		WeaselClass[] classes = new WeaselClass[gci.generics.length];
		for(int i=0; i<classes.length; i++){
			if(gci.generics[i] instanceof WeaselClass){
				classes[i] = (WeaselClass)gci.generics[i];
			}else{
				classes[i] = generics[(Integer)gci.generics[i]];
			}
		}
		return classes;
	}
	
}
