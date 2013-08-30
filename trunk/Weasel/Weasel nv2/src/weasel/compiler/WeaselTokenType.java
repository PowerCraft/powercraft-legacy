package weasel.compiler;

public enum WeaselTokenType {

	NONE("EOF"), UNKNOWN("unknown"), IDENT("ident"), INTEGER("integer"), DOUBLE("double"), STRING("string"), BOOL("bool"), NULL("null"), 
	OPENBRACKET("("), CLOSEBRACKET(")"), OPENINDEX("["), CLOSEINDEX("]"), OPENBLOCK("{"), CLOSEBLOCK("}"), COMMA(","), 
	SEMICOLON(";"), COLON(":"), QUESTIONMARK("?"), OPERATOR("operator"), KEYWORD("keyword"), MODIFIER("modifier"), CHAR("char");
	
	public final String symbol;
	public final boolean afterOperator;
	
	WeaselTokenType(String symbol){
		this.symbol = symbol;
		afterOperator = false;
	}

	WeaselTokenType(String symbol, boolean afterOperator){
		this.symbol = symbol;
		this.afterOperator = afterOperator;
	}
	
	@Override
	public String toString() {
		return symbol;
	}

	public static WeaselTokenType getTokenTypeFor(String symbol, boolean afterOperator){
		for(WeaselTokenType wtt:values()){
			if(wtt.symbol.equals(symbol) && wtt.afterOperator==afterOperator){
				return wtt;
			}
		}
		return null;
	}
	
}
