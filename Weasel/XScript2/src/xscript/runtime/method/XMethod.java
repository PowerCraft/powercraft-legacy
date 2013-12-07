package xscript.runtime.method;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xscript.runtime.XAnnotation;
import xscript.runtime.XChecks;
import xscript.runtime.XModifier;
import xscript.runtime.XRuntimeException;
import xscript.runtime.clazz.XClass;
import xscript.runtime.clazz.XClassTable;
import xscript.runtime.clazz.XGenericInfo;
import xscript.runtime.clazz.XInputStream;
import xscript.runtime.clazz.XOutputStream;
import xscript.runtime.clazz.XPackage;
import xscript.runtime.clazz.XPrimitive;
import xscript.runtime.genericclass.XClassPtr;
import xscript.runtime.genericclass.XGenericClass;
import xscript.runtime.instruction.XInstruction;
import xscript.runtime.instruction.XInstructionInvokeSpecial;
import xscript.runtime.instruction.XInstructionNew;
import xscript.runtime.nativemethod.XNativeMethod;
import xscript.runtime.object.XObject;
import xscript.runtime.threads.XGenericMethodProvider;
import xscript.runtime.threads.XMethodExecutor;

public class XMethod extends XPackage {
	
	public static final int STATICALLOWEDMODIFIFER = XModifier.FINAL | XModifier.PRIVATE | XModifier.PROTECTED | XModifier.PUBLIC | XModifier.STATIC | XModifier.NATIVE;
	public static final int ALLOWEDMODIFIFER = XModifier.FINAL | XModifier.PRIVATE | XModifier.PROTECTED | XModifier.PUBLIC | XModifier.ABSTRACT | XModifier.NATIVE;
	public static final int CONSTRUCTORMODIFIER = XModifier.PRIVATE | XModifier.PROTECTED | XModifier.PUBLIC;
	public static final int STATICCONSTRUCTORMODIFIER = XModifier.STATIC | XModifier.FINAL | XModifier.PRIVATE;
	
	protected int modifier;
	protected XClassPtr returnType;
	protected XAnnotation[] annotations;
	protected XClassPtr[] params;
	protected XAnnotation[][] paramAnnotations;
	protected XClassPtr[] mThrows;
	protected XGenericInfo[] genericInfos;
	protected XNativeMethod nativeMethod;
	protected XInstruction[] instructions;
	protected XLineEntry[] lineEntries;
	protected XCatchEntry[] catchEntries;
	protected XLocalEntry[] localEntries;
	protected int maxStackSize;
	protected int maxLocalSize;
	protected int index;
	
	public XMethod(XClass declaringClass, XInputStream inputStream) throws IOException {
		super(inputStream.readUTF());
		parent = declaringClass;
		modifier = inputStream.readUnsignedShort();
		annotations = new XAnnotation[inputStream.readUnsignedByte()];
		for(int i=0; i<annotations.length; i++){
			annotations[i] = new XAnnotation(inputStream);
		}
		(returnType = XClassPtr.load(inputStream)).getXClass(declaringClass.getVirtualMachine());
		int numParam = inputStream.readUnsignedByte();
		paramAnnotations = new XAnnotation[numParam][];
		params = new XClassPtr[numParam];
		for(int i=0; i<numParam; i++){
			XAnnotation[] annotations = new XAnnotation[inputStream.readUnsignedByte()];
			for(int j=0; j<annotations.length; j++){
				annotations[j] = new XAnnotation(inputStream);
			}
			paramAnnotations[i] = annotations;
			(params[i] = XClassPtr.load(inputStream)).getXClass(declaringClass.getVirtualMachine());
		}
		mThrows = new XClassPtr[inputStream.readUnsignedByte()];
		for(int i=0; i<mThrows.length; i++){
			(mThrows[i] = XClassPtr.load(inputStream)).getXClass(declaringClass.getVirtualMachine());
		}
		genericInfos = new XGenericInfo[inputStream.readUnsignedByte()];
		for(int i=0; i<genericInfos.length; i++){
			genericInfos[i] = new XGenericInfo(declaringClass.getVirtualMachine(), inputStream);
		}
		if(XModifier.isNative(modifier)){
			getNativeMethod();
		}else{
			instructions = new XInstruction[inputStream.readInt()];
			for(int i=0; i<instructions.length; i++){
				instructions[i] = XInstruction.load(inputStream);
			}
			lineEntries = new XLineEntry[inputStream.readUnsignedShort()];
			for(int i=0; i<lineEntries.length; i++){
				lineEntries[i] = new XLineEntry(inputStream.readInt(), inputStream.readUnsignedShort());
			}
			catchEntries = new XCatchEntry[inputStream.readUnsignedShort()];
			for(int i=0; i<catchEntries.length; i++){
				catchEntries[i] = new XCatchEntry(inputStream.readInt(), inputStream.readInt(), inputStream.readInt(), XClassPtr.load(inputStream));
			}
			localEntries = new XLocalEntry[inputStream.readUnsignedShort()];
			for(int i=0; i<localEntries.length; i++){
				localEntries[i] = new XLocalEntry(inputStream.readInt(), inputStream.readInt(), inputStream.readUnsignedShort(), inputStream.readUnsignedShort(), inputStream.readUTF(), XClassPtr.load(inputStream));
			}
			maxStackSize = inputStream.readUnsignedShort();
			maxLocalSize = inputStream.readUnsignedShort();
		}
		if(XModifier.isStatic(modifier)){
			if(name.equals("<staticInit>")){
				modifier |= STATICCONSTRUCTORMODIFIER;
				XChecks.checkModifier(declaringClass, modifier, STATICCONSTRUCTORMODIFIER);
			}else{
				XChecks.checkModifier(declaringClass, modifier, STATICALLOWEDMODIFIFER);
			}
		}else {
			index = declaringClass.getMethodIndex();
			if(name.equals("<init>")){
				XChecks.checkModifier(declaringClass, modifier, CONSTRUCTORMODIFIER);
			}else if(name.equals("<preInit>")){
				modifier |= XModifier.PROTECTED;
				XChecks.checkModifier(declaringClass, modifier, XModifier.PROTECTED);
			}else{
				XChecks.checkModifier(declaringClass, modifier, ALLOWEDMODIFIFER);
			}
		}
	}

	public XMethod(XClass declaringClass, int modifier, String name, XClassPtr returnType, XAnnotation[] annotations, XClassPtr[] params, XAnnotation[][] paramAnnotations, XClassPtr[] mThrows, XGenericInfo[] genericInfos) {
		super(name);
		parent = declaringClass;
		this.modifier = modifier;
		this.returnType = returnType;
		this.annotations = annotations;
		this.params = params;
		this.paramAnnotations = paramAnnotations;
		this.mThrows = mThrows;
		this.genericInfos = genericInfos;
		
		if(XModifier.isNative(modifier)){
			getNativeMethod();
		}
		if(XModifier.isStatic(modifier)){
			if(name.equals("<staticInit>")){
				modifier |= STATICCONSTRUCTORMODIFIER;
				XChecks.checkModifier(declaringClass, modifier, STATICCONSTRUCTORMODIFIER);
			}else{
				XChecks.checkModifier(declaringClass, modifier, STATICALLOWEDMODIFIFER);
			}
		}else {
			index = declaringClass.getMethodIndex();
			if(name.equals("<init>")){
				XChecks.checkModifier(declaringClass, modifier, CONSTRUCTORMODIFIER);
			}else if(name.equals("<preInit>")){
				modifier |= XModifier.PROTECTED;
				XChecks.checkModifier(declaringClass, modifier, XModifier.PROTECTED);
			}else{
				XChecks.checkModifier(declaringClass, modifier, ALLOWEDMODIFIFER);
			}
		}
	}
	
	@Override
	public void addChild(XPackage child) {
		throw new UnsupportedOperationException();
	}
	
	public XClass getDeclaringClass(){
		return (XClass)parent;
	}
	
	public int getModifier(){
		return modifier;
	}
	
	public XGenericClass getReturnType(XGenericClass genericClass, XGenericMethodProvider methodExecutor){
		return returnType.getXClass(getDeclaringClass().getVirtualMachine(), genericClass, methodExecutor);
	}
	
	public int getReturnTypePrimitive(){
		XClass xClass = returnType.getXClass(getDeclaringClass().getVirtualMachine());
		if(xClass==null)
			return XPrimitive.OBJECT;
		return XPrimitive.getPrimitiveID(xClass);
	}
	
	public XGenericClass[] getParams(XGenericClass genericClass, XGenericMethodProvider methodExecutor){
		XGenericClass[] p = new XGenericClass[params.length];
		for(int i=0; i<p.length; i++){
			p[i] = params[i].getXClass(getDeclaringClass().getVirtualMachine(), genericClass, methodExecutor);
		}
		return p;
	}
	
	public XGenericClass[] getThrows(XGenericClass genericClass, XMethodExecutor methodExecutor){
		XGenericClass[] p = new XGenericClass[mThrows.length];
		for(int i=0; i<p.length; i++){
			p[i] = mThrows[i].getXClass(getDeclaringClass().getVirtualMachine(), genericClass, methodExecutor);
		}
		return p;
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
	
	public XMethod getMethod(XObject object){
		if(XModifier.isStatic(modifier)){
			return this;
		}
		XClass xClass = object.getXClass().getXClass();
		XClassTable classTable = getDeclaringClass().getClassTable(xClass);
		if(classTable==null){
			throw new XRuntimeException("Can't cast %s to %s", xClass, getDeclaringClass());
		}
		int i = classTable.getMethodID(index);
		return xClass.getVirtualMethod(i);
	}

	public XNativeMethod getNativeMethod() {
		if(XModifier.isNative(modifier)){
			if(nativeMethod==null){
				nativeMethod = getDeclaringClass().getVirtualMachine().getNativeProvider().removeNativeMethod(getName());
			}
			return nativeMethod;
		}
		return null;
	}
	
	public void setNativeMethod(XNativeMethod nativeMethod){
		if(XModifier.isNative(modifier)){
			this.nativeMethod = nativeMethod;
		}
	}
	
	public XInstruction getInstruction(int i) {
		return instructions[i];
	}

	public int getExceptionHandlePoint(int programPointer, XGenericClass xClass, XGenericClass declaringClass, XMethodExecutor methodExecutor) {
		for(int i=0; i<catchEntries.length; i++){
			if(catchEntries[i].isIn(programPointer)){
				if(xClass.canCastTo(catchEntries[i].getType().getXClass(getDeclaringClass().getVirtualMachine(), declaringClass, methodExecutor))){
					return catchEntries[i].getJumpPos();
				}
			}
		}
		return -1;
	}

	public XClassPtr getLocalType(int programPointer, int local){
		for(int i=0; i<localEntries.length; i++){
			if(localEntries[i].isIn(programPointer) && localEntries[i].getIndex() == local){
				return localEntries[i].getType();
			}
		}
		return null;
	}
	
	public int getLine(int programPointer){
		for(int i=1; i<lineEntries.length; i++){
			if(lineEntries[i].getFrom()<programPointer){
				return lineEntries[i-1].getLine();
			}
		}
		return lineEntries[lineEntries.length].getLine();
	}
	
	public int getParamCount() {
		return params.length;
	}

	public int getMaxStackSize() {
		return maxStackSize;
	}
	
	public int getMaxLocalSize() {
		return maxLocalSize;
	}

	public String[] getMethodParamNames() {
		String[] s = new String[params.length];
		for(int i=0; i<s.length; i++){
			s[i] = params[i].toString();
		}
		return s;
	}

	public String getMethodReturnName() {
		return returnType.toString();
	}

	public boolean isConstructor() {
		return XModifier.isStatic(modifier)?name.equals("<staticInit>"):name.equals("<init>");
	}

	public XClass[] getExplizitSuperInvokes() {
		int numNews = 0;
		List<XClass> explitite = new ArrayList<XClass>();
		if(isConstructor() && XModifier.isStatic(modifier)){
			for(int i=0; i<instructions.length; i++){
				if(instructions[i] instanceof XInstructionInvokeSpecial){
					if(numNews==0){
						XMethod method = ((XInstructionInvokeSpecial)instructions[i]).getMethod(getDeclaringClass().getVirtualMachine());
						if(method.isConstructor() && getDeclaringClass().canCastTo(method.getDeclaringClass())){
							if(method.getParamCount()>0){
								if(explitite.contains(method.getDeclaringClass())){
									throw new XRuntimeException("Error, calling 2 times the same super constructor");
								}else{
									explitite.add(method.getDeclaringClass());
								}
							}
						}
					}else{
						numNews--;
					}
				}else if(instructions[i] instanceof XInstructionNew){
					numNews++;
				}
			}
		}
		return explitite.toArray(new XClass[explitite.size()]);
	}

	public void save(XOutputStream outputStream) throws IOException {
		outputStream.writeShort(modifier);
		
		outputStream.writeByte(annotations.length);
		for(int i=0; i<annotations.length; i++){
			annotations[i].save(outputStream);
		}
		
		returnType.save(outputStream);
		
		outputStream.writeByte(params.length);
		for(int i=0; i<params.length; i++){
			XAnnotation[] annotations = paramAnnotations[i];
			outputStream.writeByte(annotations.length);
			for(int j=0; j<annotations.length; j++){
				annotations[j].save(outputStream);
			}
			params[i].save(outputStream);
		}
		
		outputStream.writeByte(mThrows.length);
		for(int i=0; i<mThrows.length; i++){
			mThrows[i].save(outputStream);
		}
		
		outputStream.writeByte(genericInfos.length);
		for(int i=0; i<genericInfos.length; i++){
			genericInfos[i].save(outputStream);
		}
		
		if(!XModifier.isNative(modifier)){
			
			outputStream.writeInt(instructions.length);
			for(int i=0; i<instructions.length; i++){
				XInstruction.save(outputStream, instructions[i]);
			}
			
			outputStream.writeShort(lineEntries.length);
			for(int i=0; i<lineEntries.length; i++){
				outputStream.writeInt(lineEntries[i].getFrom());
				outputStream.writeShort(lineEntries[i].getLine());
			}
			
			outputStream.writeShort(catchEntries.length);
			for(int i=0; i<catchEntries.length; i++){
				outputStream.writeInt(catchEntries[i].getFrom());
				outputStream.writeInt(catchEntries[i].getTo());
				outputStream.writeInt(catchEntries[i].getJumpPos());
				catchEntries[i].getType().save(outputStream);
			}
			
			outputStream.writeShort(localEntries.length);
			for(int i=0; i<localEntries.length; i++){
				outputStream.writeInt(localEntries[i].getFrom());
				outputStream.writeInt(localEntries[i].getTo());
				outputStream.writeShort(localEntries[i].getIndex());
				outputStream.writeShort(localEntries[i].getModifier());
				outputStream.writeUTF(localEntries[i].getName());
				localEntries[i].getType().save(outputStream);
			}
			
			outputStream.writeShort(maxStackSize);
			outputStream.writeShort(maxLocalSize);
		}
	}
	
}
