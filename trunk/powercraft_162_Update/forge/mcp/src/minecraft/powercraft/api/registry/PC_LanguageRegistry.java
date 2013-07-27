package powercraft.api.registry;


public class PC_LanguageRegistry {
	
	private PC_LanguageRegistry(){}
	
	public static void registerLanguage(String key, String value){
		PC_Registry.sidedRegistry.registerLanguage(key, value);
	}
	
}
