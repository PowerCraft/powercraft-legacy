package weasel.interpreter;

public class WeaselBaseTypes {

	public final static String weaselObjectClassName = "OObject;";
	public final static String weaselClassClassName = "OClass;";
	public final static String weaselEnumClassName = "OEnum;";
	public final static String weaselBooleanClassName = "OBoolean;";
	public final static String weaselCharClassName = "OCharacter;";
	public final static String weaselByteClassName = "OByte;";
	public final static String weaselShortClassName = "OShort;";
	public final static String weaselIntClassName = "OInteger;";
	public final static String weaselLongClassName = "OLong;";
	public final static String weaselFloatClassName = "OFloat;";
	public final static String weaselDoubleClassName = "ODouble;";
	public final static String weaselStringClassName = "OString;";
	
	private final WeaselInterpreter interpreter;
	public final WeaselPrimitive booleanClass;
	public final WeaselPrimitive charClass;
	public final WeaselPrimitive byteClass;
	public final WeaselPrimitive shortClass;
	public final WeaselPrimitive intClass;
	public final WeaselPrimitive longClass;
	public final WeaselPrimitive floatClass;
	public final WeaselPrimitive doubleClass;
	public final WeaselPrimitive voidClass;
	
	public WeaselBaseTypes(WeaselInterpreter interpreter){
		this.interpreter = interpreter;
		booleanClass = new WeaselPrimitive(interpreter, WeaselPrimitive.BOOLEAN);
		charClass = new WeaselPrimitive(interpreter, WeaselPrimitive.CHAR);
		byteClass = new WeaselPrimitive(interpreter, WeaselPrimitive.BYTE);
		shortClass = new WeaselPrimitive(interpreter, WeaselPrimitive.SHORT);
		intClass = new WeaselPrimitive(interpreter, WeaselPrimitive.INT);
		longClass = new WeaselPrimitive(interpreter, WeaselPrimitive.LONG);
		floatClass = new WeaselPrimitive(interpreter, WeaselPrimitive.FLOAT);
		doubleClass = new WeaselPrimitive(interpreter, WeaselPrimitive.DOUBLE);
		voidClass = new WeaselPrimitive(interpreter, WeaselPrimitive.VOID);
	}
	
	private WeaselClass weaselObjectClass;
	
	public WeaselClass getObjectClass(){
		if(weaselObjectClass==null){
			weaselObjectClass = interpreter.getWeaselClass(weaselObjectClassName);
		}
		return weaselObjectClass;
	}
	
	
	private WeaselClass weaselClassClass;
	private WeaselField weaselClassClassNameField;
	
	public WeaselClass getClassClass(){
		if(weaselClassClass==null){
			weaselClassClass = interpreter.getWeaselClass(weaselClassClassName);
			weaselClassClassNameField = weaselClassClass.getField("className");
		}
		return weaselClassClass;
	}
	
	public WeaselField getClassClassNameField(){
		if(weaselClassClassNameField==null){
			weaselClassClassNameField = getClassClass().getField("className");
		}
		return weaselClassClassNameField;
	}
	
	protected int createClassObject(WeaselClass wClass) {
		WeaselClass weaselClass = getClassClass();
		int obj = interpreter.createObject(weaselClass, 0);
		getClassClassNameField().setObject(interpreter.getObject(obj), createStringObject(wClass.getRealName()));
		return obj;
	}
	
	public String getClassName(WeaselObject object){
		int name = getClassClassNameField().getObject(object);
		return getString(interpreter.getObject(name));
	}
	
	public WeaselClass getClassClass(WeaselObject object){
		return interpreter.getWeaselClass(getClassName(object));
	}
	
	
	private WeaselClass weaselEnumClass;
	
	public WeaselClass getEnumClass(){
		if(weaselEnumClass==null){
			weaselEnumClass = interpreter.getWeaselClass(weaselEnumClassName);
		}
		return weaselEnumClass;
	}
	
	
	private WeaselClass weaselBooleanClass;
	private WeaselField weaselBooleanValueField;
	
	public WeaselClass getBooleanClass(){
		if(weaselBooleanClass==null){
			weaselBooleanClass = interpreter.getWeaselClass(weaselBooleanClassName);
		}
		return weaselBooleanClass;
	}
	
	public WeaselField getBooleanValueField(){
		if(weaselBooleanValueField==null){
			weaselBooleanValueField = getBooleanClass().getField("value");
		}
		return weaselBooleanValueField;
	}
	
	public int createBooleanObject(boolean value) {
		WeaselClass weaselClass = getBooleanClass();
		int obj = interpreter.createObject(weaselClass, 0);
		setBoolean(interpreter.getObject(obj), value);
		return obj;
	}
	
	public boolean getBoolean(WeaselObject object){
		return getBooleanValueField().getBoolean(object);
	}
	
	public void setBoolean(WeaselObject object, boolean value){
		getBooleanValueField().setBoolean(object, value);
	}
	
	
	private WeaselClass weaselCharClass;
	private WeaselField weaselCharValueField;
	
	public WeaselClass getCharClass(){
		if(weaselCharClass==null){
			weaselCharClass = interpreter.getWeaselClass(weaselCharClassName);
		}
		return weaselCharClass;
	}
	
	public WeaselField getCharValueField(){
		if(weaselCharValueField==null){
			weaselCharValueField = getCharClass().getField("value");
		}
		return weaselCharValueField;
	}
	
	public int createCharObject(char value) {
		WeaselClass weaselClass = getCharClass();
		int obj = interpreter.createObject(weaselClass, 0);
		setChar(interpreter.getObject(obj), value);
		return obj;
	}
	
	public char getChar(WeaselObject object){
		return getCharValueField().getChar(object);
	}
	
	public void setChar(WeaselObject object, char value){
		getCharValueField().setChar(object, value);
	}

	
	private WeaselClass weaselByteClass;
	private WeaselField weaselByteValueField;
	
	public WeaselClass getByteClass(){
		if(weaselByteClass==null){
			weaselByteClass = interpreter.getWeaselClass(weaselByteClassName);
		}
		return weaselByteClass;
	}
	
	public WeaselField getByteValueField(){
		if(weaselByteValueField==null){
			weaselByteValueField = getByteClass().getField("value");
		}
		return weaselByteValueField;
	}
	
	public int createByteObject(byte value) {
		WeaselClass weaselClass = getByteClass();
		int obj = interpreter.createObject(weaselClass, 0);
		setByte(interpreter.getObject(obj), value);
		return obj;
	}
	
	public byte getByte(WeaselObject object){
		return getByteValueField().getByte(object);
	}
	
	public void setByte(WeaselObject object, byte value){
		getByteValueField().setByte(object, value);
	}
	
	
	private WeaselClass weaselShortClass;
	private WeaselField weaselShortValueField;
	
	public WeaselClass getShortClass(){
		if(weaselShortClass==null){
			weaselShortClass = interpreter.getWeaselClass(weaselShortClassName);
		}
		return weaselShortClass;
	}
	
	public WeaselField getShortValueField(){
		if(weaselShortValueField==null){
			weaselShortValueField = getShortClass().getField("value");
		}
		return weaselShortValueField;
	}
	
	public int createShortObject(short value) {
		WeaselClass weaselClass = getShortClass();
		int obj = interpreter.createObject(weaselClass, 0);
		setShort(interpreter.getObject(obj), value);
		return obj;
	}
	
	public short getShort(WeaselObject object){
		return getShortValueField().getShort(object);
	}
	
	public void setShort(WeaselObject object, short value){
		getShortValueField().setShort(object, value);
	}
	
	
	private WeaselClass weaselIntClass;
	private WeaselField weaselIntValueField;
	
	public WeaselClass getIntClass(){
		if(weaselIntClass==null){
			weaselIntClass = interpreter.getWeaselClass(weaselIntClassName);
		}
		return weaselIntClass;
	}
	
	public WeaselField getIntValueField(){
		if(weaselIntValueField==null){
			weaselIntValueField = getIntClass().getField("value");
		}
		return weaselIntValueField;
	}
	
	public int createIntObject(int value) {
		WeaselClass weaselClass = getIntClass();
		int obj = interpreter.createObject(weaselClass, 0);
		setInt(interpreter.getObject(obj), value);
		return obj;
	}
	
	public int getInt(WeaselObject object){
		return getIntValueField().getInt(object);
	}
	
	public void setInt(WeaselObject object, int value){
		getIntValueField().setInt(object, value);
	}
	
	
	private WeaselClass weaselLongClass;
	private WeaselField weaselLongValueField;
	
	public WeaselClass getLongClass(){
		if(weaselLongClass==null){
			weaselLongClass = interpreter.getWeaselClass(weaselLongClassName);
		}
		return weaselLongClass;
	}
	
	public WeaselField getLongValueField(){
		if(weaselLongValueField==null){
			weaselLongValueField = getLongClass().getField("value");
		}
		return weaselLongValueField;
	}
	
	public int createLongObject(long value) {
		WeaselClass weaselClass = getLongClass();
		int obj = interpreter.createObject(weaselClass, 0);
		setLong(interpreter.getObject(obj), value);
		return obj;
	}
	
	public long getLong(WeaselObject object){
		return getLongValueField().getLong(object);
	}
	
	public void setLong(WeaselObject object, long value){
		getLongValueField().setLong(object, value);
	}
	
	
	private WeaselClass weaselFloatClass;
	private WeaselField weaselFloatValueField;
	
	public WeaselClass getFloatClass(){
		if(weaselFloatClass==null){
			weaselFloatClass = interpreter.getWeaselClass(weaselFloatClassName);
		}
		return weaselFloatClass;
	}
	
	public WeaselField getFloatValueField(){
		if(weaselFloatValueField==null){
			weaselFloatValueField = getFloatClass().getField("value");
		}
		return weaselFloatValueField;
	}
	
	public int createFloatObject(float value) {
		WeaselClass weaselClass = getFloatClass();
		int obj = interpreter.createObject(weaselClass, 0);
		setFloat(interpreter.getObject(obj), value);
		return obj;
	}
	
	public float getFloat(WeaselObject object){
		return getFloatValueField().getFloat(object);
	}
	
	public void setFloat(WeaselObject object, float value){
		getFloatValueField().setFloat(object, value);
	}
	
	
	private WeaselClass weaselDoubleClass;
	private WeaselField weaselDoubleValueField;
	
	public WeaselClass getDoubleClass(){
		if(weaselDoubleClass==null){
			weaselDoubleClass = interpreter.getWeaselClass(weaselDoubleClassName);
		}
		return weaselDoubleClass;
	}
	
	public WeaselField getDoubleValueField(){
		if(weaselDoubleValueField==null){
			weaselDoubleValueField = getDoubleClass().getField("value");
		}
		return weaselDoubleValueField;
	}
	
	public int createDoubleObject(double value) {
		WeaselClass weaselClass = getDoubleClass();
		int obj = interpreter.createObject(weaselClass, 0);
		setDouble(interpreter.getObject(obj), value);
		return obj;
	}
	
	public double getDouble(WeaselObject object){
		return getDoubleValueField().getDouble(object);
	}
	
	public void setDouble(WeaselObject object, double value){
		getDoubleValueField().setDouble(object, value);
	}
	
	
	private WeaselClass weaselStringClass;
	private WeaselField weaselStringValueField;
	
	public WeaselClass getStringClass(){
		if(weaselStringClass==null){
			weaselStringClass = interpreter.getWeaselClass(weaselStringClassName);
		}
		return weaselStringClass;
	}
	
	public WeaselField getStringValueField(){
		if(weaselStringValueField==null){
			weaselStringValueField = getStringClass().getField("value");
		}
		return weaselStringValueField;
	}
	
	public int createStringObject(String value) {
		WeaselClass weaselClass = getStringClass();
		int obj = interpreter.createObject(weaselClass, 0);
		setString(interpreter.getObject(obj), value);
		return obj;
	}
	
	public String getString(WeaselObject object){
		int array = getStringValueField().getObject(object);
		String value = "";
		WeaselObject arrayObject = interpreter.getObject(array);
		int length = getArrayLength(arrayObject);
		for(int i=0; i<length; i++){
			value += getArrayChar(arrayObject, i);
		}
		return value;
	}
	
	public void setString(WeaselObject object, String value){
		int size = value.length();
		int array = createArrayObject(size, charClass);
		WeaselObject arrayObject = interpreter.getObject(array);
		for(int i=0; i<size; i++){
			setArrayChar(arrayObject, i, value.charAt(i));
		}
		getStringValueField().setObject(object, array);
	}
	
	
	public WeaselClass getArrayClass(WeaselClass baseClass){
		return interpreter.getWeaselClass("["+baseClass.getRealName());
	}
	
	public int createArrayObject(int length, WeaselClass baseClass) {
		WeaselClass weaselClass = getArrayClass(baseClass);
		return interpreter.createObject(weaselClass, length);
	}
	
	public int getArrayLength(WeaselObject array){
		WeaselChecks.checkArray3(array);
		return array.easyTypes[0];
	}
	
	public void setArrayObject(WeaselObject array, int index, int obj) {
		WeaselObject object = interpreter.getObject(obj);
		if(!array.getWeaselClass().getArrayClass().isPrimitive()){
			WeaselChecks.checkArray(array, index, object==null?array.getWeaselClass().getArrayClass():object.getWeaselClass());
			array.objectRefs[index] = obj;
		}else{
			WeaselField field = object.getWeaselClass().getField("value");
			switch(WeaselPrimitive.getPrimitiveID(array.getWeaselClass().getArrayClass())){
			case WeaselPrimitive.BOOLEAN:
				setArrayBoolean(array, index, field.getBoolean(object));
			case WeaselPrimitive.CHAR:
				setArrayChar(array, index, field.getChar(object));
			case WeaselPrimitive.BYTE:
				setArrayByte(array, index, field.getByte(object));
			case WeaselPrimitive.SHORT:
				setArrayShort(array, index, field.getShort(object));
			case WeaselPrimitive.INT:
				setArrayInt(array, index, field.getInt(object));
			case WeaselPrimitive.LONG:
				setArrayLong(array, index, field.getLong(object));
			case WeaselPrimitive.FLOAT:
				setArrayFloat(array, index, field.getFloat(object));
			case WeaselPrimitive.DOUBLE:
				setArrayDouble(array, index, field.getDouble(object));
			}
		}
	}
	
	public void setArrayBoolean(WeaselObject array, int index, boolean value) {
		WeaselChecks.checkArray(array, index, booleanClass);
		array.easyTypes[index+1] = value?-1:0;
	}
	
	public void setArrayChar(WeaselObject array, int index, char value) {
		WeaselChecks.checkArray(array, index, charClass);
		array.easyTypes[index+1] = value;
	}
	
	public void setArrayByte(WeaselObject array, int index, byte value) {
		WeaselChecks.checkArray(array, index, byteClass);
		array.easyTypes[index+1] = value;
	}
	
	public void setArrayShort(WeaselObject array, int index, short value) {
		WeaselChecks.checkArray(array, index, shortClass);
		array.easyTypes[index+1] = value;
	}
	
	public void setArrayInt(WeaselObject array, int index, int value) {
		WeaselChecks.checkArray(array, index, intClass);
		array.easyTypes[index+1] = value;
	}
	
	public void setArrayLong(WeaselObject array, int index, long value) {
		WeaselChecks.checkArray(array, index, longClass);
		array.easyTypes[index*2+1] = (int)(value>>32);
		array.easyTypes[index*2+2] = (int)(value);
	}
	
	public void setArrayFloat(WeaselObject array, int index, float value) {
		WeaselChecks.checkArray(array, index, floatClass);
		array.easyTypes[index+1] = Float.floatToRawIntBits(value);
	}
	
	public void setArrayDouble(WeaselObject array, int index, double value) {
		WeaselChecks.checkArray(array, index, doubleClass);
		long l = Double.doubleToRawLongBits(value);
		array.easyTypes[index*2+1] = (int)(l>>32);
		array.easyTypes[index*2+2] = (int)(l);
	}
	
	public int getArrayObject(WeaselObject array, int index) {
		switch(WeaselPrimitive.getPrimitiveID(array.getWeaselClass().getArrayClass())){
		case WeaselPrimitive.BOOLEAN:
			return createBooleanObject(getArrayBoolean(array, index));
		case WeaselPrimitive.CHAR:
			return createCharObject(getArrayChar(array, index));
		case WeaselPrimitive.BYTE:
			return createByteObject(getArrayByte(array, index));
		case WeaselPrimitive.SHORT:
			return createShortObject(getArrayShort(array, index));
		case WeaselPrimitive.INT:
			return createIntObject(getArrayInt(array, index));
		case WeaselPrimitive.LONG:
			return createLongObject(getArrayLong(array, index));
		case WeaselPrimitive.FLOAT:
			return createFloatObject(getArrayFloat(array, index));
		case WeaselPrimitive.DOUBLE:
			return createDoubleObject(getArrayDouble(array, index));
		default:
			WeaselChecks.checkArray2(array, index, getObjectClass());
			return array.objectRefs[index];
		}
	}
	
	public boolean getArrayBoolean(WeaselObject array, int index) {
		WeaselChecks.checkArray2(array, index, booleanClass);
		return array.easyTypes[index+1]!=0;
	}
	
	public char getArrayChar(WeaselObject array, int index) {
		WeaselChecks.checkArray2(array, index, charClass);
		return (char)array.easyTypes[index+1];
	}
	
	public byte getArrayByte(WeaselObject array, int index) {
		WeaselChecks.checkArray2(array, index, byteClass);
		return (byte)array.easyTypes[index+1];
	}
	
	public short getArrayShort(WeaselObject array, int index) {
		WeaselChecks.checkArray2(array, index, shortClass);
		return (short)array.easyTypes[index+1];
	}
	
	public int getArrayInt(WeaselObject array, int index) {
		WeaselChecks.checkArray2(array, index, intClass);
		return array.easyTypes[index+1];
	}
	
	public long getArrayLong(WeaselObject array, int index) {
		WeaselChecks.checkArray2(array, index, longClass);
		return (long)array.easyTypes[index*2+1]<<32 | (long)array.easyTypes[index*2+2];
	}
	
	public float getArrayFloat(WeaselObject array, int index) {
		WeaselChecks.checkArray2(array, index, floatClass);
		return Float.intBitsToFloat(array.easyTypes[index+1]);
	}
	
	public double getArrayDouble(WeaselObject array, int index) {
		WeaselChecks.checkArray2(array, index, doubleClass);
		return Double.longBitsToDouble((long)array.easyTypes[index*2+1]<<32 | (long)array.easyTypes[index*2+2]);
	}
	
	public int createRuntimeException(WeaselRuntimeException wre) {
		WeaselClass weaselClass = interpreter.getWeaselClass("OException;");
		int obj = interpreter.createObject(weaselClass, 0);
		WeaselField field = weaselClass.getField("message");
		field.setObject(interpreter.getObject(obj), createStringObject(wre.getMessage()));
		return obj;
	}
	
}
