package weasel.compiler;

import weasel.compiler.WeaselOperator.Properties;
import weasel.compiler.equationSolverNew.IWeaselTokenTreeElement;

public class WeaselToken implements IWeaselTokenTreeElement{

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
	
	@Override
	public void addSub(IWeaselTokenTreeElement te){
		subs.add(te);
	}
	
	@Override
	public String getName() {
		return ((Properties)param).operator;
	}

	// 12+5*3+5/3-2
	// -(+(12,*(5,3),/(5,3)),2)

	@Override
	public String toString() {
		return ((Properties)param).operator;
	}
	
	public String toEncryptedString() {
		return ((Properties)param).operator;
	}

	public String toClassView() {
		return this.getClass().getName();
	}
	
	public WeaselToken simplify() {
		return this;	
	}
}
