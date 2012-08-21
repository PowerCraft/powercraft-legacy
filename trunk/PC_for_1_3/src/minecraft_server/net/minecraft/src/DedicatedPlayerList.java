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
        this.viewDistance = par1DedicatedServer.getIntProperty("view-distance", 10);
        this.maxPlayers = par1DedicatedServer.getIntProperty("max-players", 20);
        this.setWhiteListEnabled(par1DedicatedServer.func_71332_a("white-list", false));

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
        this.saveOpsList();
        this.saveWhiteList();
    }

    public void setWhiteListEnabled(boolean par1)
    {
        super.setWhiteListEnabled(par1);
        this.getDedicatedServerInstance().setProperty("white-list", Boolean.valueOf(par1));
        this.getDedicatedServerInstance().saveProperties();
    }

    /**
     * This adds a username to the ops list, then saves the op list
     */
    public void addOp(String par1Str)
    {
        super.addOp(par1Str);
        this.saveOpsList();
    }

    /**
     * This removes a username from the ops list, then saves the op list
     */
    public void removeOp(String par1Str)
    {
        super.removeOp(par1Str);
        this.saveOpsList();
    }

    /**
     * Remove the specified player from the whitelist.
     */
    public void removeFromWhitelist(String par1Str)
    {
        super.removeFromWhitelist(par1Str);
        this.saveWhiteList();
    }

    /**
     * add the specified player to the white list
     */
    public void addToWhiteList(String par1Str)
    {
        super.addToWhiteList(par1Str);
        this.saveWhiteList();
    }

    /**
     * Either does nothing, or calls readWhiteList.
     */
    public void loadWhiteList()
    {
        this.readWhiteList();
    }

    private void loadOpsList()
    {
        try
        {
            this.func_72376_i().clear();
            BufferedReader var1 = new BufferedReader(new FileReader(this.opsList));
            String var2 = "";

            while ((var2 = var1.readLine()) != null)
            {
                this.func_72376_i().add(var2.trim().toLowerCase());
            }

            var1.close();
        }
        catch (Exception var3)
        {
            logger.warning("Failed to load operators list: " + var3);
        }
    }

    private void saveOpsList()
    {
        try
        {
            PrintWriter var1 = new PrintWriter(new FileWriter(this.opsList, false));
            Iterator var2 = this.func_72376_i().iterator();

            while (var2.hasNext())
            {
                String var3 = (String)var2.next();
                var1.println(var3);
            }

            var1.close();
        }
        catch (Exception var4)
        {
            logger.warning("Failed to save operators list: " + var4);
        }
    }

    private void readWhiteList()
    {
        try
        {
            this.getWhiteListedIPs().clear();
            BufferedReader var1 = new BufferedReader(new FileReader(this.whiteList));
            String var2 = "";

            while ((var2 = var1.readLine()) != null)
            {
                this.getWhiteListedIPs().add(var2.trim().toLowerCase());
            }

            var1.close();
        }
        catch (Exception var3)
        {
            logger.warning("Failed to load white-list: " + var3);
        }
    }

    private void saveWhiteList()
    {
        try
        {
            PrintWriter var1 = new PrintWriter(new FileWriter(this.whiteList, false));
            Iterator var2 = this.getWhiteListedIPs().iterator();

            while (var2.hasNext())
            {
                String var3 = (String)var2.next();
                var1.println(var3);
            }

            var1.close();
        }
        catch (Exception var4)
        {
            logger.warning("Failed to save white-list: " + var4);
        }
    }

    /**
     * Determine if the player is allowed to connect based on current server settings.
     */
    public boolean isAllowedToLogin(String par1Str)
    {
        par1Str = par1Str.trim().toLowerCase();
        return !this.func_72383_n() || this.isOp(par1Str) || this.getWhiteListedIPs().contains(par1Str);
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
