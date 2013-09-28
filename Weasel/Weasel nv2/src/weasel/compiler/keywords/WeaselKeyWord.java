package weasel.compiler.keywords;

public enum WeaselKeyWord {

	IF("if", new WeaselKeyWordCompilerIf()), 
	ELSE("else", new WeaselKeyWordCompilerError()), 
	WHILE("while", new WeaselKeyWordCompilerWhile()), 
	DO("do", new WeaselKeyWordCompilerDo()), 
	FOR("for", new WeaselKeyWordCompilerFor()), 
	BREAK("break", new WeaselKeyWordCompilerBreak()), 
	CONTINUE("continue", new WeaselKeyWordCompilerContinue()), 
	RETURN("return", new WeaselKeyWordCompilerReturn()), 
	THIS("this", null), 
	SUPER("super", null), 
	CLASS("class", null), 
	INTERFACE("interface", null), 
	NEW("new", null),
	TRY("try", null),
	CATCH("catch", new WeaselKeyWordCompilerError()),
	THROW("throw", null),
	ASM("asm", null),
	SYNCHRONIZED("synchronized", null), 
	ENUM("enum", null), 
	EXTENDS("extends", null), 
	IMPLEMENTS("implements", null),
	INSTANCEOF("instanceof", null);
	
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
