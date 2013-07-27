package powercraft.api;

public class PC_Security {

	public static boolean allowedCaller(String function, Class<?>... allowedCallers){
		Class<?> caller = PC_Reflection.getCallerClass(2);
		for(int i=0; i<allowedCallers.length; i++){
			if(allowedCallers[i]==caller){
				return true;
			}
		}
		PC_Logger.severe("Security Exception: %s tries to call a non allowed function: %s", caller, function);
		return false;
	}
	
	public static boolean allowedCallerNoException(Class<?>... allowedCallers){
		Class<?> caller = PC_Reflection.getCallerClass(2);
		for(int i=0; i<allowedCallers.length; i++){
			if(allowedCallers[i]==caller){
				return true;
			}
		}
		return false;
	}
	
}
