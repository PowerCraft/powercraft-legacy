public class Enum<T extends Enum<T>>{

	public final boolean equals(Object o){
		return this==o;
	}

	public final String toString(){
		return super.toString();
	}

	public final int hashCode(){
		return super.hashCode();
	}

}
