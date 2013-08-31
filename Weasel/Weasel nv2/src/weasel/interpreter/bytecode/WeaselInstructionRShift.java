package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionRShift extends WeaselInstruction {

	private final int primitiveID;
	
	public WeaselInstructionRShift(int primitiveID){
		this.primitiveID = primitiveID;
	}
	
	public WeaselInstructionRShift(DataInputStream dataInputStream) throws IOException{
		primitiveID = dataInputStream.readInt();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		Object o1 = thread.popValue();
		Object o2 = thread.popValue();
		
		switch(primitiveID){
		case WeaselPrimitive.CHAR:
			thread.pushValue((Character)o1>>(Integer)o2);
			break;
		case WeaselPrimitive.BYTE:
			thread.pushValue((Byte)o1>>(Integer)o2);
			break;
		case WeaselPrimitive.SHORT:
			thread.pushValue((Short)o1>>(Integer)o2);
			break;
		case WeaselPrimitive.INT:
			thread.pushValue((Integer)o1>>(Integer)o2);
			break;
		case WeaselPrimitive.LONG:
			thread.pushValue((Long)o1>>(Integer)o2);
			break;
		}
		
	}
	
	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(primitiveID);
	}

	@Override
	public String toString() {
		return "rsh "+WeaselPrimitive.primitiveNames[primitiveID];
	}
	
}
