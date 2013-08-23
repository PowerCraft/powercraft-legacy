public final class Character{

	private final char value;

	public Character(char value){
		this.value = value;
	}

	public char charValue(){
		return value;
	}

	public boolean equals(Object o){
		if(o instanceof Character){
			return ((Character)o).value==value;
		}
		return false;
	}
	
	public static char toLowerCase(char c){
		if(c>='A'&&c<='Z'){
			return c-'A'+'a';
		}
		return c;
	}

	public static char toUpperCase(char c){
		if(c>='a'&&c<='z'){
			return c-'a'+'A';
		}
		return c;
	}

}