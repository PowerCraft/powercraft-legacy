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


			mod_PCcore.onTranslationsUpdated();


		} catch (Exception e) {
			PC_Logger.throwing("PCco_ThreadDownloadTranslations", "run", e);
			e.printStackTrace();
		}
	}


}
