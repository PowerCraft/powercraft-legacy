package net.minecraft.src;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.minecraft.client.Minecraft;



/**
 * Threads which downloads latest language pack, installs it and updates names in running Minecraft
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCco_ThreadDownloadTranslations extends Thread {

	@Override
	public void run() {
		try {
			URL url = new URL(mod_PCcore.updateLangPath);

			ZipInputStream zin = new ZipInputStream(url.openStream());

			PC_Logger.fine("\n\nLanguage pack update v" + mod_PCcore.updateLangVersion + " downloaded.");
			PC_Logger.fine("Starting extraction of language files");

			ZipEntry ze = null;
			while ((ze = zin.getNextEntry()) != null) {

				File file = new File(Minecraft.getMinecraftDir() + mod_PCcore.cfgdir + "/lang/" + ze.getName());
				// file.mkdirs();

				if (ze.getName().matches("en_US.+")) {
					PC_Logger.finer(" - SKIPPING " + ze.getName());
					continue;
				}

				if (file.exists()) {
					PC_Logger.finer(" - Updated " + ze.getName());
					file.delete();
				} else {
					PC_Logger.finer(" - New file " + ze.getName());
				}

				FileOutputStream fout = new FileOutputStream(file);
				for (int c = zin.read(); c != -1; c = zin.read()) {
					fout.write(c);
				}
				zin.closeEntry();
				fout.close();
			}

			zin.close();
			PC_Logger.fine("Language pack updated.\n\n");
			PC_Logger.fine("Loading translations from updated files.\n");


			for (PC_Module module : PC_Module.modules.values()) {
				PC_Logger.finer("Loading translations for module " + module.getModuleName());
				if(module.lang != null){
					module.lang.loadTranstalions();
				}
				PC_Logger.finer("\n");
			}

			PC_Logger.fine("All translations loaded.\n");

			PC_Logger.fine("Saving Language Pack version number to property file CORE.cfg");

			PC_PropertyManager cfg = mod_PCcore.instance.cfg();

			cfg.enableValidation(false);
			cfg.cfgSilent(true);

			cfg.setValue(mod_PCcore.pk_cfgCurrentLangVersion, mod_PCcore.updateLangVersion);
			cfg.apply();

			cfg.enableValidation(true);
			cfg.cfgSilent(false);


			PC_Logger.fine("Forcing ModLoader to update Minecraft's list of translations.");
			ModLoader.setPrivateValue(ModLoader.class, null, "langPack", null);


		} catch (Exception e) {
			PC_Logger.throwing("PCco_ThreadDownloadTranslations", "run", e);
			e.printStackTrace();
		}
	}


}
