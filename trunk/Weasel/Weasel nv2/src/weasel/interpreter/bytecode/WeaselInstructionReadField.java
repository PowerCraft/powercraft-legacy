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

public class WeaselInstructionReadField extends WeaselInstruction {

	private final String fieldDesk;
	private WeaselField field;
	private WeaselClass type;
	
	public WeaselInstructionReadField(String fieldDesk){
		this.fieldDesk = fieldDesk;
	}
	
	public WeaselInstructionReadField(DataInputStream dataInputStream) throws IOException{
		fieldDesk = dataInputStream.readUTF();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		resolve(interpreter);
		WeaselObject object = interpreter.getObject(thread.popObject());
		switch(WeaselPrimitive.getPrimitiveID(type)){
		case WeaselPrimitive.BOOLEAN:
			thread.pushValue(field.getBoolean(object));
			break;
		case WeaselPrimitive.BYTE:
			thread.pushValue(field.getByte(object));
			break;
		case WeaselPrimitive.CHAR:
			thread.pushValue(field.getChar(object));
			break;
		case WeaselPrimitive.DOUBLE:
			thread.pushValue(field.getDouble(object));
			break;
		case WeaselPrimitive.FLOAT:
			thread.pushValue(field.getFloat(object));
			break;
		case WeaselPrimitive.INT:
			thread.pushValue(field.getInt(object));
			break;
		case WeaselPrimitive.LONG:
			thread.pushValue(field.getLong(object));
			break;
		case WeaselPrimitive.SHORT:
			thread.pushValue(field.getShort(object));
			break;
		default:
			thread.pushObject(field.getObject(object));
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
		return "readField "+fieldDesk;
	}
	
}
