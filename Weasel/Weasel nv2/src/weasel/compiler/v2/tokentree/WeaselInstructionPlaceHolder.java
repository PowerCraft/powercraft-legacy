package weasel.compiler.v2.tokentree;

import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;
import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselInstructionPlaceHolder extends WeaselInstruction {
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread,
			WeaselMethodExecutor method) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return "Error";
	}

}
