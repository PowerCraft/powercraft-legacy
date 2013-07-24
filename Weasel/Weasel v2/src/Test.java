import java.util.HashMap;

import weasel.compiler.WeaselCompiler;


public class Test {

	public static void main(String[] args){
		HashMap<String, String> sources = new HashMap<String, String>();
		sources.put("Test", "public class Test {}");
		WeaselCompiler compiler = new WeaselCompiler(sources);
		compiler.compile();
	}
	
}
