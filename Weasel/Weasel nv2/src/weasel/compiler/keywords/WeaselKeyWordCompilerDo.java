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
import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionIf;
import weasel.interpreter.bytecode.WeaselInstructionJump;
import weasel.interpreter.bytecode.WeaselInstructionJumperDummy;
import weasel.interpreter.bytecode.WeaselInstructionPop;
import weasel.interpreter.bytecode.WeaselInstructionPops;
import weasel.interpreter.bytecode.WeaselInstructionReservate;

public class WeaselKeyWordCompilerDo extends WeaselKeyWordCompiler {

	@Override
	public WeaselCompilerReturn compile(WeaselToken token, WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelpher, ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		compilerHelpher.openBlock(true);
		WeaselToken t = iterator.next();
		WeaselInstructionList instructions = new WeaselInstructionList();
		WeaselInstructionJumperDummy startJump = new WeaselInstructionJumperDummy();
		instructions.add(token.line, startJump);
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
		t = iterator.next();
		if(t.tokenType!=WeaselTokenType.KEYWORD || t.param!=WeaselKeyWord.WHILE){
			throw new WeaselCompilerException(t.line, "expect while but got %s", t);
		}
		WeaselInstruction continueJump = instructions.getLast();
		expect(t = iterator.next(), WeaselTokenType.OPENBRACKET);
		WeaselTree tree1 = WeaselTree.parse(iterator, WeaselTokenType.CLOSEBRACKET);
		if(tree1==null){
			throw new WeaselCompilerException(t.line, "Expect boolean value in while");
		}
		WeaselCompilerReturn wcr;
		wcr = tree1.compile(compiler, compilerHelpher, null, new WeaselGenericClass(compiler.baseTypes.booleanClass), null, false);
		instructions.addAll(wcr.instructions);
		WeaselTree.autoCast(compiler, wcr.returnType, new WeaselGenericClass(compiler.baseTypes.booleanClass), t.line, instructions, true);
		WeaselInstruction ifJump = new WeaselInstructionJump(startJump);
		instructions.add(t.line, new WeaselInstructionIf(ifJump));
		instructions.add(t.line, ifJump);
		WeaselBlockInfo wbi = compilerHelpher.closeBlock();
		int pops = wbi.varsToPop();
		if(pops==1){
			instructions.add(t.line, new WeaselInstructionPop());
		}else if(pops>1){
			instructions.add(t.line, new WeaselInstructionPops(pops));
		}
		if(pops>=1){
			instructions.addFirst(token.line, new WeaselInstructionReservate(pops));
		}
		WeaselInstruction breakJump = instructions.getLast();
		for(WeaselInstructionJump breakI:wbi.breaks){
			breakI.setTarget(breakJump);
		}
		for(WeaselInstructionJump continueI:wbi.continues){
			continueI.setTarget(continueJump);
		}
		return new WeaselCompilerReturn(instructions, new WeaselGenericClass(compiler.baseTypes.voidClass));
	}

}
