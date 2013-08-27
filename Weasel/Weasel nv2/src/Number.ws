public abstract class Number implements Cloneable<Number>{

	public abstract int intValue();
	
	public abstract long longValue();
	
	public abstract float floatValue();
	
	public abstract double doubleValue();
	
	public byte byteValue(){
		return (byte)intValue();
	}

	public short shortValue(){
		return (short)intValue();
	}

}