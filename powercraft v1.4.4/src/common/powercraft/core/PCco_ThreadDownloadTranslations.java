package powercraft.core;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.minecraft.client.Minecraft;

public class PCco_ThreadDownloadTranslations extends Thread {

	private String url;
	private PC_Module module;
	private int langVersion;
	
	public PCco_ThreadDownloadTranslations(String url, PC_Module module, int langVersion){
		this.url = url;
		this.module = module;
		this.langVersion = langVersion;
	}
	
	@Override
	public void run() {
		try {
			URL url = new URL(this.url);

			ZipInputStream zin = new ZipInputStream(url.openStream());

			PC_Logger.fine("\n\nLanguage pack update downloaded.");
			PC_Logger.fine("Starting extraction of language files");

			ZipEntry ze = null;
			while ((ze = zin.getNextEntry()) != null) {

				
				
				File file = new File(PC_Utils.getMCDirectory(), PC_Module.getPowerCraftFile() + "lang/" + ze.getName());
				// file.mkdirs();

				if (ze.getName().matches("en_US.+")) {
					PC_Logger.finer(" - REFRESHING " + ze.getName());
					if (file.exists()) file.delete();
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

			module.setLangVersion(langVersion);
			
			PC_Utils.loadLanguage(module);

		} catch (Exception e) {
			PC_Logger.throwing("PCco_ThreadDownloadTranslations", "run", e);
			e.printStackTrace();

			// don't bother with returning to core module, it'd only result in null pointer exception.
		}
	}
	
}
