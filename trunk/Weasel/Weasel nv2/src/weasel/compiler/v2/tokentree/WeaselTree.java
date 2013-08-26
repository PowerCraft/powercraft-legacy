package weasel.compiler.v2.tokentree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerReturn;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselOperator.Properties;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.compiler.keywords.WeaselKeyWord;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselGenericMethod2;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionCast;
import weasel.interpreter.bytecode.WeaselInstructionCastPrimitive;
import weasel.interpreter.bytecode.WeaselInstructionPop;

public abstract class WeaselTree {

	public abstract WeaselTreeAddResult add(List<WeaselToken> suffix, WeaselToken infix, List<WeaselToken> prefix, WeaselTree weaselTree, ListIterator<WeaselToken> iterator) throws WeaselCompilerException;

	public abstract WeaselCompilerReturn compile(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelper, WeaselGenericClass write, WeaselGenericClass expect, WeaselGenericClass elementParent, boolean isVariable) throws WeaselCompilerException;

	public abstract String toString();
	
	public static List<WeaselInstruction> parseAndCompile(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelper, ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		
		WeaselToken token = iterator.next();
		if(token.tokenType==WeaselTokenType.KEYWORD && ((WeaselKeyWord)token.param).compiler!=null){
			WeaselCompilerReturn wcr = ((WeaselKeyWord)token.param).compiler.compile(compiler, compilerHelper, iterator);
			if(wcr.returnType.getBaseClass()!=compiler.baseTypes.voidClass)
				wcr.instructions.add(new WeaselInstructionPop());
			return wcr.instructions;
		}else{
			iterator.previous();
		
			WeaselTree tree = parse(iterator, WeaselTokenType.SEMICOLON);
			if(tree!=null){
				WeaselCompilerReturn wcr = tree.compile(compiler, compilerHelper, null, new WeaselGenericClass(compiler.baseTypes.voidClass), null, false);
				if(wcr.returnType.getBaseClass()!=compiler.baseTypes.voidClass)
					wcr.instructions.add(new WeaselInstructionPop());
				return wcr.instructions;
			}
			return null;
		}
	}

	protected static WeaselTree parse(ListIterator<WeaselToken> iterator, WeaselTokenType...end) throws WeaselCompilerException {
		
		List<WeaselToken> tokenCache = new ArrayList<WeaselToken>();
		List<WeaselToken> tokenPrefix = new ArrayList<WeaselToken>();
		List<WeaselToken> tokenSuffix = new ArrayList<WeaselToken>();
		
		WeaselTree bottom = null;
		boolean start = true;
		
		WeaselTreeGeneric generic = null;
		
		while(iterator.hasNext()){
			
			WeaselToken token = iterator.next();
			
			if(Arrays.asList(end).contains(token.tokenType)){
				
				if(generic!=null)
					throw new WeaselCompilerException(token.line, "Expect Ident but got %s", token);
				
				while(!tokenCache.isEmpty() && ((Properties)tokenCache.get(0).param).suffix!=null){
					tokenSuffix.add(tokenCache.remove(0));
				}
				
				if(tokenCache.size()!=0){
					throw new WeaselCompilerException(token.line, "Expect suffix but got %s", tokenCache.get(0));
				}
				
				if(!tokenSuffix.isEmpty()){
					WeaselTreeAddResult wtar = bottom.add(tokenSuffix, null, null, null, iterator);
					bottom = wtar.newTree;
				}
				
				tokenPrefix.clear();
				tokenSuffix.clear();
				tokenCache.clear();
				
				break;
				
			}else{
			
				if(token.tokenType==WeaselTokenType.COMMA){
					token = new WeaselToken(WeaselTokenType.OPERATOR, token.line, WeaselOperator.COMMA);
				}
				
				if(token.tokenType==WeaselTokenType.QUESTIONMARK){
					
					if(generic!=null)
						throw new WeaselCompilerException(token.line, "Expect Ident but got ?:");
					
					while(!tokenCache.isEmpty() && ((Properties)tokenCache.get(0).param).suffix!=null){
						tokenSuffix.add(tokenCache.remove(0));
					}
					
					if(tokenCache.size()!=0){
						throw new WeaselCompilerException(token.line, "Expect suffix but got %s", tokenCache.get(0));
					}
					
					if(!tokenSuffix.isEmpty()){
						WeaselTreeAddResult wtar = bottom.add(tokenSuffix, null, null, null, iterator);
						bottom = wtar.newTree;
					}
					
					WeaselTree tree = parse(iterator, WeaselTokenType.COLON);
					
					WeaselTreeCondition wtc = new WeaselTreeCondition(tree, token);
					
					WeaselTreeAddResult wtar = wtc.setLeft(bottom);
					
					bottom = wtar.newTree;
					
					start = true;
					
				}else if(token.tokenType == WeaselTokenType.OPERATOR){
					
					if(generic!=null)
						throw new WeaselCompilerException(token.line, "Expect Ident but got %s", token);
					
					tokenCache.add(token);
				
					if(token.param == WeaselOperator.ELEMENT){
						WeaselToken wtoken = iterator.next();
						if(wtoken.tokenType == WeaselTokenType.OPERATOR && wtoken.param ==WeaselOperator.LESS) {
							generic = new WeaselTreeGeneric(iterator);
							token = iterator.previous();
							if(generic.close){
								throw new WeaselCompilerException(token.line, "Expect Ident but got >");
							}
						}else{
							iterator.previous();
						}
					}
					
				}else if(start){
					
					if(generic!=null)
						throw new WeaselCompilerException(token.line, "generic Error at %s", token);
					
					while(!tokenCache.isEmpty() && ((Properties)tokenCache.get(tokenCache.size()-1).param).prefix!=null){
						tokenPrefix.add(tokenCache.remove(tokenCache.size()-1));
					}
					
					if(!tokenCache.isEmpty()){
						throw new WeaselCompilerException(token.line, "Expect prefix but got %s", tokenCache.get(0));
					}
					
					if(token.tokenType==WeaselTokenType.OPENBRACKET){
						WeaselTree add = parse(iterator, WeaselTokenType.CLOSEBRACKET);
						if(tokenPrefix.isEmpty()){
							if(bottom==null){
								bottom = new WeaselTreeTop(add, iterator);
							}else{
								bottom.add(null, null, null, new WeaselTreeTop(add, iterator), iterator);
							}
						}else{
							if(bottom==null){
								bottom = new WeaselTreeLevel(((Properties)tokenPrefix.get(0).param).priority);
							}
							WeaselTreeAddResult wtar = bottom.add(null, null, tokenPrefix, add, iterator);
							bottom = wtar.newTree;
						}
					}else{
						WeaselTree add = new WeaselTreeTop(token, null, iterator);
						if(tokenPrefix.isEmpty()){
							if(bottom==null){
								bottom = add;
							}else{
								bottom.add(null, null, null, add, iterator);
							}
						}else{
							if(bottom==null){
								bottom = new WeaselTreeLevel(((Properties)tokenPrefix.get(0).param).priority);
							}
							WeaselTreeAddResult wtar = bottom.add(null, null, tokenPrefix, add, iterator);
							bottom = wtar.newTree;
						}
					}
					
					tokenPrefix.clear();
					tokenSuffix.clear();
					tokenCache.clear();
					
					start = false;
					
				}else{
					
					if(generic!=null && token.tokenType != WeaselTokenType.IDENT)
						throw new WeaselCompilerException(token.line, "Expect ident after generic but got %s", token);
					
					boolean isCast = false;
					
					if(token.tokenType==WeaselTokenType.OPENBRACKET){
						WeaselToken token2 = iterator.next();
						String className = "";
						if(token2.tokenType==WeaselTokenType.IDENT){
							isCast = true;
							className = (String)token2.param;
							token2 = iterator.next();
							while(token2.tokenType==WeaselTokenType.OPERATOR && token2.param == WeaselOperator.ELEMENT){
								token2 = iterator.next();
								if(token2.tokenType!=WeaselTokenType.IDENT){
									isCast = false;
									break;
								}
								className += "."+(String)token2.param;
								token2 = iterator.next();
							}
							if(token2.tokenType==WeaselTokenType.OPERATOR && token2.param == WeaselOperator.LESS){
								try{
									generic = new WeaselTreeGeneric(iterator);
									token2 = iterator.next();
								}catch(WeaselCompilerException e){
									isCast = false;
								}
							}
							if(isCast && token2.tokenType!=WeaselTokenType.CLOSEBRACKET){
								isCast = false;
							}
						}
						if(isCast){
							tokenCache.add(new WeaselCastToken(token.line, className, generic));
						}else{
							while(iterator.previous() != token);
							iterator.next();
						}
						generic = null;
					}
					
					if(!isCast){
					
						if(tokenCache.size()==0){
							throw new WeaselCompilerException(token.line, "Expect %s before %s", Arrays.toString(end), token);
						}
						
						while(!tokenCache.isEmpty() && ((Properties)tokenCache.get(0).param).suffix!=null){
							tokenSuffix.add(tokenCache.remove(0));
						}
						
						if(tokenCache.isEmpty()){
							while(true){
								tokenCache.add(0, tokenPrefix.remove(tokenPrefix.size()-1));
								if(((Properties)tokenCache.get(0).param).infix!=null){
									break;
								}
							}
						}
						
						while(tokenCache.size()>1 && ((Properties)tokenCache.get(tokenCache.size()-1).param).prefix!=null){
							tokenPrefix.add(0, tokenCache.remove(tokenCache.size()-1));
						}
						
						if(tokenCache.size()>1){
							throw new WeaselCompilerException(tokenCache.get(1).line, "Expect suffix or variable but got %s", tokenCache.get(1));
						}
						
						for(WeaselToken token1:tokenSuffix){
							token1.param = ((Properties)token1.param).suffix;
						}
						
						for(WeaselToken token1:tokenCache){
							token1.param = ((Properties)token1.param).infix;
						}
						
						for(WeaselToken token1:tokenPrefix){
							token1.param = ((Properties)token1.param).prefix;
						}
						
						WeaselTree add;
						if(token.tokenType==WeaselTokenType.OPENBRACKET){
							add = parse(iterator, WeaselTokenType.CLOSEBRACKET);
							add = new WeaselTreeTop(add, iterator);
						}else{
							add = new WeaselTreeTop(token, generic, iterator);
						}
						
						WeaselTreeAddResult wtar = bottom.add(tokenSuffix, tokenCache.get(0), tokenPrefix, add, iterator);
						bottom = wtar.newTree;
						
						tokenPrefix.clear();
						tokenSuffix.clear();
						tokenCache.clear();
					
					}
					
				}
			}
		}
		
		return bottom;
		
	}

	public static WeaselGenericClass autoCast(WeaselCompiler compiler, WeaselGenericClass wc1, WeaselGenericClass wc2, int line, List<WeaselInstruction> instructions, boolean b) throws WeaselCompilerException{
		if(!wc1.getBaseClass().isPrimitive() && wc2.getBaseClass().isPrimitive()){
			wc2 = new WeaselGenericClass(compiler.getWeaselClass(WeaselPrimitive.getWrapper(wc2.getBaseClass())));
			instructions.add(new WeaselInstructionCast(wc2.getBaseClass().getByteName()));
		}else if(wc1.getBaseClass().isPrimitive() && wc2.getBaseClass().isPrimitive()){
			if(wc1.getBaseClass()!=wc2.getBaseClass()){
				boolean canCast = WeaselPrimitive.canCastAutoTo(wc1.getBaseClass(), wc2.getBaseClass());
				if(canCast){
					instructions.add(new WeaselInstructionCastPrimitive(WeaselPrimitive.getPrimitiveID(wc2.getBaseClass())));
					return wc2;
				}else if(b){
					throw new WeaselCompilerException(line, "Types %s and %s are not compatible", wc1, wc2);
				}
			}
		}
		return wc1;
	}
	
	public static WeaselParameterCompileReturn compileParamList(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelper, WeaselTree func, List<WeaselGenericMethod2> methods) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
