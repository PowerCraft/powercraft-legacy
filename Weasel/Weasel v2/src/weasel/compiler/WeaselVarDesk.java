package weasel.compiler;

import weasel.interpreter.WeaselClass;

public class WeaselVarDesk {

	public final int modifier;
	public final WeaselClass wClass;
	public final int array;
	public final String var;
	public final WeaselToken varToken;
	
	public WeaselVarDesk(WeaselClassCompiler compiler) throws WeaselCompilerException{
		modifier = compiler.readModifier();
		WeaselToken token = compiler.expectToken(WeaselTokenType.IDENT);
		try{
			wClass = compiler.weaselCompiler.getClassByName((String)token.param);
		}catch(RuntimeException e){
			throw new WeaselCompilerException(token.line, e.getMessage());
		}
		array = compiler.readArray();
		var = (String) (varToken=compiler.expectToken(WeaselTokenType.IDENT)).param;
	}
	
	public WeaselVarDesk(WeaselClassCompiler compiler, int modifier, WeaselToken token) throws WeaselCompilerException{
		this.modifier = modifier;
		try{
			wClass = compiler.weaselCompiler.getClassByName((String)token.param);
		}catch(RuntimeException e){
			throw new WeaselCompilerException(token.line, e.getMessage());
		}
		array = compiler.readArray();
		var = (String) (varToken=compiler.expectToken(WeaselTokenType.IDENT)).param;
	}
	
}
