package weasel.interpreter;

import weasel.interpreter.io.WeaselClassFile.WeaselAnnotation;

public class WeaselField {

	private WeaselClassBuildPlan parent;
	private String name;
	private int modifier;
	private WeaselAnnotation[] annotations;
	private WeaselClassGenericBuildPlan type;
	private int index;
	
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
		return parent.getName()+name;
	}
	
	public String getSimpleName(){
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

	public long get(WeaselObject object) {
		WeaselClass weaselClass = weaselPointer.getWeaselObject().getWeaselClass().getAllSuperClasses()[weaselPointer.getTable()];
		if(weaselClass.getClassBuildPlan()!=parent)
			throw new WeaselRuntimeException("Can't get field %s from %s", getName(), weaselClass);
		return weaselPointer.getWeaselObject().getDatas()[index+weaselPointer.getOffset()];
	}
	
}
