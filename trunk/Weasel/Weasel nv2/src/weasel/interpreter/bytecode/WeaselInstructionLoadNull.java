package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionLoadNull extends WeaselInstruction {

	public WeaselInstructionLoadNull(){}
	
	public WeaselInstructionLoadNull(DataInputStream dataInputStream){}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		thread.pushObject(0);
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {}

	@Override
	public String toString() {
		return "null";
	}
	
}
