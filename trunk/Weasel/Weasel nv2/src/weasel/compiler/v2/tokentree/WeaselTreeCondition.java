package weasel.compiler.v2.tokentree;

import java.util.List;
import java.util.ListIterator;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerReturn;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionIf;
import weasel.interpreter.bytecode.WeaselInstructionJump;

public class WeaselTreeCondition extends WeaselTree {

	private WeaselToken token;
	private WeaselTree condition;
	private WeaselTree tree1;
	private WeaselTree tree2;
	
	public WeaselTreeCondition(WeaselTree tree1, WeaselToken token){
		this.tree1 = tree1;
		this.token = token;
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
	public WeaselCompilerReturn compile(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelper, WeaselGenericClass write, WeaselGenericClass expect, WeaselGenericClass elementParent, boolean isVariable) throws WeaselCompilerException {
		if(write!=null){
			throw new WeaselCompilerException(token.line, "Can't write any value to Condition");
		}
		List<WeaselInstruction> instructions;
		WeaselCompilerReturn wcr = condition.compile(compiler, compilerHelper, null, new WeaselGenericClass(compiler.baseTypes.booleanClass), null, false);
		if(wcr.returnType.getBaseClass()!=compiler.baseTypes.booleanClass)
			throw new WeaselCompilerException(token.line, "Condition of Conditional have to be a boolean and no %s", wcr.returnType);
		instructions = wcr.instructions;
		WeaselInstructionJump j1;
		WeaselInstructionJump j2;
		instructions.add(j1 = new WeaselInstructionIf());
		wcr = tree1.compile(compiler, compilerHelper, null, expect, elementParent, isVariable);
		WeaselGenericClass wc = wcr.returnType;
		instructions.addAll(wcr.instructions);
		wcr = tree2.compile(compiler, compilerHelper, null, expect, elementParent, isVariable);
		if(wc.getBaseClass()==compiler.baseTypes.voidClass || wcr.returnType.getBaseClass()==compiler.baseTypes.voidClass){
			throw new WeaselCompilerException(token.line, "Can't return void");
		}
		WeaselGenericClass wc2 = wcr.returnType;
		wc = WeaselTree.autoCast(compiler, wc, wc2, token.line, instructions, false);
		instructions.add(j2 = new WeaselInstructionJump());
		j1.setTarget(j2);
		instructions.addAll(wcr.instructions);
		wc2 = WeaselTree.autoCast(compiler, wc2, wc, token.line, instructions, true);
		j2.setTarget(instructions.get(instructions.size()-1));
		return new WeaselCompilerReturn(instructions, WeaselGenericClass.getSmallestSame(wc, wc2));
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
