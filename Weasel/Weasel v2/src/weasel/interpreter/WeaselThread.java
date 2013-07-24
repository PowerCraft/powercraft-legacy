package weasel.interpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionNoTime;

public class WeaselThread implements WeaselSaveable, WeaselNameable{

	private final WeaselInterpreter interpreter;
	private final String name;
	private final int stack[];
	private int stackPointer;
	private WeaselMethodInfo methodInfo;
	private long sleepTime=0;
	private long lastRunTime;
	private boolean waiting;
	private int exception;
	
	public WeaselThread(WeaselInterpreter interpreter, int stackSize){
		this.interpreter = interpreter;
		stack = new int[stackSize];
		name = "";
	}

	public WeaselThread(WeaselInterpreter interpreter, DataInputStream dataInputStream) throws IOException {
		this.interpreter = interpreter;
		name = dataInputStream.readUTF();
		int stackSize = dataInputStream.readInt();
		stack = new int[stackSize];
		stackPointer = dataInputStream.readInt();
		for(int i=0; i<stackPointer; i++){
			stack[i] = dataInputStream.readInt();
		}
		methodInfo = new WeaselMethodInfo(interpreter, this, dataInputStream);
		sleepTime = dataInputStream.readLong();
		lastRunTime = System.currentTimeMillis();
		waiting = dataInputStream.readBoolean();
		exception = dataInputStream.readInt();
	}

	@Override
	public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeUTF(name);
		dataOutputStream.writeInt(stack.length);
		dataOutputStream.writeInt(stackPointer);
		for(int i=0; i<stackPointer; i++){
			dataOutputStream.writeInt(stack[i]);
		}
		methodInfo.saveToDataStream(dataOutputStream);
		dataOutputStream.writeLong(sleepTime);
		dataOutputStream.writeBoolean(waiting);
		dataOutputStream.writeInt(exception);
	}

	public State getThreadState() {
		if(methodInfo==null)
			return State.TERMINATED;
		if(sleepTime>0)
			return State.SLEEPING;
		if(waiting)
			return State.WAITING;
		return State.RUNNING;
	}
	
	public static enum State{
		RUNNING,
		WAITING,
		TERMINATED,
		SLEEPING
	}

	public void sleepUpdate(){
		long currentTime = System.currentTimeMillis();
		sleepTime -= currentTime-lastRunTime;
		lastRunTime = currentTime;
		if(sleepTime<=0)
			sleepTime = 0;
	}
	
	public void runNextInstruction() {
		WeaselInstruction instrucion;
		do{
			while(true){
				instrucion = methodInfo.getNextInstruction();
				if(instrucion!=null)
					break;
				methodInfo = methodInfo.getCaller();
				if(methodInfo==null)
					return;
			}
			try{
				instrucion.run(interpreter, this, methodInfo);
			}catch(Throwable e){
				WeaselRuntimeException wre;
				if(e instanceof WeaselRuntimeException){
					wre = (WeaselRuntimeException)e;
				}else{
					wre = new WeaselRuntimeException(e);
				}
				wre.setWeaselThread(this);
				setException(0);
			}
			if(exception!=0){
				while(true){
					boolean anyCatch = methodInfo.gotoCatchForClass(interpreter.getObject(exception).getWeaselClass());
					if(anyCatch)
						break;
					methodInfo = methodInfo.getCaller();
					if(methodInfo==null)
						return;
				}
			}
		}while(instrucion instanceof WeaselInstructionNoTime);
	}
	
	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}

	@Override
	public String toString() {
		return "Thread-"+name;
	}

	public void sleep(int toSleep) {
		sleepTime = toSleep;
	}
	
	public StackTraceElement[] getStackTrace(){
		return methodInfo.getStackTrace().toArray(new StackTraceElement[0]);
	}
	
	public void setException(int exception){
		this.exception = exception;
	}
	
	public int getException() {
		return exception;
	}

	@Override
	public String getName() {
		return name;
	}

	public int pop(){
		if(stackPointer==0){
			throw new WeaselRuntimeException("Stack underflow");
		}
		return stack[--stackPointer];
	}
	
	public int push(){
		if(stackPointer==stack.length){
			throw new WeaselRuntimeException("Stack overflow");
		}
		return stack[stackPointer++];
	}
	
	public int getStackPointer(){
		return stackPointer;
	}
	
	public void setStackPointer(int stackPointer){
		this.stackPointer = stackPointer;
	}
	
}
