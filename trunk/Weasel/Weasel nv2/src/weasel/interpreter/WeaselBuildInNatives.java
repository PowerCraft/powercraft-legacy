package weasel.interpreter;

class WeaselBuildInNatives {

	public static void register(WeaselInterpreter weaselInterpreter){
		weaselInterpreter.registerNativeMethod("Object.getClass()LClass;", new NativeObject.NativeGetClass());
		weaselInterpreter.registerNativeMethod("Object.hashCode()I", new NativeObject.NativeHashCode());
		weaselInterpreter.registerNativeMethod("Thread.getDefaultName()OString;", new NativeThread.NativeGetDefaultName());
		weaselInterpreter.registerNativeMethod("Thread.sleep(L)", new NativeThread.NativeSleep());
		weaselInterpreter.registerNativeMethod("Arrays.newArray(OClass;I)[OObject;", new NativeArrays.NativeNewArray());
		weaselInterpreter.registerNativeMethod("Class.getArrayClass()OClass;", new NativeClass.NativeGetArrayClass());
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
				thread.sleep((Long)param[0]);
				return null;
			}
			
		}
		
	}
	
	public static class NativeArrays{
		
		public static class NativeNewArray implements WeaselNativeMethod{

			@Override
			public Object invoke(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor executor, String functionName, WeaselObject _this, Object[] param) {
				WeaselClass weaselClass = interpreter.baseTypes.getClassClass((WeaselObject)param[0]);
				return interpreter.baseTypes.createArrayObject((Integer)param[1], weaselClass);
			}
			
		}
		
	}
	
	public static class NativeClass{
		
		public static class NativeGetArrayClass implements WeaselNativeMethod{

			@Override
			public Object invoke(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor executor, String functionName, WeaselObject _this, Object[] param) {
				return _this.getWeaselClass().getArrayClass().getClassObject();
			}
			
		}
		
	}
	
}
