public final class String{

	private char[] value;
	
	public String(String value){
		this.value = value.value;
	}
	
	public String(char[] value){
		this.value = value;
	}

	public char charAt(int i){
		return value[i];
	}

}