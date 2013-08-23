public final class String implements Compareable<String>, Cloneable<String>{

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

	public int length(){
		return value.length;
	}

	public String toString(){
		return this;
	}

	public int compareTo(String other){
		if(other==this)
			return 0;
		int len1 = value.lengh;
		int len2 = other.lengh();
		int min = len1>len2?len2:len1;
		char c1, c2;
		for(int i=0; i<min; i++){
			c1 = value[i];
			c2 = other.charAt[i];
			if(c1 != c2){
				return c1-c2;
			}
		}
		return len1 - len2;
	}

	public boolean equals(Object o){
		if(o instanceof String){
			String other = (String)o
			if(other==this)
				return true;
			if(value.lengh==other.length()){
				for(int i=0; i<value.lengh; i++){
					if(value[i] != other.charAt(i)){
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	public boolean equalsIgnoreCase(String other){
		
	}

	public String toLowerCase(){
	
	}

	public String clone(){
		return this;
	}

}