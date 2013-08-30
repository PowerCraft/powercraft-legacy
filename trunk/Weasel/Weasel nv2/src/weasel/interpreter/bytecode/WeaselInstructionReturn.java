package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;
import weasel.interpreter.WeaselThread.StackElement;

public class WeaselInstructionReturn extends WeaselInstruction {

	private final int pop;
	
	public WeaselInstructionReturn(int pop){
		this.pop = pop;
	}
	
	public WeaselInstructionReturn(DataInputStream dataInputStream) throws IOException{
		pop = dataInputStream.readInt();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		StackElement se = thread.pop();
		for(int i=0; i<pop; i++)
			thread.pop();
		thread.push(se);
		thread.callReturn();
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(pop);
	}

	@Override
	public String toString() {
		return "return "+pop;
	}

}
