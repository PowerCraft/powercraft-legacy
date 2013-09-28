package weasel.interpreter;

import weasel.interpreter.io.WeaselClassFile.WeaselAnnotation;

public class WeaselMethod {

	private WeaselClassBuildPlan parent;
	private String name;
	private int modifier;
	private WeaselAnnotation[] annotations;
	private WeaselClassGenericBuildPlan returnType;
	private WeaselClassGenericBuildPlan[] paramTypes;
	private WeaselAnnotation[][] paramAnnotations;
	private WeaselClassGenericBuildPlan[] cThrows;
	
	public WeaselMethod(WeaselClassBuildPlan parent, weasel.interpreter.io.WeaselClassFile.WeaselMethod weaselMethod) {
		this.parent = parent;
		name = weaselMethod.name;
		modifier = weaselMethod.returnType.modifier;
		annotations = weaselMethod.returnType.annotations;
		returnType = parent.getGenericBuildPlan(weaselMethod.returnType);
		paramTypes = new WeaselClassGenericBuildPlan[weaselMethod.params.length];
		paramAnnotations = new WeaselAnnotation[weaselMethod.params.length][];
		for(int i=0; i<paramTypes.length; i++){
			paramTypes[i] = parent.getInterpreter().getGenericBuildPlan(weaselMethod.params[i], parent);
			paramAnnotations[i] = new WeaselAnnotation[weaselMethod.params[i].annotations.length];
			for(int j=0; j<paramAnnotations[i].length; j++){
				paramAnnotations[i][j] = weaselMethod.params[i].annotations[j];
			}
		}
		cThrows = new WeaselClassGenericBuildPlan[weaselMethod.throwClasses.length];
		for(int i=0; i<cThrows.length; i++){
			cThrows[i] = parent.getInterpreter().getGenericBuildPlan(weaselMethod.params[i], parent);
		}
	}

	public WeaselClassBuildPlan getParent(){
		return parent;
	}
	
	public String getName(){
		return name;
	}
	
	public int getModifier(){
		return modifier;
	}
	
	public WeaselAnnotation[] getAnnotations(){
		return annotations;
	}
	
	public WeaselClass getReturnType(WeaselClass[] generics){
		return returnType.getWeaselClass(generics);
	}
	
	
	
}
