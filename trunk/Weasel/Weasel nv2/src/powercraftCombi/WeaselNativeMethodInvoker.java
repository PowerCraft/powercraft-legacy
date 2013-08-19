package powercraftCombi;

import java.lang.reflect.Method;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselNativeException;
import weasel.interpreter.WeaselNativeMethod;
import weasel.interpreter.WeaselObject;
import weasel.interpreter.WeaselThread;

public class WeaselNativeMethodInvoker implements WeaselNativeMethod {

	private Method method;
	
	public WeaselNativeMethodInvoker(Method method){
		this.method = method;
	}
	
	@Override
	public Object invoke(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor executor, String functionName, WeaselObject _this, Object[] param) {
		Class<?>[] paramTypes = method.getParameterTypes();
		Object[] oParam = new Object[paramTypes.length];
		boolean gaveThis = false;
		int j=0;
		for(int i=0; i<paramTypes.length; i++){
			if(paramTypes[i]==WeaselInterpreter.class){
				oParam[i] = interpreter;
			}else if(paramTypes[i]==WeaselThread.class){
				oParam[i] = thread;
			}else if(paramTypes[i]==WeaselMethodExecutor.class){
				oParam[i] = executor;
			}else if(paramTypes[i]==WeaselObject.class){
				if(gaveThis){
					oParam[i] = param[j++];
				}else{
					oParam[i] = _this;
				}
			}else if(paramTypes[i].isPrimitive()){
				oParam[i] = param[j++];
			}else{
				throw new WeaselNativeException("Illegal parameter %s in method invokation %s", i, method);
			}
		}
		try {
			return method.invoke(null, oParam);
		} catch (Throwable e) {
			throw new WeaselNativeException(e, "Error on native method invokation %s", method);
		} 
	}

}
