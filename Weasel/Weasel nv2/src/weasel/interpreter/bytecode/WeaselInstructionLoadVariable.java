package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionLoadVariable extends WeaselInstruction {

	private final int pos;
	
	public WeaselInstructionLoadVariable(int pos){
		this.pos = pos;
	}
	
	public WeaselInstructionLoadVariable(DataInputStream dataInputStream) throws IOException{
		pos = dataInputStream.readInt();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		thread.push(thread.get(method.getStackBottom()-pos));
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(pos);
	}

	@Override
	public String toString() {
		return "loadVar "+pos;
	}
	
}
