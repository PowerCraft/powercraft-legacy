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

	public final WeaselBaseTypes baseTypes;
	
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
		baseTypes = new WeaselBaseTypes(this);
	}
	
	public WeaselInterpreter(DataInputStream dataInputStream) throws IOException{
		int memorySize = dataInputStream.readInt();
		objectPointer = new WeaselObject[memorySize];
		loadedClasses = new HashMap<String, WeaselClass>();
		synchronizeds = new HashMap<String, Synchronized>();
		baseTypes = new WeaselBaseTypes(this);
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
	
	public final WeaselClass getWeaselClass(String name){
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
				if(objectPointer.length>0){
					weaselClass.setClassObject(baseTypes.createClassObject(weaselClass));
				}
			}
			if(names.length==1)
				return weaselClass;
			return weaselClass.getChildClass(names[1]);
		case 'N':
			return baseTypes.booleanClass;
		case 'C':
			return baseTypes.charClass;
		case 'B':
			return baseTypes.byteClass;
		case 'S':
			return baseTypes.shortClass;
		case 'I':
			return baseTypes.intClass;
		case 'L':
			return baseTypes.longClass;
		case 'F':
			return baseTypes.floatClass;
		case 'D':
			return baseTypes.doubleClass;
		case 'V':
			return baseTypes.voidClass;
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
	
	protected WeaselMethod compilerCreateMethod(String name, int modifier, WeaselClass parentClass, WeaselGenericClassInfo genericReturn, WeaselGenericClassInfo[] genericParams, int id){
		return new WeaselMethod(name, modifier, parentClass, genericReturn, genericParams, id);
	}
	
	protected WeaselField compilerCreateField(String name, int modifier, WeaselClass weaselClass, WeaselGenericClassInfo typeInfo, int id) {
		return new WeaselField(name, modifier, weaselClass, typeInfo, id);
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
