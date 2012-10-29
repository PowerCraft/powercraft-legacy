package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class CommandBase implements ICommand
{
    private static IAdminCommand theAdmin = null;

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "/" + this.getCommandName();
    }

    public List getCommandAliases()
    {
        return null;
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return par1ICommandSender.canCommandSenderUseCommand(this.getCommandName());
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return null;
    }

    /**
     * Parses an int from the given string.
     */
    public static int parseInt(ICommandSender par0ICommandSender, String par1Str)
    {
        try
        {
            return Integer.parseInt(par1Str);
        }
        catch (NumberFormatException var3)
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {par1Str});
        }
    }

    /**
     * Parses an int from the given sring with a specified minimum.
     */
    public static int parseIntWithMin(ICommandSender par0ICommandSender, String par1Str, int par2)
    {
        return parseIntBounded(par0ICommandSender, par1Str, par2, Integer.MAX_VALUE);
    }

    /**
     * Parses an int from the given string within a specified bound.
     */
    public static int parseIntBounded(ICommandSender par0ICommandSender, String par1Str, int par2, int par3)
    {
        int var4 = parseInt(par0ICommandSender, par1Str);

        if (var4 < par2)
        {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] {Integer.valueOf(var4), Integer.valueOf(par2)});
        }
        else if (var4 > par3)
        {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] {Integer.valueOf(var4), Integer.valueOf(par3)});
        }
        else
        {
            return var4;
        }
    }

    /**
     * Returns the given ICommandSender as a EntityPlayer or throw an exception.
     */
    public static EntityPlayer getCommandSenderAsPlayer(ICommandSender par0ICommandSender)
    {
        if (par0ICommandSender instanceof EntityPlayer)
        {
            return (EntityPlayer)par0ICommandSender;
        }
        else
        {
            throw new PlayerNotFoundException("You must specify which player you wish to perform this action on.", new Object[0]);
        }
    }

    /**
     * Joins the given string array, starting with the given index, into a space seperated string.
     */
    public static String joinString(String[] par0ArrayOfStr, int par1)
    {
        StringBuilder var2 = new StringBuilder();

        for (int var3 = par1; var3 < par0ArrayOfStr.length; ++var3)
        {
            if (var3 > par1)
            {
                var2.append(" ");
            }

            var2.append(par0ArrayOfStr[var3]);
        }

        return var2.toString();
    }

    /**
     * Joins the given string array into a "x, y, and z" seperated string.
     */
    public static String joinNiceString(Object[] par0ArrayOfObj)
    {
        StringBuilder var1 = new StringBuilder();

        for (int var2 = 0; var2 < par0ArrayOfObj.length; ++var2)
        {
            String var3 = par0ArrayOfObj[var2].toString();

            if (var2 > 0)
            {
                if (var2 == par0ArrayOfObj.length - 1)
                {
                    var1.append(" and ");
                }
                else
                {
                    var1.append(", ");
                }
            }

            var1.append(var3);
        }

        return var1.toString();
    }

    /**
     * Returns true if the given substring is exactly equal to the start of the given string (case insensitive).
     */
    public static boolean doesStringStartWith(String par0Str, String par1Str)
    {
        return par1Str.regionMatches(true, 0, par0Str, 0, par0Str.length());
    }

    /**
     * Returns a List of strings (chosen from the given strings) which the last word in the given string array is a
     * beginning-match for. (Tab completion).
     */
    public static List getListOfStringsMatchingLastWord(String[] par0ArrayOfStr, String ... par1ArrayOfStr)
    {
        String var2 = par0ArrayOfStr[par0ArrayOfStr.length - 1];
        ArrayList var3 = new ArrayList();
        String[] var4 = par1ArrayOfStr;
        int var5 = par1ArrayOfStr.length;

        for (int var6 = 0; var6 < var5; ++var6)
        {
            String var7 = var4[var6];

            if (doesStringStartWith(var2, var7))
            {
                var3.add(var7);
            }
        }

        return var3;
    }

    /**
     * Returns a List of strings (chosen from the given string iterable) which the last word in the given string array
     * is a beginning-match for. (Tab completion).
     */
    public static List getListOfStringsFromIterableMatchingLastWord(String[] par0ArrayOfStr, Iterable par1Iterable)
    {
        String var2 = par0ArrayOfStr[par0ArrayOfStr.length - 1];
        ArrayList var3 = new ArrayList();
        Iterator var4 = par1Iterable.iterator();

        while (var4.hasNext())
        {
            String var5 = (String)var4.next();

            if (doesStringStartWith(var2, var5))
            {
                var3.add(var5);
            }
        }

        return var3;
    }

    public static void notifyAdmins(ICommandSender par0ICommandSender, String par1Str, Object ... par2ArrayOfObj)
    {
        notifyAdmins(par0ICommandSender, 0, par1Str, par2ArrayOfObj);
    }

    public static void notifyAdmins(ICommandSender par0ICommandSender, int par1, String par2Str, Object ... par3ArrayOfObj)
    {
        if (theAdmin != null)
        {
            theAdmin.notifyAdmins(par0ICommandSender, par1, par2Str, par3ArrayOfObj);
        }
    }

    /**
     * Sets the static IAdminCommander.
     */
    public static void setAdminCommander(IAdminCommand par0IAdminCommand)
    {
        theAdmin = par0IAdminCommand;
    }

    /**
     * Compares the name of this command to the name of the given command.
     */
    public int compareNameTo(ICommand par1ICommand)
    {
        return this.getCommandName().compareTo(par1ICommand.getCommandName());
    }

    public int compareTo(Object par1Obj)
    {
        return this.compareNameTo((ICommand)par1Obj);
    }
}
