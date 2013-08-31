package weasel.compiler.keywords;

import java.util.ListIterator;

import weasel.compiler.WeaselBlockInfo;
import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerReturn;
import weasel.compiler.WeaselInstructionList;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.compiler.v2.tokentree.WeaselTree;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.bytecode.WeaselInstructionIf;
import weasel.interpreter.bytecode.WeaselInstructionJump;
import weasel.interpreter.bytecode.WeaselInstructionPop;
import weasel.interpreter.bytecode.WeaselInstructionPops;
import weasel.interpreter.bytecode.WeaselInstructionReservate;

public class WeaselKeyWordCompilerIf extends WeaselKeyWordCompiler {

	@Override
	public WeaselCompilerReturn compile(WeaselToken token, WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelpher, ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		expect(iterator.next(), WeaselTokenType.OPENBRACKET);
		WeaselTree tree = WeaselTree.parse(iterator, WeaselTokenType.CLOSEBRACKET);
		if(tree==null)
			throw new WeaselCompilerException(token.line, "Condition need to be a boolean value");
		WeaselCompilerReturn wcr = tree.compile(compiler, compilerHelpher, null, new WeaselGenericClass(compiler.baseTypes.booleanClass), null, false);
		WeaselInstructionList instructions = wcr.instructions;
		WeaselTree.autoCast(compiler, wcr.returnType, new WeaselGenericClass(compiler.baseTypes.booleanClass), token.line, instructions, true);
		WeaselInstructionJump j1;
		instructions.add(token.line, j1 = new WeaselInstructionIf());
		instructions.addAll(compileBlock(compiler, compilerHelpher, iterator));
		WeaselToken t = iterator.next();
		if(t.tokenType==WeaselTokenType.KEYWORD && t.param == WeaselKeyWord.ELSE){
			WeaselInstructionJump j2;
			instructions.add(t.line, j2 = new WeaselInstructionJump());
			j1.setTarget(j2);
			instructions.addAll(compileBlock(compiler, compilerHelpher, iterator));
			j2.setTarget(instructions.getLast());
		}else{
			iterator.previous();
			j1.setTarget(instructions.getLast());
		}
		return new WeaselCompilerReturn(instructions, new WeaselGenericClass(compiler.baseTypes.voidClass));
	}

	private WeaselInstructionList compileBlock(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelpher, ListIterator<WeaselToken> iterator) throws WeaselCompilerException{
		compilerHelpher.openBlock(false);
		WeaselToken token = iterator.next();
		WeaselInstructionList instructions;
		if(token.tokenType==WeaselTokenType.OPENBLOCK){
			instructions = new WeaselInstructionList();
			token = iterator.next();
			while(token.tokenType!=WeaselTokenType.CLOSEBLOCK){
				iterator.previous();
				instructions.addAll(WeaselTree.parseAndCompile(compiler, compilerHelpher, iterator));
				token = iterator.next();
			}
		}else{
			iterator.previous();
			instructions =  WeaselTree.parseAndCompile(compiler, compilerHelpher, iterator);
		}
		WeaselBlockInfo wbi = compilerHelpher.closeBlock();
		int pops = wbi.varsToPop();
		if(pops==1){
			instructions.add(token.line, new WeaselInstructionPop());
		}else if(pops>1){
			instructions.add(token.line, new WeaselInstructionPops(pops));
		}
		if(pops>=1){
			instructions.add(token.line, new WeaselInstructionReservate(pops));
		}
		return instructions;
	}
	
}
