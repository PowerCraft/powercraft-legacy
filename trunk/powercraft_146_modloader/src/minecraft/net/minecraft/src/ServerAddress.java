package net.minecraft.src;

import java.util.Hashtable;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

public class ServerAddress
{
    private final String ipAddress;
    private final int serverPort;

    private ServerAddress(String par1Str, int par2)
    {
        this.ipAddress = par1Str;
        this.serverPort = par2;
    }

    public String getIP()
    {
        return this.ipAddress;
    }

    public int getPort()
    {
        return this.serverPort;
    }

    public static ServerAddress func_78860_a(String par0Str)
    {
        if (par0Str == null)
        {
            return null;
        }
        else
        {
            String[] var1 = par0Str.split(":");

            if (par0Str.startsWith("["))
            {
                int var2 = par0Str.indexOf("]");

                if (var2 > 0)
                {
                    String var3 = par0Str.substring(1, var2);
                    String var4 = par0Str.substring(var2 + 1).trim();

                    if (var4.startsWith(":") && var4.length() > 0)
                    {
                        var4 = var4.substring(1);
                        var1 = new String[] {var3, var4};
                    }
                    else
                    {
                        var1 = new String[] {var3};
                    }
                }
            }

            if (var1.length > 2)
            {
                var1 = new String[] {par0Str};
            }

            String var5 = var1[0];
            int var6 = var1.length > 1 ? parseIntWithDefault(var1[1], 25565) : 25565;

            if (var6 == 25565)
            {
                String[] var7 = getServerAddress(var5);
                var5 = var7[0];
                var6 = parseIntWithDefault(var7[1], 25565);
            }

            return new ServerAddress(var5, var6);
        }
    }

    /**
     * Returns a server's address and port for the specified hostname, looking up the SRV record if possible
     */
    private static String[] getServerAddress(String par0Str)
    {
        try
        {
            Hashtable var1 = new Hashtable();
            var1.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            var1.put("java.naming.provider.url", "dns:");
            InitialDirContext var2 = new InitialDirContext(var1);
            Attributes var3 = var2.getAttributes("_minecraft._tcp." + par0Str, new String[] {"SRV"});
            String[] var4 = var3.get("srv").get().toString().split(" ", 4);
            return new String[] {var4[3], var4[2]};
        }
        catch (Throwable var5)
        {
            return new String[] {par0Str, Integer.toString(25565)};
        }
    }

    private static int parseIntWithDefault(String par0Str, int par1)
    {
        try
        {
            return Integer.parseInt(par0Str.trim());
        }
        catch (Exception var3)
        {
            return par1;
        }
    }
}
