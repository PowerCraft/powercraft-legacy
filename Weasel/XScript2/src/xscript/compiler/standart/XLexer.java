package xscript.compiler.standart;

import java.util.ArrayList;
import java.util.List;

import xscript.compiler.XMessageList;
import xscript.compiler.XToken;
import xscript.compiler.XTokenParser;

public class XLexer {

	private XTokenParser parser;
	private List<XToken> tokens;
	private int i;
	private boolean read;
	
	public XLexer(String source, XMessageList messages){
		parser = new XTokenParser(source, messages);
	}
	
	public XToken getNextToken(){
		if(read && tokens!=null){
			if(tokens.isEmpty()){
				tokens = null;
			}else{
				XToken token = tokens.remove(0);
				if(tokens.isEmpty())
					tokens = null;
				return token;
			}
		}
		if(!read && tokens!=null && tokens.size()>i){
			return tokens.get(i++);
		}
		XToken token = parser.readNextToken();
		if(!read && tokens!=null){
			tokens.add(token);
		}
		return token;
	}
	
	public void notSure(){
		if(tokens==null)
			tokens = new ArrayList<XToken>();
		read = false;
		i=0;
	}
	
	public void sure(){
		tokens = null;
	}
	
	public void reset(){
		read = true;
	}
	
}
