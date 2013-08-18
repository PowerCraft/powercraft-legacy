package weasel.compiler;

import java.util.Arrays;

import weasel.compiler.keywords.WeaselKeyWord;
import weasel.interpreter.WeaselClass;

public abstract class WeaselClassCompiler extends WeaselClass {

	protected final WeaselCompiler compiler;
	protected WeaselTokenParser tokenParser;
	
	protected WeaselClassCompiler(WeaselCompiler compiler, Object parent, String name, String fileName) {
		super(compiler, parent, name, fileName);
		this.compiler = compiler;
	}

	public abstract void compileEasy(String classSourceFor);

	public abstract void finishCompile();
	
	protected void onException(Throwable exception){
		compiler.addWeaselCompilerException(new WeaselClassException(this, exception));
	}
	
	protected WeaselToken getNextToken(){
		try {
			return tokenParser.getNextToken();
		} catch (WeaselSyntaxError e) {
			onException(e);
		}
		return new WeaselToken(WeaselTokenType.NONE, tokenParser.getLine());
	}
	
	protected void expect(WeaselToken token, WeaselTokenType...tokenTypes){
		for(int i=0; i<tokenTypes.length; i++){
			if(token.tokenType == tokenTypes[i]){
				return;
			}
		}
		onException(new WeaselSyntaxError(token.line, "Unexpected token %s expected %s", token, Arrays.toString(tokenTypes)));
	}
	
	protected void expectKeyWord(WeaselToken token, WeaselKeyWord...keyWords){
		expect(token, WeaselTokenType.KEYWORD);
		for(int i=0; i<keyWords.length; i++){
			if(token.param == keyWords[i]){
				return;
			}
		}
		onException(new WeaselSyntaxError(token.line, "Unexpected keyword %s expected %s", token, Arrays.toString(keyWords)));
	}
	
}
