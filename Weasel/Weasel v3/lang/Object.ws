
public class Object{

	public native Class getClass();

	public String operator(String)(){
		return getClass()+":"+hashCode();
	}

	public native long hashCode();

	public void finalize() throws Throwable{}

}
