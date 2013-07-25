import java.util.HashMap;

import weasel.compiler.WeaselCompiler;


public class Test {

	public static void main(String[] args){
		HashMap<String, String> sources = new HashMap<String, String>();
		sources.put("Test", "public class Test { public abstract int[][] i(}");
		sources.put("int", "public class int {}");
		WeaselCompiler compiler = new WeaselCompiler(sources);
		compiler.compile();
	}
	
}
