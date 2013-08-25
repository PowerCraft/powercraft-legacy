package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionLoadConstBoolean extends WeaselInstruction {

	private final boolean value;
	
	public WeaselInstructionLoadConstBoolean(boolean value){
		this.value = value;
	}
	
	public WeaselInstructionLoadConstBoolean(DataInputStream dataInputStream) throws IOException{
		value = dataInputStream.readBoolean();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		thread.pushValue(value);
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeBoolean(value);
	}

	@Override
	public String toString() {
		return ""+value;
	}
	
}
