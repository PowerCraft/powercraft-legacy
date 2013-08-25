package weasel.compiler.v2.tokentree;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselOperator.Properties;
import weasel.compiler.WeaselToken;
import weasel.compiler.equationSolverNew.WeaselCompileReturn;
import weasel.interpreter.WeaselGenericClass;

public class WeaselTreeLevel extends WeaselTree {

	public int levelPriority;
	public List<WeaselToken> operators = new ArrayList<WeaselToken>();
	public List<WeaselTree> level = new ArrayList<WeaselTree>();
	
	public WeaselTreeLevel(int levelPriority){
		this.levelPriority = levelPriority;
	}

	public WeaselTreeLevel(WeaselTree weaselTreeTop, List<WeaselToken> suffix, WeaselToken infix, List<WeaselToken> prefix, WeaselTree weaselTree, ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		levelPriority = ((Properties)infix.param).priority;
		operators.add(infix);
		if(suffix!=null && !suffix.isEmpty()){
			WeaselTreeAddResult wtar = weaselTreeTop.add(suffix, null, null, null, iterator);
			level.add(wtar.newTree);
		}else{
			level.add(weaselTreeTop);
		}
		if(prefix!=null && !prefix.isEmpty()){
			WeaselTreeAddResult wtar = weaselTree.add(null, null, prefix, null, iterator);
			level.add(wtar.newTree);
		}else{
			level.add(weaselTree);
		}
	}

	public WeaselTreeLevel(WeaselTreeTop weaselTreeTop, List<WeaselToken> suffix, ListIterator<WeaselToken> iterator) {
		levelPriority = ((Properties)suffix.get(0).param).priority;
		operators.addAll(suffix);
		level.add(weaselTreeTop);
	}

	public WeaselTreeLevel(List<WeaselToken> prefix, WeaselTreeTop weaselTreeTop, ListIterator<WeaselToken> iterator) {
		levelPriority = ((Properties)prefix.get(0).param).priority;
		operators.addAll(prefix);
		level.add(weaselTreeTop);
	}

	@Override
	public WeaselTreeAddResult add(List<WeaselToken> suffix, WeaselToken infix, List<WeaselToken> prefix, WeaselTree weaselTree, ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		if(infix!=null){
			int priority = ((Properties)infix.param).priority;
			if(priority<levelPriority){
				return new WeaselTreeAddResult(new WeaselTreeLevel(this, suffix, infix, prefix, weaselTree, iterator));
			}else if(priority==levelPriority){
				operators.add(infix);
				WeaselTreeAddResult wtar = level.get(level.size()-1).add(suffix, null, null, null, iterator);
				level.set(level.size()-1, wtar.newTree);
				wtar = weaselTree.add(null, null, prefix, weaselTree, iterator);
				level.add(wtar.newTree);
			}else{
				WeaselTreeAddResult wtar = level.get(level.size()-1).add(suffix, infix, prefix, weaselTree, iterator);
				level.set(level.size()-1, wtar.newTree);
			}
		}else if(suffix!=null && !suffix.isEmpty()){
			int priority = ((Properties)suffix.get(0).param).priority;
			if(priority<levelPriority){
				return new WeaselTreeAddResult(new WeaselTreeLevel(this, suffix, null, null, null, iterator));
			}else if(priority==levelPriority){
				operators.addAll(suffix);
			}else if(priority>levelPriority){
				WeaselTreeAddResult wtar = level.get(level.size()-1).add(suffix, null, null, null, iterator);
				level.set(level.size()-1, wtar.newTree);
			}
		}else if(prefix!=null && !prefix.isEmpty()){
			int priority = ((Properties)prefix.get(0).param).priority;
			if(priority<levelPriority){
				return new WeaselTreeAddResult(new WeaselTreeLevel(this, null, null, prefix, null, iterator));
			}else if(priority==levelPriority){
				operators.addAll(prefix);
			}else if(priority>levelPriority){
				WeaselTreeAddResult wtar = level.get(level.size()-1).add(null, null, prefix, null, iterator);
				level.set(level.size()-1, wtar.newTree);
			}
		}else{
			WeaselTreeAddResult wtar = level.get(level.size()-1).add(suffix, infix, prefix, weaselTree, iterator);
			level.set(level.size()-1, wtar.newTree);
		}
		return new WeaselTreeAddResult(this);
	}

	@Override
	public WeaselCompileReturn compile(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelper, WeaselGenericClass write, WeaselGenericClass expect) throws WeaselCompilerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		String s = "(";
		if(!operators.isEmpty() && ((Properties)operators.get(0).param).prefix == operators.get(0).param){
			for(int i=0; i<operators.size(); i++){
				s += operators.get(i).toString();
			}
			s += level.get(0);
		}else if(!operators.isEmpty() && ((Properties)operators.get(0).param).suffix == operators.get(0).param){
			s += level.get(0).toString();
			for(int i=0; i<operators.size(); i++){
				s += operators.get(i).toString();
			}
		}else{
			s += level.get(0).toString();
			for(int i=0; i<operators.size(); i++){
				s += operators.get(i).toString();
				s += level.get(i+1).toString();
			}
		}
		return s+")";
	}
	
	
}
