package weasel.compiler.equationSolverNew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselOperator.Properties;
import weasel.compiler.equationSolverNew.WeaselTokenBrackets.BracketType;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;

public class Solver {
	Properties ops[] = WeaselOperator.operators.values().toArray(new Properties[0]);
	
	public static WeaselTokenBrackets parse(WeaselToken[] source){
		WeaselTokenBrackets target = generateBracketTree(source);
		parseTree(target);
		ArrayList<WeaselToken> subs = new ArrayList<WeaselToken>();
		for(Properties operator:ops){
		}
		return target;
	}
	
	private static void parseTree(WeaselTokenBrackets wtb){
		IWeaselTokenTreeElement te;
		WeaselToken wt;
		WeaselTokenBrackets tb;
		WeaselTokenOperator to;
		WeaselTokenFunction tf;
		WeaselTokenVariable tv;
		final int size=wtb.subs.size();
		for(int i=0; i<size; i++){
			te=wtb.subs.get(i);
			if(te instanceof WeaselToken){
				wt = (WeaselToken) te;
				switch(wt.tokenType){
				case IDENT:
 
					break;
				default:
				}
				
			}else	if(te instanceof WeaselTokenBrackets){
				tb = (WeaselTokenBrackets) te;
				parseTree(tb);
			}else	if(te instanceof WeaselTokenOperator){
				to = (WeaselTokenOperator) te;
				
			}else	if(te instanceof WeaselTokenFunction){
				tf = (WeaselTokenFunction) te;
				
			}else	if(te instanceof WeaselTokenVariable){
				tv = (WeaselTokenVariable) te;
				
			}
		}
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
				brackets.get(0).addSub(tmpTokenBracket);
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
				inner.addSub(input[i]);
				break;
			}
		}
		return target;
	}
}
