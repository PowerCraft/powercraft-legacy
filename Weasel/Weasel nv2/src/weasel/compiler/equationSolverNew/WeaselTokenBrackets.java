package weasel.compiler.equationSolverNew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import weasel.compiler.WeaselToken;
import weasel.compiler.equationSolverNew.Solver.String2D;

public class WeaselTokenBrackets extends IWeaselTokenTreeElement {

	public final BracketType type;
	public static enum BracketType{
		ROUND("(", ")"), SQUARE("[", "]"), CURLY("{", "}");
		
		private final String openChar;
		private final String closeChar;
		BracketType(String open, String close){
			openChar=open;
			closeChar=close;
		}
		
		public String getOpen(){
			return openChar;
		}
		
		public String getClose(){
			return closeChar;
		}
	}
	
	private List<IWeaselTokenTreeElement> subs = new ArrayList<IWeaselTokenTreeElement>();
	
	public WeaselTokenBrackets(BracketType t){
		type = t;
	}
	
	public void addSubs(IWeaselTokenTreeElement...elements){
		subs.addAll(Arrays.asList(elements));
	}
	
	public List<IWeaselTokenTreeElement> getSubs(){
		return subs;
	}
	
	@Override
	public String getName() {
		String target="["+subs.get(0).getName();
		for(int i=1; i<subs.size(); i++){
			target+=","+subs.get(i).getName();
		}
		target+="]";
		return target;
	}

	@Override
	public String toString(){
		return toReadableString();
	}
	
	@Override
	public String toReadableString() {
		return toEncryptedString();
	}
	
	@Override
	public String toEncryptedString() {
		if(subs.size()==0) return "[]";
		String target="["+subs.get(0).toEncryptedString();
		for(int i=1; i<subs.size(); i++){
			target+=","+subs.get(i).toEncryptedString();
		}
		target+="]";
		return target;
	}

	@Override
	public void toAdvancedEncryptedString(String2D str) {
		str.add(type.getOpen());
		str.deeper();
		for(int i=0; i<subs.size(); i++){
			subs.get(i).toAdvancedEncryptedString(str);
		}
		str.shallower();
		str.add(type.getClose());
	}

	@Override
	public String toClassView() {
		String target="["+subs.get(0).toClassView();
		for(int i=1; i<subs.size(); i++){
			target+=","+subs.get(i).toClassView();
		}
		target+="]";
		return target;
	}

	@Override
	public WeaselToken simplify() {
		String target="";
		for(IWeaselTokenTreeElement sub:subs){
			target+=sub.getName();
		}
		return null;
	}

}
