package xscript.runtime.clazz;

import java.io.IOException;

import xscript.runtime.XAnnotation;
import xscript.runtime.XChecks;
import xscript.runtime.XModifier;
import xscript.runtime.XRuntimeException;
import xscript.runtime.genericclass.XClassPtr;
import xscript.runtime.genericclass.XGenericClass;
import xscript.runtime.object.XObject;

public class XField extends XPackage {

	public static final int STATICALLOWEDMODIFIFER = XModifier.FINAL | XModifier.PRIVATE | XModifier.PROTECTED | XModifier.PUBLIC | XModifier.STATIC;
	public static final int ALLOWEDMODIFIFER = XModifier.FINAL | XModifier.PRIVATE | XModifier.PROTECTED | XModifier.PUBLIC;
	
	private int modifier;
	private XClassPtr type;
	private XAnnotation[] annotations;
	private int index;
	
	public XField(XClass declaringClass, XInputStream inputStream) throws IOException {
		super(inputStream.readUTF());
		parent = declaringClass;
		modifier = inputStream.readUnsignedShort();
		annotations = new XAnnotation[inputStream.readUnsignedByte()];
		for(int i=0; i<annotations.length; i++){
			annotations[i] = new XAnnotation(inputStream);
		}
		(type = XClassPtr.load(inputStream)).getXClass(declaringClass.getVirtualMachine());
		if(XModifier.isStatic(modifier)){
			declaringClass.getStaticFieldIndex(getSizeInObject());
			XChecks.checkModifier(declaringClass, modifier, STATICALLOWEDMODIFIFER);
		}else{
			declaringClass.getFieldIndex(getSizeInObject());
			XChecks.checkModifier(declaringClass, modifier, ALLOWEDMODIFIFER);
		}
	}

	public XField(XClass declaringClass, int modifier, String name, XClassPtr type, XAnnotation[] annotations) {
		super(name);
		parent = declaringClass;
		this.modifier = modifier;
		this.annotations = annotations;
		this.type = type;
		if(XModifier.isStatic(modifier)){
			declaringClass.getStaticFieldIndex(getSizeInObject());
			XChecks.checkModifier(declaringClass, modifier, STATICALLOWEDMODIFIFER);
		}else{
			declaringClass.getFieldIndex(getSizeInObject());
			XChecks.checkModifier(declaringClass, modifier, ALLOWEDMODIFIFER);
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
	
	public XClassPtr getType() {
		return type;
	}
	
	public XGenericClass getType(XGenericClass genericDeclaringClass){
		return type.getXClass(getDeclaringClass().getVirtualMachine(), genericDeclaringClass, null);
	}
	
	public int getTypePrimitive(){
		XClass xClass = type.getXClass(getDeclaringClass().getVirtualMachine());
		if(xClass==null)
			return XPrimitive.OBJECT;
		return XPrimitive.getPrimitiveID(xClass);
	}
	
	public XAnnotation[] getAnnotations(){
		return annotations;
	}
	
	public int getSizeInObject(){
		return XPrimitive.getSize(getTypePrimitive());
	}
	
	public long get(XObject object){
		int i;
		byte[] data;
		if(XModifier.isStatic(modifier)){
			data = getDeclaringClass().getStaticData();
			i = index;
		}else{
			XClass xClass = object.getXClass().getXClass();
			XClassTable classTable = getDeclaringClass().getClassTable(xClass);
			if(classTable==null)
				throw new XRuntimeException("Can't cast %s to %s", xClass, getDeclaringClass());
			data = object.getData();
			i = classTable.getFieldStartID()+index;
		}
		int size = getSizeInObject();
		long l = 0;
		for(int j=0; j<size; j++){
			l <<= 8;
			l |= data[i+j];
		}
		return l;
	}

	public void set(XObject object, long value) {
		if(XModifier.isFinal(modifier)){
			throw new XRuntimeException("Try to write final field %s", getName());
		}
		finalSet(object, value);
	}
	
	public void finalSet(XObject object, long value){
		int i;
		byte[] data;
		if(XModifier.isStatic(modifier)){
			data = getDeclaringClass().getStaticData();
			i = index;
		}else{
			XClass xClass = object.getXClass().getXClass();
			XClassTable classTable = getDeclaringClass().getClassTable(xClass);
			if(classTable==null)
				throw new XRuntimeException("Can't cast %s to %s", xClass, getDeclaringClass());
			data = object.getData();
			i = classTable.getFieldStartID()+index;
			if(getTypePrimitive()==XPrimitive.OBJECT){
				XGenericClass type = getType(object.getXClass());
				XChecks.checkCast(object.getXClass(), type);
			}
		}
		int size = getSizeInObject();
		for(int j=size-1; j>=0; j--){
			data[i+j] = (byte) (value & 255);
			value >>>= 8;
		}
	}

	public void save(XOutputStream outputStream) throws IOException {
		outputStream.writeShort(modifier);
		outputStream.writeByte(annotations.length);
		for(int i=0; i<annotations.length; i++){
			annotations[i].save(outputStream);
		}
		type.save(outputStream);
	}
	
}
