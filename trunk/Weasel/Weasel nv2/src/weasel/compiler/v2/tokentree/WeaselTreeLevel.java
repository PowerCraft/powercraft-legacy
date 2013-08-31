package weasel.compiler.v2.tokentree;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerReturn;
import weasel.compiler.WeaselInstructionList;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselOperator.Properties;
import weasel.compiler.WeaselToken;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.bytecode.WeaselInstructionAdd;
import weasel.interpreter.bytecode.WeaselInstructionBitwiseAnd;
import weasel.interpreter.bytecode.WeaselInstructionBitwiseOr;
import weasel.interpreter.bytecode.WeaselInstructionBitwiseXor;
import weasel.interpreter.bytecode.WeaselInstructionCast;
import weasel.interpreter.bytecode.WeaselInstructionCastPrimitive;
import weasel.interpreter.bytecode.WeaselInstructionDiv;
import weasel.interpreter.bytecode.WeaselInstructionEqual;
import weasel.interpreter.bytecode.WeaselInstructionGreater;
import weasel.interpreter.bytecode.WeaselInstructionGreaterEqual;
import weasel.interpreter.bytecode.WeaselInstructionInstanceof;
import weasel.interpreter.bytecode.WeaselInstructionLShift;
import weasel.interpreter.bytecode.WeaselInstructionLess;
import weasel.interpreter.bytecode.WeaselInstructionLessEqual;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstBoolean;
import weasel.interpreter.bytecode.WeaselInstructionLogicalAnd;
import weasel.interpreter.bytecode.WeaselInstructionLogicalOr;
import weasel.interpreter.bytecode.WeaselInstructionMod;
import weasel.interpreter.bytecode.WeaselInstructionMul;
import weasel.interpreter.bytecode.WeaselInstructionNeg;
import weasel.interpreter.bytecode.WeaselInstructionNotEqual;
import weasel.interpreter.bytecode.WeaselInstructionNotVerySame;
import weasel.interpreter.bytecode.WeaselInstructionPop;
import weasel.interpreter.bytecode.WeaselInstructionRShift;
import weasel.interpreter.bytecode.WeaselInstructionSub;
import weasel.interpreter.bytecode.WeaselInstructionVerySame;

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
				level.add(weaselTree);
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
				level.add(weaselTree);
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
	public WeaselCompilerReturn compile(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelper, WeaselGenericClass write, WeaselGenericClass expect, WeaselGenericClass elementParent, boolean isVariable) throws WeaselCompilerException {
		Properties operator = (Properties)operators.get(0).param;
		if(operator.infix==operator){
			if(operator.l2r){
				return compileInfixOperator(compiler, compilerHelper, write, expect, elementParent, isVariable, operators.size()-1);
			}else{
				return compileInfixOperator(compiler, compilerHelper, write, expect, elementParent, isVariable, 0);
			}
		}else{
			if(operator.l2r){
				return compileOperator(compiler, compilerHelper, write, expect, elementParent, isVariable, operators.size()-1);
			}else{
				return compileOperator(compiler, compilerHelper, write, expect, elementParent, isVariable, 0);
			}
		}
	}

	private WeaselCompilerReturn compileOperator(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelper, WeaselGenericClass write, WeaselGenericClass expect,
		WeaselGenericClass elementParent, boolean isVariable, int i) throws WeaselCompilerException {
		if(i==-1 || i==operators.size())
			return level.get(0).compile(compiler, compilerHelper, null, expect, elementParent, isVariable);
		WeaselToken operator = operators.get(i);
		Properties oper = (Properties)operator.param;
		WeaselInstructionList instructions;
		WeaselCompilerReturn wcr;
		WeaselGenericClass ret;
		if(oper==WeaselOperator.INSTANCEOF){
			WeaselInstanceofToken wit = (WeaselInstanceofToken) operator;
			WeaselClass wc = compiler.getWeaselClass("O"+wit.className+";");
			WeaselGenericClass wgc = new WeaselGenericClass(wc);
			wcr = compileOperator(compiler, compilerHelper, write, wgc, elementParent, isVariable, i-1);
			if(wcr.returnType.canCastTo(wgc)){
				instructions = new WeaselInstructionList();
				instructions.add(operator.line, new WeaselInstructionLoadConstBoolean(true));
			}else{
				if(wcr.returnType.getBaseClass().isPrimitive())
					throw new WeaselCompilerException(operator.line, "can't use implements for primitives");
				instructions = wcr.instructions;
				instructions.add(operator.line, new WeaselInstructionInstanceof(wc.getByteName()));
			}
			ret = new WeaselGenericClass(compiler.baseTypes.booleanClass);
		}else if(oper==WeaselOperator.CAST){
			WeaselCastToken wct = (WeaselCastToken) operator;
			WeaselClass wc = compiler.getWeaselClass(WeaselClass.mapClassNames(wct.className));
			ret = new WeaselGenericClass(wc);
			wcr = compileOperator(compiler, compilerHelper, write, ret, elementParent, isVariable, i+1);
			instructions = wcr.instructions;
			if(wc.isPrimitive()){
				instructions.add(operator.line, new WeaselInstructionCastPrimitive(WeaselPrimitive.getPrimitiveID(wc)));
			}else{
				instructions.add(operator.line, new WeaselInstructionCast(wc.getByteName()));
			}
		}else if(oper==WeaselOperator.MINUS_PREFIX || oper==WeaselOperator.PLUS_PREFIX){
			wcr = compileOperator(compiler, compilerHelper, null, null, null, false, i+1);
			instructions = wcr.instructions;
			ret = wcr.returnType;
			int primitiveID = WeaselPrimitive.getPrimitiveID(ret.getBaseClass());
			if(oper==WeaselOperator.MINUS_PREFIX || oper==WeaselOperator.PLUS_PREFIX){
				if(primitiveID!=WeaselPrimitive.CHAR && primitiveID!=WeaselPrimitive.BYTE && primitiveID!=WeaselPrimitive.SHORT 
						&& primitiveID!=WeaselPrimitive.INT && primitiveID!=WeaselPrimitive.LONG
						 && primitiveID!=WeaselPrimitive.FLOAT && primitiveID!=WeaselPrimitive.DOUBLE){
					throw new WeaselCompilerException(operator.line, "Operator %s is only usable with char, byte, short, int, long, float, double, not with %s", oper, ret);
				}
			}
			if(oper==WeaselOperator.MINUS_PREFIX){
				instructions.add(operator.line, new WeaselInstructionNeg(primitiveID));
			}
		}else{
			throw new WeaselCompilerException(operator.line, "Unknown operator %s", operator);
		}
		return new WeaselCompilerReturn(instructions, ret);
	}

	private WeaselCompilerReturn compileInfixOperator(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelper, WeaselGenericClass write, WeaselGenericClass expect,
			WeaselGenericClass elementParent, boolean isVariable, int i) throws WeaselCompilerException {
		if(i==-1)
			return level.get(0).compile(compiler, compilerHelper, write, expect, elementParent, isVariable);
		if(i==operators.size())
			return level.get(operators.size()).compile(compiler, compilerHelper, write, expect, elementParent, isVariable);
		WeaselToken operator = operators.get(i);
		Properties oper = (Properties)operator.param;
		WeaselInstructionList instructions = new WeaselInstructionList();
		WeaselCompilerReturn wcr;
		WeaselGenericClass ret;
		WeaselGenericClass wgc;
		if(oper==WeaselOperator.COMMA){
			wcr = level.get(i).compile(compiler, compilerHelper, null, expect, null, false);
			instructions.addAll(wcr.instructions);
			ret = wcr.returnType;
			wcr = compileInfixOperator(compiler, compilerHelper, null, new WeaselGenericClass(compiler.baseTypes.voidClass), null, false, i+1);
			instructions.addAll(wcr.instructions);
			if(wcr.returnType.getBaseClass()!=compiler.baseTypes.voidClass){
				instructions.add(operator.line, new WeaselInstructionPop());
			}
		}else if(oper==WeaselOperator.ASSIGN){
			WeaselCompilerReturn wcr2 = compileInfixOperator(compiler, compilerHelper, null, expect, null, false, i+1);
			wcr = level.get(i).compile(compiler, compilerHelper, wcr2.returnType, expect, null, false);
			instructions.addAll(wcr.instructions);
			instructions.replacePlaceHolderWith(wcr2.instructions);
			ret = wcr.returnType;
		}else if(oper==WeaselOperator.LOGICAL_OR || oper==WeaselOperator.LOGICAL_AND || oper==WeaselOperator.BITWISE_OR || oper==WeaselOperator.BITWISE_AND
				 || oper==WeaselOperator.BITWISE_XOR || oper==WeaselOperator.LESS || oper==WeaselOperator.GREATER
				 || oper==WeaselOperator.LESS_EQUAL || oper==WeaselOperator.GREATER_EQUAL
				 || oper==WeaselOperator.PLUS || oper==WeaselOperator.MINUS || oper==WeaselOperator.TIMES || oper==WeaselOperator.DIVIDE
				 || oper==WeaselOperator.REMAINDER){
			wcr = compileInfixOperator(compiler, compilerHelper, null, expect, null, false, i-1);
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
			}else if(oper==WeaselOperator.LESS || oper==WeaselOperator.GREATER || oper==WeaselOperator.LESS_EQUAL
					 || oper==WeaselOperator.GREATER_EQUAL){
				if(primitiveID!=WeaselPrimitive.CHAR && primitiveID!=WeaselPrimitive.BYTE && primitiveID!=WeaselPrimitive.SHORT 
						&& primitiveID!=WeaselPrimitive.INT && primitiveID!=WeaselPrimitive.LONG
						 && primitiveID!=WeaselPrimitive.FLOAT && primitiveID!=WeaselPrimitive.DOUBLE){
					throw new WeaselCompilerException(operator.line, "Operator %s is only usable with char, byte, short, int, long, float, double, not with %s %s", oper, wgc, ret);
				}
				ret = new WeaselGenericClass(compiler.baseTypes.booleanClass);
			}else if(oper==WeaselOperator.PLUS || oper==WeaselOperator.MINUS || oper==WeaselOperator.TIMES || oper==WeaselOperator.DIVIDE
					 || oper==WeaselOperator.REMAINDER){
				if(primitiveID!=WeaselPrimitive.CHAR && primitiveID!=WeaselPrimitive.BYTE && primitiveID!=WeaselPrimitive.SHORT 
						&& primitiveID!=WeaselPrimitive.INT && primitiveID!=WeaselPrimitive.LONG
						 && primitiveID!=WeaselPrimitive.FLOAT && primitiveID!=WeaselPrimitive.DOUBLE){
					throw new WeaselCompilerException(operator.line, "Operator %s is only usable with char, byte, short, int, long, float, double, not with %s %s", oper, wgc, ret);
				}
			}
			if(oper==WeaselOperator.LOGICAL_OR){
				instructions.add(operator.line, new WeaselInstructionLogicalOr(primitiveID));
			}else if(oper==WeaselOperator.LOGICAL_AND){
				instructions.add(operator.line, new WeaselInstructionLogicalAnd(primitiveID));
			}else if(oper==WeaselOperator.BITWISE_OR){
				instructions.add(operator.line, new WeaselInstructionBitwiseOr(primitiveID));
			}else if(oper==WeaselOperator.BITWISE_AND){
				instructions.add(operator.line, new WeaselInstructionBitwiseAnd(primitiveID));
			}else if(oper==WeaselOperator.BITWISE_XOR){
				instructions.add(operator.line, new WeaselInstructionBitwiseXor(primitiveID));
			}else if(oper==WeaselOperator.LESS){
				instructions.add(operator.line, new WeaselInstructionLess(primitiveID));
			}else if(oper==WeaselOperator.GREATER){
				instructions.add(operator.line, new WeaselInstructionGreater(primitiveID));
			}else if(oper==WeaselOperator.LESS_EQUAL){
				instructions.add(operator.line, new WeaselInstructionLessEqual(primitiveID));
			}else if(oper==WeaselOperator.GREATER_EQUAL){
				instructions.add(operator.line, new WeaselInstructionGreaterEqual(primitiveID));
			}else if(oper==WeaselOperator.PLUS){
				instructions.add(operator.line, new WeaselInstructionAdd(primitiveID));
			}else if(oper==WeaselOperator.MINUS){
				instructions.add(operator.line, new WeaselInstructionSub(primitiveID));
			}else if(oper==WeaselOperator.TIMES){
				instructions.add(operator.line, new WeaselInstructionMul(primitiveID));
			}else if(oper==WeaselOperator.DIVIDE){
				instructions.add(operator.line, new WeaselInstructionDiv(primitiveID));
			}else if(oper==WeaselOperator.REMAINDER){
				instructions.add(operator.line, new WeaselInstructionMod(primitiveID));
			}
		}else if(oper==WeaselOperator.RSHIFT || oper==WeaselOperator.LSHIFT){
			wcr = compileInfixOperator(compiler, compilerHelper, null, expect, null, false, i-1);
			instructions.addAll(wcr.instructions);
			ret = wcr.returnType;
			wcr = level.get(i+1).compile(compiler, compilerHelper, null, expect, null, false);
			wgc = wcr.returnType;
			instructions.addAll(wcr.instructions);
			WeaselTree.autoCast(compiler, wgc, new WeaselGenericClass(compiler.baseTypes.intClass), operator.line, instructions, true);
			int primitiveID = WeaselPrimitive.getPrimitiveID(ret.getBaseClass());
			if(oper==WeaselOperator.RSHIFT||oper==WeaselOperator.LSHIFT){
				if(primitiveID!=WeaselPrimitive.CHAR && primitiveID!=WeaselPrimitive.BYTE && primitiveID!=WeaselPrimitive.SHORT 
						&& primitiveID!=WeaselPrimitive.INT && primitiveID!=WeaselPrimitive.LONG){
					throw new WeaselCompilerException(operator.line, "Operator %s is only usable with char, byte, short, int, long, not with %s", oper, ret);
				}
			}
			if(oper==WeaselOperator.RSHIFT){
				instructions.add(operator.line, new WeaselInstructionRShift(primitiveID));
			}else if(oper==WeaselOperator.LSHIFT){
				instructions.add(operator.line, new WeaselInstructionLShift(primitiveID));
			}
		}else if(oper==WeaselOperator.VERY_SAME || oper==WeaselOperator.NOT_VERY_SAME
					 || oper==WeaselOperator.EQUAL || oper==WeaselOperator.NOT_EQUAL){
			wcr = compileInfixOperator(compiler, compilerHelper, null, expect, null, false, i-1);
			instructions.addAll(wcr.instructions);
			wgc = wcr.returnType;
			wcr = level.get(i+1).compile(compiler, compilerHelper, null, expect, null, false);
			ret = wcr.returnType;
			if(wgc.getBaseClass().isPrimitive()||ret.getBaseClass().isPrimitive())
				wgc = WeaselTree.autoCast(compiler, wgc, ret, operator.line, instructions, false);
			instructions.addAll(wcr.instructions);
			if(wgc.getBaseClass().isPrimitive()||ret.getBaseClass().isPrimitive())
				ret = WeaselTree.autoCast(compiler, ret, wgc, operator.line, instructions, true);
			int primitiveID = WeaselPrimitive.getPrimitiveID(ret.getBaseClass());
			if(oper==WeaselOperator.VERY_SAME){
				instructions.add(operator.line, new WeaselInstructionVerySame(primitiveID));
			}else if(oper==WeaselOperator.NOT_VERY_SAME){
				instructions.add(operator.line, new WeaselInstructionNotVerySame(primitiveID));
			}else if(oper==WeaselOperator.EQUAL){
				instructions.add(operator.line, new WeaselInstructionEqual(primitiveID));
			}else if(oper==WeaselOperator.NOT_EQUAL){
				instructions.add(operator.line, new WeaselInstructionNotEqual(primitiveID));
			}
			ret = new WeaselGenericClass(compiler.baseTypes.booleanClass);
		}else if(oper==WeaselOperator.ELEMENT){
			wcr = compileInfixOperator(compiler, compilerHelper, null, null, null, false, i-1);
			instructions.addAll(wcr.instructions);
			wgc = wcr.returnType;
			wcr = level.get(i+1).compile(compiler, compilerHelper, write, expect, wgc, !wcr.isClassAccess);
			ret = wcr.returnType;
			instructions.addAll(wcr.instructions);
		}else{
			throw new WeaselCompilerException(operator.line, "Unknown operator %s", operator);
		}
		return new WeaselCompilerReturn(instructions, ret);
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
