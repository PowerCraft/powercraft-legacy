package weasel.compiler.v2.tokentree;

import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;

public class WeaselCastToken extends WeaselToken {

	public final String className;
	public final WeaselTreeGeneric generic;
	
	public WeaselCastToken(int line, String className, WeaselTreeGeneric generic) {
		super(WeaselTokenType.OPERATOR, line, WeaselOperator.CAST);
		this.className = className;
		this.generic = generic;
	}

	@Override
	public String toString() {
		return "("+className+(generic==null?"":generic)+")";
	}
	
}
