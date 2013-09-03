package weasel.compiler.keywords;

import java.util.ListIterator;

import weasel.compiler.WeaselBlockInfo;
import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerReturn;
import weasel.compiler.WeaselCompilerReturnInstructionList;
import weasel.compiler.WeaselInstructionList;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.compiler.v2.tokentree.WeaselTree;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionIf;
import weasel.interpreter.bytecode.WeaselInstructionJump;
import weasel.interpreter.bytecode.WeaselInstructionJumperDummy;
import weasel.interpreter.bytecode.WeaselInstructionPop;
import weasel.interpreter.bytecode.WeaselInstructionPops;
import weasel.interpreter.bytecode.WeaselInstructionReservate;

public class WeaselKeyWordCompilerWhile extends WeaselKeyWordCompiler {

	@Override
	public WeaselCompilerReturn compile(WeaselToken token, WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelpher, ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		expect(iterator.next(), WeaselTokenType.OPENBRACKET);
		compilerHelpher.openBlock(true);
		WeaselTree tree1 = WeaselTree.parse(iterator, WeaselTokenType.CLOSEBRACKET);
		if(tree1==null){
			throw new WeaselCompilerException(token.line, "Expect boolean value in while");
		}
		WeaselToken t = iterator.next();
		WeaselCompilerReturn wcr;
		WeaselInstructionList instructions = new WeaselInstructionList();
		WeaselInstruction continueJump = new WeaselInstructionJumperDummy();
		instructions.add(token.line, continueJump);
		wcr = tree1.compile(compiler, compilerHelpher, null, new WeaselGenericClass(compiler.baseTypes.booleanClass), null, false);
		instructions.addAll(wcr.getInstructions());
		WeaselTree.autoCast(compiler, wcr.getReturnType(), new WeaselGenericClass(compiler.baseTypes.booleanClass), token.line, instructions, true);
		WeaselInstructionIf ifI;
		instructions.add(token.line, ifI = new WeaselInstructionIf());
		if(t.tokenType==WeaselTokenType.OPENBLOCK){
			t = iterator.next();
			while(t.tokenType!=WeaselTokenType.CLOSEBLOCK){
				iterator.previous();
				instructions.addAll(WeaselTree.parseAndCompile(compiler, compilerHelpher, iterator));
				t = iterator.next();
			}
		}else{
			iterator.previous();
			instructions.addAll(WeaselTree.parseAndCompile(compiler, compilerHelpher, iterator));
		}
		WeaselInstruction ifJump = new WeaselInstructionJump(continueJump);
		instructions.add(token.line, ifJump);
		ifI.setTarget(ifJump);
		WeaselBlockInfo wbi = compilerHelpher.closeBlock();
		int pops = wbi.varsToPop();
		if(pops==1){
			instructions.add(token.line, new WeaselInstructionPop());
		}else if(pops>1){
			instructions.add(token.line, new WeaselInstructionPops(pops));
		}
		if(pops>=1){
			instructions.add(t.line, new WeaselInstructionReservate(pops));
		}
		WeaselInstruction breakJump = instructions.getLast();
		for(WeaselInstructionJump breakI:wbi.breaks){
			breakI.setTarget(breakJump);
		}
		for(WeaselInstructionJump continueI:wbi.continues){
			continueI.setTarget(continueJump);
		}
		return new WeaselCompilerReturnInstructionList(instructions, new WeaselGenericClass(compiler.baseTypes.voidClass));
	}

}
