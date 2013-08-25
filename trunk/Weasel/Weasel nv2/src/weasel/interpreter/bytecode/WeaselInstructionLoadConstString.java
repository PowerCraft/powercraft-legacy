package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionLoadConstString extends WeaselInstruction {

	private final String value;
	
	public WeaselInstructionLoadConstString(String value){
		this.value = value;
	}
	
	public WeaselInstructionLoadConstString(DataInputStream dataInputStream) throws IOException{
		value = dataInputStream.readUTF();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		thread.pushObject(interpreter.baseTypes.createStringObject(value));
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeUTF(value);
	}

	@Override
	public String toString() {
		return "\""+value+"\"";
	}
	
}
