package weasel.interpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselThread implements WeaselSaveable{

	private final WeaselInterpreter interpreter;
	private final int stack[];
	private final WeaselMethod programStack[];
	private int stackPointer;
	private int programStackPointer;
	private int programPointer;
	private State state = State.RUNNING;
	private Throwable exception;
	
	public WeaselThread(WeaselInterpreter interpreter, int stackSize, int programStackSize){
		this.interpreter = interpreter;
		stack = new int[stackSize];
		programStack = new WeaselMethod[programStackSize];
	}

	public WeaselThread(WeaselInterpreter interpreter, DataInputStream dataInputStream) throws IOException {
		this.interpreter = interpreter;
		int stackSize = dataInputStream.readInt();
		stack = new int[stackSize];
		stackPointer = dataInputStream.readInt();
		for(int i=0; i<stackPointer; i++){
			stack[i] = dataInputStream.readInt();
		}
		int programStackSize = dataInputStream.readInt();
		programStack = new WeaselMethod[programStackSize];
		programStackPointer  = dataInputStream.readInt();
		for(int i=0; i<programStackPointer; i++){
			programStack[i] = interpreter.getMethodByDesk(dataInputStream.readUTF());
		}
		programPointer = dataInputStream.readInt();
		state = State.values()[dataInputStream.readInt()];
	}

	@Override
	public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(stack.length);
		dataOutputStream.writeInt(stackPointer);
		for(int i=0; i<stackPointer; i++){
			dataOutputStream.writeInt(stack[i]);
		}
		dataOutputStream.writeInt(programStack.length);
		dataOutputStream.writeInt(programStackPointer);
		for(int i=0; i<programPointer; i++){
			dataOutputStream.writeUTF(programStack[i].getNameAndDesk());
		}
		dataOutputStream.writeInt(programPointer);
		dataOutputStream.writeInt(state.ordinal());
	}

	public State getThreadState() {
		return state;
	}
	
	public static enum State{
		RUNNING,
		WAITING,
		TERMINATED,
		ERRORED
	}

	public void runNextInstruction() {
		WeaselInstruction instrucion = programStack[programPointer].getInstruction(programPointer++);
		instrucion.run(interpreter, this);
	}
	
	public int pop(){
		return stack[--stackPointer];
	}
	
	public void push(int value){
		stack[stackPointer++] = value;
	}
	
}
