package weasel.interpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionNoTime;


public final class WeaselThread implements WeaselSaveable {

	protected final WeaselInterpreter interpreter;
	protected final String name;
	protected final StackElement stack[];
	protected int stackPointer;
	protected WeaselMethodExecutor methodExecutor;
	protected long sleepTime=0;
	protected long lastRunTime;
	protected boolean waiting;
	protected int exception;
	
	protected WeaselThread(WeaselInterpreter interpreter, String name, int stackSize){
		this.interpreter = interpreter;
		this.name = name;
		stack = new StackElement[stackSize];
		lastRunTime = System.currentTimeMillis();
	}
	
	public WeaselThread(WeaselInterpreter interpreter, DataInputStream dataInputStream) throws IOException {
		this.interpreter = interpreter;
		name = dataInputStream.readUTF();
		int stackSize = dataInputStream.readInt();
		stack = new StackElement[stackSize];
		stackPointer = dataInputStream.readInt();
		for(int i=0; i<stackPointer; i++){
			stack[i] = new StackElement(dataInputStream);
		}
		if(dataInputStream.readBoolean()){
			methodExecutor = new WeaselMethodExecutor(this, dataInputStream);
		}
		sleepTime = dataInputStream.readLong();
		lastRunTime = System.currentTimeMillis();
		waiting = dataInputStream.readBoolean();
		exception = dataInputStream.readInt();
	}

	@Override
	public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException{
		dataOutputStream.writeUTF(name);
		dataOutputStream.writeInt(stack.length);
		dataOutputStream.writeInt(stackPointer);
		for(int i=0; i<stackPointer; i++){
			stack[i].saveToDataStream(dataOutputStream);
		}
		dataOutputStream.writeBoolean(methodExecutor!=null);
		if(methodExecutor!=null){
			methodExecutor.saveToDataStream(dataOutputStream);
		}
		dataOutputStream.writeLong(sleepTime);
		dataOutputStream.writeBoolean(waiting);
		dataOutputStream.writeInt(exception);
	}
	
	public State getThreadState() {
		if(methodExecutor==null)
			return State.TERMINATED;
		if(sleepTime>0)
			return State.SLEEPING;
		if(waiting)
			return State.WAITING;
		return State.RUNNING;
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
				instrucion = methodExecutor.getNextInstruction();
				if(instrucion!=null)
					break;
				methodExecutor = methodExecutor.getCaller();
				if(methodExecutor==null)
					return;
			}
			try{
				instrucion.run(interpreter, this, methodExecutor);
			}catch(Throwable e){
				WeaselRuntimeException wre;
				if(e instanceof WeaselRuntimeException){
					wre = (WeaselRuntimeException)e;
				}else{
					wre = new WeaselRuntimeException(e, this);
				}
				setException(interpreter.createRuntimeException(wre));
			}
			if(exception!=0){
				while(true){
					boolean anyCatch = methodExecutor.gotoCatchForClass(interpreter.getObject(exception).getWeaselClass());
					if(anyCatch)
						break;
					methodExecutor = methodExecutor.getCaller();
					if(methodExecutor==null)
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
		return methodExecutor.getStackTrace().toArray(new StackTraceElement[0]);
	}
	
	public void setException(int exception){
		this.exception = exception;
	}
	
	public int getException() {
		return exception;
	}

	public String getName() {
		return name;
	}

	public StackElement pop(){
		if(stackPointer==0){
			throw new WeaselNativeException("Stack underflow");
		}
		StackElement se = stack[--stackPointer];
		stack[stackPointer] = null;
		return se;
	}
	
	public int popValue(){
		return pop().value;
	}
	
	public int popObject(){
		return pop().value;
	}
	
	public void pushValue(int value){
		push(new StackElement(value, false));
	}
	
	public void pushObject(int value){
		push(new StackElement(value, true));
	}
	
	public void push(StackElement value){
		if(stackPointer==stack.length){
			throw new WeaselNativeException("Stack overflow");
		}
		stack[stackPointer++] = value;
	}
	
	public int getStackPointer(){
		return stackPointer;
	}
	
	public void setStackPointer(int stackPointer){
		this.stackPointer = stackPointer;
	}
	
	public void markKnownObjects() {
		for(int i=0; i<stackPointer; i++){
			if(stack[i].isObject){
				interpreter.getObject(stack[i].value).markVisible();
			}
		}
		if(exception!=0){
			interpreter.getObject(exception).markVisible();
		}
	}
	
	public static enum State{
		RUNNING,
		WAITING,
		TERMINATED,
		SLEEPING
	}
	
	private static class StackElement implements WeaselSaveable{
		public int value;
		public boolean isObject;
		
		public StackElement(int value, boolean isObject) {
			this.value = value;
			this.isObject = isObject;
		}

		public StackElement(DataInputStream dataInputStream) throws IOException {
			value = dataInputStream.readInt();
			isObject = dataInputStream.readBoolean();
		}
		
		@Override
		public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
			dataOutputStream.writeInt(value);
			dataOutputStream.writeBoolean(isObject);
		}
	}
	
}
