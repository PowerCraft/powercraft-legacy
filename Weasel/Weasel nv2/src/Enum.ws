public class Enum<E extends Enum<E>> implements Compareable<E>, Cloneable<E>{

	private final String name;
	private final int ordinal;
	
	protected Enum(String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }
	
	public final String name() {
        return name;
    }
    
    public final int ordinal() {
        return ordinal;
    }

	public final boolean equals(Object o){
		return this==o;
	}

	public String toString(){
		return name;
	}

	public final int hashCode(){
		return super.hashCode();
	}

	public final int compareTo(E o) {
        if (getClass() != o.getClass())
            throw new ClassCastException();
        return self.ordinal - other.ordinal;
    }

	public final E clone(){
		return this;
	}

}
