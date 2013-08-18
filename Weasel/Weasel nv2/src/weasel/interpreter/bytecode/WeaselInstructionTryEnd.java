package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionTryEnd extends WeaselInstructionNoTime {
	
	public WeaselInstructionTryEnd(){}
	
	public WeaselInstructionTryEnd(DataInputStream dataInputStream){}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		method.endTry();
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {}

}
