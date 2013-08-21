public class Object{

	public native final Class getClass();

	public boolean equals(Object o){
		return this==o;
	}

	public String toString(){
		return getClass().getName()+"@"+Integer.toHexString(hashCode());
	}

	public native int hashCode();

}
