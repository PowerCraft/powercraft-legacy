package weasel.compiler;

import weasel.interpreter.WeaselClass;

public class WeaselVariableInfo {

	public final int modifier;
	public final String name;
	public final WeaselClass type;
	
	public WeaselVariableInfo(int modifier, String name, WeaselClass type) {
		this.modifier = modifier;
		this.name = name;
		this.type = type;
	}
	
}
