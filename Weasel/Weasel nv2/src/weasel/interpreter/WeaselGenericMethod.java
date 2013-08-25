package weasel.interpreter;

public class WeaselGenericMethod {

	private final WeaselGenericClass genericParentClass;
	
	private final WeaselMethod method;
	
	public WeaselGenericMethod(WeaselGenericClass genericParentClass, WeaselMethod method) {
		this.genericParentClass = genericParentClass;
		this.method = method;
	}

	public WeaselGenericClass getGenericParentClass(){
		return genericParentClass;
	}
	
	public WeaselMethod getMethod(){
		return method;
	}
	
	public WeaselGenericMethod2 getMethod(WeaselGenericClass[] generics){
		return new WeaselGenericMethod2(this, generics);
	}
	
}
