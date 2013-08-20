public final class Long{

	private final long value;

	public Long(long value){
		this.value = value;
	}

	public long getValue(){
		return value;
	}

	public boolean equals(Object o){
		if(o instanceof Long){
			return ((Long)o).value==value;
		}
		return false;
	}

}