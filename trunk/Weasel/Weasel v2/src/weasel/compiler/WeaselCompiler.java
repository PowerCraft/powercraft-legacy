package weasel.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselClassBuffer;
import weasel.interpreter.WeaselNativeMethod;

public class WeaselCompiler implements WeaselClassBuffer {

	private HashMap<String, WeaselClass> loadedClasses = new HashMap<String, WeaselClass>();
	private List<String> toCompile;
	private HashMap<String, String> sources;
	public List<WeaselClassCompiler> classesToPreCompile = new ArrayList<WeaselClassCompiler>();
	public List<WeaselClassCompiler> classesToCompile = new ArrayList<WeaselClassCompiler>();
	
	public WeaselCompiler(HashMap<String, String> sources){
		this.sources = sources;
		toCompile = new ArrayList<String>(sources.keySet());
	}
	
	public void compile(){
		for(String className:toCompile){
			getClassByName(className);
			makeCompilings();
		}
	}

	private void makePreCompilings(){
		while(!classesToPreCompile.isEmpty()){
			classesToPreCompile.get(0).preCompiling();
		}
	}
	
	private void makeCompilings(){
		makePreCompilings();
		while(!classesToCompile.isEmpty()){
			classesToCompile.get(0).compiling();
			makePreCompilings();
		}
	}
	
	@Override
	public WeaselClass getClassByName(String className) {
		String classes[] = className.split("\\.", 2);
		WeaselClass wClass = loadedClasses.get(classes[0]);
		if(wClass==null){
			String source = sources.get(className);
			if(source!=null){
				WeaselClassCompiler wClassCompiler = new WeaselClassCompiler(this, classes[0], source);
				loadedClasses.put(className, wClassCompiler);
				classesToPreCompile.add(wClassCompiler);
				wClass = wClassCompiler;
			}
		}
		if(classes.length==1){
			return wClass;
		}
		return wClass.getChildClass(classes[1]);
	}
	
	@Override
	public WeaselNativeMethod getNativeMethod(String nameAndDesk) {
		return null;
	}
	
}
