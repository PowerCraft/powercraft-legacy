package weasel.compiler.keywords;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import weasel.compiler.WeaselBlockInfo;
import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerReturn;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.compiler.v2.tokentree.WeaselTree;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionJumperDummy;
import weasel.interpreter.bytecode.WeaselInstructionPop;
import weasel.interpreter.bytecode.WeaselInstructionPops;
import weasel.interpreter.bytecode.WeaselInstructionReservate;

public class WeaselKeyWordCompilerFor extends WeaselKeyWordCompiler {

	@Override
	public WeaselCompilerReturn compile(WeaselToken token, WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelpher, ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		expect(iterator.next(), WeaselTokenType.OPENBRACKET);
		compilerHelpher.openBlock(false);
		WeaselTree tree1 = WeaselTree.parse(iterator, WeaselTokenType.SEMICOLON);
		WeaselTree tree2 = WeaselTree.parse(iterator, WeaselTokenType.SEMICOLON);
		if(tree2==null){
			throw new WeaselCompilerException(token.line, "Expect boolean value in secound part of for");
		}
		WeaselTree tree3 = WeaselTree.parse(iterator, WeaselTokenType.CLOSEBRACKET);
		
		WeaselToken t = iterator.next();
		List<WeaselInstruction> instructions;
		WeaselCompilerReturn wcr;
		if(tree1==null){
			instructions = new ArrayList<WeaselInstruction>();
			instructions.add(new WeaselInstructionJumperDummy());
		}else{
			wcr = tree1.compile(compiler, compilerHelpher, null, new WeaselGenericClass(compiler.baseTypes.voidClass), null, false);
			instructions = wcr.instructions;
			if(wcr.returnType.getBaseClass()!=compiler.baseTypes.voidClass)
				instructions.add(new WeaselInstructionPop());
		}
		wcr = tree2.compile(compiler, compilerHelpher, null, new WeaselGenericClass(compiler.baseTypes.voidClass), null, false);
		
		WeaselInstruction startJump = instructions.get(instructions.size()-1);
		if(t.tokenType==WeaselTokenType.OPENBLOCK){
			instructions = new ArrayList<WeaselInstruction>();
			t = iterator.next();
			while(t.tokenType!=WeaselTokenType.CLOSEBLOCK){
				iterator.previous();
				instructions.addAll(WeaselTree.parseAndCompile(compiler, compilerHelpher, iterator));
				t = iterator.next();
			}
		}else{
			iterator.previous();
			instructions =  WeaselTree.parseAndCompile(compiler, compilerHelpher, iterator);
		}
		WeaselBlockInfo wbi = compilerHelpher.closeBlock();
		int pops = wbi.varsToPop();
		if(pops==1){
			instructions.add(new WeaselInstructionPop());
		}else if(pops>1){
			instructions.add(new WeaselInstructionPops(pops));
		}
		if(pops>=1){
			instructions.add(0, new WeaselInstructionReservate(pops));
		}
		return instructions;
		
		return null;
	}

}
