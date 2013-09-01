package weasel.compiler;

import java.util.ListIterator;

import weasel.compiler.WeaselCompilerMessage.MessageType;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselField;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselGenericClassInfo;
import weasel.interpreter.WeaselGenericInformation;
import weasel.interpreter.WeaselMethod;

public abstract class WeaselClassCompiler extends WeaselClass {

	protected final WeaselCompiler compiler;
	protected WeaselTokenParser tokenParser;
	
	protected WeaselClassCompiler(WeaselCompiler compiler, Object parent, String name, String fileName) {
		super(compiler, parent, name, fileName);
		this.compiler = compiler;
	}

	public abstract void compileEasy();

	public abstract void finishCompile();
	
	public abstract WeaselGenericClassInfo readGenericClass(WeaselToken token, ListIterator<WeaselToken> iterator) throws WeaselCompilerException;
	
	protected void onException(int line, String message, Object...obj){
		compiler.addWeaselCompilerMessage(new WeaselCompilerMessage(MessageType.ERROR, line, getFileName(), String.format(message, obj)));
	}
	
	protected void onException(WeaselCompilerException e) {
		onException(e.getLine(), e.getMessage());
	}
	
	protected WeaselToken getNextToken(){
		try {
			return tokenParser.getNextToken();
		} catch (WeaselCompilerException e) {
			onException(tokenParser.getLine(), e.getMessage());
		}
		return new WeaselToken(WeaselTokenType.NONE, tokenParser.getLine());
	}
	
	protected void setNextToken(WeaselToken token){
		tokenParser.setNextToken(token);
	}
	
	protected WeaselMethod createMethod(String name, int modifier, WeaselClass parentClass, WeaselGenericClassInfo genericReturn, WeaselGenericClassInfo[] genericParams, WeaselGenericInformation[] genericInformations, int id){
		return compiler.createMethod(name, modifier, parentClass, genericReturn, genericParams, genericInformations, id);
	}

	protected WeaselField createField(String name, int modifier, WeaselClass weaselClass, WeaselGenericClassInfo typeInfo, int id) {
		return compiler.createField(name, modifier, weaselClass, typeInfo, id);
	}

	public abstract WeaselGenericClass getGenericClass();

	public void setSource(String source) {
		tokenParser = new WeaselTokenParser(source);
	}
	
}
