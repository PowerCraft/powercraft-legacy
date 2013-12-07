package xscript.runtime.clazz;

import xscript.runtime.XChecks;
import xscript.runtime.genericclass.XGenericClass;
import xscript.runtime.object.XObject;
import xscript.runtime.object.XObjectProvider;

public class XWrapper {

	public static Object getJavaObject(XObjectProvider objectProvider, XGenericClass genericClass, long value){
		switch(XPrimitive.getPrimitiveID(genericClass.getXClass())){
		case XPrimitive.OBJECT:
			XObject obj = objectProvider.getObject(value);
			XChecks.checkCast(obj.getXClass(), genericClass);
			return obj;
		case XPrimitive.BOOL:
			return value!=0;
		case XPrimitive.BYTE:
			return (byte)value;
		case XPrimitive.CHAR:
			return (char)value;
		case XPrimitive.SHORT:
			return (short)value;
		case XPrimitive.INT:
			return (int)value;
		case XPrimitive.LONG:
			return value;
		case XPrimitive.FLOAT:
			return Float.intBitsToFloat((int)value);
		case XPrimitive.DOUBLE:
			return Double.longBitsToDouble(value);
		}
		return null;
	}
	
	public static long getXObject(XObjectProvider objectProvider, XGenericClass genericClass, Object value){
		switch(XPrimitive.getPrimitiveID(genericClass.getXClass())){
		case XPrimitive.OBJECT:
			return objectProvider.getPointer((XObject)value);
		case XPrimitive.BOOL:
			return (Boolean)value?-1:0;
		case XPrimitive.BYTE:
			return (Byte)value;
		case XPrimitive.CHAR:
			return (Character)value;
		case XPrimitive.SHORT:
			return (Short)value;
		case XPrimitive.INT:
			return (Integer)value;
		case XPrimitive.LONG:
			return (Long)value;
		case XPrimitive.FLOAT:
			return Float.floatToIntBits((Float)value);
		case XPrimitive.DOUBLE:
			return Double.doubleToLongBits((Double)value);
		}
		return 0;
	}
	
}
