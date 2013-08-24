import java.util.Arrays;
import java.util.List;

import weasel.compiler.WeaselClassFileProvider;
import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerMessage;
import weasel.interpreter.WeaselClass;
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
						return "public class Test<A> implements B<A> {\npublic int[] a(){\n"
								+ "a=new b()<a&&b<c&&a>b>>2;"
										+ "\n}}";
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
			WeaselClass b = compiler.getWeaselClass("OB;");
			WeaselGenericClass gc = new WeaselGenericClass(compiler, compiler.getWeaselClass("OTest;"), new WeaselClass[]{compiler.getWeaselClass("OClass;")});
			System.out.println(gc);
			System.out.println(gc.getGenericInterfaces()[0]);
			System.out.println(gc.getGenericInterfaces()[0].getGenericFieldType(b.getField("test")));
		}catch(Throwable e){
			e.printStackTrace();
		}
		for(WeaselCompilerMessage e:compiler.getExceptions()){
			System.err.println(e);
		}
	}
	
}
