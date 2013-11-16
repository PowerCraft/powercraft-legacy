package powercraft.api;

public class PC_Debug {

	public static final boolean DEBUG = false;
	
	public static void println(String s){
		if(DEBUG)
			System.out.println(s);
	}
	
}
