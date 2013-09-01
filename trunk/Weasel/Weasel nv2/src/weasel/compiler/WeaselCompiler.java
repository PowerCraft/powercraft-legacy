package weasel.compiler;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import weasel.compiler.WeaselCompilerMessage.MessageType;
import weasel.compiler.keywords.WeaselKeyWord;
import weasel.compiler.v2.WeaselClassCompilerV2;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselField;
import weasel.interpreter.WeaselGenericClassInfo;
import weasel.interpreter.WeaselGenericInformation;
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
	protected List<WeaselClassCompiler> classesToCompileEasy = new ArrayList<WeaselClassCompiler>();
	protected WeaselClassFileProvider classFileProvider;
	protected List<WeaselCompilerMessage> compilerMessages = new ArrayList<WeaselCompilerMessage>();
	
	public WeaselCompiler(){
		super(0);
	}
	
	public void compileEasy(WeaselClass c){
		if(classesToCompileEasy.contains(c)){
			classesToCompileEasy.remove(c);
			WeaselClassCompiler cc = (WeaselClassCompiler)c;
			try{
				cc.compileEasy();
				classesToCompileFinish.add(cc);
			}catch(Throwable e){
				cc.tokenParser = null;
				if(e instanceof WeaselCompilerException){
					addWeaselCompilerMessage(new WeaselCompilerMessage(MessageType.ERROR, ((WeaselCompilerException) e).getLine(), c.getFileName(), e.getMessage()));
				}else{
					e.printStackTrace();
					addWeaselCompilerMessage(new WeaselCompilerMessage(MessageType.ERROR, 0, c.getFileName(), "Native Exception: "+e));
				}
			}
		}
	}
	
	public boolean compile(WeaselClassFileProvider classFileProvider){
		this.classFileProvider = classFileProvider;
		classesToCompile.addAll(classFileProvider.allKnowClasses());
		while(!classesToCompile.isEmpty()){
			getWeaselClass("O"+classesToCompile.get(0)+";");
			do{
				while(!classesToCompileEasy.isEmpty()){
					WeaselClassCompiler c = classesToCompileEasy.remove(0);
					try{
						c.compileEasy();
						classesToCompileFinish.add(c);
					}catch(Throwable e){
						c.tokenParser = null;
						if(e instanceof WeaselCompilerException){
							addWeaselCompilerMessage(new WeaselCompilerMessage(MessageType.ERROR, ((WeaselCompilerException) e).getLine(), c.getFileName(), e.getMessage()));
						}else{
							e.printStackTrace();
							addWeaselCompilerMessage(new WeaselCompilerMessage(MessageType.ERROR, 0, c.getFileName(), "Native Exception: "+e));
						}
					}
				}
				if(!classesToCompileFinish.isEmpty()){
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
			}while(!classesToCompileEasy.isEmpty() || !classesToCompileFinish.isEmpty());
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
			classesToCompileEasy.add(weaselClass);
			weaselClass.setSource(classFileProvider.getClassSourceFor(name));
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
	
	protected WeaselMethod createMethod(String name, int modifier, WeaselClass parentClass, WeaselGenericClassInfo genericReturn, WeaselGenericClassInfo[] genericParams, WeaselGenericInformation[] genericInformations, int id){
		return compilerCreateMethod(name, modifier, parentClass, genericReturn, genericParams, genericInformations, id);
	}

	protected WeaselField createField(String name, int modifier, WeaselClass weaselClass, WeaselGenericClassInfo typeInfo, int id) {
		return compilerCreateField(name, modifier, weaselClass, typeInfo, id);
	}
	
	public static void expect(WeaselToken token, WeaselTokenType...tokenTypes) throws WeaselCompilerException{
		for(int i=0; i<tokenTypes.length; i++){
			if(token.tokenType == tokenTypes[i]){
				return;
			}
		}
		throw new WeaselCompilerException(token.line, "Unexpected token %s expected %s", token, Arrays.toString(tokenTypes));
	}
	
	public static void expectKeyWord(WeaselToken token, WeaselKeyWord...keyWords) throws WeaselCompilerException{
		expect(token, WeaselTokenType.KEYWORD);
		for(int i=0; i<keyWords.length; i++){
			if(token.param == keyWords[i]){
				return;
			}
		}
		throw new WeaselCompilerException(token.line, "Unexpected keyword %s expected %s", token, Arrays.toString(keyWords));
	}
	
}
