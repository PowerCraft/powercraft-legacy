package weasel.interpreter;

import java.io.DataInputStream;
import java.io.IOException;

import weasel.interpreter.WeaselClass.WeaselID;

public final class WeaselMethod {

	public static final int normalModifier = WeaselModifier.PUBLIC|WeaselModifier.PRIVATE|WeaselModifier.PROTECTED|WeaselModifier.FINAL|WeaselModifier.STATIC|WeaselModifier.NATIVE;
	public static final int abstractModifier = WeaselModifier.PUBLIC|WeaselModifier.PRIVATE|WeaselModifier.PROTECTED|WeaselModifier.FINAL|WeaselModifier.STATIC|WeaselModifier.ABSTRACT|WeaselModifier.NATIVE;
	public static final int constructorModifier = WeaselModifier.PUBLIC|WeaselModifier.PRIVATE|WeaselModifier.PROTECTED;
	
	public WeaselGenericInformation[] genericInfo;
	protected final int modifier;
	protected final String name;
	protected final WeaselClass parentClass;
	protected final WeaselGenericClassInfo genericReturn;
	protected final WeaselClass returnParam;
	protected final WeaselGenericClassInfo[] genericParams;
	protected final WeaselClass[] params;
	protected final int id;
	
	protected WeaselMethod(String name, int modifier, WeaselClass parentClass, WeaselClass returnParam, WeaselGenericClassInfo genericReturn, WeaselClass[] params, WeaselGenericClassInfo[] genericParams, int id){
		boolean isConstructor = name.equals("<init>");
		boolean isPreConstructor = name.equals("<preInit>");
		boolean isStaticConstructor = name.equals("<staticInit>");
		if(!(isConstructor||isPreConstructor||isStaticConstructor))
			WeaselChecks.checkName(name);
		if(parentClass.isInterface()){
			if(!WeaselModifier.isStatic(modifier)){
				modifier |= WeaselModifier.ABSTRACT;
			}
		}
		WeaselChecks.checkModifier(modifier, isStaticConstructor?WeaselModifier.STATIC:isPreConstructor?0:isConstructor?parentClass.isEnum()?0:
			constructorModifier:WeaselModifier.isAbstract(parentClass.getModifier())?abstractModifier:normalModifier);
		this.name = name;
		this.modifier = modifier;
		this.parentClass = parentClass;
		this.returnParam = returnParam;
		this.genericReturn = genericReturn;
		this.params = params;
		this.genericParams = genericParams;
		this.id = id;
	}
	
	protected WeaselMethod(WeaselClass parentClass, DataInputStream dataInputStream, WeaselID wid) throws IOException {
		this.parentClass = parentClass;
		name = dataInputStream.readUTF();
		boolean isConstructor = name.equals("<init>");
		boolean isPreConstructor = name.equals("<preInit>");
		boolean isStaticConstructor = name.equals("<staticInit>");
		if(!(isConstructor||isPreConstructor||isStaticConstructor))
			WeaselChecks.checkName(name);
		int modifier = dataInputStream.readInt();
		if(parentClass.isInterface()){
			if(!WeaselModifier.isStatic(modifier)){
				modifier |= WeaselModifier.ABSTRACT;
			}
		}
		this.modifier = modifier;
		WeaselChecks.checkModifier(modifier,  isStaticConstructor?WeaselModifier.STATIC:isPreConstructor?0:isConstructor?parentClass.isEnum()?0:
			constructorModifier:WeaselModifier.isAbstract(parentClass.getModifier())?abstractModifier:normalModifier);
		returnParam = parentClass.interpreter.getWeaselClass(dataInputStream.readUTF());
		genericReturn = new WeaselGenericClassInfo(parentClass.interpreter, dataInputStream);
		int paramCount = dataInputStream.readInt();
		params = new WeaselClass[paramCount];
		genericParams = new WeaselGenericClassInfo[paramCount];
		for(int i=0; i<paramCount; i++){
			params[i] = parentClass.interpreter.getWeaselClass(dataInputStream.readUTF());
			genericParams[i] = new WeaselGenericClassInfo(parentClass.interpreter, dataInputStream);
		}
		if(WeaselModifier.isStatic(modifier)){
			id = wid.staticMethod++;
		}else{
			if(parentClass.superClass==null){
				id = wid.method++;
			}else{
				WeaselMethod superMethod = parentClass.superClass.getMethod(name, params);
				if(superMethod==null){
					id = wid.method++;
				}else{
					id = superMethod.id;
				}
			}
		}
	}

	public String getName() {
		return name;
	}
	
	public int getModifier() {
		return modifier;
	}
	
	public WeaselClass[] getParamClasses() {
		return params;
	}

	public WeaselClass getReturnClasses() {
		return returnParam;
	}
	
	private int getID(WeaselClass weaselClass){
		if(parentClass.isInterface()){
			return weaselClass.getInterfaceMethodMap(parentClass)[id];
		}
		return id;
	}
	
	private void checkObject(WeaselObject object){
		if(!WeaselModifier.isStatic(modifier)){
			WeaselClass weaselClass = object.getWeaselClass();
			if(!weaselClass.canCastTo(parentClass)){
				throw new WeaselNativeException("%s is no superClass from %s", parentClass, weaselClass);
			}
		}
	}
	
	public WeaselMethodBody getMethod(WeaselObject object){
		checkObject(object);
		return getMethodFromClass(object==null?null:object.getWeaselClass());
	}

	public String getDesk(){
		String desk="(";
		for(int i=0; i<params.length; i++){
			desk += params[i].getByteName();
		}
		desk += ")";
		desk += returnParam.getByteName();
		return desk;
	}
	
	public String getNameAndDesk() {
		return name+getDesk();
	}

	public WeaselMethodBody getMethodFromClass(WeaselClass weaselClass) {
		if(WeaselModifier.isStatic(modifier)){
			return parentClass.staticMethodBodys[id];
		}else{
			int id = getID(weaselClass);
			while(weaselClass.methodBodys[id]==null){
				weaselClass = weaselClass.superClass;
			}
			return weaselClass.methodBodys[id];
		}
	}
	
}
