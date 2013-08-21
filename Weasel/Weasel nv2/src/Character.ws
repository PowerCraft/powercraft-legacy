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

}