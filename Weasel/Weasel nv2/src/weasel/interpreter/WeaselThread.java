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
				setException(interpreter.baseTypes.createRuntimeException(wre));
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
	
	public void call(WeaselMethodBody methodBody){
		methodExecutor = new WeaselMethodExecutor(this, methodBody, methodExecutor);
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
	
	public Object popValue(){
		return pop().value;
	}
	
	public int popObject(){
		return pop().object;
	}
	
	public void push(StackElement value){
		if(stackPointer==stack.length){
			throw new WeaselNativeException("Stack overflow");
		}
		stack[stackPointer++] = value;
	}
	
	public void pushValue(Object value){
		push(new StackElement(value));
	}
	
	public void pushObject(int value){
		push(new StackElement(value));
	}
	
	public StackElement get(int pos){
		if(pos<0||pos>=stackPointer)
			throw new WeaselNativeException("Stack out of bounds %s %s", pos, stackPointer);
		StackElement se = stack[pos];
		return se;
	}
	
	public Object getValue(int pos){
		return get(pos).value;
	}
	
	public int getObject(int pos){
		return get(pos).object;
	}
	
	public void set(int pos, StackElement se){
		if(pos<0||pos>=stackPointer)
			throw new WeaselNativeException("Stack out of bounds %s %s", pos, stackPointer);
		stack[pos] = se;
	}
	
	public void setValue(int pos, Object value){
		get(pos).value = value;
	}
	
	public void setObject(int pos, int value){
		get(pos).object = value;
	}
	
	public int getStackPointer(){
		return stackPointer;
	}
	
	public void setStackPointer(int stackPointer){
		this.stackPointer = stackPointer;
	}
	
	public void markKnownObjects() {
		for(int i=0; i<stackPointer; i++){
			if(stack[i].value==null){
				interpreter.getObject(stack[i].object).markVisible();
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
	
	public static class StackElement implements WeaselSaveable{
		public Object value;
		public int object;
		
		public StackElement(Object value) {
			this.value = value;
		}
		
		public StackElement(int object) {
			this.object = object;
		}

		public StackElement(DataInputStream dataInputStream) throws IOException {
			int t = dataInputStream.readInt();
			switch(t){
			case 1:
				object = dataInputStream.readInt();
				break;
			case 2:
				value = dataInputStream.readBoolean();
				break;
			case 3:
				value = dataInputStream.readChar();
				break;
			case 4:
				value = dataInputStream.readByte();
				break;
			case 5:
				value = dataInputStream.readShort();
				break;
			case 6:
				value = dataInputStream.readInt();
				break;
			case 7:
				value = dataInputStream.readLong();
				break;
			case 8:
				value = dataInputStream.readFloat();
				break;
			case 9:
				value = dataInputStream.readDouble();
				break;
			}
		}
		
		@Override
		public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
			if(value==null){
				dataOutputStream.writeInt(1);
				dataOutputStream.writeInt(object);
			}else{
				Class<?> c = value.getClass();
				if(c==Boolean.class){
					dataOutputStream.writeInt(2);
					dataOutputStream.writeBoolean((Boolean)value);
				}else if(c==Character.class){
					dataOutputStream.writeInt(3);
					dataOutputStream.writeChar((Character)value);
				}else if(c==Byte.class){
					dataOutputStream.writeInt(4);
					dataOutputStream.writeByte((Byte)value);
				}else if(c==Short.class){
					dataOutputStream.writeInt(5);
					dataOutputStream.writeShort((Short)value);
				}else if(c==Integer.class){
					dataOutputStream.writeInt(6);
					dataOutputStream.writeInt((Integer)value);
				}else if(c==Long.class){
					dataOutputStream.writeInt(7);
					dataOutputStream.writeLong((Long)value);
				}else if(c==Float.class){
					dataOutputStream.writeInt(8);
					dataOutputStream.writeFloat((Float)value);
				}else if(c==Double.class){
					dataOutputStream.writeInt(9);
					dataOutputStream.writeDouble((Double)value);
				}
			}
		}
	}
	
	
	
}
