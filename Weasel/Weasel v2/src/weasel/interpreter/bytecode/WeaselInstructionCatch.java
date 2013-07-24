package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodInfo;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionCatch extends WeaselInstruction {

	private final String aceptedExceptionClassName;
	private WeaselClass weaselClass;
	
	public WeaselInstructionCatch(String aceptedExceptionClassName){
		this.aceptedExceptionClassName = aceptedExceptionClassName;
	}
	
	public WeaselInstructionCatch(DataInputStream dataInputStream) throws IOException{
		aceptedExceptionClassName = dataInputStream.readUTF();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodInfo method) {
		
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeUTF(aceptedExceptionClassName);
	}

	public WeaselClass getAceptedExceptionClass(WeaselInterpreter interpreter) {
		if(weaselClass==null){
			weaselClass = interpreter.getClassByName(aceptedExceptionClassName);
		}
		return weaselClass;
	}

}
