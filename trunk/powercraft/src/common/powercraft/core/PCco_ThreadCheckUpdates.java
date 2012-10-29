package powercraft.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class PCco_ThreadCheckUpdates extends Thread {

	@Override
	public void run() {
		try {
			URL url = new URL(mod_PowerCraftCore.updateInfoPath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String page = "";

			String line;
			while ((line = reader.readLine()) != null) {
				page += line + "\n";
			}

			reader.close();

			mod_PowerCraftCore.onUpdateInfoDownloaded(page);

		} catch (Exception e) {
			PC_Logger.throwing("PCco_ThreadCheckUpdates", "run", e);
			e.printStackTrace();

			// don't bother with returning to core module, it'd only result in null pointer exception.
		}
	}
}
