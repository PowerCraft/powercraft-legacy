package weasel.interpreter;

public interface WeaselClassBuffer {

	public WeaselClass getClassByName(String className);

	public WeaselNativeMethod getNativeMethod(String nameAndDesk);
	
}
