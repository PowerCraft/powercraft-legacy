package weasel.interpreter;

public interface WeaselNativeMethod {

	public Object invoke(WeaselInterpreter interpreter, String methodName, Object _this, Object[] param);
	
}
