package weasel.interpreter;

public final class WeaselPrimitive extends WeaselClass {
	
	public static final int BOOLEAN = 1;
	public static final int CHAR = 2;
	public static final int BYTE = 3;
	public static final int SHORT = 4;
	public static final int INT = 5;
	public static final int LONG = 6;
	public static final int FLOAT = 7;
	public static final int DOUBLE = 8;
	public static final int VOID = 9;
	
	public static final String[] primitiveNames = {"NULL", "boolean", "char", "byte", "short", "int", "long", "float", "double", "void"};
	public static final char[] primitiveEasyNames = {0, 'N', 'C', 'B', 'S', 'I', 'L', 'F', 'D', 'V'};
	
	public static final boolean[][] castMap = 
		{
		{false, false, false, false, false, false, false, false, false, false},
		{false,  true, false, false, false, false, false, false, false, false},
		{false, false,  true, false,  true,  true,  true,  true,  true, false},
		{false, false,  true,  true,  true,  true,  true,  true,  true, false},
		{false, false,  true, false,  true,  true,  true,  true,  true, false},
		{false, false, false, false, false,  true,  true,  true,  true, false},
		{false, false, false, false, false, false,  true,  true,  true, false},
		{false, false, false, false, false, false, false,  true, false, false},
		{false, false, false, false, false, false, false,  true,  true, false},
		{false, false, false, false, false, false, false, false, false, false},
		};
	
	private final int primitiveID;
	
	protected WeaselPrimitive(WeaselInterpreter interpreter, int primitiveID){
		super(interpreter, null, primitiveNames[primitiveID], WeaselModifier.FINAL | WeaselModifier.PUBLIC, null);
		this.primitiveID = primitiveID;
	}
	
	@Override
	public String getByteName() {
		return ""+primitiveEasyNames[primitiveID];
	}

	@Override
	public boolean canCastTo(WeaselClass weaselClass) {
		return castMap[primitiveID][getPrimitiveID(weaselClass)];
	}

	public static int makeWrapper(WeaselObject object, WeaselClass type, WeaselField weaselField){
		if(type.isPrimitive()){
			WeaselInterpreter interpreter = type.getInterpreter();
			WeaselPrimitive primitive = (WeaselPrimitive)type;
			switch(primitive.primitiveID){
			case BOOLEAN:
				return interpreter.baseTypes.createBooleanObject(weaselField.getBoolean(object));
			case CHAR:
				return interpreter.baseTypes.createCharObject(weaselField.getChar(object));
			case BYTE:
				return interpreter.baseTypes.createByteObject(weaselField.getByte(object));
			case SHORT:
				return interpreter.baseTypes.createShortObject(weaselField.getShort(object));
			case INT:
				return interpreter.baseTypes.createIntObject(weaselField.getInt(object));
			case LONG:
				return interpreter.baseTypes.createLongObject(weaselField.getLong(object));
			case DOUBLE:
				return interpreter.baseTypes.createDoubleObject(weaselField.getDouble(object));
			case FLOAT:
				return interpreter.baseTypes.createFloatObject(weaselField.getFloat(object));
			}
		}
		throw new WeaselNativeException("Can't make Weapper for %s", type);
	}

	public static void unwrapp(WeaselObject object, WeaselClass type, int pointer, WeaselField weaselField) {
		if(type.isPrimitive()){
			WeaselInterpreter interpreter = type.getInterpreter();
			WeaselPrimitive primitive = (WeaselPrimitive)type;
			WeaselObject value = interpreter.getObject(pointer);
			WeaselField field = value.getWeaselClass().getField("value");
			switch(primitive.primitiveID){
			case BOOLEAN:
				weaselField.setBoolean(object, field.getBoolean(value));
			case CHAR:
				weaselField.setChar(object, field.getChar(value));
			case BYTE:
				weaselField.setBoolean(object, field.getBoolean(value));
			case SHORT:
				weaselField.setShort(object, field.getShort(value));
			case INT:
				weaselField.setInt(object, field.getInt(value));
			case LONG:
				weaselField.setLong(object, field.getLong(value));
			case DOUBLE:
				weaselField.setDouble(object, field.getDouble(value));
			case FLOAT:
				weaselField.setFloat(object, field.getFloat(value));
			}
		}
	}

	public static int getPrimitiveID(WeaselClass type){
		if(type.isPrimitive()){
			return ((WeaselPrimitive)type).primitiveID;
		}
		return 0;
	}

}
