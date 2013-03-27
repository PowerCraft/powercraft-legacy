package net.minecraft.src;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogAgent implements ILogAgent
{
    private final Logger field_98242_a;
    private final String field_98240_b;
    private final String field_98241_c;
    private final String field_98239_d;

    public LogAgent(String par1Str, String par2Str, String par3Str)
    {
        this.field_98242_a = Logger.getLogger(par1Str);
        this.field_98241_c = par1Str;
        this.field_98239_d = par2Str;
        this.field_98240_b = par3Str;
        this.func_98238_b();
    }

    private void func_98238_b()
    {
        this.field_98242_a.setUseParentHandlers(false);
        Handler[] var1 = this.field_98242_a.getHandlers();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            Handler var4 = var1[var3];
            this.field_98242_a.removeHandler(var4);
        }

        LogFormatter var6 = new LogFormatter(this, (LogAgentINNER1)null);
        ConsoleHandler var7 = new ConsoleHandler();
        var7.setFormatter(var6);
        this.field_98242_a.addHandler(var7);

        try
        {
            FileHandler var8 = new FileHandler(this.field_98240_b, true);
            var8.setFormatter(var6);
            this.field_98242_a.addHandler(var8);
        }
        catch (Exception var5)
        {
            this.field_98242_a.log(Level.WARNING, "Failed to log " + this.field_98241_c + " to " + this.field_98240_b, var5);
        }
    }

    public void logInfo(String par1Str)
    {
        this.field_98242_a.log(Level.INFO, par1Str);
    }

    public void logWarning(String par1Str)
    {
        this.field_98242_a.log(Level.WARNING, par1Str);
    }

    public void func_98231_b(String par1Str, Object ... par2ArrayOfObj)
    {
        this.field_98242_a.log(Level.WARNING, par1Str, par2ArrayOfObj);
    }

    public void func_98235_b(String par1Str, Throwable par2Throwable)
    {
        this.field_98242_a.log(Level.WARNING, par1Str, par2Throwable);
    }

    public void func_98232_c(String par1Str)
    {
        this.field_98242_a.log(Level.SEVERE, par1Str);
    }

    public void func_98234_c(String par1Str, Throwable par2Throwable)
    {
        this.field_98242_a.log(Level.SEVERE, par1Str, par2Throwable);
    }

    public void func_98230_d(String par1Str)
    {
        this.field_98242_a.log(Level.FINE, par1Str);
    }

    static String func_98237_a(LogAgent par0LogAgent)
    {
        return par0LogAgent.field_98239_d;
    }
}
