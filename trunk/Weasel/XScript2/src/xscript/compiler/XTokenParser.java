package xscript.compiler;

import java.util.ArrayList;
import java.util.List;


public class XTokenParser {

	private String source;
	private int pos;
	private int line = 1;
	private int linepos = 0;
	private int prevline = 1;
	private int prevlinepos = 0;
	private int startline = 1;
	private int startlinepos = 0;
	private char scannChar;
	private List<XComment> comments = null;
	private XMessageList messages;
	private XToken next;
	private boolean space;
	
	public XTokenParser(String source, XMessageList messages){
		this.source = source;
		scannChar();
	}
	
	private boolean charOkForRadix(int radix, char c){
		if(radix==2){
			return c=='0' || c=='1';
		}else if(radix==8){
			return c>='0' && c<='7';
		}else if(radix==10){
			return c>='0' && c<='9';
		}else if(radix==16){
			return (c>='a' && c<='f') || (c>='A' && c<='F') || (c>='0' && c<='9');
		}
		return false;
	}
	
	private String scannDigit(int radix){
		String number = "";
		while(charOkForRadix(radix, scannChar) || scannChar=='_'){
			if(scannChar!='_'){
				number += scannChar;
			}
			scannChar();
		}
		return number;
	}
	
	private XToken scannNumber(){
		int radix = 10;
		XTokenKind kind = XTokenKind.INTLITERAL;
		if(scannChar=='0'){
			scannChar();
			if(scannChar=='x' || scannChar=='X'){
				scannChar();
				radix = 16;
			}else if(scannChar=='b' || scannChar=='B'){
				scannChar();
				radix = 2;
			}
		}
		String number = scannDigit(radix);
		if(scannChar=='.' && radix==10){
			kind = XTokenKind.DOUBLELITERAL;
			scannChar();
			boolean nothing = number.isEmpty();
			if(nothing)
				number = "0";
			number += ".";
			String scann = scannDigit(radix);
			if(scann.isEmpty() && nothing){
				scann = "0";
				parserMessage(XMessageLevel.ERROR, "literal.single_dot");
			}
			number += scann;
		}
		if(radix==10 && (scannChar=='e' || scannChar=='E')){
			scannChar();
			number += "e";
			if(scannChar=='+' || scannChar=='-'){
				number += scannChar;
				scannChar();
			}
			String scann = scannDigit(radix);
			if(scann.isEmpty()){
				scann = "0";
				parserMessage(XMessageLevel.ERROR, "literal.empty_exponent");
			}
			number += scann;
		}
		if(scannChar=='f' || scannChar=='F'){
			kind = XTokenKind.FLOATLITERAL;
		}else if(scannChar=='d' || scannChar=='D'){
			kind = XTokenKind.DOUBLELITERAL;
		}else if(scannChar=='l' || scannChar=='L'){
			kind = XTokenKind.LONGLITERAL;
		}
		return makeToken(kind, (radix==2?"0b":radix==16?"0x":"")+number);
	}
	
	private XToken scannIdent(){
		String ident = "";
		while((scannChar>='A' && scannChar<='Z') || (scannChar>='a' && scannChar<='z') || scannChar=='_' || (scannChar>='0' && scannChar<='9')){
			ident += scannChar;
			scannChar();
		}
		XTokenKind keyword = XTokenKind.getKeyword(ident);
		if(keyword==null)
			return makeToken(XTokenKind.IDENT, ident);
		return makeToken(keyword);
	}
	
	private XToken scannString(boolean isChar){
		String string = "";
		char end = isChar?'\'':'"';
		while(scannChar!=0 && scannChar!=end){
			if(scannChar=='\\'){
				scannChar();
				if(scannChar=='\\'){
					string += '\\';
				}else if(scannChar=='\n'){
					string += '\n';
				}else if(scannChar=='\r'){
					string += '\r';
				}else if(scannChar=='\t'){
					string += '\t';
				}else if(scannChar=='\''){
					string += '\'';
				}else if(scannChar=='"'){
					string += '"';
				}
			}else{
				string += scannChar;
			}
			scannChar();
		}
		if(scannChar!=end){
			parserMessage(XMessageLevel.ERROR, isChar?"char.eof":"string.eof");
		}
		return makeToken(isChar?XTokenKind.CHARLITERAL:XTokenKind.STRINGLITERAL, string);
	}
	
	private void readLineComment(boolean multiline){
		scannChar();
		if(comments==null)
			comments = new ArrayList<XComment>();
		if(multiline){
			boolean doc = scannChar=='*';
			if(doc){
				scannChar();
			}
			String comment = "";
			while(scannChar!=0){
				if(scannChar=='*'){
					scannChar();
					if(scannChar=='/')
						break;
					comment += '*';
				}
				comment += scannChar;
				scannChar();
			}
			if(scannChar!='\n'){
				parserMessage(XMessageLevel.ERROR, "comment.eof");
			}
			comments.add(new XComment(doc?XCommentType.DOCMULTILINE:XCommentType.MULTILINE, comment));
		}else{
			String comment = "";
			while(scannChar!='\n' && scannChar!=0){
				comment += scannChar;
				scannChar();
			}
			if(scannChar!='\n'){
				parserMessage(XMessageLevel.ERROR, "comment.eof");
			}
			comments.add(new XComment(XCommentType.SINGLELINE, comment));
		}
	}
	
	public XToken readNextToken(){
		if(next!=null){
			XToken token = next;
			next = null;
			return token;
		}
		comments = null;
		space=false;
		while(scannChar!=0){
			setStartLineAndPos();
			if(scannChar>='0' && scannChar<='9'){
				return scannNumber();
			}else if((scannChar>='A' && scannChar<='Z') || (scannChar>='a' && scannChar<='z') || scannChar=='_'){
				return scannIdent();
			}else if(scannChar=='/'){
				scannChar();
				if(scannChar=='/' || scannChar=='*'){
					readLineComment(scannChar=='*');
				}else{
					return makeToken(XTokenKind.DIV);
				}
			}else if(scannChar=='"' || scannChar=='\''){
				scannChar();
				return scannString(scannChar=='\'');
			}else if(!(scannChar==' ' || scannChar=='\t' || scannChar=='\n' || scannChar=='\r')){
				XTokenKind tokenKind = XTokenKind.getCharToken(scannChar);
				if(tokenKind!=null){
					if(tokenKind==XTokenKind.AT){
						int sl = startline;
						int slp = startlinepos;
						scannChar();
						next = readNextToken();
						if(next.kind==XTokenKind.INTERFACE){
							next = null;
							tokenKind = XTokenKind.ANNOTATION;
						}
						return new XToken(tokenKind, new XLineDesk(sl, slp, prevline, prevlinepos), comments, space);
					}
					scannChar();
					return makeToken(tokenKind);
				}
				parserMessage(XMessageLevel.ERROR, "unknown.char");
			}
			scannChar();
			space = true;
		}
		return makeToken(XTokenKind.EOF);
		
	}
	
	private XToken makeToken(XTokenKind kind){
		return new XToken(kind, new XLineDesk(startline, startlinepos, prevline, prevlinepos), comments, space);
	}
	
	private XToken makeToken(XTokenKind kind, String param){
		return new XToken(kind, new XLineDesk(startline, startlinepos, prevline, prevlinepos), comments, param, space);
	}
	
	private void setStartLineAndPos(){
		startline = line;
		startlinepos = linepos;
	}
	
	private void parserMessage(XMessageLevel level, String key, Object...args){
		parserMessage(level, key, new XLineDesk(startline, startlinepos, line, linepos), args);
	}
	
	private void parserMessage(XMessageLevel level, String key, XLineDesk lineDesk, Object...args){
		messages.postMessage(level, "tokenparser."+key, lineDesk, args);
	}
	
	private char preview(int p){
		if(source.length()>pos+p){
			return source.charAt(pos+p);
		}
		return 0;
	}

	private void scannChar(){
		prevline = line;
		prevlinepos = linepos;
		if(source.length()>pos){
			scannChar = source.charAt(pos++);
			linepos++;
			if(scannChar=='\r'){
				char c = preview(1);
				if(c=='\n'){
					pos++;
				}
				line++;
				linepos=0;
				scannChar = '\n';
			}else if(scannChar=='\n'){
				line++;
				linepos=0;
			}
		}else{
			scannChar=0;
		}
	}
	
}
