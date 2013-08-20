public final class Double{

	private final double value;

	public Double(double value){
		this.value = value;
	}

	public double getValue(){
		return value;
	}

	public boolean equals(Object o){
		if(o instanceof Double){
			return ((Double)o).value==value;
		}
		return false;
	}

}