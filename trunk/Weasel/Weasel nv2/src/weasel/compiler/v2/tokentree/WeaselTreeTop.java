package weasel.compiler.v2.tokentree;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.compiler.equationSolverNew.WeaselCompileReturn;
import weasel.compiler.keywords.WeaselKeyWord;
import weasel.interpreter.WeaselClass;

public class WeaselTreeTop extends WeaselTree {

	private WeaselToken token;
	private List<WeaselToken> newTokens;
	private WeaselTree tree;
	private WeaselTree func;
	private boolean isFunc;
	private boolean isIndex;
	private WeaselTreeGeneric generic;
	
	public WeaselTreeTop(WeaselTree tree, ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		this.tree = tree;
		if(iterator.hasNext()){
			WeaselToken token = iterator.next();
			if(token.tokenType==WeaselTokenType.OPENBRACKET){
				isFunc = true;
				func = WeaselTree.parse(iterator, WeaselTokenType.CLOSEBRACKET);
			}else if(token.tokenType==WeaselTokenType.OPENINDEX){
				isIndex = true;
				func = WeaselTree.parse(iterator, WeaselTokenType.CLOSEINDEX);
			}else{
				iterator.previous();
			}
		}
	}

	public WeaselTreeTop(WeaselToken token, WeaselTreeGeneric generic, ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		this.token = token;
		this.generic = generic;
		if(token.tokenType==WeaselTokenType.KEYWORD && token.param == WeaselKeyWord.NEW){
			newTokens = new ArrayList<WeaselToken>();
			do{
				token = iterator.next();
				if(token.tokenType!=WeaselTokenType.IDENT)
					throw new WeaselCompilerException(token.line, "Expect Ident but got %s", token);
				newTokens.add(token);
				token = iterator.next();
			}while(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.ELEMENT);
			if(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.LESS){
				generic = new WeaselTreeGeneric(iterator);
				if(generic.close){
					throw new WeaselCompilerException(token.line, "Expect Ident but got >");
				}
				token = iterator.next();
			}
			if(token.tokenType!=WeaselTokenType.OPENBRACKET){
				throw new WeaselCompilerException(token.line, "Expect ( but got %s", token);
			}
			func = WeaselTree.parse(iterator, WeaselTokenType.CLOSEBRACKET);
		}else{
			if(iterator.hasNext()){
				token = iterator.next();
				if(token.tokenType==WeaselTokenType.OPENBRACKET){
					isFunc = true;
					func = WeaselTree.parse(iterator, WeaselTokenType.CLOSEBRACKET);
				}else if(token.tokenType==WeaselTokenType.OPENINDEX){
					isIndex = true;
					func = WeaselTree.parse(iterator, WeaselTokenType.CLOSEINDEX);
				}else{
					iterator.previous();
				}
			}
		}
	}
	
	@Override
	public WeaselTreeAddResult add(List<WeaselToken> suffix, WeaselToken infix, List<WeaselToken> prefix, WeaselTree weaselTree, ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		WeaselTree wtl;
		if(infix!=null){
			wtl = new WeaselTreeLevel(this, suffix, infix, prefix, weaselTree, iterator);
		}else if(suffix!=null && !suffix.isEmpty()){
			wtl = new WeaselTreeLevel(this, suffix, iterator);
		}else if(prefix!=null && !prefix.isEmpty()){
			wtl = new WeaselTreeLevel(prefix, this, iterator);
		}else{
			return new WeaselTreeAddResult(this);
		}
		return new WeaselTreeAddResult(wtl);
	}

	@Override
	public WeaselCompileReturn compile(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelper, WeaselClass write) throws WeaselCompilerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		if(newTokens==null){
			return (generic==null?"":generic.toString())+(token==null?"("+tree.toString()+")":token.toString())+(isFunc?func==null?"()":"("+func.toString()+")":"")+(isIndex?func==null?"[]":"["+func.toString()+"]":"");
		}else{
			String s = "new "+newTokens.get(0);
			for(int i=1; i<newTokens.size(); i++){
				s += "."+newTokens.get(i).toString();
			}
			return s + (generic==null?"":generic.toString()) + "("+(func==null?"":func.toString())+")";
		}
	}

}
