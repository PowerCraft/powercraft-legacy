package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodInfo;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionTryBreak extends WeaselInstructionNoTime {

	public WeaselInstructionTryBreak(){}
	
	public WeaselInstructionTryBreak(DataInputStream dataInputStream){}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodInfo method) {
		method.endTry();
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {}

}
