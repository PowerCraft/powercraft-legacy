package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionLoadConstDouble extends WeaselInstruction {

	private final double value;
	
	public WeaselInstructionLoadConstDouble(double value){
		this.value = value;
	}
	
	public WeaselInstructionLoadConstDouble(DataInputStream dataInputStream) throws IOException{
		value = dataInputStream.readDouble();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		thread.pushValue(value);
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeDouble(value);
	}

	@Override
	public String toString() {
		return ""+value;
	}
	
}
