package weasel.compiler.v2.tokentree;

import java.util.ListIterator;

import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselGenericClassInfo;
import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselNativeException;

public class WeaselTreeGenericElement {

	private WeaselToken token;
	private String realClassName;
	private String className;
	private String classByteName;
	private WeaselTreeGeneric generic;
	public boolean close;
	
	public WeaselTreeGenericElement(ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		WeaselToken token = iterator.next();
		if(token.tokenType!=WeaselTokenType.IDENT){
			throw new WeaselCompilerException(token.line, "Expect Ident but got %s", token);
		}
		this.token = token;
		realClassName = (String) token.param;
		token = iterator.next();
		while(token.tokenType==WeaselTokenType.OPERATOR && token.param==WeaselOperator.ELEMENT){
			token = iterator.next();
			if(token.tokenType!=WeaselTokenType.IDENT){
				throw new WeaselCompilerException(token.line, "Expect Ident but got %s", token);
			}
			realClassName = "."+(String) token.param;
			token = iterator.next();
		}
		className = realClassName;
		classByteName = WeaselClass.mapClassNames(className);
		if(token.tokenType==WeaselTokenType.OPERATOR && token.param==WeaselOperator.LESS){
			generic = new WeaselTreeGeneric(iterator);
			close = generic.close;
		}else{
			iterator.previous();
		}
	}

	public WeaselGenericClass getGenericClass(WeaselGenericClass parentClass) throws WeaselCompilerException {
		try{
			return new WeaselGenericClass(parentClass.getBaseClass().getInterpreter().getWeaselClass(classByteName), generic.getGenericClasses(parentClass));
		}catch(WeaselNativeException e){}
		WeaselGenericClass wgc = parentClass.getGenericClass(realClassName);
		if(wgc==null){
			throw new WeaselCompilerException(token.line, "Class %s not found", className);
		}
		WeaselClass wc = wgc.getBaseClass();
		int i=0;
		while(classByteName.charAt(i++)=='['){
			wc = new WeaselClass(wc.getInterpreter(), wc, "["+wc.getByteName(), null);
		}
		return new WeaselGenericClass(wc, wgc.getGenerics());
	}

	public WeaselClass getWeaselClass(WeaselInterpreter interpreter) {
		return interpreter.getWeaselClass(classByteName);
	}
	
	@Override
	public String toString() {
		return className+(generic==null?"":generic.toString());
	}

	public WeaselGenericClassInfo getGenericClassInfo(WeaselClass parentClass) {
		WeaselClass wc = null;
		int genericID = -1;
		try{
			wc = parentClass.getInterpreter().getWeaselClass(classByteName);
		}catch(WeaselNativeException e){}
		if(wc==null){
			genericID = parentClass.getGenericID(realClassName);
			wc = parentClass.getGenericInformation(genericID).genericInfo.genericClass;
		}
		return new WeaselGenericClassInfo(wc, genericID, generic==null?new WeaselGenericClassInfo[0]:generic.getGenericClassInfo(parentClass));
	}
	
}
