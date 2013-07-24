package weasel.interpreter;


public interface WeaselModifier {

	public static final int PRIVATE=1;
	public static final int PUBLIC=2;
	public static final int PROTECTED=4;
	public static final int FIANL=8;
	public static final int NATIVE=16;
	public static final int STATIC=32;
	
	public int getModifier();
	
}
