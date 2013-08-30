package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionPops extends WeaselInstruction {
	
	private final int pops;
	
	public WeaselInstructionPops(int pops){
		this.pops = pops;
	}
	
	public WeaselInstructionPops(DataInputStream dataInputStream) throws IOException{
		pops = dataInputStream.readInt();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		for(int i=0; i<pops; i++){
			thread.pop();
		}
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(pops);
	}

	@Override
	public String toString() {
		return "pops "+pops;
	}
	
}
