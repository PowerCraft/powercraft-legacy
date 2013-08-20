public final class Integer{

	private final int value;

	public Integer(int value){
		this.value = value;
	}

	public int getValue(){
		return value;
	}

	public boolean equals(Object o){
		if(o instanceof Integer){
			return ((Integer)o).value==value;
		}
		return false;
	}

}