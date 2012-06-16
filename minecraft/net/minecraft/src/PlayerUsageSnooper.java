package net.minecraft.src;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PlayerUsageSnooper
{
    /** String map for report data */
    private Map dataMap;

    /** URL of the server to send the report to */
    private final URL serverUrl;

    public PlayerUsageSnooper(String par1Str)
    {
        dataMap = new HashMap();

        try
        {
            serverUrl = new URL((new StringBuilder()).append("http://snoop.minecraft.net/").append(par1Str).toString());
        }
        catch (MalformedURLException malformedurlexception)
        {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Adds information to the report
     */
    public void addData(String par1Str, Object par2Obj)
    {
        dataMap.put(par1Str, par2Obj);
    }

    /**
     * Starts a new thread to send the information to the report server
     */
    public void sendReport()
    {
        PlayerUsageSnooperThread playerusagesnooperthread = new PlayerUsageSnooperThread(this, "reporter");
        playerusagesnooperthread.setDaemon(true);
        playerusagesnooperthread.start();
    }

    /**
     * Returns the server URL for the given usage snooper
     */
    static URL getServerURL(PlayerUsageSnooper par0PlayerUsageSnooper)
    {
        return par0PlayerUsageSnooper.serverUrl;
    }

    /**
     * Returns the data map for the given usage snooper
     */
    static Map getDataMap(PlayerUsageSnooper par0PlayerUsageSnooper)
    {
        return par0PlayerUsageSnooper.dataMap;
    }
}
