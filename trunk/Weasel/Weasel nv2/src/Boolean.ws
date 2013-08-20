public final class Boolean{

	private final boolean value;

	public Boolean(boolean value){
		this.value = value;
	}

	public boolean getValue(){
		return value;
	}

	public boolean equals(Object o){
		if(o instanceof Boolean){
			return ((Boolean)o).value==value;
		}
		return false;
	}

}