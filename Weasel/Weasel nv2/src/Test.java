import java.util.Arrays;
import java.util.List;

import weasel.compiler.WeaselClassFileProvider;
import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerMessage;


public class Test {
	
	public static void main(String[] args) {
		WeaselCompiler compiler = new WeaselCompiler();
		try{
			compiler.compile(new WeaselClassFileProvider() {
				
				@Override
				public String getClassSourceFor(String file) {
					if(file.equals("Test")){
						return "public class Test implements B {public int[] a(){a = 0;}}";
					}else if(file.equals("Enum")){
						return "public class Enum{public Enum(){}}";
					}else if(file.equals("Object")){
						return "public class Object {}";
					}else if(file.equals("Class")){
						return "public class Class {private final String className;}";
					}else if(file.equals("String")){
						return "public class String {private final char[] value;}";
					}else{
						return "public interface B {\n"
								+ "public int f(int d);\n"
								+ "}";
					}
				}
				
				@Override
				public String getClassSourceVersionFor(String file) {
					return "v2";
				}
				
				@Override
				public List<String> allKnowClasses() {
					return Arrays.asList(new String[]{"Test", "B", "Enum", "Object", "Class", "String"});
				}
	
			});
		}catch(Throwable e){
			e.printStackTrace();
		}
		for(WeaselCompilerMessage e:compiler.getExceptions()){
			System.err.println(e);
		}
	}
	
}
