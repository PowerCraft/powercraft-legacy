package weasel.compiler.v2.tokentree;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselNativeException;

public class WeaselTreeGeneric {

	private List<TreeGenericElement> list = new ArrayList<TreeGenericElement>();
	public boolean close;
	
	public WeaselTreeGeneric(ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		WeaselToken token = iterator.next();
		if(!(token.tokenType==WeaselTokenType.OPERATOR && (token.param==WeaselOperator.GREATER || token.param==WeaselOperator.RSHIFT))){
			iterator.previous();
			do{
				TreeGenericElement tge = new TreeGenericElement(iterator);
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
	
	private static class TreeGenericElement{

		private WeaselToken token;
		private String realClassName;
		private String className;
		private String classByteName;
		private WeaselTreeGeneric generic;
		public boolean close;
		
		public TreeGenericElement(ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
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
			classByteName = WeaselCompiler.mapClassNames(className);
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

		@Override
		public String toString() {
			return className+(generic==null?"":generic.toString());
		}
		
	}
	
}
