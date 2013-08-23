package weasel.compiler.equationSolverNew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselOperator.Properties;
import weasel.compiler.equationSolverNew.WeaselTokenBrackets.BracketType;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;

public class Solver {
	Properties ops[] = WeaselOperator.operators.values().toArray(new Properties[0]);
	
	public static WeaselTokenOperator parse(WeaselToken[] source) throws WeaselCompilerException{
		WeaselTokenOperator target;
		WeaselTokenBrackets tmp = generateBracketTree(source);
		String2D str = new String2D();
		tmp.toAdvancedEncryptedString(str);
		str.print();
		System.exit(0);
		target = parseTree(WeaselOperator.operators.values().iterator(), tmp);
		ArrayList<WeaselToken> subs = new ArrayList<WeaselToken>();
		//for(Properties operator:ops){
		//}
		return target;
	}
	
	private static WeaselTokenOperator parseTree(Iterator<Properties> it, WeaselTokenBrackets wtb){
		WeaselTokenOperator target = null;
		IWeaselTokenTreeElement te;
		WeaselToken wt;
		WeaselTokenBrackets tb;
		WeaselTokenOperator to;
		WeaselTokenFunction tf;
		WeaselTokenVariable tv;
		final int size=wtb.getSubs().size();
		WeaselTokenBrackets currentTmp;
		Properties op;
		while(target==null && it.hasNext()){
			op = it.next();
			if(op==WeaselOperator.GREATER
			|| op==WeaselOperator.LESS
			|| op==WeaselOperator.RSHIFT)
				continue;
			currentTmp=new WeaselTokenBrackets(BracketType.ROUND);
			for(int i=0; i<size; i++){
				te=wtb.getSubs().get(i);
				if(te instanceof WeaselToken){
					wt = (WeaselToken) te;
					switch(wt.tokenType){
					case IDENT:
	 
					case OPERATOR:
						if(((Properties)wt.param)!=op)
							break;
						if(target==null)
							target = new WeaselTokenOperator(op, wt);
						target.addOldOperatorToken(wt);
						target.addSubs(currentTmp);
						currentTmp = new WeaselTokenBrackets(BracketType.ROUND);
						continue;
					default:
					}
					
				}else	if(te instanceof WeaselTokenBrackets){
					tb = (WeaselTokenBrackets) te;
					//parseTree(tb);
				}else	if(te instanceof WeaselTokenOperator){
					to = (WeaselTokenOperator) te;
					
				}else	if(te instanceof WeaselTokenFunction){
					tf = (WeaselTokenFunction) te;
					
				}else	if(te instanceof WeaselTokenVariable){
					tv = (WeaselTokenVariable) te;
					
				}
				currentTmp.addSubs(te);
			}
			if(target!=null)
				target.addSubs(currentTmp);
		}
		return target;
	}
	
	private static WeaselTokenBrackets generateBracketTree(WeaselToken[] input){
		WeaselTokenBrackets target = null;
		ArrayList<WeaselTokenBrackets> brackets = new ArrayList<WeaselTokenBrackets>();
		brackets.add(target = new WeaselTokenBrackets(BracketType.ROUND));
		WeaselToken current;
		WeaselTokenBrackets inner = target;
		WeaselTokenBrackets tmpTokenBracket;
		for(int i=0; i<input.length; i++){
			current = input[i];
			tmpTokenBracket=null;
			switch(current.tokenType){
			case OPENBLOCK:
				tmpTokenBracket = new WeaselTokenBrackets(BracketType.CURLY);
			case OPENINDEX:
				if(tmpTokenBracket==null)
					tmpTokenBracket = new WeaselTokenBrackets(BracketType.SQUARE);
			case OPENBRACKET:
				if(tmpTokenBracket==null)
					tmpTokenBracket = new WeaselTokenBrackets(BracketType.ROUND);
				brackets.get(0).addSubs(tmpTokenBracket);
				brackets.add(0, tmpTokenBracket);
				inner = tmpTokenBracket;
				break;
			case CLOSEBLOCK:
			case CLOSEINDEX:
			case CLOSEBRACKET:
				brackets.remove(0);
				inner = brackets.get(0);
				break;
			default:
				inner.addSubs(input[i]);
				break;
			}
		}
		return target;
	}
	
	public static class String2D{
		private ArrayList<String> strings = new ArrayList<String>();
		private int level=0;
		public String2D(){
			strings.add("");
		}
		
		public void deeper(){
			level++;
		}
		
		public void shallower(){
			level--;
		}
		
		public void add(String text){
			String emptyText=getSpaceString(text.length());
			while(strings.size()<=level){
				strings.add(getSpaceString(strings.get(strings.size()-1).length()));
			}
			for(int i=0; i<strings.size(); i++){
				if(i==level) strings.set(i, strings.get(i)+text);
				else strings.set(i, strings.get(i)+emptyText);
			}
		}
		
		public static String getSpaceString(int length){
			String target="";
			for(; length>10; length-=10){
				target+="          ";
			}
			for(; length>0; length--){
				target+=" ";
			}
			return target;
		}
		
		public void print(){
			for(String str:strings){
				System.out.println(str);
			}
		}
	}
	
	public static class Security{
		public static WeaselToken checkAllowed(WeaselTokenType wtt, WeaselToken...tokens){
			for(WeaselToken wt:tokens){
				if(wt.tokenType!=wtt)
					return wt;
			}
			return null;
		}
	}
}
