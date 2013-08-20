package weasel.compiler.keywords;

import java.util.ArrayList;
import java.util.List;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerReturn;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;

public class WeaselKeyWordCompilerNew extends WeaselKeyWordCompiler {

	@Override
	public WeaselCompilerReturn compile(WeaselToken token, WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelpher,  WeaselTokenType statementEnd) throws WeaselCompilerException {
		expect(token = compilerHelpher.getNextToken(), WeaselTokenType.IDENT);
		String className = (String)token.param;
		token = compilerHelpher.getNextToken();
		List<Integer> arraySize = new ArrayList<Integer>();
		while(token.tokenType==WeaselTokenType.OPENINDEX){
			token = compilerHelpher.getNextToken();
			if(token.tokenType==WeaselTokenType.INTEGER){
				arraySize.add((Integer)token.param);
				token = compilerHelpher.getNextToken();
			}else{
				arraySize.add(null);
			}
			expect(token, WeaselTokenType.CLOSEINDEX);
			token = compilerHelpher.getNextToken();
			className = "["+className;
		}
		if(arraySize.isEmpty()){
			expect(token, WeaselTokenType.OPENBRACKET);
		}else{
			
		}
		return null;
	}

}
