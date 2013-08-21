package weasel.compiler;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import weasel.compiler.WeaselCompilerMessage.MessageType;
import weasel.compiler.v2.WeaselClassCompilerV2;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselField;
import weasel.interpreter.WeaselGenericInfo;
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
	protected List<WeaselCompilerMessage> compilerMessages = new ArrayList<WeaselCompilerMessage>();
	
	public WeaselCompiler(){
		super(0);
	}
	
	public boolean compile(WeaselClassFileProvider classFileProvider){
		this.classFileProvider = classFileProvider;
		classesToCompile.addAll(classFileProvider.allKnowClasses());
		while(!classesToCompile.isEmpty()){
			getWeaselClass("O"+classesToCompile.get(0)+";");
			while(!classesToCompileFinish.isEmpty()){
				WeaselClassCompiler c = classesToCompileFinish.remove(0);
				try{
					c.finishCompile();
				}catch(Throwable e){
					if(e instanceof WeaselCompilerException){
						addWeaselCompilerMessage(new WeaselCompilerMessage(MessageType.ERROR, ((WeaselCompilerException) e).getLine(), c.getFileName(), e.getMessage()));
					}else{
						e.printStackTrace();
						addWeaselCompilerMessage(new WeaselCompilerMessage(MessageType.ERROR, 0, c.getFileName(), "Native Exception: "+e));
					}
				}
			}
		}
		return compilerMessages.isEmpty();
	}
	
	public List<WeaselCompilerMessage> getExceptions(){
		return compilerMessages;
	}
	
	@Override
	protected WeaselClass loadClass(String name){
		if(classesToCompile.contains(name)){
			classesToCompile.remove(name);
			classesCompiled.add(name);
			WeaselClassCompiler weaselClass = makeClassCompilerFor(classFileProvider.getClassSourceVersionFor(name), name, name);
			loadedClasses.put(name, weaselClass);
			try{
				weaselClass.compileEasy(classFileProvider.getClassSourceFor(name));
				classesToCompileFinish.add(weaselClass);
			}catch(Throwable e){
				weaselClass.tokenParser = null;
				if(e instanceof WeaselCompilerException){
					addWeaselCompilerMessage(new WeaselCompilerMessage(MessageType.ERROR, ((WeaselCompilerException) e).getLine(), weaselClass.getFileName(), e.getMessage()));
				}else{
					e.printStackTrace();
					addWeaselCompilerMessage(new WeaselCompilerMessage(MessageType.ERROR, 0, weaselClass.getFileName(), "Native Exception: "+e));
				}
			}
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
	
	public void addWeaselCompilerMessage(WeaselCompilerMessage message){
		compilerMessages.add(message);
	}
	
	protected WeaselMethod createMethod(String name, int modifier, WeaselClass parentClass, WeaselClass returnParam, WeaselGenericInfo genericReturn, WeaselClass[] params, WeaselGenericInfo[] genericParams, int id){
		return compilerCreateMethod(name, modifier, parentClass, returnParam, genericReturn, params, genericParams, id);
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

	protected WeaselField createField(String name, int modifier, WeaselClass weaselClass, WeaselClass type, WeaselGenericInfo typeInfo, int id) {
		return compilerCreateField(name, modifier, weaselClass, type, typeInfo, id);
	}
	
}
