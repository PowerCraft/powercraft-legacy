public final class Byte{

	private final byte value;

	public Byte(byte value){
		this.value = value;
	}

	public byte getValue(){
		return value;
	}

	public boolean equals(Object o){
		if(o instanceof Byte){
			return ((Byte)o).value==value;
		}
		return false;
	}

}