package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionLoadConstClass extends WeaselInstruction {

	private final String className;
	private int classObject;
	
	public WeaselInstructionLoadConstClass(String value){
		this.className = value;
	}
	
	public WeaselInstructionLoadConstClass(DataInputStream dataInputStream) throws IOException{
		className = dataInputStream.readUTF();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		resolve(interpreter);
		thread.pushObject(classObject);
	}

	private void resolve(WeaselInterpreter interpreter){
		if(classObject==0){
			classObject = interpreter.getWeaselClass(className).getClassObject();
		}
	}
	
	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeUTF(className);
	}

	@Override
	public String toString() {
		return className;
	}

}
