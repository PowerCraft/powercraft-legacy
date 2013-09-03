package weasel.compiler.v2.tokentree;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerReturn;
import weasel.compiler.WeaselInstructionList;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstInteger;
import weasel.interpreter.bytecode.WeaselInstructionNewArray;
import weasel.interpreter.bytecode.WeaselInstructionPush;
import weasel.interpreter.bytecode.WeaselInstructionWriteIndex;

public class WeaselArrayInit {

	private WeaselToken token;
	private List<Object> indexes = new ArrayList<Object>();
	
	public WeaselArrayInit(ListIterator<WeaselToken> iterator, int arrays) throws WeaselCompilerException {
		boolean canComeArray = arrays>1;
		WeaselToken token = iterator.next();
		this.token = token;
		if(token.tokenType!=WeaselTokenType.CLOSEBLOCK){
			iterator.previous();
			while(true){
				token = iterator.next();
				if(token.tokenType==WeaselTokenType.OPENBLOCK){
					if(canComeArray)
						throw new WeaselCompilerException(token.line, "Too many {");
					indexes.add(new WeaselArrayInit(iterator, arrays-1));
					token = iterator.next();
					if(token.tokenType==WeaselTokenType.CLOSEBLOCK)
						break;
					if(token.tokenType!=WeaselTokenType.COMMA)
						throw new WeaselCompilerException(token.line, "Expect } but gto %s", token);
				}else{
					iterator.previous();
					indexes.add(WeaselTree.parse(iterator, WeaselTokenType.COMMA, WeaselTokenType.CLOSEBLOCK));
					iterator.previous();
					token = iterator.next();
					if(token.tokenType==WeaselTokenType.CLOSEBLOCK)
						break;
				}
			}
		}
	}

	@Override
	public String toString() {
		String s = "{";
		if(indexes.size()>0){
			s += indexes.get(0);
			for(int i=1; i<indexes.size(); i++){
				s += ", "+indexes.get(i);
			}
		}
		return s + "}";
	}

	public WeaselInstructionList compile(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelper, WeaselGenericClass arrayClass) throws WeaselCompilerException {
		arrayClass = new WeaselGenericClass(arrayClass.getBaseClass().getArrayClass(), arrayClass.getGenerics());
		WeaselInstructionList instructions = new WeaselInstructionList();
		instructions.add(token.line, new WeaselInstructionLoadConstInteger(indexes.size()));
		instructions.add(token.line, new WeaselInstructionNewArray(arrayClass.getBaseClass().getByteName(), 1));
		for(int i=0; i<indexes.size(); i++){
			Object o = indexes.get(i);
			if(o instanceof WeaselArrayInit){
				instructions.addAll(((WeaselArrayInit)o).compile(compiler, compilerHelper, arrayClass));
			}else{
				WeaselCompilerReturn wcr = ((WeaselTree)o).compile(compiler, compilerHelper, null, arrayClass, null, false);
				if(!wcr.getReturnType().canCastTo(arrayClass))
					throw new WeaselCompilerException(token.line, "Can't cast %s to %s", wcr.getReturnType(), arrayClass);
				instructions.addAll(wcr.getInstructions());
			}
			instructions.add(token.line, new WeaselInstructionPush(2));
			instructions.add(token.line, new WeaselInstructionLoadConstInteger(i));
			instructions.add(token.line, new WeaselInstructionWriteIndex(WeaselPrimitive.getPrimitiveID(arrayClass.getBaseClass())));
		}
		return instructions;
	}
	
}
