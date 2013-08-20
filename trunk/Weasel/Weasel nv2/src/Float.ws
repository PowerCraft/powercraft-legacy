public final class Float{

	private final float value;

	public Float(float value){
		this.value = value;
	}

	public float getValue(){
		return value;
	}

	public boolean equals(Object o){
		if(o instanceof Float){
			return ((Float)o).value==value;
		}
		return false;
	}

}