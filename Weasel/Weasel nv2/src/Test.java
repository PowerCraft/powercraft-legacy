import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import weasel.compiler.WeaselClassFileProvider;
import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerMessage;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselNativeException;


public class Test implements WeaselClassFileProvider{
	
	private File file;
	private List<String> allKnowClasses = new ArrayList<String>();
	
	public Test(){
		file = new File(".");
		file = new File(file, "src");
		File[] fiels = file.listFiles();
		for(int i=0; i<fiels.length; i++){
			String fileName = fiels[i].getName();
			if(fileName.endsWith(".ws")){
				allKnowClasses.add(fileName.substring(0, fileName.length()-3));
			}
		}
	}
	
	@Override
	public String getClassSourceFor(String file) {
		try {
			LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(new File(this.file, file+".ws"))));
			String source = "";
			String line;
			while((line=lnr.readLine())!=null)
				source += line + "\n";
			lnr.close();
			return source;
		} catch (IOException e) {
			throw new WeaselNativeException(e, "Error while read class %s", file);
		}
	}

	@Override
	public String getClassSourceVersionFor(String file) {
		return "v2";
	}

	@Override
	public List<String> allKnowClasses() {
		return allKnowClasses;
	}
	
	public static void main(String[] args) throws WeaselCompilerException {
		
		//System.out.println(tokenList);
		WeaselCompiler compiler = new WeaselCompiler();
		try{
			compiler.compile(new Test());
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
