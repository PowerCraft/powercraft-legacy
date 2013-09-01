package weasel.interpreter;

public class WeaselConvertHelper {

	public static Object readField(WeaselField field, WeaselObject object){
		switch(WeaselPrimitive.getPrimitiveID(field.genericType.genericClass)){
		case WeaselPrimitive.BOOLEAN:
			return field.getBoolean(object);
		case WeaselPrimitive.CHAR:
			return field.getChar(object);
		case WeaselPrimitive.BYTE:
			return field.getByte(object);
		case WeaselPrimitive.SHORT:
			return field.getShort(object);
		case WeaselPrimitive.INT:
			return field.getInt(object);
		case WeaselPrimitive.LONG:
			return field.getLong(object);
		case WeaselPrimitive.FLOAT:
			return field.getFloat(object);
		case WeaselPrimitive.DOUBLE:
			return field.getDouble(object);
		default:
			return object.getInterpreter().getObject(field.getObject(object));
		}
	}

	public static void writeField(WeaselField field, WeaselObject object, Object value) {
		switch(WeaselPrimitive.getPrimitiveID(field.genericType.genericClass)){
		case WeaselPrimitive.BOOLEAN:
			field.setBoolean(object, (Boolean)value);
		case WeaselPrimitive.CHAR:
			field.setChar(object, (Character)value);
		case WeaselPrimitive.BYTE:
			field.setByte(object, (Byte)value);
		case WeaselPrimitive.SHORT:
			field.setShort(object, (Short)value);
		case WeaselPrimitive.INT:
			field.setInt(object, (Integer)value);
		case WeaselPrimitive.LONG:
			field.setLong(object, (Long)value);
		case WeaselPrimitive.FLOAT:
			field.setFloat(object, (Float)value);
		case WeaselPrimitive.DOUBLE:
			field.setDouble(object, (Double)value);
		default:
			field.setObject(object, object.getInterpreter().getObjectID((WeaselObject)value));
		}
	}
	
}
