package weasel.interpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import weasel.interpreter.WeaselThread.State;

public class WeaselInterpreter implements WeaselSaveable {

	private WeaselMemory memory;
	private TreeMap<String, WeaselNativeMethod> nativeMethods = new TreeMap<String, WeaselNativeMethod>();
	private List<WeaselNativeGenerator> nativeGenerators = new ArrayList<WeaselNativeGenerator>();
	private TreeMap<String, WeaselClass> loadedClasses = new TreeMap<String, WeaselClass>();
	private List<WeaselThread> threads = new ArrayList<WeaselThread>();
	private int activeThreadID;
	
	public WeaselInterpreter(){
		memory = new WeaselMemory(this, 1024);
	}
	
	public WeaselInterpreter(DataInputStream dataInputStream) throws IOException{
		int count = dataInputStream.readInt();
		for(int i=0; i<count; i++){
			String className = dataInputStream.readUTF();
			WeaselClass wClass = getClassByName(className);
			wClass.loadFromDataStream(dataInputStream);
		}
		memory = new WeaselMemory(this, dataInputStream);
		int threadCount = dataInputStream.readInt();
		for(int i=0; i<threadCount; i++){
			threads.add(new WeaselThread(this, dataInputStream));
		}
	}
	
	public WeaselClass getClassByName(String className) {
		String classes[] = className.split("\\.", 2);
		WeaselClass wClass = loadedClasses.get(classes[0]);
		if(wClass==null){
			DataInputStream classInputStream = getClassInputStream(classes[0]);
			wClass = new WeaselClass(this, null, classes[0]);
			loadedClasses.put(className, wClass);
			try {
				wClass.loadClassFormInputStream(classInputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(classes.length==1){
			return wClass;
		}
		return wClass.getChildClass(classes[1]);
	}
	
	private DataInputStream getClassInputStream(String className){
		return null;
	}
	
	public void gc() {
		memory.setAllObjectsInvisible();
		memory.deleteInvisibleObjects();
	}

	public WeaselNativeMethod getNativeMethod(String nativeMathodName) {
		WeaselNativeMethod nativeMethod = nativeMethods.get(nativeMathodName);
		if(nativeMethod==null){
			for(WeaselNativeGenerator nativeGenerator:nativeGenerators){
				nativeMethod = nativeGenerator.createNativeMethod(this, nativeMathodName);
				if(nativeMethod!=null){
					nativeMethods.put(nativeMathodName, nativeMethod);
					break;
				}
			}
		}
		if(nativeMethod==null){
			
		}
		return nativeMethod;
	}

	@Override
	public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(loadedClasses.size());
		for(Entry<String, WeaselClass> e:loadedClasses.entrySet()){
			dataOutputStream.writeUTF(e.getKey());
			e.getValue().saveToDataStream(dataOutputStream);
		}
		memory.saveToDataStream(dataOutputStream);
		dataOutputStream.writeInt(threads.size());
		for(WeaselThread thread:threads){
			thread.saveToDataStream(dataOutputStream);
		}
	}

	private WeaselThread getNextThread(){
		WeaselThread thread = threads.get(activeThreadID);
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

	public WeaselMethod getMethodByDesk(String methodDesk) {
		int openBreak = methodDesk.indexOf('(');
		int lastDot = methodDesk.lastIndexOf('.', openBreak);
		String className = methodDesk.substring(0, lastDot);
		String name = methodDesk.substring(lastDot+1, openBreak);
		String desk = methodDesk.substring(openBreak);
		WeaselClass wClass = getClassByName(className);
		WeaselMethod method = wClass.getMethod(name, desk);
		return method;
	}
	
}
