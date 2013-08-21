package weasel.compiler;

import weasel.compiler.equationSolverOld.Operand;

public class WeaselToken {

	public final WeaselTokenType tokenType;
	public final int line;
	public final Object param;
	
	public WeaselToken(WeaselTokenType tokenType, int line){
		this.tokenType = tokenType;
		this.line = line;
		param = tokenType.symbol;
	}

	public WeaselToken(WeaselTokenType tokenType, int line, Object param) {
		this.tokenType = tokenType;
		this.line = line;
		this.param = param;
	}	


	protected WeaselToken subs[] = new WeaselToken[10];
	
	public String getName() {
		return ((WeaselOperator)param).name;
	}

	// 12+5*3+5/3-2
	// -(+(12,*(5,3),/(5,3)),2)

	@Override
	public String toString() {
		return ((WeaselOperator)param).name;
	}
	
	public String toEncryptedString() {
		return ((WeaselOperator)param).name;
	}

	public String toClassView() {
		return this.getClass().getName();
	}
	
	public WeaselToken simplify() {
		return this;	
	}
}
	
}