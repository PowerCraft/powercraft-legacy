package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionLoadConstShort extends WeaselInstruction {

	private final short value;
	
	public WeaselInstructionLoadConstShort(short value){
		this.value = value;
	}
	
	public WeaselInstructionLoadConstShort(DataInputStream dataInputStream) throws IOException{
		value = dataInputStream.readShort();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		thread.pushValue(value);
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeShort(value);
	}

	@Override
	public String toString() {
		return ""+value;
	}
	
}
