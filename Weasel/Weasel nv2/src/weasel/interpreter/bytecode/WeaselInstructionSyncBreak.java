package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionSyncBreak extends WeaselInstruction {

	private final String token;
	
	public WeaselInstructionSyncBreak(String token){
		this.token = token;
	}
	
	public WeaselInstructionSyncBreak(DataInputStream dataInputStream) throws IOException{
		token = dataInputStream.readUTF();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		interpreter.endSync(thread, token);
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeUTF(token);
	}

	@Override
	public String toString() {
		return "sync break "+token;
	}
	
}
