package weasel.compiler.keywords;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerReturn;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.compiler.v2.tokentree.WeaselTree;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionIf;
import weasel.interpreter.bytecode.WeaselInstructionJump;

public class WeaselKeyWordCompilerIf extends WeaselKeyWordCompiler {

	@Override
	public WeaselCompilerReturn compile(WeaselToken token, WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelpher, ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		expect(iterator.next(), WeaselTokenType.OPENBRACKET);
		WeaselTree tree = WeaselTree.parse(iterator, WeaselTokenType.CLOSEBRACKET);
		if(tree==null)
			throw new WeaselCompilerException(token.line, "Condition need to be a boolean value");
		WeaselCompilerReturn wcr = tree.compile(compiler, compilerHelpher, null, new WeaselGenericClass(compiler.baseTypes.booleanClass), null, false);
		List<WeaselInstruction> instructions = new ArrayList<WeaselInstruction>();
		WeaselTree.autoCast(compiler, wcr.returnType, new WeaselGenericClass(compiler.baseTypes.booleanClass), token.line, instructions, true);
		WeaselInstructionJump j1;
		instructions.add(j1 = new WeaselInstructionIf());
		instructions.addAll(compileBlock(compiler, compilerHelpher, iterator));
		WeaselToken t = iterator.next();
		if(t.tokenType==WeaselTokenType.KEYWORD && t.param == WeaselKeyWord.ELSE){
			WeaselInstructionJump j2;
			instructions.add(j2 = new WeaselInstructionJump());
			j1.setTarget(j2);
			instructions.addAll(compileBlock(compiler, compilerHelpher, iterator));
			j2.setTarget(instructions.get(instructions.size()-1));
		}else{
			iterator.previous();
			j1.setTarget(instructions.get(instructions.size()-1));
		}
		return new WeaselCompilerReturn(instructions, new WeaselGenericClass(compiler.baseTypes.voidClass));
	}

	private List<WeaselInstruction> compileBlock(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelpher, ListIterator<WeaselToken> iterator) throws WeaselCompilerException{
		WeaselToken token = iterator.next();
		if(token.tokenType==WeaselTokenType.OPENBLOCK){
			List<WeaselInstruction> instructions = new ArrayList<WeaselInstruction>();
			token = iterator.next();
			while(token.tokenType!=WeaselTokenType.CLOSEBLOCK){
				iterator.previous();
				instructions.addAll(WeaselTree.parseAndCompile(compiler, compilerHelpher, iterator));
				token = iterator.next();
			}
			return instructions;
		}else{
			iterator.previous();
			return WeaselTree.parseAndCompile(compiler, compilerHelpher, iterator);
		}
	}
	
}
