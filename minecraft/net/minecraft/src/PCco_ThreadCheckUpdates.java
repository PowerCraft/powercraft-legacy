package net.minecraft.src;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


/**
 * thread which downloads information about powercraft updates.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCco_ThreadCheckUpdates extends Thread {

	@Override
	public void run() {
		try {
			URL url = new URL(mod_PCcore.updateInfoPath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String page = "";

			String line;
			while ((line = reader.readLine()) != null) {
				page += line + "\n";
			}

			reader.close();

			mod_PCcore.onUpdateInfoDownloaded(page);

		} catch (Exception e) {
			PC_Logger.throwing("PCco_ThreadCheckUpdates", "run", e);
			e.printStackTrace();

			// don't bother with returning to core module, it'd only result in null pointer exception.
		}
	}
}
