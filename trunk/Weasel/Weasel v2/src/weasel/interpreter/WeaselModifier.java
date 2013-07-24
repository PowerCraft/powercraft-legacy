package weasel.interpreter;

public interface WeaselModifier {

	public static int PRIVATE=1;
	public static int PUBLIC=2;
	public static int PROTECTED=4;
	public static int FIANL=8;
	public static int NATIVE=16;
	public static int STATIC=32;
	
	
	public int getModifier();
	
}
