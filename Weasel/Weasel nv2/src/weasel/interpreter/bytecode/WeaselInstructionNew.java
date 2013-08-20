package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselObject;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionNew extends WeaselInstruction {
	
	public WeaselInstructionNew(){}
	
	public WeaselInstructionNew(DataInputStream dataInputStream){}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		WeaselObject object = interpreter.getObject(thread.popObject());
		WeaselClass weaselClass = interpreter.baseTypes.getClassClass(object);
		int obj = interpreter.createObject(weaselClass);
		thread.pushObject(obj);
	}
	
	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {}

}
