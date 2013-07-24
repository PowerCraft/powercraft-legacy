package weasel.compiler;

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

	@Override
	public String toString() {
		return param.equals(tokenType.symbol)?tokenType.symbol:"\""+param.toString()+"\"";
	}	
	
}
