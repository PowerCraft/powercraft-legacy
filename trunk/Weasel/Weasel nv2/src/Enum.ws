public class Enum<E extends Enum<E>>{

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

	public final int operator<=>(E o) {
        if (getClass() != o.getClass())
            throw new ClassCastException();
        return ordinal - o.ordinal;
    }

	public final E operator<:(){
		return this;
	}

}
