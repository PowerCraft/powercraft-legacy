package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodBody;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselNativeMethod;
import weasel.interpreter.WeaselObject;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionEqual extends WeaselInstruction {

	private final int primitiveID;
	
	public WeaselInstructionEqual(int primitiveID){
		this.primitiveID = primitiveID;
	}
	
	public WeaselInstructionEqual(DataInputStream dataInputStream) throws IOException{
		primitiveID = dataInputStream.readInt();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		if(primitiveID==0){
			int obj2 = thread.popObject();
			int obj1 = thread.popObject();
			WeaselObject o2 = interpreter.getObject(obj2);
			WeaselObject o1 = interpreter.getObject(obj1);
			if(o1==null && o2==null){
				thread.pushValue(true);
			}else if(o1!=null && o2!=null){
				WeaselMethodBody methodBody = interpreter.baseTypes.getlObjectEqualMethod().getMethod(o1);
				if(methodBody.isNative()){
					WeaselNativeMethod nativeMethod = interpreter.getNativeMethod(methodBody.getNameAndDesk());
					Object[] params = new Object[1];
					params[0] = o2;
					Object ret = nativeMethod.invoke(interpreter, thread, method, methodBody.getNameAndDesk(), o1, params);
					thread.pushValue((Boolean)ret);
				}else{
					thread.pushObject(obj1);
					thread.pushObject(obj2);
					thread.call(methodBody);
				}
			}else{
				thread.pushValue(false);
			}
		}else{
			Object o1 = thread.popValue();
			Object o2 = thread.popValue();
			
			switch(primitiveID){
			case WeaselPrimitive.BOOLEAN:
				thread.pushValue((Character)o1==(Character)o2);
				break;
			case WeaselPrimitive.CHAR:
				thread.pushValue((Character)o1==(Character)o2);
				break;
			case WeaselPrimitive.BYTE:
				thread.pushValue((Byte)o1==(Byte)o2);
				break;
			case WeaselPrimitive.SHORT:
				thread.pushValue((Short)o1==(Short)o2);
				break;
			case WeaselPrimitive.INT:
				thread.pushValue((Integer)o1==(Integer)o2);
				break;
			case WeaselPrimitive.LONG:
				thread.pushValue((Long)o1==(Long)o2);
				break;
			case WeaselPrimitive.DOUBLE:
				thread.pushValue((Double)o1==(Double)o2);
				break;
			case WeaselPrimitive.FLOAT:
				thread.pushValue((Float)o1==(Float)o2);
				break;
			}
		}
	}
	
	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(primitiveID);
	}

	@Override
	public String toString() {
		return "equal "+WeaselPrimitive.primitiveNames[primitiveID];
	}

}
