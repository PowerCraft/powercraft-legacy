package weasel.compiler;

import java.util.HashMap;


public class WeaselOperator {

	public static final HashMap<String, Properties> operators = new HashMap<String, Properties>();
	public static final boolean unknown=false;
	public static class Properties{
		public int priority;
		public String operator, fullName;
		public boolean prefix, infix, suffix, l2r;
		public boolean isCommutative, isSimplifyPossible;
		
		public Properties(String operator, String fullName,
				int priority, boolean prefix, boolean infix, boolean suffix, boolean l2r,
				boolean isCommutative, boolean simplifyPossible) {
			this.operator = operator;
			this.fullName = fullName;
			this.priority = priority;
			this.prefix = prefix;
			this.infix = infix;
			this.suffix = suffix;
			this.l2r = l2r;
			this.isCommutative = isCommutative;
			this.isSimplifyPossible = simplifyPossible;
		}
	}
	public static final int prioRange;
	
																//		operator,	fullName	, prio  ,	prefix	,	infix	,	suffix	,	l2r		,commutative,	simplify
	public static final Properties COMMA =				new Properties(","		,"comma"		,	0	,	false	,	true	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties ASSIGN =				new Properties("="		,"assign"		,	2	,	false	,	true	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties PLUS_ASSIGN =		new Properties("+="		,"plus assign"	,	2	,	false	,	true	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties MINUS_ASSIGN =		new Properties("-="		,"minus assign"	,	2	,	false	,	true	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties TIMES_ASSIGN =		new Properties("*="		,"times assign"	,	2	,	false	,	true	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties DIVIDE_ASSIGN =		new Properties("/="		,"divide assign",	2	,	false	,	true	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties MOD_ASSIGN =			new Properties("%="		,"mod assign"	,	2	,	false	,	true	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties AND_ASSIGN =			new Properties("&="		,"and assign"	,	2	,	false	,	true	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties OR_ASSIGN =			new Properties("|="		,"or assign"	,	2	,	false	,	true	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties XOR_ASSIGN =			new Properties("^="		,"xor assign"	,	2	,	false	,	true	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties LSHIFT_ASSIGN =		new Properties("<<="	,"lshift assign",	2	,	false	,	true	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties RSHIFT_ASSIGN =		new Properties(">>="	,"rshift assign",	2	,	false	,	true	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties COPY =				new Properties("<:"		,"copy"			,	2	,	false	,	true	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties CONDITIONAL =		new Properties("?:"		,"conditional"	,	3	,	false	,	true	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties LOGICAL_OR =			new Properties("||"		,"logical or"	,	4	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties LOGICAL_AND =		new Properties("&&"		,"logical and"	,	5	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties BITWISE_OR =			new Properties("|"		,"bitwise or"	,	6	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties BITWISE_XOR =		new Properties("^"		,"bitwise xor"	,	7	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties BITWISE_AND =		new Properties("&"		,"bitwise and"	,	8	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties VERY_SAME =			new Properties("=="		,"very same"	,	9	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties NOT_VERY_SAME =		new Properties("!="		,"not very same",	9	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties EQUAL =				new Properties("==="	,"equal"		,	9	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties COMPARE =			new Properties("<=>"	,"compare"		,	9	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties GREATER =			new Properties(">"		,"greater"		,	10	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties LESS =				new Properties("<"		,"less"			,	10	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties GREATER_EQUAL =		new Properties(">="		,"greater equal",	10	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties LESS_EQUAL =			new Properties("<="		,"less equal"	,	10	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties SHIFT_LEFT =			new Properties("<<"		,"shift left"	,	11	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties SHIFT_RIGHT =		new Properties(">>"		,"shift right"	,	11	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties PLUS =				new Properties("+"		,"plus"			,	12	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties MINUS =				new Properties("-"		,"minus"		,	12	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties TIMES =				new Properties("*"		,"times"		,	13	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties DIVIDE =				new Properties("/"		,"divide"		,	13	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties REMAINDER =			new Properties("%"		,"remainder"	,	13	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties COPY_PREFIX =		new Properties("<:"		,"copy"			,	15	,	true	,	false	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties NOT =				new Properties("!"		,"not"			,	15	,	true	,	false	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties BITWISE_NOT =		new Properties("~"		,"bitwise not"	,	15	,	true	,	false	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties MINUS_PREFIX =		new Properties("-"		,"minus"		,	15	,	true	,	false	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties PLUS_PREFIX =		new Properties("+"		,"plus"			,	15	,	true	,	false	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties DECREASE_PREFIX =	new Properties("--"		,"decrease"		,	15	,	true	,	false	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties INCREASE_PREFIX =	new Properties("++"		,"increase"		,	15	,	true	,	false	,	false	,	false	,	unknown	,	unknown	);
	public static final Properties ELEMENT =			new Properties("."		,"element"		,	16	,	false	,	true	,	false	,	true	,	unknown	,	unknown	);
	public static final Properties ARRAY =				new Properties("[]"		,"array"		,	16	,	true	,	false	,	true	,	true	,	unknown	,	unknown	);
	public static final Properties CALL =				new Properties("%s"		,"call"			,	16	,	false	,	false	,	true	,	true	,	unknown	,	unknown	);
	public static final Properties CAST =				new Properties("()"		,"cast"			,	16	,	false	,	false	,	true	,	true	,	unknown	,	unknown	);
	public static final Properties DECREASE =			new Properties("--"		,"decrease"		,	16	,	false	,	false	,	true	,	true	,	unknown	,	unknown	);
	public static final Properties INCREASE =			new Properties("++"		,"increase"		,	16	,	false	,	false	,	true	,	true	,	unknown	,	unknown	);
	public static final Properties BRACKETS =			new Properties("()"		,"brackets"		,	-1	,	false	,	false	,	false	,	true	,	unknown	,	unknown	);
	
	static {
		operators.put(DIVIDE_ASSIGN.operator, DIVIDE_ASSIGN);
		operators.put(MOD_ASSIGN.operator, MOD_ASSIGN);
		operators.put(AND_ASSIGN.operator, AND_ASSIGN);
		operators.put(OR_ASSIGN.operator, OR_ASSIGN);
		operators.put(XOR_ASSIGN.operator, XOR_ASSIGN);
		operators.put(LSHIFT_ASSIGN.operator, LSHIFT_ASSIGN);
		operators.put(RSHIFT_ASSIGN.operator, RSHIFT_ASSIGN);
		operators.put(COPY.operator, COPY);
		//operators.put(CONDITIONAL.operator, CONDITIONAL);
		operators.put(LOGICAL_OR.operator, LOGICAL_OR);
		operators.put(LOGICAL_AND.operator, LOGICAL_AND);
		operators.put(BITWISE_OR.operator, BITWISE_OR);
		operators.put(BITWISE_XOR.operator, BITWISE_XOR);
		operators.put(BITWISE_AND.operator, BITWISE_AND);
		operators.put(VERY_SAME.operator, VERY_SAME);
		operators.put(NOT_VERY_SAME.operator, NOT_VERY_SAME);
		operators.put(EQUAL.operator, EQUAL);
		operators.put(COMPARE.operator, COMPARE);
		operators.put(GREATER.operator, GREATER);
		operators.put(LESS.operator, LESS);
		operators.put(GREATER_EQUAL.operator, GREATER_EQUAL);
		operators.put(LESS_EQUAL.operator, LESS_EQUAL);
		operators.put(SHIFT_LEFT.operator, SHIFT_LEFT);
		operators.put(SHIFT_RIGHT.operator, SHIFT_RIGHT);
		operators.put(PLUS_PREFIX.operator, PLUS_PREFIX);
		operators.put(MINUS.operator, MINUS);
		operators.put(TIMES.operator, TIMES);
		operators.put(DIVIDE.operator, DIVIDE);
		operators.put(REMAINDER.operator, REMAINDER);
		operators.put(COPY_PREFIX.operator, COPY_PREFIX);
		operators.put(NOT.operator, NOT);
		operators.put(BITWISE_NOT.operator, BITWISE_NOT);
		operators.put(MINUS_PREFIX.operator, MINUS_PREFIX);
		operators.put(PLUS.operator, PLUS);
		operators.put(DECREASE_PREFIX.operator, DECREASE_PREFIX);
		operators.put(INCREASE_PREFIX.operator, INCREASE_PREFIX);
		operators.put(ELEMENT.operator, ELEMENT);
		//operators.put(ARRAY.operator, ARRAY);
		//operators.put(CALL.operator, CALL);
		//operators.put(CAST.operator, CAST);
		operators.put(DECREASE.operator, DECREASE);
		operators.put(INCREASE.operator, INCREASE);

		int max = 0, tmpPrio;
		for (Properties prio : operators.values()) {
			tmpPrio = prio.priority;
			if (tmpPrio > max)
				max = tmpPrio;
		}
		prioRange = max;
	}
	
}
