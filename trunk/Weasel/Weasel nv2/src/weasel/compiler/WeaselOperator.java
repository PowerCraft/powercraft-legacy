package weasel.compiler;


public enum WeaselOperator {

	LET("=", false, 0, false, false), 
	BRACKETS("()", false, 14, false), 
	FIELD(".", false, 13, false), 
	ADD("+", true, 10, false),
	LET_ADD("+=", false, 0, false),
	SUB("-", true, 10, false),
	LET_SUB("-=", false, 0, false),
	MUL("*", false, 11, false),
	LET_MUL("*=", false, 0, false),
	DIV("/", false, 11, false),
	LET_DIV("/=", false, 0, false),
	MOD("%", false, 11, false),
	LET_MOD("%=", false, 0, false),
	AND("&&", false, 3, false),
	BIT_AND("&", false, 6, false),
	LET_BIT_AND("&=", false, 0, false),
	OR("||", false, 2, false),
	BIT_OR("|", false, 4, false),
	LET_BIT_OR("|=", false, 0, false),
	BIT_XOR("^", false, 5, false),
	LET_BIT_XOR("^=", false, 0, false),
	SHL("<<", false, 9, false),
	LET_SHL("<<=", false, 0, false),
	SHR(">>", false, 9, false),
	LET_SHR(">>=", false, 0, false),
	NOT("!", true, -1, false, false),
	BIT_NOT("~", true, -1, false, false),
	EQUAL("==", false, 7, false),
	NOT_EQUAL("!=", false, 7, false),
	GREATER(">", false, 8, false),
	LESS("<", false, 8, false),
	GREATER_EQUAL(">=", false, 8, false),
	LESS_EQUAL("<=", false, 8, false),
	INDEX("[]", true, -1, true),
	INC("++", true, -1, true),
	DEC("--", true, -1, true), 
	COMMA(",", false, -1, false), 
	IF("?:", false, 1, false),
	CLONE("<-", false, 0, false, false),
	EQUALS("===", false, 7, false),
	COMPARE("<=>", false, 7, false);
	
	public final String name;
	public final boolean prefix;
	public final int priority;
	public final boolean suffix;
	public final boolean l2r;
	
	WeaselOperator(String name, boolean prefix, int precedence, boolean suffix){
		this.name = name;
		this.prefix = prefix;
		this.priority = precedence;
		this.suffix = suffix;
		l2r = true;
	}
	
	WeaselOperator(String name, boolean prefix, int precedence, boolean suffix, boolean l2r){
		this.name = name;
		this.prefix = prefix;
		this.priority = precedence;
		this.suffix = suffix;
		this.l2r = l2r;
	}
	
	public static enum ParamType{
		NORMAL(1), PREFIX(0), SUFFIX(0);
		
		public final int param;
		
		ParamType(int param){
			this.param = param;
		}
		
	}
	
}
