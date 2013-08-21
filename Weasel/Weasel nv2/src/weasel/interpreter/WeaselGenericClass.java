package weasel.interpreter;

public class WeaselGenericClass extends WeaselGeneric<WeaselClass> {

	public WeaselGenericClass(WeaselClass t, WeaselClass[] generics) {
		super(t, generics);
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
		return new WeaselGenericClass(method.t.genericReturn.genericClass, getGenericClassesForMethod(method.t.genericReturn, method.generics));
	}
	
	public WeaselGenericClass[] getGenericMethodParam(WeaselGenericMethod method){
		WeaselGenericClass[] genericParams = new WeaselGenericClass[method.t.genericParams.length];
		for(int i=0; i<genericParams.length; i++){
			genericParams[i] = new WeaselGenericClass(method.t.genericParams[i].genericClass, getGenericClassesForMethod(method.t.genericParams[i], method.generics));
		}
		return genericParams;
	}
	
	protected WeaselClass[] getGenericClassesForMethod(WeaselGenericClassInfo gci, WeaselClass[] generic){
		if(gci==null)
			return new WeaselClass[0];
		WeaselClass[] classes = new WeaselClass[gci.generics.length];
		for(int i=0; i<classes.length; i++){
			if(gci.generics[i] instanceof WeaselClass){
				classes[i] = (WeaselClass)gci.generics[i];
			}else{
				int index = (Integer)gci.generics[i];
				if(index>=generics.length){
					classes[i] = generic[index-generics.length];
				}else{
					classes[i] = generics[index];
				}
			}
		}
		return classes;
	}
	
	public WeaselGenericClass getGenericFieldType(WeaselField field){
		return getGenericClassesFor(field.genericType);
	}
	
}
