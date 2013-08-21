public final class Byte extends Number{

	private final byte value;

	public Byte(byte value){
		this.value = value;
	}

	public byte byteValue(){
		return value;
	}

	public short shortValue(){
		return value;
	}

	public int intValue(){
		return value;
	}
	
	public float floatValue(){
		return value;
	}
	
	public double doubleValue(){
		return value;
	}

	public boolean equals(Object o){
		if(o instanceof Byte){
			return ((Byte)o).byteValue()==value;
		}
		return false;
	}

}