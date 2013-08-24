package weasel.compiler.v2.tokentree;

import java.util.List;
import java.util.ListIterator;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.equationSolverNew.WeaselCompileReturn;
import weasel.interpreter.WeaselClass;

public class WeaselTreeCondition extends WeaselTree {

	private WeaselTree condition;
	private WeaselTree tree1;
	private WeaselTree tree2;
	
	public WeaselTreeCondition(WeaselTree tree1){
		this.tree1 = tree1;
	}
	
	@Override
	public WeaselTreeAddResult add(List<WeaselToken> suffix, WeaselToken infix,
			List<WeaselToken> prefix, WeaselTree weaselTree,
			ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		if(tree2==null){
			tree2 = weaselTree.add(null, null, prefix, weaselTree, iterator).newTree;
		}else{
			WeaselTreeAddResult wtar = tree2.add(suffix, infix, prefix, weaselTree, iterator);
			tree2 = wtar.newTree;
		}
		return new WeaselTreeAddResult(this);
	}

	@Override
	public WeaselCompileReturn compile(WeaselCompiler compiler,
			WeaselKeyWordCompilerHelper compilerHelper, WeaselClass write)
			throws WeaselCompilerException {
		
		return null;
	}

	@Override
	public String toString() {
		return condition.toString()+"?"+tree1.toString()+":"+tree2.toString();
	}

	public WeaselTreeAddResult setLeft(WeaselTree bottom) {
		if(bottom instanceof WeaselTreeLevel){
			WeaselTreeLevel wtl = (WeaselTreeLevel)bottom;
			if(wtl.levelPriority<3){
				WeaselTreeAddResult wtar = setLeft(wtl.level.get(wtl.level.size()-1));
				wtl.level.set(wtl.level.size()-1, wtar.newTree);
				return new WeaselTreeAddResult(bottom);
			}else{
				condition = bottom;
				return new WeaselTreeAddResult(this);
			}
		}
		condition = bottom;
		return new WeaselTreeAddResult(this);
	}

}
