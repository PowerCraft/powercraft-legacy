package weasel.interpreter;


public interface WeaselModifier {

	public static final int PRIVATE=1;
	public static final int PUBLIC=2;
	public static final int PROTECTED=4;
	public static final int FINAL=8;
	public static final int NATIVE=16;
	public static final int STATIC=32;
	public static final int ABSTRACT=64;
	public static final int INTERFACE = 128;
	
	public int getModifier();
	
}
