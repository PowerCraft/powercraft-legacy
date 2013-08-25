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
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionBitwiseAnd;
import weasel.interpreter.bytecode.WeaselInstructionBitwiseOr;
import weasel.interpreter.bytecode.WeaselInstructionBitwiseXor;
import weasel.interpreter.bytecode.WeaselInstructionLogicalAnd;
import weasel.interpreter.bytecode.WeaselInstructionLogicalOr;
import weasel.interpreter.bytecode.WeaselInstructionPop;

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
	public WeaselCompileReturn compile(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelper, WeaselGenericClass write, WeaselGenericClass expect, WeaselGenericClass elementParent, boolean isVariable) throws WeaselCompilerException {
		WeaselCompileReturn wcr = null;
		Properties operator = (Properties)operators.get(0).param;
		if(operator.infix==operator){
			if(operator.l2r){
				return compileOperator(compiler, compilerHelper, write, expect, elementParent, isVariable, operators.size()-1);
			}else{
				return compileOperator(compiler, compilerHelper, write, expect, elementParent, isVariable, 0);
			}
		}else{
			wcr = level.get(0).compile(compiler, compilerHelper, null, null, null, false);
			/*if(operator.l2r){
				for(int i=operators.size()-1; i>=0; i--){
					wcr = compileOperator(operators.get(i), wcr);
				}
			}else{
				for(int i=0; i<operators.size(); i++){
					wcr = compileOperator(operators.get(i), wcr);
				}
			}*/
		}
		for(WeaselTree l:level){
			l.compile(compiler, compilerHelper, write, expect, elementParent, isVariable);
		}
		return null;
	}

	private WeaselCompileReturn compileOperator(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelper, WeaselGenericClass write, WeaselGenericClass expect,
			WeaselGenericClass elementParent, boolean isVariable, int i) throws WeaselCompilerException {
		if(i==-1)
			return level.get(0).compile(compiler, compilerHelper, write, expect, elementParent, isVariable);
		if(i==operators.size())
			return level.get(operators.size()).compile(compiler, compilerHelper, write, expect, elementParent, isVariable);
		WeaselToken operator = operators.get(i);
		Properties oper = (Properties)operator.param;
		List<WeaselInstruction> instructions = new ArrayList<WeaselInstruction>();
		WeaselCompileReturn wcr;
		WeaselGenericClass ret;
		WeaselGenericClass wgc;
		if(oper==WeaselOperator.COMMA){
			wcr = level.get(i).compile(compiler, compilerHelper, null, expect, null, false);
			instructions.addAll(wcr.instructions);
			ret = wcr.returnType;
			wcr = compileOperator(compiler, compilerHelper, null, new WeaselGenericClass(compiler.baseTypes.voidClass), null, false, i+1);
			instructions.addAll(wcr.instructions);
			if(wcr.returnType.getBaseClass()!=compiler.baseTypes.voidClass){
				instructions.add(new WeaselInstructionPop());
			}
		}else if(oper==WeaselOperator.ASSIGN){
			WeaselCompileReturn wcr2 = compileOperator(compiler, compilerHelper, null, expect, null, false, i+1);
			wcr = level.get(i).compile(compiler, compilerHelper, wcr2.returnType, expect, null, false);
			instructions.addAll(wcr.instructions);
			List<WeaselInstruction> after = new ArrayList<WeaselInstruction>();
			WeaselInstruction wi;
			while(!((wi = instructions.remove(instructions.size()-1)) instanceof WeaselInstructionPlaceHolder)){
				after.add(0, wi);
			}
			instructions.addAll(wcr2.instructions);
			instructions.addAll(after);
			ret = wcr.returnType;
		}else if(oper==WeaselOperator.LOGICAL_OR || oper==WeaselOperator.LOGICAL_AND || oper==WeaselOperator.BITWISE_OR || oper==WeaselOperator.BITWISE_AND
				 || oper==WeaselOperator.BITWISE_XOR){
			wcr = compileOperator(compiler, compilerHelper, null, expect, null, false, i-1);
			instructions.addAll(wcr.instructions);
			wgc = wcr.returnType;
			wcr = level.get(i+1).compile(compiler, compilerHelper, null, expect, null, false);
			ret = wcr.returnType;
			wgc = WeaselTree.autoCast(compiler, wgc, ret, operator.line, instructions, false);
			instructions.addAll(wcr.instructions);
			ret = WeaselTree.autoCast(compiler, ret, wgc, operator.line, instructions, true);
			ret = WeaselGenericClass.getSmallestSame(wgc, ret);
			int primitiveID = WeaselPrimitive.getPrimitiveID(ret.getBaseClass());
			if(oper==WeaselOperator.LOGICAL_OR||oper==WeaselOperator.LOGICAL_AND){
				if(primitiveID!=WeaselPrimitive.BOOLEAN){
					throw new WeaselCompilerException(operator.line, "Operator %s is only usable with boolean, not with %s %s", oper, wgc, ret);
				}
			}else if(oper==WeaselOperator.BITWISE_OR || oper==WeaselOperator.BITWISE_AND || oper==WeaselOperator.BITWISE_XOR){
				if(primitiveID!=WeaselPrimitive.BOOLEAN && primitiveID!=WeaselPrimitive.CHAR && primitiveID!=WeaselPrimitive.BYTE && 
						primitiveID!=WeaselPrimitive.SHORT && primitiveID!=WeaselPrimitive.INT && primitiveID!=WeaselPrimitive.LONG){
					throw new WeaselCompilerException(operator.line, "Operator %s is only usable with boolean, char, byte, short, int, long, not with %s %s", oper, wgc, ret);
				}
			}
			if(oper==WeaselOperator.LOGICAL_OR){
				instructions.add(new WeaselInstructionLogicalOr(primitiveID));
			}else if(oper==WeaselOperator.LOGICAL_AND){
				instructions.add(new WeaselInstructionLogicalAnd(primitiveID));
			}else if(oper==WeaselOperator.BITWISE_OR){
				instructions.add(new WeaselInstructionBitwiseOr(primitiveID));
			}else if(oper==WeaselOperator.BITWISE_AND){
				instructions.add(new WeaselInstructionBitwiseAnd(primitiveID));
			}else if(oper==WeaselOperator.BITWISE_XOR){
				instructions.add(new WeaselInstructionBitwiseXor(primitiveID));
			}
			ret = wcr.returnType;
		}else{
			throw new WeaselCompilerException(operator.line, "Unknown operator %s", operator);
		}
		return new WeaselCompileReturn(instructions, ret);
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
