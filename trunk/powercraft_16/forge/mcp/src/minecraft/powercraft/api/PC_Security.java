package powercraft.api;

public class PC_Security {

	public static boolean allowedCaller(Class<?>... allowedCallers){
		Class<?> caller = PC_Reflection.getCallerClass(2);
		for(int i=0; i<allowedCallers.length; i++){
			if(allowedCallers[i]==caller){
				return true;
			}
		}
		PC_Logger.severe("Security Exception %s try to call a non allowed function", caller);
		return false;
	}
	
}
