package net.minecraft.src;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrashReport
{
    /** Description of the crash report. */
    private final String description;

    /** The Throwable that is the "cause" for this crash and Crash Report. */
    private final Throwable cause;

    /** Holds the keys and values of all crash report sections. */
    private final Map crashReportSections = new LinkedHashMap();

    /** File of crash report. */
    private File crashReportFile = null;

    public CrashReport(String par1Str, Throwable par2Throwable)
    {
        this.description = par1Str;
        this.cause = par2Throwable;
        this.func_71504_g();
    }

    private void func_71504_g()
    {
        this.addCrashSectionCallable("Minecraft Version", new CallableMinecraftVersion(this));
        this.addCrashSectionCallable("Operating System", new CallableOSInfo(this));
        this.addCrashSectionCallable("Java Version", new CallableJavaInfo(this));
        this.addCrashSectionCallable("Java VM Version", new CallableJavaInfo2(this));
        this.addCrashSectionCallable("Memory", new CallableMemoryInfo(this));
        this.addCrashSectionCallable("JVM Flags", new CallableJVMFlags(this));
        FMLCommonHandler.instance().enhanceCrashReport(this);
    }

    /**
     * Adds a Crashreport section with the given name with the value set to the result of the given Callable;
     */
    public void addCrashSectionCallable(String par1Str, Callable par2Callable)
    {
        try
        {
            this.addCrashSection(par1Str, par2Callable.call());
        }
        catch (Throwable var4)
        {
            this.addCrashSectionThrowable(par1Str, var4);
        }
    }

    /**
     * Adds a Crashreport section with the given name with the given value (convered .toString())
     */
    public void addCrashSection(String par1Str, Object par2Obj)
    {
        this.crashReportSections.put(par1Str, par2Obj == null ? "null" : par2Obj.toString());
    }

    /**
     * Adds a Crashreport section with the given name with the given Throwable
     */
    public void addCrashSectionThrowable(String par1Str, Throwable par2Throwable)
    {
        this.addCrashSection(par1Str, "~ERROR~ " + par2Throwable.getClass().getSimpleName() + ": " + par2Throwable.getMessage());
    }

    /**
     * Returns the description of the Crash Report.
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * Returns the Throwable object that is the cause for the crash and Crash Report.
     */
    public Throwable getCrashCause()
    {
        return this.cause;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets a string representation of all sections in the crash report.
     */
    public String getSections()
    {
        StringBuilder var1 = new StringBuilder();
        this.getSectionsInStringBuilder(var1);
        return var1.toString();
    }

    /**
     * Gets the various sections of the crash report into the given StringBuilder
     */
    public void getSectionsInStringBuilder(StringBuilder par1StringBuilder)
    {
        boolean var2 = true;

        for (Iterator var3 = this.crashReportSections.entrySet().iterator(); var3.hasNext(); var2 = false)
        {
            Entry var4 = (Entry)var3.next();

            if (!var2)
            {
                par1StringBuilder.append("\n");
            }

            par1StringBuilder.append("- ");
            par1StringBuilder.append((String)var4.getKey());
            par1StringBuilder.append(": ");
            par1StringBuilder.append((String)var4.getValue());
        }
    }

    /**
     * Gets the stack trace of the Throwable that caused this crash report, or if that fails, the cause .toString().
     */
    public String getCauseStackTraceOrString()
    {
        StringWriter var1 = null;
        PrintWriter var2 = null;
        String var3 = this.cause.toString();

        try
        {
            var1 = new StringWriter();
            var2 = new PrintWriter(var1);
            this.cause.printStackTrace(var2);
            var3 = var1.toString();
        }
        finally
        {
            try
            {
                if (var1 != null)
                {
                    var1.close();
                }

                if (var2 != null)
                {
                    var2.close();
                }
            }
            catch (IOException var10)
            {
                ;
            }
        }

        return var3;
    }

    /**
     * Gets the complete report with headers, stack trace, and different sections as a string.
     */
    public String getCompleteReport()
    {
        StringBuilder var1 = new StringBuilder();
        var1.append("---- Minecraft Crash Report ----\n");
        var1.append("// ");
        var1.append(getWittyComment());
        var1.append("\n\n");
        var1.append("Time: ");
        var1.append((new SimpleDateFormat()).format(new Date()));
        var1.append("\n");
        var1.append("Description: ");
        var1.append(this.description);
        var1.append("\n\n");
        var1.append(this.getCauseStackTraceOrString());
        var1.append("\n");
        var1.append("Relevant Details:");
        var1.append("\n");
        this.getSectionsInStringBuilder(var1);
        return var1.toString();
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets the file this crash report is saved into.
     */
    public File getFile()
    {
        return this.crashReportFile;
    }

    /**
     * Saves the complete crash report to the given File.
     */
    public boolean saveToFile(File par1File)
    {
        if (this.crashReportFile != null)
        {
            return false;
        }
        else
        {
            if (par1File.getParentFile() != null)
            {
                par1File.getParentFile().mkdirs();
            }

            try
            {
                FileWriter var2 = new FileWriter(par1File);
                var2.write(this.getCompleteReport());
                var2.close();
                this.crashReportFile = par1File;
                return true;
            }
            catch (Throwable var3)
            {
                Logger.getLogger("Minecraft").log(Level.SEVERE, "Could not save crash report to " + par1File, var3);
                return false;
            }
        }
    }

    /**
     * Gets a random witty comment for inclusion in this CrashReport
     */
    private static String getWittyComment()
    {
        String[] var0 = new String[] {"Who set us up the TNT?", "Everything\'s going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I\'m sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don\'t be sad. I\'ll do better next time, I promise!", "Don\'t be sad, have a hug! <3", "I just don\'t know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn\'t worry myself about that.", "I bet Cylons wouldn\'t have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I\'m Minecraft, and I\'m a crashaholic.", "Ooh. Shiny.", "This doesn\'t make any sense!", "Why is it breaking :("};

        try
        {
            return var0[(int)(System.nanoTime() % (long)var0.length)];
        }
        catch (Throwable var2)
        {
            return "Witty comment unavailable :(";
        }
    }
}
