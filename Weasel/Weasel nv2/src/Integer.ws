public final class Integer extends Number{

	private final int value;

	public Integer(int value){
		this.value = value;
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
		if(o instanceof Integer){
			return ((Integer)o).intValue()==value;
		}
		return false;
	}

	public static String toHexString(int value){
		return "";
	}

}