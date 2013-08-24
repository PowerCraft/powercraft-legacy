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
		return param.toString();
	}
	
	/*@Override
	public String getName() {
		return ((Properties)param).operator;
	}

	// 12+5*3+5/3-2
	// -(+(12,*(5,3),/(5,3)),2)

	@Override
	public String toString(){
		return toReadableString();
	}
	
	@Override
	public String toReadableString() {
		return param.toString();
	}
	
	public String toEncryptedString() {
		return ((Properties)param).operator;
	}

	@Override
	public void toAdvancedEncryptedString(String2D str) {
		str.add(toString());
	}

	public String toClassView() {
		return this.getClass().getName();
	}
	
	public WeaselToken simplify() {
		return this;	
	}

	*/
}
