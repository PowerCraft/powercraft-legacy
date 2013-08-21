public final class Long extends Number{

	private final long value;

	public Long(long value){
		this.value = value;
	}

	public int intValue(){
		return (int)value;
	}

	public long longValue(){
		return value;
	}
	
	public float floatValue(){
		return (float)value;
	}

	public double doubleValue(){
		return (double)value;
	}

	public boolean equals(Object o){
		if(o instanceof Long){
			return ((Long)o).value==value;
		}
		return false;
	}

}