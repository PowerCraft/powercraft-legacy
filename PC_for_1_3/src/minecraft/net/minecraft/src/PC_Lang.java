package net.minecraft.src;


import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;


/**
 * PowerCraft translation helper
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PC_Lang {

	private HashMap<String, String> defaults;
	private String module;
	private static final String deflang = "en_US";

	/**
	 * Get translated string.
	 * 
	 * @param identifier string identifier in the localizations
	 * @return translated.
	 */
	public static String tr(String identifier) {
		return StringTranslate.getInstance().translateKey(identifier).trim();
	}

	/**
	 * Get translated string formatted
	 * 
	 * @param identifier string identifier in the localizations
	 * @param replacements array of strings for replacing the %s and %1$s
	 *            things.
	 * @return translated.
	 */
	public static String tr(String identifier, String... replacements) {
		return StringTranslate.getInstance().translateKeyFormat(identifier, (Object[])replacements);
	}

	/**
	 * @param moduleName name of the module, eg. TRANSPORT
	 * @param en_US map of default english names for this module.
	 */
	public PC_Lang(String moduleName, HashMap<String, String> en_US) {

		defaults = en_US;
		module = moduleName;

		addLocalizations(deflang, defaults);

	}

	private void addLocalizations(String lang, HashMap<String, String> locs) {
		for (Entry<String, String> a : locs.entrySet()) {
			ModLoader.addLocalization(a.getKey(), lang, a.getValue());
		}
	}

	/**
	 * Load translation files for this module and add them to Minecraft
	 */
	public void loadTranstalions() {
		File folder = new File(Minecraft.getMinecraftDir(), mod_PCcore.cfgdir + "/lang/");

		String[] files = folder.list(new FilenameFilter() {

			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.matches("[a-z]{2,3}_[A-Z]{2,3}-" + module + "[.]lang");
			}

		});

		if (files == null) {
			PC_Logger.severe("Received NULL instead of list of translations.");
			return;
		}

		for (String filename : files) {

			PC_Logger.finest("* loading names from file " + filename + "...");
			String language = filename.substring(0, filename.indexOf('-'));

			PC_PropertyManager p = new PC_PropertyManager(mod_PCcore.cfgdir + "/lang/" + filename, language + " translation of " + module
					+ " module.");

			p.cfgSilent(true);

			for (Entry<String, String> a : defaults.entrySet()) {
				p.putString(a.getKey(), a.getValue());
			}

			p.apply();

			for (String key : defaults.keySet()) {
				ModLoader.addLocalization(key, language, p.getString(key).trim());
			}

		}

		PC_Logger.finer("Translations loaded.");
	}

	/**
	 * Generate default translation file.<br>
	 * Example: mod_PCcore.cfgdir+"/lang/en_US-MOBILE.lang"
	 */
	public void generateDefaultTranslationFile() {

		PC_PropertyManager p = new PC_PropertyManager(mod_PCcore.cfgdir + "/lang/" + deflang + "-" + module + ".lang",
				"English (default, immutable) translation of " + module + " module.\n"
						+ "To add your own translation, copy this file, change language prefix and transtalte the names.");

		p.cfgSilent(true);

		for (Entry<String, String> a : defaults.entrySet()) {
			p.putString(a.getKey(), a.getValue());
		}

		p.apply();

	}



}
