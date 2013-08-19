package weasel.compiler;

import java.util.Arrays;

import weasel.compiler.WeaselCompilerMessage.MessageType;
import weasel.compiler.keywords.WeaselKeyWord;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselField;
import weasel.interpreter.WeaselMethod;

public abstract class WeaselClassCompiler extends WeaselClass {

	protected final WeaselCompiler compiler;
	protected WeaselTokenParser tokenParser;
	
	protected WeaselClassCompiler(WeaselCompiler compiler, Object parent, String name, String fileName) {
		super(compiler, parent, name, fileName);
		this.compiler = compiler;
	}

	public abstract void compileEasy(String classSourceFor);

	public abstract void finishCompile();
	
	protected void onException(int line, String message, Object...obj){
		compiler.addWeaselCompilerMessage(new WeaselCompilerMessage(MessageType.ERROR, line, getFileName(), String.format(message, obj)));
	}
	
	protected WeaselToken getNextToken(){
		try {
			return tokenParser.getNextToken();
		} catch (WeaselCompilerException e) {
			onException(tokenParser.getLine(), e.getMessage());
		}
		return new WeaselToken(WeaselTokenType.NONE, tokenParser.getLine());
	}
	
	protected void expect(WeaselToken token, WeaselTokenType...tokenTypes){
		for(int i=0; i<tokenTypes.length; i++){
			if(token.tokenType == tokenTypes[i]){
				return;
			}
		}
		onException(token.line, "Unexpected token %s expected %s", token, Arrays.toString(tokenTypes));
	}
	
	protected void expectKeyWord(WeaselToken token, WeaselKeyWord...keyWords){
		expect(token, WeaselTokenType.KEYWORD);
		for(int i=0; i<keyWords.length; i++){
			if(token.param == keyWords[i]){
				return;
			}
		}
		onException(token.line, "Unexpected keyword %s expected %s", token, Arrays.toString(keyWords));
	}
	
	protected WeaselMethod createMethod(String name, int modifier, WeaselClass parentClass, WeaselClass returnParam, WeaselClass[] params, int id){
		return compiler.createMethod(name, modifier, parentClass, returnParam, params, id);
	}

	protected WeaselField createField(String name, int modifier, WeaselClass weaselClass, WeaselClass type, int id) {
		return compiler.createField(name, modifier, weaselClass, type, id);
	}
	
}
