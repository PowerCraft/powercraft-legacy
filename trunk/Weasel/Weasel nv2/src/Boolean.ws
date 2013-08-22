public final class Boolean implements Compareable<Boolean>{

	private final boolean value;

	public Boolean(boolean value){
		this.value = value;
	}

	public boolean booleanValue(){
		return value;
	}

	public int compareTo(Boolean object){
		return value?object.booleanValue?0:-1:object.booleanValue?1:0;
	}

	public boolean equals(Object o){
		if(o instanceof Boolean){
			return ((Boolean)o).booleanValue()==value;
		}
		return false;
	}

}