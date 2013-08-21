package weasel.compiler.equationSolverNew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselOperator.Properties;
import weasel.compiler.WeaselToken;

public class Solver {
	
	public static WeaselToken order(WeaselToken[] source){
		WeaselToken target = null;
		ArrayList<Integer> openBrackets= new ArrayList<Integer>();
		Properties ops[] = WeaselOperator.operators.values().toArray(new Properties[0]);
		WeaselToken current;
		ArrayList<WeaselToken> subs = new ArrayList<WeaselToken>();
		for(Properties operator:ops){
			for(int i=0; i<source.length; i++){
				current = source[i];
				switch(current.tokenType){
				case BOOL:
					break;
				case CLOSEBLOCK:
					break;
				case CLOSEBRACKET:
					subs.add(Arrays.asList(source).subList(openBrackets.get(openBrackets.size()-1), i));
					break;
				case CLOSEINDEX:
					break;
				case COLON:
					break;
				case COMMA:
					break;
				case DOUBLE:
					break;
				case IDENT:
					break;
				case INTEGER:
					break;
				case KEYWORD:
					break;
				case MODIFIER:
					break;
				case NONE:
					break;
				case NULL:
					break;
				case OPENBLOCK:
					break;
				case OPENBRACKET:
					openBrackets=i;
					break;
				case OPENINDEX:
					break;
				case OPERATOR:
					break;
				case QUESTIONMARK:
					break;
				case SEMICOLON:
					break;
				case STRING:
					break;
				case UNKNOWN:
					break;
				default:
					break;
				}
			}
		}
		return target;
	}
}
