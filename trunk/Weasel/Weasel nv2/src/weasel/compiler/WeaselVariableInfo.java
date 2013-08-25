package weasel.compiler;

import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselModifier;

public class WeaselVariableInfo {

	public final int modifier;
	public final String name;
	public final WeaselGenericClass type;
	public final int pos;
	
	public WeaselVariableInfo(int modifier, String name, WeaselGenericClass type, int pos) {
		this.modifier = modifier;
		this.name = name;
		this.type = type;
		this.pos = pos;
	}

	@Override
	public String toString() {
		return WeaselModifier.toString2(modifier)+type+" "+name;
	}
	
}
