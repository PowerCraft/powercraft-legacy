package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselField;
import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionWriteStaticField extends WeaselInstruction {

	private final String fieldDesk;
	private WeaselField field;
	private WeaselClass type;
	
	public WeaselInstructionWriteStaticField(String fieldDesk){
		this.fieldDesk = fieldDesk;
	}
	
	public WeaselInstructionWriteStaticField(DataInputStream dataInputStream) throws IOException{
		fieldDesk = dataInputStream.readUTF();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		resolve(interpreter);
		switch(WeaselPrimitive.getPrimitiveID(type)){
		case WeaselPrimitive.BOOLEAN:
			field.setBoolean(null, (Boolean)thread.popValue());
			break;
		case WeaselPrimitive.BYTE:
			field.setByte(null, (Byte)thread.popValue());
			break;
		case WeaselPrimitive.CHAR:
			field.setChar(null, (Character)thread.popValue());
			break;
		case WeaselPrimitive.DOUBLE:
			field.setDouble(null, (Double)thread.popValue());
			break;
		case WeaselPrimitive.FLOAT:
			field.setFloat(null, (Float)thread.popValue());
			break;
		case WeaselPrimitive.INT:
			field.setInt(null, (Integer)thread.popValue());
			break;
		case WeaselPrimitive.LONG:
			field.setLong(null, (Long)thread.popValue());
			break;
		case WeaselPrimitive.SHORT:
			field.setShort(null, (Short)thread.popValue());
			break;
		default:
			field.setObject(null, thread.popObject());
			break;
		}
	}

	private void resolve(WeaselInterpreter interpreter){
		if(field==null || type==null){
			int d = fieldDesk.lastIndexOf(":");
			int p = fieldDesk.lastIndexOf(".", d);
			WeaselClass weaselClass = interpreter.getWeaselClass("O"+fieldDesk.substring(0, p)+";");
			field = weaselClass.getField(fieldDesk.substring(p+1, d));
			type = interpreter.getWeaselClass(fieldDesk.substring(d+1));
		}
	}
	
	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeUTF(fieldDesk);
	}

	@Override
	public String toString() {
		return "writeField static "+fieldDesk;
	}
	
}
