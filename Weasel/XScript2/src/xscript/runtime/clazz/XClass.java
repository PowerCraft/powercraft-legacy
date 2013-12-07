package xscript.runtime.clazz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xscript.runtime.XAnnotation;
import xscript.runtime.XChecks;
import xscript.runtime.XModifier;
import xscript.runtime.XRuntimeException;
import xscript.runtime.XVirtualMachine;
import xscript.runtime.genericclass.XClassPtr;
import xscript.runtime.genericclass.XGenericClass;
import xscript.runtime.method.XMethod;
import xscript.runtime.object.XObject;

public class XClass extends XPackage{

	public final int OUTERMODIFIER = XModifier.PUBLIC | XModifier.FINAL | XModifier.ABSTRACT;
	public final int INNERMODIFIER = XModifier.PUBLIC | XModifier.PRIVATE | XModifier.PROTECTED 
			| XModifier.FINAL | XModifier.ABSTRACT | XModifier.STATIC;
	
	public static final int STATE_CREATED=0;
	public static final int STATE_LOADING=1;
	public static final int STATE_LOADED=2;
	public static final int STATE_POST_LOADING=3;
	public static final int STATE_RUNNABLE=4;
	public static final int STATE_ERRORED=5;
	
	protected XVirtualMachine virtualMachine;
	
	protected int modifier;
	protected XClassPtr[] superClasses;
	protected boolean[] canBeDiamondExtender;
	
	protected XField[] fields;
	protected XMethod[] methods;
	
	protected XGenericInfo[] genericInfos;
	
	protected XAnnotation[] annotations;
	
	protected int classIndex;
	protected XClassTable[] classTable;
	protected XMethod[] virtualMethods;
	
	protected int staticFieldCount;
	protected int fieldCount;
	protected int methodCount;
	
	protected int objectFieldCount;
	
	private byte[] staticData;
	
	protected int state;
	
	protected XClass(XVirtualMachine virtualMachine, String name) {
		super(name);
		state = STATE_CREATED;
		this.virtualMachine = virtualMachine;
	}
	
	@Override
	public void addChild(XPackage child) {
		throw new UnsupportedOperationException();
	}
	
	public XVirtualMachine getVirtualMachine(){
		return virtualMachine;
	}
	
	public int getModifier(){
		return modifier;
	}
	
	public XAnnotation[] getAnnotations(){
		return annotations;
	}
	
	public XClass getOuterClass(){
		return parent instanceof XClass?((XClass)parent):null;
	}
	
	public XPackage getPackage() {
		return parent instanceof XClass?((XClass)parent).getPackage():parent;
	}
	
	public XMethod getOuterMethod() {
		return parent instanceof XMethod?((XMethod)parent):null;
	}
	
	public boolean isObjectClass(){
		return superClasses.length==0;
	}
	
	public boolean isAnnotation(){
		return superClasses[0].getXClass(virtualMachine).getName().equals("xscript.lang.Annotation");
	}
	
	public boolean isEnum(){
		return superClasses[0].getXClass(virtualMachine).getName().equals("xscript.lang.Enum");
	}
	
	public int getGenericID(String genericName) {
		for(int i=0; i<genericInfos.length; i++){
			if(genericInfos[i].getName().equals(genericName)){
				return i;
			}
		}
		throw new XRuntimeException("Can't find generic class %s", genericName);
	}

	public int getGenericParams() {
		return genericInfos.length;
	}

	public XClassTable getClassTable(XClass xClass){
		if(isObjectClass()){
			return classTable[0];
		}
		int ci = xClass.classIndex;
		if(classTable.length>ci){
			XClassTable ct = classTable[ci];
			if(ct!=null && ct.getXClass()==xClass)
				return ct;
		}
		return null;
	}
	
	public boolean canCastTo(XClass xClass){
		return xClass.getClassTable(this)!=null;
	}
	
	public XGenericClass getGeneric(XGenericClass genericClass, int genericID) {
		if(genericClass.getXClass()==this){
			return genericClass.getGeneric(genericID);
		}
		XClassTable classTable = genericClass.getXClass().getClassTable(this);
		if(classTable==null)
			throw new XRuntimeException("Can't get generic %s of class %s in class %s", "", this, genericClass);
		return classTable.getGenericPtr(genericID).getXClass(virtualMachine, genericClass, null);
	}

	public int getObjectSize() {
		return objectFieldCount;
	}

	public void markObjectObjectsVisible(XObject object) {
		for(int i=0; i<fields.length; i++){
			if(!XModifier.isStatic(fields[i].getModifier())){
				if(fields[i].getTypePrimitive()==XPrimitive.OBJECT){
					long pointer = fields[i].get(object);
					XObject obj = virtualMachine.getObjectProvider().getObject(pointer);
					if(obj!=null)
						obj.markVisible();
				}
			}
		}
		for(int i=0; i<superClasses.length; i++){
			superClasses[i].getXClassNonNull(virtualMachine).markObjectObjectsVisible(object);
		}
	}
	
	@Override
	public void markVisible(){
		super.markVisible();
		for(int i=0; i<fields.length; i++){
			if(XModifier.isStatic(fields[i].getModifier())){
				if(fields[i].getTypePrimitive()==XPrimitive.OBJECT){
					long pointer = fields[i].get(null);
					XObject obj = virtualMachine.getObjectProvider().getObject(pointer);
					if(obj!=null)
						obj.markVisible();
				}
			}
		}
	}

	public byte[] getStaticData() {
		return staticData;
	}

	public XMethod getVirtualMethod(int i) {
		return virtualMethods[i];
	}

	public XField getField(String name){
		for(int i=0; i<fields.length; i++){
			if(fields[i].getSimpleName().equals(name)){
				return fields[i];
			}
		}
		return null;
	}
	
	public XField getFieldAndParents(String name){
		for(int i=0; i<fields.length; i++){
			if(fields[i].getSimpleName().equals(name)){
				return fields[i];
			}
		}
		for(int i=0; i<superClasses.length; i++){
			XField field = superClasses[i].getXClass(virtualMachine).getFieldAndParents(name);
			if(field!=null)
				return field;
		}
		return null;
	}
	
	public XMethod getMethod(String methodName, String[] paramNames, String retName) {
		for(int i=0; i<methods.length; i++){
			if(methods[i].getSimpleName().equals(name)){
				return methods[i];
			}
		}
		return null;
	}
	
	public XMethod getMethod(String name) {
		for(int i=0; i<methods.length; i++){
			if(methods[i].getSimpleName().equals(name)){
				return methods[i];
			}
		}
		return null;
	}

	protected void load(XInputStream inputStream) throws IOException {
		if(state==STATE_CREATED){
			try{
				state = STATE_LOADING;
				String className = inputStream.readUTF();
				if(!getName().equals(className))
					throw new XRuntimeException("Wrong class name %s expect %s", getName(), className);
				modifier = inputStream.readUnsignedShort();
				if(getPackage() instanceof XClass){
					XChecks.checkModifier(this, modifier, INNERMODIFIER);
				}else if(getPackage() instanceof XMethod){
					XChecks.checkModifier(this, modifier, INNERMODIFIER);
				}else{
					XChecks.checkModifier(this, modifier, OUTERMODIFIER);
				}
				annotations = new XAnnotation[inputStream.readUnsignedByte()];
				for(int i=0; i<annotations.length; i++){
					annotations[i] = new XAnnotation(inputStream);
				}
				int childCount = inputStream.readUnsignedByte();
				for(int i=0; i<childCount; i++){
					XClass xClass = new XClass(virtualMachine, inputStream.readUTF());
					super.addChild(xClass);
					xClass.load(inputStream);
				}
				superClasses = new XClassPtr[inputStream.readUnsignedByte()];
				canBeDiamondExtender = new boolean[superClasses.length];
				for(int i=0; i<superClasses.length; i++){
					(superClasses[i] = XClassPtr.load(inputStream)).getXClassNonNull(virtualMachine);
					canBeDiamondExtender[i] = true;
				}
				checkDiamonds(new HashMap<XClass, XClass>());
				genericInfos = new XGenericInfo[inputStream.readUnsignedByte()];
				for(int i=0; i<genericInfos.length; i++){
					genericInfos[i] = new XGenericInfo(virtualMachine, inputStream);
				}
				fields = new XField[inputStream.readUnsignedShort()];
				for(int i=0; i<fields.length; i++){
					super.addChild(fields[i] = new XField(this, inputStream));
				}
				methods = new XMethod[inputStream.readUnsignedShort()];
				for(int i=0; i<methods.length; i++){
					super.addChild(methods[i] = new XMethod(this, inputStream));
					if(methods[i].isConstructor() && !XModifier.isStatic(methods[i].getModifier())){
						XClass[] superClasses = methods[i].getExplizitSuperInvokes();
						for(int j=0; j<superClasses.length; j++){
							for(int k=0; k<this.superClasses.length; k++){
								if(superClasses[j] == this.superClasses[k].getXClassNonNull(virtualMachine)){
									canBeDiamondExtender[k] = false;
									break;
								}
							}
						}
					}
				}
				state = STATE_LOADED;
				virtualMachine.getClassProvider().addClassForLoading(this);
			}catch(Throwable e){
				state = STATE_ERRORED;
				if(!(e instanceof XRuntimeException)){
					e = new XRuntimeException(e, "Error while loading class %s", getName());
				}
				throw (XRuntimeException)e;
			}
		}
	}
	
	private void checkDiamonds(HashMap<XClass, XClass> checkedClasses) {
		for(int i=0; i<superClasses.length; i++){
			XClass xClass = superClasses[i].getXClassNonNull(virtualMachine);
			XClass first = checkedClasses.get(xClass);
			if(first == null){
				checkedClasses.put(xClass, this);
			}else{
				first.checkDiamondAbleFor(xClass);
			}
			xClass.checkDiamonds(checkedClasses);
		}
	}

	private void checkDiamondAbleFor(XClass xClass) {
		for(int i=0; i<superClasses.length; i++){
			if(superClasses[i].getXClassNonNull(virtualMachine)==xClass){
				if(canBeDiamondExtender[i])
					return;
				throw new XRuntimeException("Class %s can be used as diamond base", xClass);
			}
		}
		throw new XRuntimeException("Oh, this should never be happened, this is a fatal error :(");
	}

	protected void postLoad() {
		virtualMachine.getClassProvider().removeClassForLoading(this);
		if(state==STATE_LOADED){
			try{
				state = STATE_POST_LOADING;
				for(int i=0; i<superClasses.length; i++){
					XClass xClass = superClasses[i].getXClass(virtualMachine);
					xClass.postLoad();
					if(xClass.state!=STATE_RUNNABLE){
						throw new XRuntimeException("Class %s isn't inited correctly, so %s can't inited", xClass, this);
					}
				}
				
				state = STATE_RUNNABLE;
				XMethod xMethod = getMethod("<staticInit>", new String[0], "void");
				virtualMachine.getThreadProvider().importantInterrupt("Static "+getName(), xMethod, new XGenericClass[0], new long[0]);
			}catch(Throwable e){
				state = STATE_ERRORED;
				if(!(e instanceof XRuntimeException)){
					e = new XRuntimeException(e, "Error while post loading class %s", getName());
				}
				throw (XRuntimeException)e;
			}
		}
	}

	public int getStaticFieldIndex(int sizeInObject) {
		if(state!=STATE_LOADING)
			throw new XRuntimeException("You can't get a field index now");
		int ret = staticFieldCount;
		staticFieldCount += sizeInObject;
		return ret;
	}

	public int getFieldIndex(int sizeInObject) {
		if(state!=STATE_LOADING)
			throw new XRuntimeException("You can't get a field index now");
		int ret = fieldCount;
		fieldCount += sizeInObject;
		return ret;
	}

	public int getMethodIndex() {
		if(state!=STATE_LOADING)
			throw new XRuntimeException("You can't get a method index now");
		return methodCount++;
	}

	public int getState() {
		return state;
	}
	
	public void save(XOutputStream outputStream) throws IOException{
		outputStream.writeUTF(getName());
		outputStream.writeShort(modifier);
		outputStream.writeShort(annotations.length);
		for(int i=0; i<annotations.length; i++){
			annotations[i].save(outputStream);
		}
		
		List<XClass> classChilds = new ArrayList<XClass>();
		
		for(XPackage p:childs.values()){
			if(p instanceof XClass){
				classChilds.add((XClass)p);
			}
		}
		
		outputStream.writeByte(classChilds.size());
		for(XClass classChild:classChilds){
			classChild.save(outputStream);
		}
		
		outputStream.writeByte(superClasses.length);
		for(int i=0; i<superClasses.length; i++){
			superClasses[i].save(outputStream);
		}
		
		outputStream.writeByte(genericInfos.length);
		for(int i=0; i<genericInfos.length; i++){
			genericInfos[i].save(outputStream);
		}

		outputStream.writeShort(fields.length);
		for(int i=0; i<fields.length; i++){
			fields[i].save(outputStream);
		}
		
		outputStream.writeShort(methods.length);
		for(int i=0; i<methods.length; i++){
			methods[i].save(outputStream);
		}
		
	}
	
}
