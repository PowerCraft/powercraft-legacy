package weasel.compiler.v2.tokentree;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselOperator.Properties;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.compiler.equationSolverNew.WeaselCompileReturn;
import weasel.interpreter.WeaselClass;

public abstract class WeaselTree {

	public abstract WeaselTreeAddResult add(List<WeaselToken> suffix, WeaselToken infix, List<WeaselToken> prefix, WeaselTree weaselTree, ListIterator<WeaselToken> iterator) throws WeaselCompilerException;

	public abstract WeaselCompileReturn compile(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelper, WeaselClass write) throws WeaselCompilerException;

	public abstract String toString();
	
	public static WeaselTree parse(List<WeaselToken> tokenList) throws WeaselCompilerException {
		
		ListIterator<WeaselToken> iterator = tokenList.listIterator();
		
		WeaselTree tree =  parse(iterator, null);
		
		System.out.println("tree:"+tree);
		return tree;
	}

	protected static WeaselTree parse(ListIterator<WeaselToken> iterator, WeaselTokenType end) throws WeaselCompilerException {
		
		List<WeaselToken> tokenCache = new ArrayList<WeaselToken>();
		List<WeaselToken> tokenPrefix = new ArrayList<WeaselToken>();
		List<WeaselToken> tokenSuffix = new ArrayList<WeaselToken>();
		
		WeaselTree bottom = null;
		boolean start = true;
		
		while(iterator.hasNext()){
			
			WeaselToken token = iterator.next();
			
			if(token.tokenType==WeaselTokenType.COMMA){
				token = new WeaselToken(WeaselTokenType.OPERATOR, token.line, WeaselOperator.COMMA);
			}
			
			if(token.tokenType==WeaselTokenType.QUESTIONMARK){
				
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
				
				WeaselTreeCondition wtc = new WeaselTreeCondition(tree);
				
				WeaselTreeAddResult wtar = wtc.setLeft(bottom);
				
				bottom = wtar.newTree;
				
				start = true;
				
			}else if(token.tokenType == WeaselTokenType.OPERATOR){
				
				tokenCache.add(token);
				
			}else if(token.tokenType == end){
				
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
				
			}else if(start){
				
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
					WeaselTree add = new WeaselTreeTop(token, iterator);
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
				
				if(tokenCache.size()==0){
					throw new WeaselCompilerException(token.line, "Expect %s before %s", end==null?";":end, token);
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
					add = new WeaselTreeTop(parse(iterator, WeaselTokenType.CLOSEBRACKET), iterator);
				}else{
					add = new WeaselTreeTop(token, iterator);
				}
				
				WeaselTreeAddResult wtar = bottom.add(tokenSuffix, tokenCache.get(0), tokenPrefix, add, iterator);
				bottom = wtar.newTree;
				
				tokenPrefix.clear();
				tokenSuffix.clear();
				tokenCache.clear();
				
			}
			
		}
		
		return bottom;
		
	}
	
}
