package weasel.interpreter;

import weasel.interpreter.io.WeaselClassFile.WeaselAnnotation;

public class WeaselMethod {

	private WeaselClassBuildPlan parent;
	private String name;
	private int modifier;
	private WeaselAnnotation[] annotations;
	private WeaselClassGenericBuildPlan returnType;
	
	public WeaselMethod(WeaselClassBuildPlan parent, weasel.interpreter.io.WeaselClassFile.WeaselMethod weaselMethod) {
		this.parent = parent;
		name = weaselMethod.name;
		modifier = weaselMethod.returnType.modifier;
		annotations = weaselMethod.returnType.annotations;
		returnType = parent.getGenericBuildPlan(weaselMethod.returnType);
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
