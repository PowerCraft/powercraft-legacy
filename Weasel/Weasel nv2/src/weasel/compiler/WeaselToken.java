package weasel.compiler;


public class WeaselToken{

	public final WeaselTokenType tokenType;
	public final int line;
	public Object param;
	
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
	public String toString(){
		if(tokenType == WeaselTokenType.STRING){
			return "\""+param+"\"";
		}
		return ""+param;
	}

}
