package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.WeaselThread;
import weasel.interpreter.WeaselThread.StackElement;

public class WeaselInstructionNotVerySame extends WeaselInstruction {

	private final int primitiveID;
	
	public WeaselInstructionNotVerySame(int primitiveID){
		this.primitiveID = primitiveID;
	}
	
	public WeaselInstructionNotVerySame(DataInputStream dataInputStream) throws IOException{
		primitiveID = dataInputStream.readInt();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		StackElement o1 = thread.pop();
		StackElement o2 = thread.pop();
		
		switch(primitiveID){
		case WeaselPrimitive.BOOLEAN:
			thread.pushValue((Boolean)o1.value!=(Boolean)o2.value);
		case WeaselPrimitive.CHAR:
			thread.pushValue((Character)o1.value!=(Character)o2.value);
		case WeaselPrimitive.BYTE:
			thread.pushValue((Byte)o1.value!=(Byte)o2.value);
		case WeaselPrimitive.SHORT:
			thread.pushValue((Short)o1.value!=(Short)o2.value);
		case WeaselPrimitive.INT:
			thread.pushValue((Integer)o1.value!=(Integer)o2.value);
		case WeaselPrimitive.LONG:
			thread.pushValue((Long)o1.value!=(Long)o2.value);
		case WeaselPrimitive.FLOAT:
			thread.pushValue((Float)o1.value!=(Float)o2.value);
		case WeaselPrimitive.DOUBLE:
			thread.pushValue((Double)o1.value!=(Double)o2.value);
		default:
			thread.pushValue(o1.object!=o2.object);
		}
		
	}
	
	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(primitiveID);
	}

	@Override
	public String toString() {
		return "not verySame "+WeaselPrimitive.primitiveNames[primitiveID];
	}

}
