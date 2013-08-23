public final class String{

	private final char[] value;
	
	public String(String value){
		this.value = value.value;
	}
	
	public String(char[] value){
		this.value = Arrays.arrayCopy(value);
	}

	public String(char[] value, int start, int end){
		this.value = Arrays.arrayCopy(value, start, end);
	}

	public char charAt(int i){
		return value[i];
	}

}