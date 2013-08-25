package weasel.compiler.v2.tokentree;

import java.util.List;
import java.util.ListIterator;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.equationSolverNew.WeaselCompileReturn;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionCast;
import weasel.interpreter.bytecode.WeaselInstructionCastPrimitive;
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
	public WeaselCompileReturn compile(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelper, WeaselClass write) throws WeaselCompilerException {
		if(write!=null){
			throw new WeaselCompilerException(token.line, "Can't write any value to Condition");
		}
		List<WeaselInstruction> instructions;
		WeaselCompileReturn wcr = condition.compile(compiler, compilerHelper, null);
		if(wcr.returnType!=compiler.baseTypes.booleanClass)
			throw new WeaselCompilerException(token.line, "Condition of Conditional have to be a boolean and no %s", wcr.returnType);
		instructions = wcr.instructions;
		WeaselInstructionJump j1;
		WeaselInstructionJump j2;
		instructions.add(j1 = new WeaselInstructionIf());
		wcr = tree1.compile(compiler, compilerHelper, null);
		WeaselClass wc = wcr.returnType;
		instructions.addAll(wcr.instructions);
		wcr = tree2.compile(compiler, compilerHelper, null);
		if(wc==compiler.baseTypes.voidClass || wcr.returnType==compiler.baseTypes.voidClass){
			throw new WeaselCompilerException(token.line, "Can't return void");
		}
		WeaselClass wc2 = wcr.returnType;
		boolean canCast = true;
		if(wc.isPrimitive() && !wc2.isPrimitive()){
			wc = compiler.getWeaselClass(WeaselPrimitive.getWrapper(wc));
			instructions.add(new WeaselInstructionCast(WeaselPrimitive.getWrapper(wc)));
		}else if(wc.isPrimitive() && wc2.isPrimitive()){
			if(wc!=wc2){
				canCast = WeaselPrimitive.canCastAutoTo(wc, wc2);
				if(canCast){
					instructions.add(new WeaselInstructionCastPrimitive(WeaselPrimitive.getPrimitiveID(wc2)));
					wc2 = wc;
				}
			}
		}
		instructions.add(j2 = new WeaselInstructionJump());
		j1.setTarget(j2);
		instructions.addAll(wcr.instructions);
		if(!wc.isPrimitive() && wc2.isPrimitive()){
			wc2 = compiler.getWeaselClass(WeaselPrimitive.getWrapper(wc2));
			instructions.add(new WeaselInstructionCast(wc2.getByteName()));
		}else if(wc.isPrimitive() && wc2.isPrimitive()){
			if(wc!=wc2 && !canCast){
				canCast = WeaselPrimitive.canCastAutoTo(wc2, wc);
				if(canCast){
					instructions.add(new WeaselInstructionCastPrimitive(WeaselPrimitive.getPrimitiveID(wc)));
					wc = wc2;
				}else{
					throw new WeaselCompilerException(token.line, "Types %s and %s are not compatible", wc, wc2);
				}
			}
		}
		j2.setTarget(instructions.get(instructions.size()-1));
		return new WeaselCompileReturn(instructions, WeaselClass.getSmallestSame(wc, wc2));
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
