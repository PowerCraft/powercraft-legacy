package xscript.compiler;

import java.util.HashMap;

public enum XTokenKind {

	EOF, UNKNOWN, ERROR, 
	IDENT, 
	FLOATLITERAL, DOUBLELITERAL, LONGLITERAL, INTLITERAL, CHARLITERAL, STRINGLITERAL, 
	TRUE("true"), FALSE("false"), NULL("null"), 
	BOOL("bool"), BYTE("byte"), CHAR("char"), SHORT("short"), INT("int"), LONG("long"), FLOAT("float"), DOUBLE("double"), VOID("void"),
	CLASS("class"), ENUM("enum"), INTERFACE("interface"), ANNOTATION("@interface"),
	PACKAGE("package"), IMPORT("import"),
	PUBLIC("public"), PRIVATE("private"), PROTECTED("protected"), FINAL("final"), ABSTRACT("abstract"), NATIVE("native"), STATIC("static"),
	THIS("this"), SUPER("super"),
	FOR("for"), WHILE("while"), DO("do"), IF("if"), ELSE("else"), 
	SWITCH("switch"), CASE("case"), DEFAULT("default"), 
	RETURN("return"), BREAK("break"), CONTINUE("continue"), THROW("throw"), TRY("try"), CATCH("catch"), FINALLY("finally"),
	ADD('+'), SUB('-'), MUL('*'), DIV('/'), MOD('%'), 
	NOT('!'), BNOT('~'), 
	AND('&'), OR('|'), XOR('^'), 
	EQUAL('='), GREATER('>'), SMALLER('<'), INSTANCEOF("instanceof"),
	QUESTIONMARK('?'), COLON(':'),
	ELEMENT('.'), AT('@'),
	COMMA(','), LBRAKET('{'), RBRAKET('}'), LINDEX('['), RINDEX(']'), LGROUP('('), RGROUP(')'),
	SEMICOLON(';'), 
	EXTENDS("extends"), IMPLEMENTS("implements"), THROWS("throws"), NEW("new"), SYNCHRONIZED("synchronized");
	
	public final String name;
	
	XTokenKind(){
		name = null;
	}
	
	XTokenKind(String keyword){
		Statics.keywords.put(keyword, this);
		name = keyword;
	}
	
	XTokenKind(char charToken){
		Statics.charTokens.put(charToken, this);
		name = ""+charToken;
	}
	
	public String getName() {
		if(name==null){
			if(this==EOF){
				return "EOF";
			}else if(this==UNKNOWN){
				return "UNKNOWN";
			}else if(this==ERROR){
				return "ERROR";
			}
		}
		return name;
	}

	public static XTokenKind getKeyword(String name){
		return Statics.keywords.get(name);
	}
	
	public static XTokenKind getCharToken(char charToken) {
		return Statics.charTokens.get(charToken);
	}
	
	private static class Statics{
		private static HashMap<String, XTokenKind> keywords = new HashMap<String, XTokenKind>();
		private static HashMap<Character, XTokenKind> charTokens = new HashMap<Character, XTokenKind>();
	}

	
	
}
