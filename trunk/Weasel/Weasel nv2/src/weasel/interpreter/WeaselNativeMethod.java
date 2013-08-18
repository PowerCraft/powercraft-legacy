package weasel.interpreter;

public interface WeaselNativeMethod {

	public WeaselObject invoke(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor executor, String functionName, WeaselObject _this, WeaselObject[] param);
	
}
