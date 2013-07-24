package weasel.interpreter;

public interface WeaselNativeMethod {

	public Object invoke(WeaselClassBuffer interpreter, String methodName, Object _this, Object[] param);
	
}
