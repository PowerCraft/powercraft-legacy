public final class Double extends Number{

	private final double value;

	public Double(double value){
		this.value = value;
	}

	public int intValue(){
		return (int)value;
	}

	public long longValue(){
		return (long)value;
	}
	
	public float floatValue(){
		return (float)value;
	}

	public double doubleValue(){
		return value;
	}

	public boolean equals(Object o){
		if(o instanceof Double){
			return ((Double)o).value==value;
		}
		return false;
	}

}