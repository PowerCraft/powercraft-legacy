package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselField;
import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselNativeException;
import weasel.interpreter.WeaselObject;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.WeaselThread;
import weasel.interpreter.WeaselThread.StackElement;

public class WeaselInstructionCastPrimitive extends WeaselInstruction {

	private final int primitiveID;
	
	public WeaselInstructionCastPrimitive(int primitiveID){
		this.primitiveID = primitiveID;
	}
	
	public WeaselInstructionCastPrimitive(DataInputStream dataInputStream) throws IOException{
		primitiveID = dataInputStream.readInt();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		StackElement se = thread.pop();
		if(se.value==null){
			WeaselObject object = interpreter.getObject(se.object);
			if(object==null)
				throw new WeaselNativeException("Null pointer");
			WeaselField field = object.getWeaselClass().getField("value");
			switch(primitiveID){
			case WeaselPrimitive.BOOLEAN:
				thread.pushValue(field.getBoolean(object));
				break;
			case WeaselPrimitive.CHAR:
				thread.pushValue(field.getChar(object));
				break;
			case WeaselPrimitive.BYTE:
				thread.pushValue(field.getBoolean(object));
				break;
			case WeaselPrimitive.SHORT:
				thread.pushValue(field.getShort(object));
				break;
			case WeaselPrimitive.INT:
				thread.pushValue(field.getInt(object));
				break;
			case WeaselPrimitive.LONG:
				thread.pushValue(field.getLong(object));
				break;
			case WeaselPrimitive.DOUBLE:
				thread.pushValue(field.getDouble(object));
				break;
			case WeaselPrimitive.FLOAT:
				thread.pushValue(field.getFloat(object));
				break;
			}
		}else{
			Object object = se.value;
			Class<?> c = object.getClass();
			switch(primitiveID){
			case WeaselPrimitive.BOOLEAN:
				thread.pushValue((Boolean)object);
				break;
			case WeaselPrimitive.CHAR:
				char cValue;
				if(c==Character.class){
					cValue = (Character)object;
				}else if(c==Byte.class){
					cValue = (char)(byte)(Byte)object;
				}else if(c==Short.class){
					cValue = (char)(short)(Short)object;
				}else if(c==Integer.class){
					cValue = (char)(int)(Integer)object;
				}else if(c==Long.class){
					cValue = (char)(long)(Long)object;
				}else if(c==Float.class){
					cValue = (char)(float)(Float)object;
				}else if(c==Double.class){
					cValue = (char)(double)(Double)object;
				}else{
					throw new WeaselNativeException("Can't cast %s to char", c);
				}
				thread.pushValue(cValue);
				break;
			case WeaselPrimitive.BYTE:
				byte bValue;
				if(c==Character.class){
					bValue = (byte)(char)(Character)object;
				}else if(c==Byte.class){
					bValue = (byte)(Byte)object;
				}else if(c==Short.class){
					bValue = (byte)(short)(Short)object;
				}else if(c==Integer.class){
					bValue = (byte)(int)(Integer)object;
				}else if(c==Long.class){
					bValue = (byte)(long)(Long)object;
				}else if(c==Float.class){
					bValue = (byte)(float)(Float)object;
				}else if(c==Double.class){
					bValue = (byte)(double)(Double)object;
				}else{
					throw new WeaselNativeException("Can't cast %s to byte", c);
				}
				thread.pushValue(bValue);
				break;
			case WeaselPrimitive.SHORT:
				short sValue;
				if(c==Character.class){
					sValue = (short)(char)(Character)object;
				}else if(c==Byte.class){
					sValue = (byte)(Byte)object;
				}else if(c==Short.class){
					sValue = (short)(Short)object;
				}else if(c==Integer.class){
					sValue = (short)(int)(Integer)object;
				}else if(c==Long.class){
					sValue = (short)(long)(Long)object;
				}else if(c==Float.class){
					sValue = (short)(float)(Float)object;
				}else if(c==Double.class){
					sValue = (short)(double)(Double)object;
				}else{
					throw new WeaselNativeException("Can't cast %s to short", c);
				}
				thread.pushValue(sValue);
				break;
			case WeaselPrimitive.INT:
				int iValue;
				if(c==Character.class){
					iValue = (char)(Character)object;
				}else if(c==Byte.class){
					iValue = (byte)(Byte)object;
				}else if(c==Short.class){
					iValue = (short)(Short)object;
				}else if(c==Integer.class){
					iValue = (int)(Integer)object;
				}else if(c==Long.class){
					iValue = (int)(long)(Long)object;
				}else if(c==Float.class){
					iValue = (int)(float)(Float)object;
				}else if(c==Double.class){
					iValue = (int)(double)(Double)object;
				}else{
					throw new WeaselNativeException("Can't cast %s to int", c);
				}
				thread.pushValue(iValue);
				break;
			case WeaselPrimitive.LONG:
				long lValue;
				if(c==Character.class){
					lValue = (char)(Character)object;
				}else if(c==Byte.class){
					lValue = (byte)(Byte)object;
				}else if(c==Short.class){
					lValue = (short)(Short)object;
				}else if(c==Integer.class){
					lValue = (int)(Integer)object;
				}else if(c==Long.class){
					lValue = (long)(Long)object;
				}else if(c==Float.class){
					lValue = (long)(float)(Float)object;
				}else if(c==Double.class){
					lValue = (long)(double)(Double)object;
				}else{
					throw new WeaselNativeException("Can't cast %s to long", c);
				}
				thread.pushValue(lValue);
				break;
			case WeaselPrimitive.DOUBLE:
				double dValue;
				if(c==Character.class){
					dValue = (char)(Character)object;
				}else if(c==Byte.class){
					dValue = (byte)(Byte)object;
				}else if(c==Short.class){
					dValue = (short)(Short)object;
				}else if(c==Integer.class){
					dValue = (int)(Integer)object;
				}else if(c==Long.class){
					dValue = (long)(Long)object;
				}else if(c==Float.class){
					dValue = (float)(Float)object;
				}else if(c==Double.class){
					dValue = (double)(Double)object;
				}else{
					throw new WeaselNativeException("Can't cast %s to double", c);
				}
				thread.pushValue(dValue);
				break;
			case WeaselPrimitive.FLOAT:
				float fValue;
				if(c==Character.class){
					fValue = (char)(Character)object;
				}else if(c==Byte.class){
					fValue = (byte)(Byte)object;
				}else if(c==Short.class){
					fValue = (short)(Short)object;
				}else if(c==Integer.class){
					fValue = (int)(Integer)object;
				}else if(c==Long.class){
					fValue = (long)(Long)object;
				}else if(c==Float.class){
					fValue = (float)(Float)object;
				}else if(c==Double.class){
					fValue = (float)(double)(Double)object;
				}else{
					throw new WeaselNativeException("Can't cast %s to float", c);
				}
				thread.pushValue(fValue);
				break;
			}
		}
	}
	
	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(primitiveID);
	}

}
