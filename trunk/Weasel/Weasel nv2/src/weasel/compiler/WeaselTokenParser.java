package weasel.compiler;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import weasel.compiler.WeaselOperator.Properties;
import weasel.compiler.keywords.WeaselKeyWord;
import weasel.interpreter.WeaselModifier;

public class WeaselTokenParser {

	private String source;
	private int pos;
	private int line;
	private boolean isEOFOk=true;
	private List<WeaselToken> firstTokens = new ArrayList<WeaselToken>();
	
	public WeaselTokenParser(String source){
		this.source = source;
		pos = 0;
		line = 1;
	}
	
	public void setNextToken(WeaselToken token){
		firstTokens.add(0, token);
	}
	
	public WeaselToken getNextToken() throws WeaselCompilerException{
		if(!firstTokens.isEmpty())
			return firstTokens.remove(0);
		isEOFOk=true;
		try{
			char c = skipWhiteSpace();
			if(isAlphabetical(c)){
				isEOFOk = false;
				String ident="";
				while(isAlphabetical(c)||isDigit(c)){
					ident += c;
					c = readNextChar();
				}
				readPrevChar();
				if(ident.equals("true")){
					return new WeaselToken(WeaselTokenType.BOOL, line, true);
				}else if(ident.equals("false")){
					return new WeaselToken(WeaselTokenType.BOOL, line, false);
				}else if(ident.equals("null")){
					return new WeaselToken(WeaselTokenType.NULL, line, null);
				}
				int modifier = WeaselModifier.getModifier(ident);
				if(modifier!=0){
					return new WeaselToken(WeaselTokenType.MODIFIER, line, modifier);
				}
				WeaselKeyWord wkw = WeaselKeyWord.getWeaselKeyWord(ident);
				if(wkw!=null){
					return new WeaselToken(WeaselTokenType.KEYWORD, line, wkw);
				}
				return new WeaselToken(WeaselTokenType.IDENT, line, ident);
			}
			if(isDigit(c) || c=='.'){
				isEOFOk = false;
				boolean isDot = c=='.';
				int num=0;
				if(c=='0'){
					c = readNextChar();
					if(c=='x'){
						c = readNextChar();
						if(!(isDigit(c) || (c>='a' && c<='f') || (c>='A' && c<='F')))
							throw new WeaselCompilerException(line, "Expect hex but got "+c);
						while(true) {
							if(isDigit(c)){
								num<<=4;
								num+=c-'0';
							}else if(c>='a' && c<='f'){
								num<<=4;
								num+=c-'a'+10;
							}else if(c>='A' && c<='F'){
								num<<=4;
								num+=c-'A'+10;
							}else if(isAlphabetical(c)){
								throw new WeaselCompilerException(line, "Expect hex but got "+c);
							}else{
								break;
							}
							c = readNextChar();
						}
						readPrevChar();
						return new WeaselToken(WeaselTokenType.INTEGER, line, num);
					}else if(c=='b'){
						c = readNextChar();
						if(!(c=='0' || c=='1'))
							throw new WeaselCompilerException(line, "Expect bit but got "+c);
						while(true) {
							if(c=='0' || c=='1'){
								num<<=1;
								if(c=='1')
									num++;
							}else if(isDigit(c)||isAlphabetical(c)){
								throw new WeaselCompilerException(line, "Expect bit but got "+c);
							}else{
								break;
							}
							c = readNextChar();
						}
						readPrevChar();
						return new WeaselToken(WeaselTokenType.INTEGER, line, num);
					}
				}
				while(isDigit(c)){
					num *= 10;
					num += c-'0';
					c = readNextChar();
				}
				if(c=='.'){
					c = readNextChar();
					if(!isDot || isDigit(c)){
						int d=0;
						int i=1;
						while(isDigit(c)){
							d += c-'0';
							i *= 10;
							c = readNextChar();
						}
						readPrevChar();
						return new WeaselToken(WeaselTokenType.DOUBLE, line, num+(double)d/i);
					}
					c = readPrevChar();
				}else if(isAlphabetical(c)){
					throw new WeaselCompilerException(line, "Expect number but got "+c);
				}else{
					readPrevChar();
					return new WeaselToken(WeaselTokenType.INTEGER, line, num);
				}
			}
			isEOFOk = true;
			if(c=='"'){
				isEOFOk = false;
				c = readNextChar();
				String s="";
				while(c!='"'){
					if(c=='\\'){
						c = readNextChar();
						switch(c){
						case 'n':
							s+='\n';
							break;
						case 't':
							s+='\t';
							break;
						case '\\':
							s+='\\';
							break;
						default:
							throw new WeaselCompilerException(line, "Expect \n, \t or \\");
						}
					}else{
						s += c;
					}
					c = readNextChar();
				}
				return new WeaselToken(WeaselTokenType.STRING, line, s);
			}
			WeaselTokenType wtt = WeaselTokenType.getTokenTypeFor(""+c, false);
			if(wtt!=null){
				return new WeaselToken(wtt, line);
			}
			Properties lastFullEqual = null;
			boolean again = true;
			String s="";
			int back=0;
			isEOFOk = false;
			while(again){
				again = false;
				s+=c;
				for(Entry<String, Properties> wo:WeaselOperator.operators.entrySet()){
					if(wo.getKey().equals(s)){
						lastFullEqual = wo.getValue();
						back = 0;
					}else if(wo.getKey().startsWith(s)){
						again = true;
					}
				}
				back++;
				c=readNextChar();
			}
			isEOFOk = true;
			while(back>0){
				back--;
				c = readPrevChar();
			}
			if(lastFullEqual!=null){
				return new WeaselToken(WeaselTokenType.OPERATOR, line, lastFullEqual);
			}
			wtt = WeaselTokenType.getTokenTypeFor(""+c, true);
			if(wtt!=null){
				return new WeaselToken(wtt, line);
			}
			throw new WeaselCompilerException(line, "Unexpected char "+c);
		}catch(EOFException e){
			if(isEOFOk){
				return new WeaselToken(WeaselTokenType.NONE, line);
			}
			throw new WeaselCompilerException(line, "Unexpect end of file");
		}
	}
	
	private boolean isSpace(char c){
		return c==' '||c=='\t'||c=='\r'||c=='\n';
	}
	
	private boolean isDigit(char c){
		return c>='0' && c<='9';
	}
	
	private boolean isAlphabetical(char c){
		return (c>='a' && c<='z') || (c>='A' && c<='Z') || c=='_';
	}
	
	private char skipWhiteSpace() throws EOFException{
		int length = source.length();
		if(length<=pos){
			throw new EOFException();
		}
		char c = source.charAt(pos);
		pos++;
		while(isSpace(c)){
			if(c=='\n')
				line++;
			if(length<=pos){
				throw new EOFException();
			}
			c = source.charAt(pos);
			pos++;
		}
		if(c=='/'){
			c = readNextChar();
			if(c=='/'){
				while(readNextChar()!='\n');
				return skipWhiteSpace();
			}else if(c=='*'){
				isEOFOk=false;
				while(true){
					c=readNextChar();
					if(c=='*'){
						c=readNextChar();
						if(c=='/')
							break;
					}
				}
				isEOFOk=true;
				return skipWhiteSpace();
			}else{
				c = readPrevChar();
			}
		}
		return c;
	}
	
	private char readNextChar() throws EOFException{
		int length = source.length();
		if(length<=pos){
			throw new EOFException();
		}
		char c = source.charAt(pos);
		pos++;
		if(c=='\n')
			line++;
		return c;
	}
	
	private char readPrevChar() throws EOFException{
		pos--;
		if(pos<=0){
			throw new EOFException();
		}
		char c = source.charAt(pos);
		if(c=='\n')
			line--;
		pos--;
		c = source.charAt(pos);
		pos++;
		return c;
	}

	public int getLine() {
		return line;
	}

	public ListIterator<WeaselToken> listIterator() {
		
		return new ListIterator<WeaselToken>(){

			private List<WeaselToken> readed = new ArrayList<WeaselToken>();
			private int index=0;
			
			@Override
			public void add(WeaselToken e) {
				setNextToken(e);
			}

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public boolean hasPrevious() {
				return !readed.isEmpty();
			}

			@Override
			public WeaselToken next() {
				if(readed.size()>index){
					return readed.get(index++);
				}
				WeaselToken wt = null;
				try {
					wt = getNextToken();
				} catch (WeaselCompilerException e) {
					e.printStackTrace();
				}
				readed.add(wt);
				index++;
				return wt;
			}

			@Override
			public int nextIndex() {
				return index;
			}

			@Override
			public WeaselToken previous() {
				if(index==0)
					return null;
				return readed.get(--index);
			}

			@Override
			public int previousIndex() {
				return index-1;
			}

			@Override
			public void remove() {}

			@Override
			public void set(WeaselToken e) {}
		};
	}
	
}
