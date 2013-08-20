package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionLoadConstChar extends WeaselInstruction {

	private final char value;
	
	public WeaselInstructionLoadConstChar(char value){
		this.value = value;
	}
	
	public WeaselInstructionLoadConstChar(DataInputStream dataInputStream) throws IOException{
		value = dataInputStream.readChar();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		thread.pushValue(value);
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeChar(value);
	}

}
