package weasel.interpreter;

import weasel.interpreter.io.WeaselClassFile.WeaselAnnotation;

public class WeaselField {

	private WeaselClassBuildPlan parent;
	private String name;
	private int modifier;
	private WeaselAnnotation[] annotations;
	private WeaselClassGenericBuildPlan type;

	public WeaselField(WeaselClassBuildPlan parent, weasel.interpreter.io.WeaselClassFile.WeaselField weaselField) {
		this.parent = parent;
		name = weaselField.name;
		modifier = weaselField.type.modifier;
		annotations = weaselField.type.annotations;
		type = parent.getInterpreter().getGenericBuildPlan(weaselField.type, parent);
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
	
	public WeaselClass getType(WeaselClass[] generics){
		return type.getWeaselClass(generics);
	}
	
}
