package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionPop extends WeaselInstruction {
	
	public WeaselInstructionPop(){}
	
	public WeaselInstructionPop(DataInputStream dataInputStream){}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		thread.pop();
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {}

	@Override
	public String toString() {
		return "pop";
	}
	
}
