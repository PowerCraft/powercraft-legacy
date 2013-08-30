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

	public int length(){
		return value.length;
	}

	public String toString(){
		return this;
	}

	public int compareTo(String other){
		if(other==this)
			return 0;
		int len1 = value.length;
		int len2 = other.length();
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
			String other = (String)o;
			if(other==this)
				return true;
			if(value.length==other.length()){
				for(int i=0; i<value.length; i++){
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
		if(other==this)
			return true;
		if(value.length==other.length()){
			for(int i=0; i<value.length; i++){
				char c1, c2;
				c1 = value[i];
				c2 = other.charAt(i);
				if(c1 != c2){
					if(Character.toLowerCase(c1)!=Character.toLowerCase(c2)){
						return false;
					}
				}
			}
		}
		return false;
	}

	public String toLowerCase(){
		char[] copy = new char[value.length];
		for(int i=0; i<copy.length; i++){
			copy[i] = Character.toLowerCase(value[i]);
		}
		return new String(copy);
	}

	public String toUpperCase(){
		char[] copy = new char[value.length];
		for(int i=0; i<copy.length; i++){
			copy[i] = Character.toUpperCase(value[i]);
		}
		return new String(copy);
	}

	public String clone(){
		return this;
	}

}