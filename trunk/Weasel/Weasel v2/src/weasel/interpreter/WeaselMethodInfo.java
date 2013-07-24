package weasel.interpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionCatch;
import weasel.interpreter.bytecode.WeaselInstructionLine;
import weasel.interpreter.bytecode.WeaselInstructionTry;
import weasel.interpreter.bytecode.WeaselInstructionTryEnd;

public class WeaselMethodInfo implements WeaselSaveable {

	private final WeaselInterpreter interpreter;
	private final WeaselThread thread;
	private final WeaselMethod method;
	private final WeaselMethodInfo caller;
	private int programPointer;
	private int line;
	private List<Integer> tryStackPointer = new ArrayList<Integer>();
	
	public WeaselMethodInfo(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethod method, WeaselMethodInfo caller){
		this.interpreter = interpreter;
		this.thread = thread;
		this.caller = caller;
		this.method = method;
	}
	
	public WeaselMethodInfo(WeaselInterpreter interpreter, WeaselThread thread, DataInputStream dataInputStream) throws IOException {
		this.interpreter = interpreter;
		this.thread = thread;
		method = interpreter.getMethodByDesk(dataInputStream.readUTF());
		programPointer = dataInputStream.readInt();
		if(dataInputStream.readBoolean()){
			caller = new WeaselMethodInfo(interpreter, thread, dataInputStream);
		}else{
			caller = null;
		}
		int tryStackPointerSize = dataInputStream.readInt();
		for(int i=0; i<tryStackPointerSize; i++){
			tryStackPointer.add(dataInputStream.readInt());
		}
		line = dataInputStream.readInt();
	}

	@Override
	public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeUTF(method.getNameAndDesk());
		dataOutputStream.writeInt(programPointer);
		dataOutputStream.writeBoolean(caller!=null);
		if(caller!=null){
			caller.saveToDataStream(dataOutputStream);
		}
		dataOutputStream.writeInt(tryStackPointer.size());
		for(Integer i:tryStackPointer){
			dataOutputStream.writeInt(i);
		}
		dataOutputStream.writeInt(line);
	}

	public WeaselInstruction getNextInstruction() {
		return method.getInstruction(programPointer++);
	}

	public StackTraceElement getStackTraceElement() {
		return new StackTraceElement(method.getParentClass().getName(), method.getName(), method.getParentClass().getFileName(), line);
	}

	public void setLine(int line){
		this.line = line;
	}

	public boolean gotoCatchForClass(WeaselClass weaselClass) {
		int newOpened = 0;
		WeaselInstruction instruction;
		while(true){
			instruction = method.getInstruction(programPointer++);
			if(instruction==null)
				return false;
			if(instruction instanceof WeaselInstructionTry){
				newOpened++;
			}else if(instruction instanceof WeaselInstructionTryEnd){
				if(newOpened>0){
					newOpened--;
				}else{
					endTry();
				}
			}else if(instruction instanceof WeaselInstructionLine){
				instruction.run(interpreter, thread, this);
			}else if(instruction instanceof WeaselInstructionCatch){
				if(newOpened==0){
					if(weaselClass.canBeCastTo(((WeaselInstructionCatch)instruction).getAceptedExceptionClass(interpreter))){
						programPointer--;
						return true;
					}
				}
			}
		}
	}

	public WeaselMethodInfo getCaller() {
		return caller;
	}

	public List<StackTraceElement> getStackTrace() {
		List<StackTraceElement> list;
		if(caller==null){
			list = new ArrayList<StackTraceElement>();
		}else{
			list = getStackTrace();
		}
		list.add(getStackTraceElement());
		return list;
	}

	public void startTry() {
		tryStackPointer.add(0, thread.getStackPointer());
	}
	
	public void endTry() {
		thread.setStackPointer(tryStackPointer.remove(0));
	}
	
}
