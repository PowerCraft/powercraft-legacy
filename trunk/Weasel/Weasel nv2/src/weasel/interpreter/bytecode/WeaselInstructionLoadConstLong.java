package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionLoadConstLong extends WeaselInstruction {

	private final long value;
	
	public WeaselInstructionLoadConstLong(long value){
		this.value = value;
	}
	
	public WeaselInstructionLoadConstLong(DataInputStream dataInputStream) throws IOException{
		value = dataInputStream.readLong();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		thread.pushValue(value);
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeLong(value);
	}

}
