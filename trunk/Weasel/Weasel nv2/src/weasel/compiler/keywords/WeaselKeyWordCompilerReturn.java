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
import weasel.interpreter.WeaselGenericMethod2;
import weasel.interpreter.WeaselMethod;
import weasel.interpreter.WeaselModifier;
import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionReturn;
import weasel.interpreter.bytecode.WeaselInstructionReturnNull;

public class WeaselKeyWordCompilerReturn extends WeaselKeyWordCompiler {

	@Override
	public WeaselCompilerReturn compile(WeaselToken token, WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelpher, ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		WeaselGenericMethod2 method = compilerHelpher.getCompilingMethod();
		WeaselMethod m = method.getMethod().getMethod();
		WeaselGenericClass retClass = method.getGenericReturn();
		List<WeaselInstruction> instructions;
		if(retClass.getBaseClass()==compiler.baseTypes.voidClass){
			expect(iterator.next(), WeaselTokenType.SEMICOLON);
			instructions = new ArrayList<WeaselInstruction>();
			instructions.add(new WeaselInstructionReturnNull(m.getParamClasses().length+(WeaselModifier.isStatic(m.getModifier())?0:1)));
		}else{
			WeaselTree tree = WeaselTree.parse(iterator, WeaselTokenType.SEMICOLON);
			if(tree==null){
				throw new WeaselCompilerException(token.line, "return need to return %s", retClass);
			}
			WeaselCompilerReturn wcr = tree.compile(compiler, compilerHelpher, null, retClass, null, false);
			instructions = wcr.instructions;
			WeaselTree.autoCast(compiler, wcr.returnType, retClass, token.line, instructions, true);
			instructions.add(new WeaselInstructionReturn(m.getParamClasses().length+(WeaselModifier.isStatic(m.getModifier())?0:1)));
		}
		return new WeaselCompilerReturn(instructions, new WeaselGenericClass(compiler.baseTypes.voidClass));
	}

}
