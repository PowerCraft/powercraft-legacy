package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselObject;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionReadIndex extends WeaselInstruction {

	private final int primitiveID;
	
	public WeaselInstructionReadIndex(int primitiveID){
		this.primitiveID = primitiveID;
	}
	
	public WeaselInstructionReadIndex(DataInputStream dataInputStream) throws IOException{
		primitiveID = dataInputStream.readInt();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		int index = (Integer)thread.popValue();
		WeaselObject array = interpreter.getObject(thread.popObject());
		switch(primitiveID){
		case WeaselPrimitive.BOOLEAN:
			thread.pushValue(interpreter.baseTypes.getArrayBoolean(array, index));
		case WeaselPrimitive.CHAR:
			thread.pushValue(interpreter.baseTypes.getArrayChar(array, index));
		case WeaselPrimitive.BYTE:
			thread.pushValue(interpreter.baseTypes.getArrayByte(array, index));
		case WeaselPrimitive.SHORT:
			thread.pushValue(interpreter.baseTypes.getArrayShort(array, index));
		case WeaselPrimitive.INT:
			thread.pushValue(interpreter.baseTypes.getArrayInt(array, index));
		case WeaselPrimitive.LONG:
			thread.pushValue(interpreter.baseTypes.getArrayLong(array, index));
		case WeaselPrimitive.FLOAT:
			thread.pushValue(interpreter.baseTypes.getArrayFloat(array, index));
		case WeaselPrimitive.DOUBLE:
			thread.pushValue(interpreter.baseTypes.getArrayDouble(array, index));
		default:
			thread.pushObject(interpreter.baseTypes.getArrayObject(array, index));
		}
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(primitiveID);
	}

	@Override
	public String toString() {
		return "readIndex "+WeaselPrimitive.primitiveNames[primitiveID];
	}
	
}
