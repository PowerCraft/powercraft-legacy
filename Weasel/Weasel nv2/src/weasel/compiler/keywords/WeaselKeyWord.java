package weasel.compiler.keywords;

public enum WeaselKeyWord {

	IF("if", null), 
	ELSE("else", null), 
	WHILE("while", null), 
	DO("do", null), 
	FOR("for", null), 
	BREAK("break", null), 
	CONTINUE("continue", null), 
	FUNCTION("function", null), 
	RETURN("return", null), 
	THIS("this", null), 
	GLOBAL("global", null), 
	CLASS("class", null), 
	INTERFACE("interface", null), 
	NEW("new", null),
	TRY("try", null),
	CATCH("catch", null),
	THROW("throw", null),
	ASM("asm", null),
	SYNCHRONIZED("synchronized", null), 
	ENUM("enum", null), 
	EXTENDS("extends", null), 
	IMPLEMENTS("implements", null);
	
	public final String name;
	public final WeaselKeyWordCompiler compiler;
	
	WeaselKeyWord(String name, WeaselKeyWordCompiler compiler){
		this.name = name;
		this.compiler = compiler;
	}

	public static WeaselKeyWord getWeaselKeyWord(String keyWord) {
		for(WeaselKeyWord wkw:values()){
			if(wkw.name.equals(keyWord)){
				return wkw;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
