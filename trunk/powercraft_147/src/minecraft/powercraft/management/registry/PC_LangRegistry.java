package powercraft.management.registry;

import net.minecraft.util.StringTranslate;
import powercraft.management.PC_IModule;
import powercraft.management.PC_Struct3;

public final class PC_LangRegistry {
	
	public static void registerLanguageForLang(PC_IModule module,
			String lang, LangEntry... translations) {
		PC_RegistryServer.getInstance().registerLanguage(module, lang, translations);
	}

	public static void registerLanguage(PC_IModule module,
			LangEntry... translations) {
		registerLanguageForLang(module, "en_US", translations);
	}

	public static void loadLanguage(PC_IModule module) {
		PC_RegistryServer.getInstance().loadLanguage(module);
	}

	public static void saveLanguage(PC_IModule module) {
		PC_RegistryServer.getInstance().saveLanguage(module);
	}

	public static String tr(String identifier) {
		return StringTranslate.getInstance().translateKey(identifier)
				.trim();
	}

	public static String tr(String identifier, String... replacements) {
		return StringTranslate.getInstance().translateKeyFormat(identifier,
				(Object[]) replacements);
	}
	
	public static class LangEntry extends PC_Struct3<String, String, String[]>{
		public LangEntry(String key, String trans, String... desc) {
			super(key, trans, desc);
		}
	}
	
}
