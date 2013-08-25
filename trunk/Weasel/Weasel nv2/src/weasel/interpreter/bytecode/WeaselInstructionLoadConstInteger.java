package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionLoadConstInteger extends WeaselInstruction {

	private final int value;
	
	public WeaselInstructionLoadConstInteger(int value){
		this.value = value;
	}
	
	public WeaselInstructionLoadConstInteger(DataInputStream dataInputStream) throws IOException{
		value = dataInputStream.readInt();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		thread.pushValue(value);
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(value);
	}

	@Override
	public String toString() {
		return ""+value;
	}
	
}
