package weasel.compiler.keywords;

import java.util.ListIterator;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerReturn;
import weasel.compiler.WeaselInstructionList;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.compiler.v2.tokentree.WeaselTree;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselGenericMethod2;
import weasel.interpreter.bytecode.WeaselInstructionReturn;
import weasel.interpreter.bytecode.WeaselInstructionReturnNull;

public class WeaselKeyWordCompilerReturn extends WeaselKeyWordCompiler {

	@Override
	public WeaselCompilerReturn compile(WeaselToken token, WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelpher, ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		WeaselGenericMethod2 method = compilerHelpher.getCompilingMethod();
		WeaselGenericClass retClass = method.getGenericReturn();
		WeaselInstructionList instructions;
		if(retClass.getBaseClass()==compiler.baseTypes.voidClass){
			expect(iterator.next(), WeaselTokenType.SEMICOLON);
			instructions = new WeaselInstructionList();
			instructions.add(token.line, new WeaselInstructionReturnNull(compilerHelpher.getVarCount()));
		}else{
			WeaselTree tree = WeaselTree.parse(iterator, WeaselTokenType.SEMICOLON);
			if(tree==null){
				throw new WeaselCompilerException(token.line, "return need to return %s", retClass);
			}
			WeaselCompilerReturn wcr = tree.compile(compiler, compilerHelpher, null, retClass, null, false);
			instructions = wcr.instructions;
			WeaselTree.autoCast(compiler, wcr.returnType, retClass, token.line, instructions, true);
			instructions.add(token.line, new WeaselInstructionReturn(compilerHelpher.getVarCount()));
		}
		return new WeaselCompilerReturn(instructions, new WeaselGenericClass(compiler.baseTypes.voidClass));
	}

}
