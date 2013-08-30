package weasel.compiler.v2.tokentree;

import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;

public class WeaselImplementsToken extends WeaselToken {

	public final String className;
	public final WeaselTreeGeneric generic;
	
	public WeaselImplementsToken(int line, String className, WeaselTreeGeneric generic) {
		super(WeaselTokenType.OPERATOR, line, WeaselOperator.IMPLEMENTS);
		this.className = className;
		this.generic = generic;
	}

}
