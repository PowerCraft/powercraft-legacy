package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionSaveVariable extends WeaselInstruction {

	private final int pos;
	
	public WeaselInstructionSaveVariable(int pos){
		this.pos = pos;
	}
	
	public WeaselInstructionSaveVariable(DataInputStream dataInputStream) throws IOException{
		pos = dataInputStream.readInt();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		thread.set(method.getStackBottom()-pos, thread.get(thread.getStackPointer()-1));
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(pos);
	}

	@Override
	public String toString() {
		return "saveVar "+pos;
	}
	
}
