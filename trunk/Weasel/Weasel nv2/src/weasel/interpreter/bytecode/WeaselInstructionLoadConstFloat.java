package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionLoadConstFloat extends WeaselInstruction {

	private final float value;
	
	public WeaselInstructionLoadConstFloat(float value){
		this.value = value;
	}
	
	public WeaselInstructionLoadConstFloat(DataInputStream dataInputStream) throws IOException{
		value = dataInputStream.readFloat();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		thread.pushValue(value);
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeFloat(value);
	}

}
