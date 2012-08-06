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
    private File field_72423_e;
    private File field_72422_f;

    public DedicatedPlayerList(DedicatedServer par1DedicatedServer)
    {
        super(par1DedicatedServer);
        this.field_72423_e = par1DedicatedServer.getFile("ops.txt");
        this.field_72422_f = par1DedicatedServer.getFile("white-list.txt");
        this.field_72402_d = par1DedicatedServer.getIntProperty("view-distance", 10);
        this.maxPlayers = par1DedicatedServer.getIntProperty("max-players", 20);
        this.func_72371_a(par1DedicatedServer.func_71332_a("white-list", false));

        if (!par1DedicatedServer.func_71264_H())
        {
            this.func_72390_e().func_73708_a(true);
            this.func_72363_f().func_73708_a(true);
        }

        this.func_72390_e().func_73707_e();
        this.func_72390_e().func_73711_f();
        this.func_72363_f().func_73707_e();
        this.func_72363_f().func_73711_f();
        this.func_72417_t();
        this.func_72418_v();
        this.func_72419_u();
        this.saveWhiteList();
    }

    public void func_72371_a(boolean par1)
    {
        super.func_72371_a(par1);
        this.func_72420_s().setProperty("white-list", Boolean.valueOf(par1));
        this.func_72420_s().saveProperties();
    }

    /**
     * This adds a username to the ops list, then saves the op list
     */
    public void addOp(String par1Str)
    {
        super.addOp(par1Str);
        this.func_72419_u();
    }

    /**
     * This removes a username from the ops list, then saves the op list
     */
    public void removeOp(String par1Str)
    {
        super.removeOp(par1Str);
        this.func_72419_u();
    }

    /**
     * remove the specified player from the whitelist
     */
    public void removeFromWhiteList(String par1Str)
    {
        super.removeFromWhiteList(par1Str);
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
     * reloads the whitelist
     */
    public void reloadWhiteList()
    {
        this.func_72418_v();
    }

    private void func_72417_t()
    {
        try
        {
            this.func_72376_i().clear();
            BufferedReader var1 = new BufferedReader(new FileReader(this.field_72423_e));
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

    private void func_72419_u()
    {
        try
        {
            PrintWriter var1 = new PrintWriter(new FileWriter(this.field_72423_e, false));
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

    private void func_72418_v()
    {
        try
        {
            this.getWhiteListedIPs().clear();
            BufferedReader var1 = new BufferedReader(new FileReader(this.field_72422_f));
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
            PrintWriter var1 = new PrintWriter(new FileWriter(this.field_72422_f, false));
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
     * Determine if the player is allowed to connect based on current server settings
     */
    public boolean isAllowedToLogin(String par1Str)
    {
        par1Str = par1Str.trim().toLowerCase();
        return !this.func_72383_n() || this.isOp(par1Str) || this.getWhiteListedIPs().contains(par1Str);
    }

    public DedicatedServer func_72420_s()
    {
        return (DedicatedServer)super.func_72365_p();
    }

    public MinecraftServer func_72365_p()
    {
        return this.func_72420_s();
    }
}
