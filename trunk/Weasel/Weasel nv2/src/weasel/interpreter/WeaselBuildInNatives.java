package weasel.interpreter;

class WeaselBuildInNatives {

	public static void register(WeaselInterpreter weaselInterpreter){
		weaselInterpreter.registerNativeMethod("Object.getClass()LClass;", new NativeObject.NativeGetClass());
		weaselInterpreter.registerNativeMethod("Object.hashCode()I", new NativeObject.NativeHashCode());
		weaselInterpreter.registerNativeMethod("Thread.getDefaultName()OString;", new NativeThread.NativeGetDefaultName());
		weaselInterpreter.registerNativeMethod("Thread.sleep(L)", new NativeThread.NativeSleep());
	}
	
	public static class NativeObject{
		
		public static class NativeGetClass implements WeaselNativeMethod{

			@Override
			public Object invoke(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor executor, String functionName, WeaselObject _this, Object[] param) {
				return _this.getWeaselClass().getClassObject();
			}
			
		}
		
		public static class NativeHashCode implements WeaselNativeMethod{
			
			@Override
			public Object invoke(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor executor, String functionName, WeaselObject _this, Object[] param) {
				return _this.hashCode();
			}
			
		}
		
	}
	
	public static class NativeThread{
		
		public static class NativeGetDefaultName implements WeaselNativeMethod{

			@Override
			public Object invoke(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor executor, String functionName, WeaselObject _this, Object[] param) {
				return interpreter.baseTypes.createStringObject(interpreter.getDefaultThreadName());
			}
			
		}
		
		public static class NativeSleep implements WeaselNativeMethod{

			@Override
			public Object invoke(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor executor, String functionName, WeaselObject _this, Object[] param) {
				thread.sleep((long)param[0]);
				return null;
			}
			
		}
		
	}
	
}
