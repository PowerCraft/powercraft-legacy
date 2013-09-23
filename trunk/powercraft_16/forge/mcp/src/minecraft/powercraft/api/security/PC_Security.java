package powercraft.api.security;

import powercraft.api.PC_Logger;
import powercraft.api.PC_Reflection;

public class PC_Security {

	public static boolean allowedCaller(String funktion, Class<?>... allowedCallers){
		Class<?> caller = PC_Reflection.getCallerClass(2);
		for(int i=0; i<allowedCallers.length; i++){
			if(allowedCallers[i]==caller){
				return true;
			}
		}
		PC_Logger.severe("Security Exception %s try to call a non allowed function: %s", caller, funktion);
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
