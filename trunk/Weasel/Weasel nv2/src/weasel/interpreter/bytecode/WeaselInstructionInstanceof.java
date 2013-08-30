package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselObject;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionInstanceof extends WeaselInstruction {

	private WeaselClass weaselClass;
	private final String className;
	
	public WeaselInstructionInstanceof(String className){
		this.className = className;
	}
	
	public WeaselInstructionInstanceof(DataInputStream dataInputStream) throws IOException{
		className = dataInputStream.readUTF();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		resolve(interpreter);
		WeaselObject obj = interpreter.getObject(thread.popObject());
		if(obj==null){
			thread.pushValue(false);
		}else{
			thread.pushValue(obj.getWeaselClass().canCastTo(weaselClass));
		}
	}

	private void resolve(WeaselInterpreter interpreter) {
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
		return "implements "+className;
	}

}
