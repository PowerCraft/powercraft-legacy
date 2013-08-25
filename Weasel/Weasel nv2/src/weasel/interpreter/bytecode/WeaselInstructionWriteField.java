package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselField;
import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselObject;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.WeaselThread;
import weasel.interpreter.WeaselThread.StackElement;

public class WeaselInstructionWriteField extends WeaselInstruction {

	private final String fieldDesk;
	private WeaselField field;
	private WeaselClass type;
	
	public WeaselInstructionWriteField(String fieldDesk){
		this.fieldDesk = fieldDesk;
	}
	
	public WeaselInstructionWriteField(DataInputStream dataInputStream) throws IOException{
		fieldDesk = dataInputStream.readUTF();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		resolve(interpreter);
		StackElement se = thread.pop();
		WeaselObject object = interpreter.getObject(thread.getObject(thread.popObject()));
		thread.push(se);
		switch(WeaselPrimitive.getPrimitiveID(type)){
		case WeaselPrimitive.BOOLEAN:
			field.setBoolean(object, (Boolean)se.value);
			break;
		case WeaselPrimitive.BYTE:
			field.setByte(object, (Byte)se.value);
			break;
		case WeaselPrimitive.CHAR:
			field.setChar(object, (Character)se.value);
			break;
		case WeaselPrimitive.DOUBLE:
			field.setDouble(object, (Double)se.value);
			break;
		case WeaselPrimitive.FLOAT:
			field.setFloat(object, (Float)se.value);
			break;
		case WeaselPrimitive.INT:
			field.setInt(object, (Integer)se.value);
			break;
		case WeaselPrimitive.LONG:
			field.setLong(object, (Long)se.value);
			break;
		case WeaselPrimitive.SHORT:
			field.setShort(object, (Short)se.value);
			break;
		default:
			field.setObject(object, se.object);
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
		return "writeField "+fieldDesk;
	}
	
}
