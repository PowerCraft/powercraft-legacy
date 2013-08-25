package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionIf extends WeaselInstructionJump {

	public WeaselInstructionIf() {
	}
	
	public WeaselInstructionIf(WeaselInstruction target) {
		super(target);
	}
	
	public WeaselInstructionIf(DataInputStream dataInputStream) throws IOException {
		super(dataInputStream);
	}

	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		Boolean b = (Boolean)thread.popValue();
		if(!b){
			super.run(interpreter, thread, method);
		}
	}

}
