package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import net.minecraft.server.MinecraftServer;

public class DedicatedPlayerList extends ServerConfigurationManager
{
    private File opsList;
    private File whiteList;

    public DedicatedPlayerList(DedicatedServer par1DedicatedServer)
    {
        super(par1DedicatedServer);
        this.opsList = par1DedicatedServer.getFile("ops.txt");
        this.whiteList = par1DedicatedServer.getFile("white-list.txt");
        this.viewDistance = par1DedicatedServer.getOrSetIntProperty("view-distance", 10);
        this.maxPlayers = par1DedicatedServer.getOrSetIntProperty("max-players", 20);
        this.setWhiteListEnabled(par1DedicatedServer.getOrSetBoolProperty("white-list", false));

        if (!par1DedicatedServer.isSinglePlayer())
        {
            this.getBannedPlayers().setListActive(true);
            this.getBannedIPs().setListActive(true);
        }

        this.getBannedPlayers().loadBanList();
        this.getBannedPlayers().saveToFileWithHeader();
        this.getBannedIPs().loadBanList();
        this.getBannedIPs().saveToFileWithHeader();
        this.loadOpsList();
        this.readWhiteList();
        this.saveOpsListOrWhitelist();
        this.saveOpsList();
    }

    public void setWhiteListEnabled(boolean par1)
    {
        super.setWhiteListEnabled(par1);
        this.getDedicatedServerInstance().setArbitraryProperty("white-list", Boolean.valueOf(par1));
        this.getDedicatedServerInstance().saveSettingsToFile();
    }

    public void addNameToWhitelist(String par1Str)
    {
        super.addNameToWhitelist(par1Str);
        this.saveOpsListOrWhitelist();
    }

    public void removeNameFromWhitelist(String par1Str)
    {
        super.removeNameFromWhitelist(par1Str);
        this.saveOpsListOrWhitelist();
    }

    public void removeFromIPWhitelist(String par1Str)
    {
        super.removeFromIPWhitelist(par1Str);
        this.saveOpsList();
    }

    public void addToIPWhitelist(String par1Str)
    {
        super.addToIPWhitelist(par1Str);
        this.saveOpsList();
    }

    /**
     * eithre does nothing, or calls readWhiteList
     */
    public void loadWhiteList()
    {
        this.readWhiteList();
    }

    private void loadOpsList()
    {
        try
        {
            this.getNamesWhiteList().clear();
            BufferedReader var1 = new BufferedReader(new FileReader(this.opsList));
            String var2 = "";

            while ((var2 = var1.readLine()) != null)
            {
                this.getNamesWhiteList().add(var2.trim().toLowerCase());
            }

            var1.close();
        }
        catch (Exception var3)
        {
            myLogger.warning("Failed to load operators list: " + var3);
        }
    }

    private void saveOpsListOrWhitelist()
    {
        try
        {
            PrintWriter var1 = new PrintWriter(new FileWriter(this.opsList, false));
            Iterator var2 = this.getNamesWhiteList().iterator();

            while (var2.hasNext())
            {
                String var3 = (String)var2.next();
                var1.println(var3);
            }

            var1.close();
        }
        catch (Exception var4)
        {
            myLogger.warning("Failed to save operators list: " + var4);
        }
    }

    private void readWhiteList()
    {
        try
        {
            this.getIPWhiteList().clear();
            BufferedReader var1 = new BufferedReader(new FileReader(this.whiteList));
            String var2 = "";

            while ((var2 = var1.readLine()) != null)
            {
                this.getIPWhiteList().add(var2.trim().toLowerCase());
            }

            var1.close();
        }
        catch (Exception var3)
        {
            myLogger.warning("Failed to load white-list: " + var3);
        }
    }

    private void saveOpsList()
    {
        try
        {
            PrintWriter var1 = new PrintWriter(new FileWriter(this.whiteList, false));
            Iterator var2 = this.getIPWhiteList().iterator();

            while (var2.hasNext())
            {
                String var3 = (String)var2.next();
                var1.println(var3);
            }

            var1.close();
        }
        catch (Exception var4)
        {
            myLogger.warning("Failed to save white-list: " + var4);
        }
    }

    public boolean isWhiteListed(String par1Str)
    {
        par1Str = par1Str.trim().toLowerCase();
        return !this.isWhiteListEnabled() || this.areCommandsAllowed(par1Str) || this.getIPWhiteList().contains(par1Str);
    }

    public DedicatedServer getDedicatedServerInstance()
    {
        return (DedicatedServer)super.getServerInstance();
    }

    public MinecraftServer getServerInstance()
    {
        return this.getDedicatedServerInstance();
    }
}
