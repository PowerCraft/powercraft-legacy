public final class Float extends Number{

	private final float value;

	public Float(float value){
		this.value = value;
	}

	public int intValue(){
		return (int)value;
	}
	
	public long longValue(){
		return (long)value;
	}

	public float floatValue(){
		return value;
	}
	
	public double doubleValue(){
		return value;
	}

	public boolean equals(Object o){
		if(o instanceof Float){
			return ((Float)o).value==value;
		}
		return false;
	}

}