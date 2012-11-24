package powercraft.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class PCco_ThreadCheckUpdates extends Thread
{
    private String url;

    public PCco_ThreadCheckUpdates(String url)
    {
        this.url = url;
    }

    @Override
    public void run()
    {
        try
        {
            URL url = new URL(this.url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String page = "";
            String line;

            while ((line = reader.readLine()) != null)
            {
                page += line + "\n";
            }

            reader.close();
            mod_PowerCraftCore.onUpdateInfoDownloaded(page);
        }
        catch (Exception e)
        {
            PC_Logger.throwing("PCco_ThreadCheckUpdates", "run", e);
            e.printStackTrace();
        }
    }
}
