package weasel.interpreter;

import java.io.DataInputStream;
import java.io.IOException;

import weasel.interpreter.WeaselClass.WeaselID;

public final class WeaselField {

	public static final int normalModifier = WeaselModifier.PUBLIC|WeaselModifier.PRIVATE|WeaselModifier.PROTECTED|WeaselModifier.FINAL|WeaselModifier.STATIC;
	
	protected final int modifier;
	protected final String name;
	protected final WeaselClass parentClass;
	protected final WeaselGenericClassInfo genericType;
	protected final int id;
	
	protected WeaselField(String name, int modifier, WeaselClass parentClass, WeaselGenericClassInfo typeInfo, int id){
		WeaselChecks.checkName(name);
		WeaselChecks.checkModifier(modifier, normalModifier);
		this.name = name;
		this.modifier = modifier;
		this.parentClass = parentClass;
		this.genericType = typeInfo;
		this.id = id;
	}
	
	protected WeaselField(WeaselClass parentClass, DataInputStream dataInputStream, WeaselID wid) throws IOException {
		this.parentClass = parentClass;
		name = dataInputStream.readUTF();
		WeaselChecks.checkName(name);
		modifier = dataInputStream.readInt();
		WeaselChecks.checkModifier(modifier, normalModifier);
		genericType = new WeaselGenericClassInfo(parentClass.interpreter, dataInputStream);
		if(WeaselModifier.isStatic(modifier)){
			if(genericType.genericClass.isPrimitive()){
				id = wid.staticEasyType++;
				if(WeaselPrimitive.getPrimitiveID(genericType.genericClass)==WeaselPrimitive.LONG||WeaselPrimitive.getPrimitiveID(genericType.genericClass)==WeaselPrimitive.DOUBLE){
					wid.staticEasyType++;
				}
			}else{
				id = wid.staticObjectRef++;
			}
		}else{
			if(genericType.genericClass.isPrimitive()){
				id = wid.easyType++;
				if(WeaselPrimitive.getPrimitiveID(genericType.genericClass)==WeaselPrimitive.LONG||WeaselPrimitive.getPrimitiveID(genericType.genericClass)==WeaselPrimitive.DOUBLE){
					wid.staticEasyType++;
				}
			}else{
				id = wid.objectRef++;
			}
		}
	}

	public String getName() {
		return name;
	}
	
	public int getModifier() {
		return modifier;
	}
	
	public WeaselClass getType() {
		return genericType.genericClass;
	}
	
	private int getID(WeaselObject weaselObject){
		if(parentClass.isInterface() && !WeaselModifier.isStatic(modifier)){
			if(genericType.genericClass.isPrimitive()){
				return weaselObject.getWeaselClass().getInterfaceEasyTypeMap(parentClass)[id];
			}else{
				return weaselObject.getWeaselClass().getInterfaceObjectRefMap(parentClass)[id];
			}
		}
		return id;
	}
	
	private int[] getRefMap(WeaselObject object){
		if(WeaselModifier.isStatic(modifier)){
			return parentClass.staticObjectRefs;
		}
		return object.objectRefs;
	}
	
	private int[] getPrimitiveMap(WeaselObject object){
		if(WeaselModifier.isStatic(modifier)){
			return parentClass.staticEasyTypes;
		}
		return object.easyTypes;
	}
	
	public WeaselClass getParentClass(){
		return parentClass;
	}
	
	private void checkObject(WeaselObject object){
		if(!WeaselModifier.isStatic(modifier)){
			WeaselClass weaselClass = object.getWeaselClass();
			if(!weaselClass.canCastTo(parentClass)){
				throw new WeaselNativeException("%s is no superClass from %s", parentClass, weaselClass);
			}
		}
	}
	
	public int getObject(WeaselObject object){
		checkObject(object);
		if(genericType.genericClass.isPrimitive()){
			return WeaselPrimitive.makeWrapper(object, genericType.genericClass, this);
		}else{
			return getRefMap(object)[getID(object)];
		}
	}

	public boolean getBoolean(WeaselObject object) {
		checkObject(object);
		if(WeaselPrimitive.getPrimitiveID(genericType.genericClass)!=WeaselPrimitive.BOOLEAN){
			throw new WeaselNativeException("Field isn't a Boolean Field it is a %s", genericType.genericClass);
		}
		return getPrimitiveMap(object)[getID(object)]!=0;
	}
	
	public byte getByte(WeaselObject object) {
		checkObject(object);
		if(WeaselPrimitive.getPrimitiveID(genericType.genericClass)!=WeaselPrimitive.BYTE){
			throw new WeaselNativeException("Field isn't a Byte Field it is a %s", genericType.genericClass);
		}
		return (byte) getPrimitiveMap(object)[getID(object)];
	}
	
	public short getShort(WeaselObject object) {
		checkObject(object);
		if(WeaselPrimitive.getPrimitiveID(genericType.genericClass)!=WeaselPrimitive.SHORT){
			throw new WeaselNativeException("Field isn't a Short Field it is a %s", genericType.genericClass);
		}
		return (short) getPrimitiveMap(object)[getID(object)];
	}
	
	public char getChar(WeaselObject object) {
		checkObject(object);
		if(WeaselPrimitive.getPrimitiveID(genericType.genericClass)!=WeaselPrimitive.CHAR){
			throw new WeaselNativeException("Field isn't a Char Field it is a %s", genericType.genericClass);
		}
		return (char) getPrimitiveMap(object)[getID(object)];
	}
	
	public int getInt(WeaselObject object) {
		checkObject(object);
		if(WeaselPrimitive.getPrimitiveID(genericType.genericClass)!=WeaselPrimitive.INT){
			throw new WeaselNativeException("Field isn't a Int Field it is a %s", genericType.genericClass);
		}
		return (int) getPrimitiveMap(object)[getID(object)];
	}
	
	public long getLong(WeaselObject object) {
		checkObject(object);
		if(WeaselPrimitive.getPrimitiveID(genericType.genericClass)!=WeaselPrimitive.LONG){
			throw new WeaselNativeException("Field isn't a Long Field it is a %s", genericType.genericClass);
		}
		int id = getID(object);
		int[] map = getPrimitiveMap(object);
		return (long) map[id]<<32 | (long) map[id+1];
	}
	
	public float getFloat(WeaselObject object) {
		checkObject(object);
		if(WeaselPrimitive.getPrimitiveID(genericType.genericClass)!=WeaselPrimitive.FLOAT){
			throw new WeaselNativeException("Field isn't a Float Field it is a %s", genericType.genericClass);
		}
		return Float.intBitsToFloat(getInt(object));
	}
	
	public double getDouble(WeaselObject object) {
		checkObject(object);
		if(WeaselPrimitive.getPrimitiveID(genericType.genericClass)!=WeaselPrimitive.DOUBLE){
			throw new WeaselNativeException("Field isn't a Double Field it is a %s", genericType.genericClass);
		}
		return Double.longBitsToDouble(getLong(object));
	}

	public void setObject(WeaselObject object, int pointer){
		checkObject(object);
		if(genericType.genericClass.isPrimitive()){
			WeaselPrimitive.unwrapp(object, genericType.genericClass, pointer, this);
		}else{
			getRefMap(object)[getID(object)] = pointer;
		}
	}
	
	public void setBoolean(WeaselObject object, boolean value) {
		checkObject(object);
		if(WeaselPrimitive.getPrimitiveID(genericType.genericClass)!=WeaselPrimitive.BOOLEAN){
			throw new WeaselNativeException("Field isn't a Boolean Field it is a %s", genericType.genericClass);
		}
		getPrimitiveMap(object)[getID(object)]=value?-1:0;
	}

	public void setByte(WeaselObject object, byte value) {
		checkObject(object);
		if(WeaselPrimitive.getPrimitiveID(genericType.genericClass)!=WeaselPrimitive.BYTE){
			throw new WeaselNativeException("Field isn't a Byte Field it is a %s", genericType.genericClass);
		}
		getPrimitiveMap(object)[getID(object)]=value;
	}
	
	public void setShort(WeaselObject object, short value) {
		checkObject(object);
		if(WeaselPrimitive.getPrimitiveID(genericType.genericClass)!=WeaselPrimitive.SHORT){
			throw new WeaselNativeException("Field isn't a Short Field it is a %s", genericType.genericClass);
		}
		getPrimitiveMap(object)[getID(object)]=value;
	}

	public void setChar(WeaselObject object, char value) {
		checkObject(object);
		if(WeaselPrimitive.getPrimitiveID(genericType.genericClass)!=WeaselPrimitive.CHAR){
			throw new WeaselNativeException("Field isn't a Char Field it is a %s", genericType.genericClass);
		}
		getPrimitiveMap(object)[getID(object)]=value;
	}
	
	public void setInt(WeaselObject object, int value) {
		checkObject(object);
		if(WeaselPrimitive.getPrimitiveID(genericType.genericClass)!=WeaselPrimitive.INT){
			throw new WeaselNativeException("Field isn't a Integer Field it is a %s", genericType.genericClass);
		}
		getPrimitiveMap(object)[getID(object)]=value;
	}
	
	public void setLong(WeaselObject object, long value) {
		checkObject(object);
		if(WeaselPrimitive.getPrimitiveID(genericType.genericClass)!=WeaselPrimitive.LONG){
			throw new WeaselNativeException("Field isn't a Long Field it is a %s", genericType.genericClass);
		}
		int id = getID(object);
		int[] map = getPrimitiveMap(object);
		map[id]=(int)(value>>32);
		map[id+1]=(int)value;
	}
	
	public void setFloat(WeaselObject object, float value) {
		checkObject(object);
		if(WeaselPrimitive.getPrimitiveID(genericType.genericClass)!=WeaselPrimitive.FLOAT){
			throw new WeaselNativeException("Field isn't a Float Field it is a %s", genericType.genericClass);
		}
		setInt(object, Float.floatToRawIntBits(value));
	}
	
	public void setDouble(WeaselObject object, double value) {
		checkObject(object);
		if(WeaselPrimitive.getPrimitiveID(genericType.genericClass)!=WeaselPrimitive.DOUBLE){
			throw new WeaselNativeException("Field isn't a Double Field it is a %s", genericType.genericClass);
		}
		setLong(object, Double.doubleToRawLongBits(value));
	}

	@Override
	public String toString() {
		return WeaselModifier.toString2(modifier)+genericType.getName(parentClass)+" "+name;
	}
	
}
