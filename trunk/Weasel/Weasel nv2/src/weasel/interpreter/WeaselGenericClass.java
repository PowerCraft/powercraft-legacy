package weasel.interpreter;

public class WeaselGenericClass extends WeaselGeneric<WeaselClass> {
	
	public WeaselGenericClass(WeaselInterpreter interpreter, WeaselClass t, WeaselClass[] generics) {
		super(interpreter, t, generics);
	}

	public WeaselGenericClass getGenericSuperClass(){
		return getGenericClassesFor(t.genericSuperClass);
	}
	
	public WeaselGenericClass[] getGenericInterfaces(){
		WeaselGenericClass[] genericInterfaces = new WeaselGenericClass[t.genericInterfaces.length];
		for(int i=0; i<genericInterfaces.length; i++){
			genericInterfaces[i] = getGenericClassesFor(t.genericInterfaces[i]);
		}
		return genericInterfaces;
	}
	
	public WeaselGenericClass[] getGenericBases(){
		WeaselGenericClass[] genericBases = new WeaselGenericClass[t.genericInformation.length];
		for(int i=0; i<genericBases.length; i++){
			genericBases[i] = getGenericClassesFor(t.genericInformation[i].genericInfo);
		}
		return genericBases;
	}
	
	public WeaselGenericClass getGenericMethodReturn(WeaselGenericMethod method){
		return new WeaselGenericClass(interpreter, method.t.genericReturn.genericClass, getGenericClassesForMethod(method.t.genericReturn, method.generics));
	}
	
	public WeaselGenericClass[] getGenericMethodParam(WeaselGenericMethod method){
		WeaselGenericClass[] genericParams = new WeaselGenericClass[method.t.genericParams.length];
		for(int i=0; i<genericParams.length; i++){
			genericParams[i] = new WeaselGenericClass(interpreter, method.t.genericParams[i].genericClass, getGenericClassesForMethod(method.t.genericParams[i], method.generics));
		}
		return genericParams;
	}
	
	protected WeaselClass[] getGenericClassesForMethod(WeaselGenericClassInfo gci, WeaselClass[] generic){
		if(gci==null)
			return new WeaselClass[0];
		WeaselClass[] classes = new WeaselClass[gci.generics.length];
		for(int i=0; i<classes.length; i++){
			classes[i] = gci.generics[i].genericClass;
		}
		return classes;
	}
	
	public WeaselGenericClass getGenericFieldType(WeaselField field){
		return getGenericClassesFor(field.genericType);
	}
	
}
