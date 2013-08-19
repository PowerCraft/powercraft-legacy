package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionJump extends WeaselInstruction {

	private final int jumpTo;
	
	public WeaselInstructionJump(int jumpTo){
		this.jumpTo = jumpTo;
	}
	
	public WeaselInstructionJump(DataInputStream dataInputStream) throws IOException{
		jumpTo = dataInputStream.readInt();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		method.jumpTo(jumpTo);
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(jumpTo);
	}

}
