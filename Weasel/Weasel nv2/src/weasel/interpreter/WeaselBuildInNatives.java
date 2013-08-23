package weasel.interpreter;

class WeaselBuildInNatives {

	public static void register(WeaselInterpreter weaselInterpreter){
		weaselInterpreter.registerNativeMethod("Object.getClass()LClass;", new NativeObject.NativeGetClass());
		weaselInterpreter.registerNativeMethod("Object.hashCode()I", new NativeObject.NativeHashCode());
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
	
}
