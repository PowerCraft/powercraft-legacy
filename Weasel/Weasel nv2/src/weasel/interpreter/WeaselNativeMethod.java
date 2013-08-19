package weasel.interpreter;

public interface WeaselNativeMethod {

	public Object invoke(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor executor, String functionName, WeaselObject _this, Object[] param);
	
}
