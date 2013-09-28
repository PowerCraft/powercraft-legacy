package weasel.interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeaselModifier {

	public static final int PUBLIC = 1;
	public static final int PRIVATE = 2;
	public static final int PROTECTED = 4;
	public static final int STATIC = 8;
	public static final int FINAL = 16;
	public static final int NATIVE = 32;
	public static final int ABSTRACT = 64;
	
public static final HashMap<String, Integer> MODIFIER = new HashMap<String, Integer>();
	
	static{
		MODIFIER.put("public", PUBLIC);
		MODIFIER.put("private", PRIVATE);
		MODIFIER.put("protected", PROTECTED);
		MODIFIER.put("static", STATIC);
		MODIFIER.put("final", FINAL);
		MODIFIER.put("native", NATIVE);
		MODIFIER.put("abstract", ABSTRACT);
	}
	
	public static boolean isPublic(int modifier){
		return (modifier & PUBLIC)!=0;
	}
	
	public static boolean isPrivate(int modifier){
		return (modifier & PRIVATE)!=0;
	}
	
	public static boolean isProtected(int modifier){
		return (modifier & PROTECTED)!=0;
	}
	
	public static boolean isStatic(int modifier){
		return (modifier & STATIC)!=0;
	}
	
	public static boolean isFinal(int modifier){
		return (modifier & FINAL)!=0;
	}
	
	public static boolean isNative(int modifier){
		return (modifier & NATIVE)!=0;
	}
	
	public static boolean isAbstract(int modifier){
		return (modifier & ABSTRACT)!=0;
	}

	public static String toString(int modifier) {
		List<String> output = new ArrayList<String>();
		if((modifier & WeaselModifier.PRIVATE)!=0){
			output.add("private");
		}
		if((modifier & WeaselModifier.PUBLIC)!=0){
			output.add("public");
		}
		if((modifier & WeaselModifier.PROTECTED)!=0){
			output.add("protected");
		}
		if((modifier & WeaselModifier.STATIC)!=0){
			output.add("static");
		}
		if((modifier & WeaselModifier.ABSTRACT)!=0){
			output.add("abstract");
		}
		if((modifier & WeaselModifier.FINAL)!=0){
			output.add("final");
		}
		if((modifier & WeaselModifier.NATIVE)!=0){
			output.add("native");
		}
		if(output.isEmpty())
			return "";
		if(output.size()==1)
			return output.get(0);
		String sOutput = "";
		for(int i=0; i<output.size()-2; i++){
			sOutput += output.get(i)+", ";
		}
		sOutput += output.get(output.size()-2)+" & ";
		sOutput += output.get(output.size()-1);
		return sOutput;
	}

	public static String toSourceString(int modifier) {
		String output = "";
		if((modifier & WeaselModifier.PRIVATE)!=0){
			output += "private ";
		}
		if((modifier & WeaselModifier.PUBLIC)!=0){
			output += "public ";
		}
		if((modifier & WeaselModifier.PROTECTED)!=0){
			output += "protected ";
		}
		if((modifier & WeaselModifier.STATIC)!=0){
			output += "static ";
		}
		if((modifier & WeaselModifier.ABSTRACT)!=0){
			output += "abstract ";
		}
		if((modifier & WeaselModifier.FINAL)!=0){
			output += "final ";
		}
		if((modifier & WeaselModifier.NATIVE)!=0){
			output += "native ";
		}
		return output;
	}
	
	public static int count(int modifier) {
		int count=0;
		for(int i=0; i<32; i++){
			if((modifier&1<<i)!=0)
				count++;
		}
		return count;
	}

	public static int getModifier(String ident) {
		Integer i = MODIFIER.get(ident);
		return i==null?0:i;
	}

}
