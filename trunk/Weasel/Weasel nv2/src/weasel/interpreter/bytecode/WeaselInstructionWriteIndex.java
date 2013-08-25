package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselObject;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.WeaselThread;
import weasel.interpreter.WeaselThread.StackElement;

public class WeaselInstructionWriteIndex extends WeaselInstruction {

	private final int primitiveID;
	
	public WeaselInstructionWriteIndex(int primitiveID){
		this.primitiveID = primitiveID;
	}
	
	public WeaselInstructionWriteIndex(DataInputStream dataInputStream) throws IOException{
		primitiveID = dataInputStream.readInt();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		StackElement se = thread.pop();
		int index = (Integer)thread.popValue();
		WeaselObject array = interpreter.getObject(thread.popObject());
		thread.push(se);
		switch(primitiveID){
		case WeaselPrimitive.BOOLEAN:
			interpreter.baseTypes.setArrayBoolean(array, index, (Boolean)se.value);
		case WeaselPrimitive.CHAR:
			interpreter.baseTypes.setArrayChar(array, index, (Character)se.value);
		case WeaselPrimitive.BYTE:
			interpreter.baseTypes.setArrayByte(array, index, (Byte)se.value);
		case WeaselPrimitive.SHORT:
			interpreter.baseTypes.setArrayShort(array, index, (Short)se.value);
		case WeaselPrimitive.INT:
			interpreter.baseTypes.setArrayInt(array, index, (Integer)se.value);
		case WeaselPrimitive.LONG:
			interpreter.baseTypes.setArrayLong(array, index, (Long)se.value);
		case WeaselPrimitive.FLOAT:
			interpreter.baseTypes.setArrayFloat(array, index, (Float)se.value);
		case WeaselPrimitive.DOUBLE:
			interpreter.baseTypes.setArrayDouble(array, index, (Double)se.value);
		default:
			interpreter.baseTypes.setArrayObject(array, index, se.object);
		}
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(primitiveID);
	}
	
	@Override
	public String toString() {
		return "writeIndex "+WeaselPrimitive.primitiveNames[primitiveID];
	}
	
}
