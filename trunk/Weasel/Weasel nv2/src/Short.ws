public final class Short{

	private final short value;

	public Short(short value){
		this.value = value;
	}

	public short getValue(){
		return value;
	}

	public boolean equals(Object o){
		if(o instanceof Short){
			return ((Short)o).value==value;
		}
		return false;
	}

}