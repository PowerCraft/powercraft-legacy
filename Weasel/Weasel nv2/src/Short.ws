public final class Short extends Number{

	private final short value;

	public Short(short value){
		this.value = value;
	}

	public byte byteValue(){
		return (byte)value;
	}

	public short shortValue(){
		return value;
	}
	
	public int intValue(){
		return value;
	}

	public long longValue(){
		return value;
	}
	
	public float floatValue(){
		return value;
	}

	public double doubleValue(){
		return value;
	}
	
	public boolean equals(Object o){
		if(o instanceof Short){
			return ((Short)o).shortValue()==value;
		}
		return false;
	}

}