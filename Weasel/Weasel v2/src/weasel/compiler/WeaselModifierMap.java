package weasel.compiler;

import java.util.ArrayList;
import java.util.List;

import weasel.interpreter.WeaselModifier;

public class WeaselModifierMap {
	
	public static String getModifierName(int modifier){
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
		if((modifier & WeaselModifier.FIANL)!=0){
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
	
	public static int getModifier(String modifier){
		if(modifier.equals("private")){
			return WeaselModifier.PRIVATE;
		}else if(modifier.equals("public")){
			return WeaselModifier.PUBLIC;
		}else if(modifier.equals("protected")){
			return WeaselModifier.PROTECTED;
		}else if(modifier.equals("final")){
			return WeaselModifier.FIANL;
		}else if(modifier.equals("native")){
			return WeaselModifier.NATIVE;
		}else if(modifier.equals("static")){
			return WeaselModifier.STATIC;
		}
		return 0;
	}
	
}
