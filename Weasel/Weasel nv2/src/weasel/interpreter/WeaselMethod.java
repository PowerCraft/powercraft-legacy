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
	protected final WeaselGenericClassInfo[] genericParams;
	protected final int id;
	
	protected WeaselMethod(String name, int modifier, WeaselClass parentClass, WeaselGenericClassInfo genericReturn, WeaselGenericClassInfo[] genericParams, int id){
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
		this.genericReturn = genericReturn;
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
		genericReturn = new WeaselGenericClassInfo(parentClass.interpreter, dataInputStream);
		int paramCount = dataInputStream.readInt();
		genericParams = new WeaselGenericClassInfo[paramCount];
		for(int i=0; i<paramCount; i++){
			genericParams[i] = new WeaselGenericClassInfo(parentClass.interpreter, dataInputStream);
		}
		if(WeaselModifier.isStatic(modifier)){
			id = wid.staticMethod++;
		}else{
			if(parentClass.genericSuperClass==null){
				id = wid.method++;
			}else{
				WeaselMethod superMethod = parentClass.genericSuperClass.genericClass.getMethod(name, genericParams);
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
		WeaselClass[] weaselClasses = new WeaselClass[genericParams.length];
		for(int i=0; i<weaselClasses.length; i++){
			weaselClasses[i] = genericParams[i].genericClass;
		}
		return weaselClasses;
	}

	public WeaselClass getReturnClasses() {
		return genericReturn.genericClass;
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
		for(int i=0; i<genericParams.length; i++){
			desk += genericParams[i].genericClass.getByteName();
		}
		desk += ")";
		if(genericReturn.genericClass!=parentClass.interpreter.baseTypes.voidClass){
			desk += genericReturn.genericClass.getByteName();
		}
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
				weaselClass = weaselClass.genericSuperClass.genericClass;
			}
			return weaselClass.methodBodys[id];
		}
	}

	@Override
	public String toString() {
		String params = "";
		for(int i=0; i<genericParams.length; i++){
			params += genericParams[i].getName(parentClass)+" ";
		}
		return WeaselModifier.toString2(modifier)+genericReturn.getName(parentClass)+" "+name+"("+params.trim()+")";
	}

	public WeaselClass getParentClass() {
		return parentClass;
	}
	
}
