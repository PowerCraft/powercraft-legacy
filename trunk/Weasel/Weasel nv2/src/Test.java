import java.util.Arrays;
import java.util.List;

import weasel.compiler.WeaselClassFileProvider;
import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerMessage;
import weasel.interpreter.WeaselGenericClass;


public class Test {
	
	public static void main(String[] args) throws WeaselCompilerException {
		
		//System.out.println(tokenList);
		WeaselCompiler compiler = new WeaselCompiler();
		try{
			compiler.compile(new WeaselClassFileProvider() {
				
				@Override
				public String getClassSourceFor(String file) {
					if(file.equals("Test")){
						return "public class Test<A> implements B<A> {\n"
								+ "public String b, r[];\n"
								+ "public int[] a(){\n"
								+ "   b = r[3];\n"
								+ "}}\n";
					}else if(file.equals("Enum")){
						return "public class Enum{public Enum(){}}";
					}else if(file.equals("Object")){
						return "public class Object {public boolean equals(Object o){}}";
					}else if(file.equals("Class")){
						return "public class Class {}";
					}else if(file.equals("String")){
						return "public class String {}";
					}else{
						return "public interface B<C> {\n"
								+ "public Test<C[]> f(C d);public Test<C>[] test;\n"
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
			System.out.println(compiler.getWeaselClass("OTest;").toSource());
			System.out.println(compiler.getWeaselClass("OB;").toSource());
			WeaselGenericClass wgc = new WeaselGenericClass(compiler.getWeaselClass("OTest;"), new WeaselGenericClass[]{new WeaselGenericClass(compiler.getWeaselClass("OString;"))});
			System.out.println(wgc);
			wgc = wgc.getGenericInterfaces()[0];
			System.out.println(wgc);
			System.out.println(wgc.getGenericField("test"));
			System.out.println(wgc.getGenericMethod("f(OObject;)OTest;", new WeaselGenericClass[0]));
		}catch(Throwable e){
			e.printStackTrace();
		}
		for(WeaselCompilerMessage e:compiler.getExceptions()){
			System.err.println(e);
		}
	}
	
}
