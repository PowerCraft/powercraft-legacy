package weasel.compiler.v2.tokentree;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselGenericClassInfo;

public class WeaselTreeGeneric {

	private List<WeaselTreeGenericElement> list = new ArrayList<WeaselTreeGenericElement>();
	public boolean close;
	
	public WeaselTreeGeneric(ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		WeaselToken token = iterator.next();
		if(!(token.tokenType==WeaselTokenType.OPERATOR && (token.param==WeaselOperator.GREATER || token.param==WeaselOperator.RSHIFT))){
			iterator.previous();
			do{
				WeaselTreeGenericElement tge = new WeaselTreeGenericElement(iterator);
				list.add(tge);
				if(tge.close)
					return;
				token = iterator.next();
			}while(token.tokenType==WeaselTokenType.COMMA);
			if(!(token.tokenType==WeaselTokenType.OPERATOR && (token.param==WeaselOperator.GREATER || token.param==WeaselOperator.RSHIFT))){
				throw new WeaselCompilerException(token.line, "Expect > but got %s", token);
			}
		}
		close = token.tokenType==WeaselTokenType.OPERATOR && token.param==WeaselOperator.RSHIFT;
	}
	
	@Override
	public String toString() {
		String s = "<";
		if(!list.isEmpty()){
			s += list.get(0).toString();
			for(int i=1; i<list.size(); i++){
				s += ", "+list.get(i).toString();
			}
		}
		return s+">";
	}

	public WeaselGenericClass[] getGenericClasses(WeaselGenericClass parentClass) throws WeaselCompilerException{
		WeaselGenericClass[] genericClasses = new WeaselGenericClass[list.size()];
		for(int i=0; i<genericClasses.length; i++){
			genericClasses[i] = list.get(i).getGenericClass(parentClass);
		}
		return genericClasses;
	}

	public WeaselGenericClassInfo[] getGenericClassInfo(WeaselClass parentClass) {
		WeaselGenericClassInfo[] generics = new WeaselGenericClassInfo[list.size()];
		for(int i=0; i<generics.length; i++){
			generics[i] = list.get(i).getGenericClassInfo(parentClass);
		}
		return generics;
	}
	
}
