import java.util.Arrays;
import java.util.List;

import weasel.compiler.WeaselClassFileProvider;
import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;


public class Test {
	
	public static void main(String[] args) {
		WeaselCompiler compiler = new WeaselCompiler();
		try{
			compiler.compile(new WeaselClassFileProvider() {
				
				@Override
				public String getClassSourceFor(String file) {
					if(file.equals("Test")){
						return "public class Test implements B {public static int i[]={0};}";
					}else if(file.equals("Enum")){
						return "public class Enum{public Enum(){}}";
					}else if(file.equals("Object")){
						return "public class Object {}";
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
					return Arrays.asList(new String[]{"Test", "B", "Enum", "Object"});
				}
	
			});
		}catch(Throwable e){
			e.printStackTrace();
		}
		for(WeaselCompilerException e:compiler.getExceptions()){
			printExc(e);
		}
	}

	private static void printExc(Throwable e){
		System.err.println(e);
		if(e.getCause()!=null){
			System.err.print("Caused by: ");
			printExc(e.getCause());
		}
	}
	
}
