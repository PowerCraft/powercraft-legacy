package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionBitwiseAnd extends WeaselInstruction {

	private final int primitiveID;
	
	public WeaselInstructionBitwiseAnd(int primitiveID){
		this.primitiveID = primitiveID;
	}
	
	public WeaselInstructionBitwiseAnd(DataInputStream dataInputStream) throws IOException{
		primitiveID = dataInputStream.readInt();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		Object o1 = thread.popValue();
		Object o2 = thread.popValue();
		
		switch(primitiveID){
		case WeaselPrimitive.BOOLEAN:
			thread.pushValue((Boolean)o1&(Boolean)o2);
		case WeaselPrimitive.CHAR:
			thread.pushValue((Character)o1&(Character)o2);
		case WeaselPrimitive.BYTE:
			thread.pushValue((Byte)o1&(Byte)o2);
		case WeaselPrimitive.SHORT:
			thread.pushValue((Short)o1&(Short)o2);
		case WeaselPrimitive.INT:
			thread.pushValue((Integer)o1&(Integer)o2);
		case WeaselPrimitive.LONG:
			thread.pushValue((Long)o1&(Long)o2);
		}
		
	}
	
	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(primitiveID);
	}

	@Override
	public String toString() {
		return "bitAnd "+WeaselPrimitive.primitiveNames[primitiveID];
	}
	
}
