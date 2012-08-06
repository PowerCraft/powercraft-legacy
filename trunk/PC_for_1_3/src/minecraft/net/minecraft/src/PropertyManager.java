package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyManager
{
    public static Logger myLogger = Logger.getLogger("Minecraft");
    private Properties properties = new Properties();
    private File associatedFile;

    public PropertyManager(File par1File)
    {
        this.associatedFile = par1File;

        if (par1File.exists())
        {
            FileInputStream var2 = null;

            try
            {
                var2 = new FileInputStream(par1File);
                this.properties.load(var2);
            }
            catch (Exception var12)
            {
                myLogger.log(Level.WARNING, "Failed to load " + par1File, var12);
                this.logMessageAndSave();
            }
            finally
            {
                if (var2 != null)
                {
                    try
                    {
                        var2.close();
                    }
                    catch (IOException var11)
                    {
                        ;
                    }
                }
            }
        }
        else
        {
            myLogger.log(Level.WARNING, par1File + " does not exist");
            this.logMessageAndSave();
        }
    }

    /**
     * logs an info message then calls saveSettingsToFile Yes this appears to be a potential stack overflow - these 2
     * functions call each other repeatdly if an exception occurs.
     */
    public void logMessageAndSave()
    {
        myLogger.log(Level.INFO, "Generating new properties file");
        this.saveSettingsToFile();
    }

    /**
     * calls logMessageAndSave if an exception occurs
     */
    public void saveSettingsToFile()
    {
        FileOutputStream var1 = null;

        try
        {
            var1 = new FileOutputStream(this.associatedFile);
            this.properties.store(var1, "Minecraft server properties");
        }
        catch (Exception var11)
        {
            myLogger.log(Level.WARNING, "Failed to save " + this.associatedFile, var11);
            this.logMessageAndSave();
        }
        finally
        {
            if (var1 != null)
            {
                try
                {
                    var1.close();
                }
                catch (IOException var10)
                {
                    ;
                }
            }
        }
    }

    public File getFile()
    {
        return this.associatedFile;
    }

    /**
     * set if it doesn't exist, otherwise get
     */
    public String getOrSetProperty(String par1Str, String par2Str)
    {
        if (!this.properties.containsKey(par1Str))
        {
            this.properties.setProperty(par1Str, par2Str);
            this.saveSettingsToFile();
        }

        return this.properties.getProperty(par1Str, par2Str);
    }

    /**
     * set if it doesn't exist, otherwise get
     */
    public int getOrSetIntProperty(String par1Str, int par2)
    {
        try
        {
            return Integer.parseInt(this.getOrSetProperty(par1Str, "" + par2));
        }
        catch (Exception var4)
        {
            this.properties.setProperty(par1Str, "" + par2);
            return par2;
        }
    }

    /**
     * set if it doesn't exist, otherwise get
     */
    public boolean getOrSetBoolProperty(String par1Str, boolean par2)
    {
        try
        {
            return Boolean.parseBoolean(this.getOrSetProperty(par1Str, "" + par2));
        }
        catch (Exception var4)
        {
            this.properties.setProperty(par1Str, "" + par2);
            return par2;
        }
    }

    /**
     * returns void, rather than what you input
     */
    public void setArbitraryProperty(String par1Str, Object par2Obj)
    {
        this.properties.setProperty(par1Str, "" + par2Obj);
    }
}
