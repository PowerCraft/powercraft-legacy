package xscript.compiler;

import static xscript.compiler.XOperator.Type.*;

public enum XOperator {

	NONE(null, null, -1),
	
	ADD("+", INFIX, 10),
	SUB("-", INFIX, 10),
	MUL("*", INFIX, 11),
	DIV("/", INFIX, 11),
	MOD("%", INFIX, 11),
	SHR("<<", INFIX, 9),
	SHL(">>", INFIX, 9),
	
	POS("+", PREFIX, -1),
	NEG("-", PREFIX, -1),
	
	BOR("|", INFIX, 4),
	BAND("&", INFIX, 6),
	XOR("^", INFIX, 5),
	OR("||", INFIX, 2),
	AND("&&", INFIX, 3),
	
	NOT("!", PREFIX, -1),
	BNOT("~", PREFIX, -1),
	
	EQ("==", INFIX, 7),
	REQ("===", INFIX, 7),
	NEQ("!=", INFIX, 7),
	RNEQ("!==", INFIX, 7),
	BIG(">", INFIX, 8),
	BEQ(">=", INFIX, 8),
	SMA("<", INFIX, 8),
	SEQ("<=", INFIX, 8),
	COMP("<=>", INFIX, 7),
	
	LET("=", INFIX, 0),
	LETADD("+=", INFIX, 0),
	LETSUB("-=", INFIX, 0),
	LETMUL("*=", INFIX, 0),
	LETDIV("/=", INFIX, 0),
	LETMOD("%=", INFIX, 0),
	LETOR("|=", INFIX, 0),
	LETAND("&=", INFIX, 0),
	LETXOR("^=", INFIX, 0),
	LETSHR("<<=", INFIX, 0),
	LETSHL(">>=", INFIX, 0),
	COPY("<:", INFIX, 0),
	
	INC("++", PREFIX, -1),
	DEC("--", PREFIX, -1),
	INCS("++", SUFFIX, -1),
	DECS("--", SUFFIX, -1),
	COPYS("<:", SUFFIX, -1),
	
	ELEMENT(".", INFIX, 12),
	
	IF("?", INFIX, 1),
	
	;
	
	public final static boolean[] L2R = {false, false, true, true, true, true, true, true, true, true, true, true, true};
	
	public final String op;
	public final Type type;
	public final int priority;
	
	XOperator(String op, Type type, int priority){
		this.op = op;
		this.type = type;
		this.priority = priority;
	}
	
	public static enum Type{
		PREFIX, INFIX, SUFFIX
	}
	
}
