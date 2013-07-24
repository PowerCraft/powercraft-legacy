package weasel.compiler;

import java.util.ArrayList;
import java.util.List;

import weasel.compiler.keywords.WeaselKeyWord;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselModifier;

public class WeaselClassCompiler extends WeaselClass {
	
	private final WeaselCompiler weaselCompiler;
	private WeaselTokenParser tokenParser;
	private List<WeaselToken> nextTokens = new ArrayList<WeaselToken>();
	private List<WeaselCompilerException> error = new ArrayList<WeaselCompilerException>();
	
	public WeaselClassCompiler(WeaselCompiler weaselCompiler, String className, String source) {
		super(weaselCompiler, null, className);
		this.weaselCompiler = weaselCompiler;
		tokenParser = new WeaselTokenParser(source);
		try {
			modifier = readModifier(WeaselModifier.PUBLIC | WeaselModifier.FIANL);
		} catch (WeaselSyntaxError e) {
			error.add(e);
			e.printStackTrace();
			modifier = WeaselModifier.PUBLIC;
		}
		try{
			WeaselToken token = expectToken(WeaselTokenType.KEYWORD, WeaselKeyWord.CLASS, WeaselKeyWord.ENUM, WeaselKeyWord.INTERFACE);
			if(token.param==WeaselKeyWord.CLASS){
				
			}else if(token.param==WeaselKeyWord.ENUM){
				
			}else if(token.param==WeaselKeyWord.INTERFACE){
				
			}
			String scriptClassName = (String) expectToken(WeaselTokenType.IDENT).param;
			
		}catch(WeaselSyntaxError e){
			error.add(e);
			e.printStackTrace();
		}
	}

	public void preCompiling() {
		try{
			weaselCompiler.classesToPreCompile.remove(this);
			WeaselToken token = nextToken();
			weaselCompiler.classesToCompile.add(this);
		}catch(WeaselCompilerException e){
			error.add(e);
		}
	}

	public void compiling() {
		weaselCompiler.classesToCompile.remove(this);
		
		
		
	}

	public void setNextToken(WeaselToken token){
		nextTokens.add(0, token);
	}
	
	public WeaselToken expectToken(WeaselTokenType tt) throws WeaselSyntaxError{
		WeaselToken token = nextToken();
		if(token.tokenType==tt)
			return token;
		throw new WeaselSyntaxError(token.line, "Illegal token %s, only %s is permitted", token, tt.symbol);
	}
	
	public WeaselToken expectToken(WeaselTokenType tt, Object...datas) throws WeaselSyntaxError{
		
		WeaselToken token = nextToken();
		if(token.tokenType==tt){
			for(int i=0; i<datas.length; i++){
				if(token.param.equals(datas[i]))
					return token;
			}
		}
		String allowed = "";
		if(datas.length==1){
			allowed = datas[0].toString();
		}else if(datas.length>1){
			for(int i=0; i<datas.length-2; i++){
				allowed += datas[i].toString() + ", ";
			}
			allowed += datas[datas.length-2].toString() + " & ";
			allowed += datas[datas.length-1].toString();
		}
		throw new WeaselSyntaxError(token.line, "Illegal %s, only %s are permitted", token, allowed);
	}
	
	public int readModifier(int allowed) throws WeaselSyntaxError{
		WeaselToken token = nextToken();
		int modifier = 0;
		while(token.tokenType==WeaselTokenType.MODIFIER){
			int m = (Integer) token.param;
			int notAllowed = m^(allowed & m);
			if(notAllowed!=0){
				throw new WeaselSyntaxError(token.line, "Illegal modifier %s, only %s are permitted", WeaselModifierMap.getModifierName(notAllowed), WeaselModifierMap.getModifierName(allowed));
			}
			if((modifier & m)!=0){
				throw new WeaselSyntaxError(token.line, "Duplicate modifier %s", WeaselModifierMap.getModifierName(modifier & m));
			}
			modifier |= m;
			token = nextToken();
		}
		setNextToken(token);
		return modifier;
	}
	
	public WeaselToken nextToken() throws WeaselSyntaxError{
		if(nextTokens.isEmpty())
			return tokenParser.getNextToken();
		return nextTokens.remove(0);
	}
	
}
