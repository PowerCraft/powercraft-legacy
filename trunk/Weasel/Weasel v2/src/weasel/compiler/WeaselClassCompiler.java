package weasel.compiler;

import java.util.ArrayList;
import java.util.List;

import weasel.compiler.keywords.WeaselKeyWord;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselModifier;

public class WeaselClassCompiler extends WeaselClass {
	
	public final WeaselCompiler weaselCompiler;
	private WeaselTokenParser tokenParser;
	private List<WeaselToken> nextTokens = new ArrayList<WeaselToken>();
	private List<WeaselCompilerException> error = new ArrayList<WeaselCompilerException>();
	
	public WeaselClassCompiler(WeaselCompiler weaselCompiler, String className, String source) {
		super(weaselCompiler, null, className);
		this.weaselCompiler = weaselCompiler;
		tokenParser = new WeaselTokenParser(source);
		try {
			modifier = readModifier();
		} catch (WeaselSyntaxError e) {
			error.add(e);
			e.printStackTrace();
			modifier = WeaselModifier.PUBLIC;
		}
		try{
			WeaselToken token = expectToken(WeaselTokenType.KEYWORD, WeaselKeyWord.CLASS, WeaselKeyWord.INTERFACE);
			if(token.param==WeaselKeyWord.CLASS){
				if((modifier & WeaselModifier.ABSTRACT)!=0){
					checkModifier(token, modifier, WeaselModifier.PUBLIC | WeaselModifier.ABSTRACT);
				}else{
					checkModifier(token, modifier, WeaselModifier.PUBLIC | WeaselModifier.FINAL);
				}
			}else if(token.param==WeaselKeyWord.INTERFACE){
				checkModifier(token, modifier, WeaselModifier.PUBLIC);
				modifier |= WeaselModifier.ABSTRACT;
				modifier |= WeaselModifier.INTERFACE;
			}
			expectToken(WeaselTokenType.IDENT, className);
			expectToken(WeaselTokenType.OPENBLOCK);
		}catch(WeaselSyntaxError e){
			error.add(e);
			e.printStackTrace();
		}
	}

	public void preCompiling() {
		try{
			weaselCompiler.classesToPreCompile.remove(this);
			WeaselToken token = nextToken();
			while(token.tokenType!=WeaselTokenType.CLOSEBLOCK){
				setNextToken(token);
				int modifier = readModifier();
				token = expectToken(WeaselTokenType.IDENT, WeaselTokenType.KEYWORD);
				if(token.tokenType==WeaselTokenType.KEYWORD){
					setNextToken(token);
				}else{
					WeaselVarDesk varDesk = new WeaselVarDesk(this, modifier, token);
					if((this.modifier & WeaselModifier.INTERFACE)!=0){
						token = expectToken(WeaselTokenType.OPENBRACKET);
					}else{
						token = nextToken();
					}
					if(token.tokenType==WeaselTokenType.OPENBRACKET){
						readMethod(varDesk);
					}else{
						setNextToken(token);
						readField(varDesk);
						expectToken(WeaselTokenType.SEMICOLON);
					}
				}
				token = nextToken();
			}
			expectToken(WeaselTokenType.NONE);
			weaselCompiler.classesToCompile.add(this);
		}catch(WeaselCompilerException e){
			e.printStackTrace();
			error.add(e);
		}
	}
	
	private void readMethod(WeaselVarDesk varDesk) throws WeaselSyntaxError{
		if((this.modifier & WeaselModifier.INTERFACE)!=0){
			checkModifier(varDesk.varToken, modifier, WeaselModifier.PUBLIC | WeaselModifier.ABSTRACT);
		}else{
			if((modifier & WeaselModifier.PRIVATE)!=0){
				checkModifier(varDesk.varToken, modifier, WeaselModifier.PRIVATE | WeaselModifier.FINAL | WeaselModifier.STATIC);
			}else if((modifier & WeaselModifier.PROTECTED)!=0){
				if((modifier & WeaselModifier.ABSTRACT)!=0 && (this.modifier & WeaselModifier.ABSTRACT)!=0)
					checkModifier(varDesk.varToken, modifier, WeaselModifier.PROTECTED | WeaselModifier.ABSTRACT);
				else
					checkModifier(varDesk.varToken, modifier, WeaselModifier.PROTECTED | WeaselModifier.FINAL | WeaselModifier.STATIC);
			}else{
				if((modifier & WeaselModifier.ABSTRACT)!=0 && (this.modifier & WeaselModifier.ABSTRACT)!=0)
					checkModifier(varDesk.varToken, modifier, WeaselModifier.PUBLIC | WeaselModifier.ABSTRACT);
				else
					checkModifier(varDesk.varToken, modifier, WeaselModifier.PUBLIC | WeaselModifier.FINAL | WeaselModifier.STATIC);
			}
		}
		
	}
	
	private void readField(WeaselVarDesk varDesk) throws WeaselSyntaxError{
		checkModifier(varDesk.varToken, modifier, WeaselModifier.PRIVATE | WeaselModifier.PROTECTED | WeaselModifier.PUBLIC | WeaselModifier.FINAL | WeaselModifier.STATIC);
		int arrayVar = readArray();
		
	}
	
	public void compiling() {
		weaselCompiler.classesToCompile.remove(this);
		
		
		
	}

	public void setNextToken(WeaselToken token){
		nextTokens.add(0, token);
	}
	
	public WeaselToken expectToken(WeaselTokenType ...tt) throws WeaselSyntaxError{
		WeaselToken token = nextToken();
		for(int i=0; i<tt.length; i++){
			if(token.tokenType==tt[i])
				return token;
		}
		String allowed = "";
		if(tt.length==1){
			allowed = tt[0].symbol;
		}else if(tt.length>1){
			for(int i=0; i<tt.length-2; i++){
				allowed += tt[i].symbol + ", ";
			}
			allowed += tt[tt.length-2].symbol + " & ";
			allowed += tt[tt.length-1].symbol;
		}
		throw new WeaselSyntaxError(token.line, "Illegal token %s, only %s are permitted", token, allowed);
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
	
	public int readArray() throws WeaselSyntaxError{
		WeaselToken token = nextToken();
		int array = 0;
		while(token.tokenType==WeaselTokenType.OPENINDEX){
			array++;
			expectToken(WeaselTokenType.CLOSEINDEX);
			token = nextToken();
		}
		setNextToken(token);
		return array;
	}
	
	public void checkModifier(WeaselToken token, int modifier, int allowed) throws WeaselSyntaxError{
		int notAllowed = modifier^(allowed & modifier);
		if(notAllowed!=0){
			throw new WeaselSyntaxError(token.line, "Illegal modifier %s, only %s are permitted", WeaselModifierMap.getModifierName(notAllowed), WeaselModifierMap.getModifierName(allowed));
		}
	}
	
	public int readModifier() throws WeaselSyntaxError{
		WeaselToken token = nextToken();
		int modifier = 0;
		while(token.tokenType==WeaselTokenType.MODIFIER){
			int m = (Integer) token.param;
			if((modifier & m)!=0){
				throw new WeaselSyntaxError(token.line, "Duplicate modifier %s", WeaselModifierMap.getModifierName(modifier & m));
			}
			if((modifier & (WeaselModifier.PRIVATE | WeaselModifier.PROTECTED | WeaselModifier.PUBLIC))!=0 && (m & (WeaselModifier.PRIVATE | WeaselModifier.PROTECTED | WeaselModifier.PUBLIC))!=0){
				throw new WeaselSyntaxError(token.line, "Illegal modifier %s", WeaselModifierMap.getModifierName(m & (WeaselModifier.PRIVATE | WeaselModifier.PROTECTED | WeaselModifier.PUBLIC)));
			}if((modifier & (WeaselModifier.ABSTRACT | WeaselModifier.FINAL))!=0 && (m & (WeaselModifier.ABSTRACT | WeaselModifier.FINAL))!=0){
				throw new WeaselSyntaxError(token.line, "Illegal modifier %s", WeaselModifierMap.getModifierName(m & (WeaselModifier.ABSTRACT | WeaselModifier.FINAL)));
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
