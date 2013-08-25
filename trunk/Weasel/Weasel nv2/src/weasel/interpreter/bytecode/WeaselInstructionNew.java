package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionNew extends WeaselInstruction {
	
	private final String className;
	private WeaselClass weaselClass;
	
	public WeaselInstructionNew(String className){
		this.className = className;
	}
	
	public WeaselInstructionNew(DataInputStream dataInputStream) throws IOException{
		className = dataInputStream.readUTF();
	}

	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		resolve(interpreter);
		int obj = interpreter.createObject(weaselClass);
		thread.pushObject(obj);
	}
	
	public void resolve(WeaselInterpreter interpreter){
		if(weaselClass==null){
			weaselClass = interpreter.getWeaselClass(className);
		}
	}
	
	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeUTF(className);
	}

	@Override
	public String toString() {
		return "new "+className;
	}
	
}
