package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodInfo;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionTry extends WeaselInstructionNoTime {

	public WeaselInstructionTry(){}
	
	public WeaselInstructionTry(DataInputStream dataInputStream){}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodInfo method) {
		method.startTry();
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {}

}
