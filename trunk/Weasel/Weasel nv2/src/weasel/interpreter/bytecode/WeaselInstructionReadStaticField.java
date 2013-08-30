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

public class WeaselInstructionReadStaticField extends WeaselInstruction {

	private final String fieldDesk;
	private WeaselField field;
	private WeaselClass type;
	
	public WeaselInstructionReadStaticField(String fieldDesk){
		this.fieldDesk = fieldDesk;
	}
	
	public WeaselInstructionReadStaticField(DataInputStream dataInputStream) throws IOException{
		fieldDesk = dataInputStream.readUTF();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		resolve(interpreter);
		switch(WeaselPrimitive.getPrimitiveID(type)){
		case WeaselPrimitive.BOOLEAN:
			thread.pushValue(field.getBoolean(null));
			break;
		case WeaselPrimitive.BYTE:
			thread.pushValue(field.getByte(null));
			break;
		case WeaselPrimitive.CHAR:
			thread.pushValue(field.getChar(null));
			break;
		case WeaselPrimitive.DOUBLE:
			thread.pushValue(field.getDouble(null));
			break;
		case WeaselPrimitive.FLOAT:
			thread.pushValue(field.getFloat(null));
			break;
		case WeaselPrimitive.INT:
			thread.pushValue(field.getInt(null));
			break;
		case WeaselPrimitive.LONG:
			thread.pushValue(field.getLong(null));
			break;
		case WeaselPrimitive.SHORT:
			thread.pushValue(field.getShort(null));
			break;
		default:
			thread.pushObject(field.getObject(null));
			break;
		}
	}

	private void resolve(WeaselInterpreter interpreter){
		if(field==null || type==null){
			int d = fieldDesk.lastIndexOf(":");
			int p = fieldDesk.lastIndexOf(".", d);
			WeaselClass weaselClass = getWeaselClass(interpreter, fieldDesk.substring(0, p));
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
		return "readField static "+fieldDesk;
	}

}
