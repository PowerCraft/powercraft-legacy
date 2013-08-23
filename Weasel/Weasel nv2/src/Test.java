import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import weasel.compiler.WeaselClassFileProvider;
import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerMessage;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenParser;
import weasel.compiler.WeaselTokenType;
import weasel.compiler.equationSolverNew.Solver;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselGenericClass;


public class Test  {
	
	public static void main(String[] args) throws WeaselCompilerException {
		
		WeaselTokenParser tp = new WeaselTokenParser("x=(5+4)*(3-3*(2+4))+5*Class.<Integer>rand()/new Integer(2,4) ");
		List<WeaselToken> tokenList = new ArrayList<WeaselToken>();
		WeaselToken token = tp.getNextToken();
		while(token.tokenType!=WeaselTokenType.NONE){
			tokenList.add(token);
			token = tp.getNextToken();
		}
		Solver.parse(tokenList.toArray(new WeaselToken[0]));
		//System.out.println(tokenList);
		WeaselCompiler compiler = new WeaselCompiler();
		try{
			compiler.compile(new WeaselClassFileProvider() {
				
				@Override
				public String getClassSourceFor(String file) {
					if(file.equals("Test")){
						return "public class Test<A> implements B<A> {\npublic int[] a(){\nb = 4-5*-4;\n}}";
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
