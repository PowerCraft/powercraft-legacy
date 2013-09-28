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
		type = parent.getGenericBuildPlan(weaselField.type);
	}
	
	
	
}
