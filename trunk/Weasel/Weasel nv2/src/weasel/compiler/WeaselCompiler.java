package weasel.compiler;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import weasel.compiler.v2.WeaselClassCompilerV2;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselField;
import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethod;
import weasel.interpreter.WeaselNativeException;

public class WeaselCompiler extends WeaselInterpreter {
	
	private static final HashMap<String, Class<? extends WeaselClassCompiler>> classCompiler = new HashMap<String, Class<? extends WeaselClassCompiler>>();
	
	static{
		classCompiler.put("v2", WeaselClassCompilerV2.class);
	}
	
	protected List<String> classesCompiled = new ArrayList<String>();
	protected List<String> classesToCompile = new ArrayList<String>();
	protected List<WeaselClassCompiler> classesToCompileFinish = new ArrayList<WeaselClassCompiler>();
	protected WeaselClassFileProvider classFileProvider;
	protected List<WeaselCompilerException> exceptions = new ArrayList<WeaselCompilerException>();
	
	public WeaselCompiler(){
		super(0);
	}
	
	public boolean compile(WeaselClassFileProvider classFileProvider){
		this.classFileProvider = classFileProvider;
		classesToCompile.addAll(classFileProvider.allKnowClasses());
		while(!classesToCompile.isEmpty()){
			getWeaselClass("O"+classesToCompile.get(0)+";");
			while(!classesToCompileFinish.isEmpty()){
				classesToCompileFinish.remove(0).finishCompile();
			}
		}
		return exceptions.isEmpty();
	}
	
	public List<WeaselCompilerException> getExceptions(){
		return exceptions;
	}
	
	@Override
	protected WeaselClass loadClass(String name){
		if(classesToCompile.contains(name)){
			classesToCompile.remove(name);
			classesCompiled.add(name);
			WeaselClassCompiler weaselClass = makeClassCompilerFor(classFileProvider.getClassSourceVersionFor(name), name, name);
			loadedClasses.put(name, weaselClass);
			weaselClass.compileEasy(classFileProvider.getClassSourceFor(name));
			classesToCompileFinish.add(weaselClass);
			return weaselClass;
		}else{
			return super.loadClass(name);
		}
	}
	
	protected WeaselClassCompiler makeClassCompilerFor(String version, String name, String fileName){
		Class<? extends WeaselClassCompiler> c = classCompiler.get(version);
		try {
			Constructor<? extends WeaselClassCompiler> constructor = c.getDeclaredConstructor(WeaselCompiler.class, Object.class, String.class, String.class);
			constructor.setAccessible(true);
			return constructor.newInstance(this, null, name, fileName);
		} catch (Exception e) {
			throw new WeaselNativeException(e, "Error while create Weasel class compiler %s", version);
		}
	}
	
	public void addWeaselCompilerException(WeaselCompilerException exception){
		exceptions.add(exception);
	}
	
	protected WeaselMethod createMethod(String name, int modifier, WeaselClass parentClass, WeaselClass returnParam, WeaselClass[] params, int id){
		return compilerCreateMethod(name, modifier, parentClass, returnParam, params, id);
	}
	
	private static final HashMap<String, String> classNameMap = new HashMap<String, String>();
	
	static{
		classNameMap.put("boolean", "N");
		classNameMap.put("char", "C");
		classNameMap.put("byte", "B");
		classNameMap.put("short", "S");
		classNameMap.put("int", "I");
		classNameMap.put("long", "L");
		classNameMap.put("float", "F");
		classNameMap.put("double", "D");
		classNameMap.put("void", "V");
	}
	
	public static String mapClassNames(String name){
		String mapedName = classNameMap.get(name);
		if(mapedName!=null)
			return mapedName;
		return "O"+name+";";
	}

	protected WeaselField createField(String name, int modifier, WeaselClass weaselClass, WeaselClass type, int id) {
		return compilerCreateField(name, modifier, weaselClass, type, id);
	}
	
}
