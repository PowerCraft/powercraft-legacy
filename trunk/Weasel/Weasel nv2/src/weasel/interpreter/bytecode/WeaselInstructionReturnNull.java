package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionReturnNull extends WeaselInstruction {

	private final int pop;
	
	public WeaselInstructionReturnNull(int pop){
		this.pop = pop;
	}
	
	public WeaselInstructionReturnNull(DataInputStream dataInputStream) throws IOException{
		pop = dataInputStream.readInt();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		for(int i=0; i<pop; i++)
			thread.pop();
		thread.callReturn();
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(pop);
	}

	@Override
	public String toString() {
		return "return null "+pop;
	}

}
