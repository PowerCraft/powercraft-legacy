package weasel.interpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import weasel.interpreter.WeaselThread.State;

public class WeaselInterpreter implements WeaselSaveable {

	public final WeaselPrimitive booleanClass;
	public final WeaselPrimitive charClass;
	public final WeaselPrimitive byteClass;
	public final WeaselPrimitive shortClass;
	public final WeaselPrimitive intClass;
	public final WeaselPrimitive longClass;
	public final WeaselPrimitive floatClass;
	public final WeaselPrimitive doubleClass;
	public final WeaselPrimitive voidClass;
	
	protected final WeaselObject[] objectPointer;
	protected final HashMap<String, WeaselClass> loadedClasses;
	private List<WeaselThread> threads = new ArrayList<WeaselThread>();
	private int activeThreadID;
	private HashMap<String, Synchronized> synchronizeds;
	private HashMap<String, WeaselNativeMethod> nativeMethods = new HashMap<String, WeaselNativeMethod>();
	
	public WeaselInterpreter(int memorySize){
		objectPointer = new WeaselObject[memorySize];
		loadedClasses = new HashMap<String, WeaselClass>();
		synchronizeds = new HashMap<String, Synchronized>();
		booleanClass = new WeaselPrimitive(this, WeaselPrimitive.BOOLEAN);
		charClass = new WeaselPrimitive(this, WeaselPrimitive.CHAR);
		byteClass = new WeaselPrimitive(this, WeaselPrimitive.BYTE);
		shortClass = new WeaselPrimitive(this, WeaselPrimitive.SHORT);
		intClass = new WeaselPrimitive(this, WeaselPrimitive.INT);
		longClass = new WeaselPrimitive(this, WeaselPrimitive.LONG);
		floatClass = new WeaselPrimitive(this, WeaselPrimitive.FLOAT);
		doubleClass = new WeaselPrimitive(this, WeaselPrimitive.DOUBLE);
		voidClass = new WeaselPrimitive(this, WeaselPrimitive.VOID);
	}
	
	public WeaselInterpreter(DataInputStream dataInputStream) throws IOException{
		int memorySize = dataInputStream.readInt();
		objectPointer = new WeaselObject[memorySize];
		loadedClasses = new HashMap<String, WeaselClass>();
		synchronizeds = new HashMap<String, Synchronized>();
		booleanClass = new WeaselPrimitive(this, WeaselPrimitive.BOOLEAN);
		charClass = new WeaselPrimitive(this, WeaselPrimitive.CHAR);
		byteClass = new WeaselPrimitive(this, WeaselPrimitive.BYTE);
		shortClass = new WeaselPrimitive(this, WeaselPrimitive.SHORT);
		intClass = new WeaselPrimitive(this, WeaselPrimitive.INT);
		longClass = new WeaselPrimitive(this, WeaselPrimitive.LONG);
		floatClass = new WeaselPrimitive(this, WeaselPrimitive.FLOAT);
		doubleClass = new WeaselPrimitive(this, WeaselPrimitive.DOUBLE);
		voidClass = new WeaselPrimitive(this, WeaselPrimitive.VOID);
		int count = dataInputStream.readInt();
		for(int i=0; i<count; i++){
			String className = dataInputStream.readUTF();
			WeaselClass weaselClass = getWeaselClass(className);
			weaselClass.loadFromDataStream(dataInputStream);
		}
		for(int i=0; i<memorySize; i++){
			if(dataInputStream.readBoolean()){
				objectPointer[i] = new WeaselObject(this, dataInputStream);
			}
		}
		int threadSize = dataInputStream.readInt();
		for(int i=0; i<threadSize; i++){
			threads.add(new WeaselThread(this, dataInputStream));
		}
		activeThreadID = dataInputStream.readInt();
		count = dataInputStream.readInt();
		for(int i=0; i<count; i++){
			String token = dataInputStream.readUTF();
			synchronizeds.put(token, new Synchronized(this, dataInputStream));
		}
	}
	
	@Override
	public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException{
		dataOutputStream.writeInt(objectPointer.length);
		dataOutputStream.writeInt(loadedClasses.size());
		for(Entry<String, WeaselClass> e:loadedClasses.entrySet()){
			dataOutputStream.writeUTF(e.getKey());
			e.getValue().saveToDataStream(dataOutputStream);
		}
		for(int i=0; i<objectPointer.length; i++){
			dataOutputStream.writeBoolean(objectPointer[i]!=null);
			if(objectPointer[i]!=null){
				objectPointer[i].saveToDataStream(dataOutputStream);
			}
		}
		dataOutputStream.writeInt(threads.size());
		for(WeaselThread thread:threads){
			thread.saveToDataStream(dataOutputStream);
		}
		dataOutputStream.writeInt(activeThreadID);
		dataOutputStream.writeInt(synchronizeds.size());
		for(Entry<String, Synchronized> e:synchronizeds.entrySet()){
			dataOutputStream.writeUTF(e.getKey());
			e.getValue().saveToDataStream(dataOutputStream);
		}
	}
	
	public WeaselObject getObject(int pointer){
		if(pointer==0)
			return null;
		return objectPointer[pointer-1];
	}

	public void gc(){
		for(int i=0; i<objectPointer.length; i++){
			if(objectPointer[i]!=null){
				objectPointer[i].setInvisible();
			}
		}
		for(WeaselThread thread:threads){
			thread.markKnownObjects();
		}
		for(int i=0; i<objectPointer.length; i++){
			if(objectPointer[i]!=null && !objectPointer[i].isVisible()){
				objectPointer[i] = null;
			}
		}
	}
	
	public WeaselClass getWeaselClass(String name){
		char firstChar = name.charAt(0);
		WeaselClass weaselClass;
		switch(firstChar){
		case 'O':
			if(name.charAt(name.length()-1)!=';')
				throw new WeaselNativeException("Wrong Class Name %s", name);
			String[] names = name.substring(1, name.length()-1).split("\\.", 2);
			weaselClass = loadedClasses.get(names[0]);
			if(weaselClass==null){
				weaselClass = loadClass(names[0]);
				loadedClasses.put(names[0], weaselClass);
			}
			if(names.length==1)
				return weaselClass;
			return weaselClass.getChildClass(names[1]);
		case 'N':
			return booleanClass;
		case 'C':
			return charClass;
		case 'B':
			return byteClass;
		case 'S':
			return shortClass;
		case 'I':
			return intClass;
		case 'L':
			return longClass;
		case 'F':
			return floatClass;
		case 'D':
			return doubleClass;
		case 'V':
			return voidClass;
		case '[':
			String className = "[";
			int i=1;
			char c;
			while((c = name.charAt(i))=='['){
				className += "[";
			}
			if(c=='O'){
				className += name.substring(i+1, name.length()-1);
			}else{
				className += c;
			}
			weaselClass = loadedClasses.get(className);
			if(weaselClass==null){
				weaselClass = new WeaselClass(this, getWeaselClass(name.substring(1)), className, null);
				loadedClasses.put(className, weaselClass);
			}
			return weaselClass;
		}
		throw new WeaselNativeException("Wrong Class Name %s", name);
	}
	
	protected WeaselClass loadClass(String name){
		DataInputStream classInputStream = getClassInputStream(name);
		WeaselClass weaselClass = new WeaselClass(this, null, name, name);
		loadedClasses.put(name, weaselClass);
		try {
			weaselClass.loadClassFormInputStream(classInputStream);
		} catch (IOException e) {
			throw new WeaselNativeException(e, "Error while loading class %s", name);
		}
		return weaselClass;
	}
	
	private DataInputStream getClassInputStream(String className){
		throw new WeaselNativeException("Class not found %s", className);
	}
	
	public int createObject(WeaselClass weaselClass){
		return createObject(weaselClass, 0, 0);
	}
	
	public int createObject(WeaselClass weaselClass, int parent){
		return createObject(weaselClass, parent, 0);
	}
	
	public int createObject(WeaselClass weaselClass, int parent, int arrayLength){
		if(weaselClass.getParentClass()!=null && getObject(parent)==null)
			throw new WeaselNativeException("No Parent specified for %s", weaselClass);
		if(weaselClass.getParentClass()==null && getObject(parent)!=null)
			throw new WeaselNativeException("Parent specified for %s", weaselClass);
		for(int i=0; i<objectPointer.length; i++){
			if(objectPointer[i]==null){
				objectPointer[i] = new WeaselObject(weaselClass, parent, arrayLength);
				return i+1;
			}
		}
		gc();
		for(int i=0; i<objectPointer.length; i++){
			if(objectPointer[i]==null){
				objectPointer[i] = new WeaselObject(weaselClass, parent, arrayLength);
				return i+1;
			}
		}
		throw new WeaselNativeException("Out of Memory");
	}
	
	public int createBooleanObject(boolean value) {
		WeaselClass weaselClass = getWeaselClass("OBoolean;");
		int obj = createObject(weaselClass, 0);
		WeaselField field = weaselClass.getField("value");
		field.setBoolean(getObject(obj), value);
		return obj;
	}

	public int createCharObject(char value) {
		WeaselClass weaselClass = getWeaselClass("OChar;");
		int obj = createObject(weaselClass, 0);
		WeaselField field = weaselClass.getField("value");
		field.setChar(getObject(obj), value);
		return obj;
	}

	public int createByteObject(byte value) {
		WeaselClass weaselClass = getWeaselClass("OByte;");
		int obj = createObject(weaselClass, 0);
		WeaselField field = weaselClass.getField("value");
		field.setByte(getObject(obj), value);
		return obj;
	}

	public int createShortObject(short value) {
		WeaselClass weaselClass = getWeaselClass("OShort;");
		int obj = createObject(weaselClass, 0);
		WeaselField field = weaselClass.getField("value");
		field.setShort(getObject(obj), value);
		return obj;
	}

	public int createIntObject(int value) {
		WeaselClass weaselClass = getWeaselClass("OInteger;");
		int obj = createObject(weaselClass, 0);
		WeaselField field = weaselClass.getField("value");
		field.setInt(getObject(obj), value);
		return obj;
	}

	public int createLongObject(long value) {
		WeaselClass weaselClass = getWeaselClass("OLong;");
		int obj = createObject(weaselClass, 0);
		WeaselField field = weaselClass.getField("value");
		field.setLong(getObject(obj), value);
		return obj;
	}

	public int createDoubleObject(double value) {
		WeaselClass weaselClass = getWeaselClass("ODouble;");
		int obj = createObject(weaselClass, 0);
		WeaselField field = weaselClass.getField("value");
		field.setDouble(getObject(obj), value);
		return obj;
	}

	public int createFloatObject(float value) {
		WeaselClass weaselClass = getWeaselClass("OFloat;");
		int obj = createObject(weaselClass, 0);
		WeaselField field = weaselClass.getField("value");
		field.setFloat(getObject(obj), value);
		return obj;
	}

	public int createStringObject(String value) {
		WeaselClass weaselClass = getWeaselClass("OString;");
		int obj = createObject(weaselClass, 0);
		WeaselField field = weaselClass.getField("value");
		int size = value.length();
		int array = createArrayObject(size, "C");
		WeaselObject arrayObject = getObject(array);
		for(int i=0; i<size; i++){
			setArrayIndexChar(arrayObject, i, value.charAt(i));
		}
		field.setObject(getObject(obj), array);
		return obj;
	}
	
	public int createArrayObject(int length, String className) {
		WeaselClass weaselClass = getWeaselClass("["+className);
		return createObject(weaselClass, length);
	}
	
	public int createRuntimeException(WeaselRuntimeException wre) {
		WeaselClass weaselClass = getWeaselClass("OException;");
		int obj = createObject(weaselClass, 0);
		WeaselField field = weaselClass.getField("message");
		field.setObject(getObject(obj), createStringObject(wre.getMessage()));
		return obj;
	}
	
	public void setArrayIndexObject(WeaselObject array, int index, int obj) {
		WeaselObject object = getObject(obj);
		if(!array.getWeaselClass().getArrayClass().isPrimitive()){
			WeaselChecks.checkArray(array, index, object==null?array.getWeaselClass().getArrayClass():object.getWeaselClass());
			array.objectRefs[index] = obj;
		}else{
			WeaselField field = object.getWeaselClass().getField("value");
			switch(WeaselPrimitive.getPrimitiveID(array.getWeaselClass().getArrayClass())){
			case WeaselPrimitive.BOOLEAN:
				setArrayIndexBoolean(array, index, field.getBoolean(object));
			case WeaselPrimitive.CHAR:
				setArrayIndexChar(array, index, field.getChar(object));
			case WeaselPrimitive.BYTE:
				setArrayIndexByte(array, index, field.getByte(object));
			case WeaselPrimitive.SHORT:
				setArrayIndexShort(array, index, field.getShort(object));
			case WeaselPrimitive.INT:
				setArrayIndexInt(array, index, field.getInt(object));
			case WeaselPrimitive.LONG:
				setArrayIndexLong(array, index, field.getLong(object));
			case WeaselPrimitive.FLOAT:
				setArrayIndexFloat(array, index, field.getFloat(object));
			case WeaselPrimitive.DOUBLE:
				setArrayIndexDouble(array, index, field.getDouble(object));
			}
		}
	}
	
	public void setArrayIndexBoolean(WeaselObject array, int index, boolean value) {
		WeaselChecks.checkArray(array, index, booleanClass);
		array.easyTypes[index+1] = value?-1:0;
	}
	
	public void setArrayIndexChar(WeaselObject array, int index, char value) {
		WeaselChecks.checkArray(array, index, charClass);
		array.easyTypes[index+1] = value;
	}
	
	public void setArrayIndexByte(WeaselObject array, int index, byte value) {
		WeaselChecks.checkArray(array, index, byteClass);
		array.easyTypes[index+1] = value;
	}
	
	public void setArrayIndexShort(WeaselObject array, int index, short value) {
		WeaselChecks.checkArray(array, index, shortClass);
		array.easyTypes[index+1] = value;
	}
	
	public void setArrayIndexInt(WeaselObject array, int index, int value) {
		WeaselChecks.checkArray(array, index, intClass);
		array.easyTypes[index+1] = value;
	}
	
	public void setArrayIndexLong(WeaselObject array, int index, long value) {
		WeaselChecks.checkArray(array, index, longClass);
		array.easyTypes[index*2+1] = (int)(value>>32);
		array.easyTypes[index*2+2] = (int)(value);
	}
	
	public void setArrayIndexFloat(WeaselObject array, int index, float value) {
		WeaselChecks.checkArray(array, index, floatClass);
		array.easyTypes[index+1] = Float.floatToRawIntBits(value);
	}
	
	public void setArrayIndexDouble(WeaselObject array, int index, double value) {
		WeaselChecks.checkArray(array, index, doubleClass);
		long l = Double.doubleToRawLongBits(value);
		array.easyTypes[index*2+1] = (int)(l>>32);
		array.easyTypes[index*2+2] = (int)(l);
	}
	
	public int getArrayIndexObject(WeaselObject array, int index) {
		switch(WeaselPrimitive.getPrimitiveID(array.getWeaselClass().getArrayClass())){
		case WeaselPrimitive.BOOLEAN:
			return createBooleanObject(getArrayIndexBoolean(array, index));
		case WeaselPrimitive.CHAR:
			return createCharObject(getArrayIndexChar(array, index));
		case WeaselPrimitive.BYTE:
			return createByteObject(getArrayIndexByte(array, index));
		case WeaselPrimitive.SHORT:
			return createShortObject(getArrayIndexShort(array, index));
		case WeaselPrimitive.INT:
			return createIntObject(getArrayIndexInt(array, index));
		case WeaselPrimitive.LONG:
			return createLongObject(getArrayIndexLong(array, index));
		case WeaselPrimitive.FLOAT:
			return createFloatObject(getArrayIndexFloat(array, index));
		case WeaselPrimitive.DOUBLE:
			return createDoubleObject(getArrayIndexDouble(array, index));
		default:
			WeaselChecks.checkArray2(array, index, getWeaselClass("OObject;"));
			return array.objectRefs[index];
		}
	}
	
	public boolean getArrayIndexBoolean(WeaselObject array, int index) {
		WeaselChecks.checkArray2(array, index, booleanClass);
		return array.easyTypes[index+1]!=0;
	}
	
	public char getArrayIndexChar(WeaselObject array, int index) {
		WeaselChecks.checkArray2(array, index, charClass);
		return (char)array.easyTypes[index+1];
	}
	
	public byte getArrayIndexByte(WeaselObject array, int index) {
		WeaselChecks.checkArray2(array, index, byteClass);
		return (byte)array.easyTypes[index+1];
	}
	
	public short getArrayIndexShort(WeaselObject array, int index) {
		WeaselChecks.checkArray2(array, index, shortClass);
		return (short)array.easyTypes[index+1];
	}
	
	public int getArrayIndexInt(WeaselObject array, int index) {
		WeaselChecks.checkArray2(array, index, intClass);
		return array.easyTypes[index+1];
	}
	
	public long getArrayIndexLong(WeaselObject array, int index) {
		WeaselChecks.checkArray2(array, index, longClass);
		return (long)array.easyTypes[index*2+1]<<32 | (long)array.easyTypes[index*2+2];
	}
	
	public float getArrayIndexFloat(WeaselObject array, int index) {
		WeaselChecks.checkArray2(array, index, floatClass);
		return Float.intBitsToFloat(array.easyTypes[index+1]);
	}
	
	public double getArrayIndexDouble(WeaselObject array, int index) {
		WeaselChecks.checkArray2(array, index, doubleClass);
		return Double.longBitsToDouble((long)array.easyTypes[index*2+1]<<32 | (long)array.easyTypes[index*2+2]);
	}
	
	public WeaselMethodBody getMethodByDesk(String methodDesk) {
		int openBreak = methodDesk.indexOf('(');
		int lastDot = methodDesk.lastIndexOf('.', openBreak);
		String className = methodDesk.substring(0, lastDot);
		String name = methodDesk.substring(lastDot+1, openBreak);
		String desk = methodDesk.substring(openBreak);
		WeaselClass weaselClass = getWeaselClass("O"+className+";");
		WeaselMethod method = weaselClass.getMethod(name, desk);
		return method.getMethodFromClass(weaselClass);
	}
	
	private WeaselThread getNextThread(){
		WeaselThread thread = threads.get(activeThreadID);
		thread.sleepUpdate();
		if(thread.getThreadState()!=State.RUNNING){
			int startThread = activeThreadID;
			do{
				activeThreadID++;
				if(activeThreadID>=threads.size()){
					activeThreadID = 0;
				}
				if(activeThreadID==startThread)
					return null;
				thread = threads.get(activeThreadID);
				thread.sleepUpdate();
			}while(thread.getThreadState()==State.RUNNING);
		}
		return thread;
	}
	
	public int run(int numInstructions){
		WeaselThread thread;
		while(numInstructions>0){
			thread = getNextThread();
			if(thread==null){
				return numInstructions;
			}
			thread.runNextInstruction();
			numInstructions--;
		}
		return 0;
	}
	
	public void sync(WeaselThread thread, String token){
		Synchronized sync = synchronizeds.get(token);
		if(sync==null){
			sync = new Synchronized(thread);
			synchronizeds.put(token, sync);
		}else{
			sync.addSync(thread);
		}
	}
	
	public void endSync(WeaselThread thread, String token){
		Synchronized sync = synchronizeds.get(token);
		if(sync!=null){
			if(sync.removeSync(thread)){
				synchronizeds.remove(token);
			}
		}
	}
	
	protected WeaselMethod compilerCreateMethod(String name, int modifier, WeaselClass parentClass, WeaselClass returnParam, WeaselClass[] params, int id){
		return new WeaselMethod(name, modifier, parentClass, returnParam, params, id);
	}
	
	protected WeaselField compilerCreateField(String name, int modifier, WeaselClass weaselClass, WeaselClass type, int id) {
		return new WeaselField(name, modifier, weaselClass, type, id);
	}
	
	public WeaselNativeMethod getNativeMethod(String name){
		return nativeMethods.get(name);
	}
	
	public void registerNativeMethod(String name, WeaselNativeMethod nativeMethod){
		nativeMethods.put(name, nativeMethod);
	}
	
	private class Synchronized implements WeaselSaveable{
		
		private WeaselThread owner;
		private int num;
		private List<WeaselThread> waitings = new ArrayList<WeaselThread>();
		
		public Synchronized(WeaselThread owner) {
			this.owner = owner;
			num = 1;
		}
		
		public boolean removeSync(WeaselThread thread) {
			if(thread==owner){
				if(--num<=0){
					if(waitings.isEmpty()){
						owner = null;
					}else{
						owner = waitings.remove(0);
						owner.setWaiting(false);
					}
				}
			}
			return owner==null;
		}

		public void addSync(WeaselThread thread){
			if(owner==thread){
				num++;
			}else{
				waitings.add(thread);
				thread.setWaiting(true);
			}
		}
		
		public Synchronized(WeaselInterpreter interpreter, DataInputStream dataInputStream) throws IOException {
			owner = interpreter.threads.get(dataInputStream.readInt());
			num = dataInputStream.readInt();
			int count = dataInputStream.readInt();
			for(int i=0; i<count; i++){
				waitings.add(interpreter.threads.get(dataInputStream.readInt()));
			}
		}

		@Override
		public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
			dataOutputStream.writeInt(owner.interpreter.threads.indexOf(owner));
			dataOutputStream.writeInt(num);
			dataOutputStream.writeInt(waitings.size());
			for(WeaselThread waiting:waitings){
				dataOutputStream.writeInt(owner.interpreter.threads.indexOf(waiting));
			}
		}
		
	}
	
}
