package weasel.compiler.keywords;

import java.util.ListIterator;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerReturn;
import weasel.compiler.WeaselInstructionList;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.interpreter.bytecode.WeaselInstructionJump;

public class WeaselKeyWordCompilerContinue extends WeaselKeyWordCompiler {

	@Override
	public WeaselCompilerReturn compile(WeaselToken token, WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelpher, ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		WeaselToken t = iterator.next();
		int s = 1;
		if(t.tokenType==WeaselTokenType.INTEGER){
			s = (Integer)t.param;
			if(s<=0)
				throw new WeaselCompilerException(t.line, "Negatives and 0 are not permitted");
			t = iterator.next();
		}
		WeaselInstructionList instructionList = new WeaselInstructionList();
		compilerHelpher.addClosingsAndFrees(s, instructionList, false);
		WeaselInstructionJump continueJump;
		instructionList.add(token.line, continueJump = new WeaselInstructionJump());
		compilerHelpher.addContinue(s, continueJump);
		expect(t, WeaselTokenType.SEMICOLON);
		return null;
	}

}
