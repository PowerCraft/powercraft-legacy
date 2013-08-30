package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionJumperDummy extends WeaselInstructionNoTime {

	public WeaselInstructionJumperDummy(){}
	
	public WeaselInstructionJumperDummy(DataInputStream dataInputStream){}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream)throws IOException {}

	@Override
	public String toString() {
		return "jumperDummy";
	}

}
