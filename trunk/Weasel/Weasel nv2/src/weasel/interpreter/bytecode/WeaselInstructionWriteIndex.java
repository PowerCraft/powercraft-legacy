package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselObject;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.WeaselThread;

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
		int index = (Integer)thread.popValue();
		WeaselObject array = interpreter.getObject(thread.popObject());
		switch(primitiveID){
		case WeaselPrimitive.BOOLEAN:
			interpreter.baseTypes.setArrayBoolean(array, index, (Boolean)thread.popValue());
		case WeaselPrimitive.CHAR:
			interpreter.baseTypes.setArrayChar(array, index, (Character)thread.popValue());
		case WeaselPrimitive.BYTE:
			interpreter.baseTypes.setArrayByte(array, index, (Byte)thread.popValue());
		case WeaselPrimitive.SHORT:
			interpreter.baseTypes.setArrayShort(array, index, (Short)thread.popValue());
		case WeaselPrimitive.INT:
			interpreter.baseTypes.setArrayInt(array, index, (Integer)thread.popValue());
		case WeaselPrimitive.LONG:
			interpreter.baseTypes.setArrayLong(array, index, (Long)thread.popValue());
		case WeaselPrimitive.FLOAT:
			interpreter.baseTypes.setArrayFloat(array, index, (Float)thread.popValue());
		case WeaselPrimitive.DOUBLE:
			interpreter.baseTypes.setArrayDouble(array, index, (Double)thread.popValue());
		default:
			interpreter.baseTypes.setArrayObject(array, index, thread.popObject());
		}
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(primitiveID);
	}
	
}
