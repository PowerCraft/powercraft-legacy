package weasel.compiler;

import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselModifier;

public class WeaselVariableInfo {

	public final int modifier;
	public final String name;
	public final WeaselClass type;
	public final int pos;
	
	public WeaselVariableInfo(int modifier, String name, WeaselClass type, int pos) {
		this.modifier = modifier;
		this.name = name;
		this.type = type;
		this.pos = pos;
	}

	@Override
	public String toString() {
		return WeaselModifier.toString2(modifier)+type.getRealName()+" "+name;
	}
	
}
