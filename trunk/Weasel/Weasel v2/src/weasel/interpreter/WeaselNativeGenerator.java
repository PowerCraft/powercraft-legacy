package weasel.interpreter;

public interface WeaselNativeGenerator {

	public WeaselNativeMethod createNativeMethod(WeaselInterpreter interpreter, String methodName);
	
}
