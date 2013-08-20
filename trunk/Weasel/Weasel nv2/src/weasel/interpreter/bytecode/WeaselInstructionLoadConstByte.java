package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionLoadConstByte extends WeaselInstruction {

	private final byte value;
	
	public WeaselInstructionLoadConstByte(byte value){
		this.value = value;
	}
	
	public WeaselInstructionLoadConstByte(DataInputStream dataInputStream) throws IOException{
		value = dataInputStream.readByte();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		thread.pushValue(value);
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeByte(value);
	}

}
