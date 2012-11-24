package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.MinecraftServer;

public abstract class CommandBase implements ICommand
{
    private static IAdminCommand theAdmin = null;

    public int getRequiredPermissionLevel()
    {
        return 4;
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "/" + this.getCommandName();
    }

    public List getCommandAliases()
    {
        return null;
    }

    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return null;
    }

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

    public static int parseIntWithMin(ICommandSender par0ICommandSender, String par1Str, int par2)
    {
        return parseIntBounded(par0ICommandSender, par1Str, par2, Integer.MAX_VALUE);
    }

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

    public static double func_82363_b(ICommandSender par0ICommandSender, String par1Str)
    {
        try
        {
            return Double.parseDouble(par1Str);
        }
        catch (NumberFormatException var3)
        {
            throw new NumberInvalidException("commands.generic.double.invalid", new Object[] {par1Str});
        }
    }

    public static EntityPlayerMP getCommandSenderAsPlayer(ICommandSender par0ICommandSender)
    {
        if (par0ICommandSender instanceof EntityPlayerMP)
        {
            return (EntityPlayerMP)par0ICommandSender;
        }
        else
        {
            throw new PlayerNotFoundException("You must specify which player you wish to perform this action on.", new Object[0]);
        }
    }

    public static EntityPlayerMP func_82359_c(ICommandSender par0ICommandSender, String par1Str)
    {
        EntityPlayerMP var2 = PlayerSelector.func_82386_a(par0ICommandSender, par1Str);

        if (var2 != null)
        {
            return var2;
        }
        else
        {
            var2 = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(par1Str);

            if (var2 == null)
            {
                throw new PlayerNotFoundException();
            }
            else
            {
                return var2;
            }
        }
    }

    public static String func_82360_a(ICommandSender par0ICommandSender, String[] par1ArrayOfStr, int par2)
    {
        return func_82361_a(par0ICommandSender, par1ArrayOfStr, par2, false);
    }

    public static String func_82361_a(ICommandSender par0ICommandSender, String[] par1ArrayOfStr, int par2, boolean par3)
    {
        StringBuilder var4 = new StringBuilder();

        for (int var5 = par2; var5 < par1ArrayOfStr.length; ++var5)
        {
            if (var5 > par2)
            {
                var4.append(" ");
            }

            String var6 = par1ArrayOfStr[var5];

            if (par3)
            {
                String var7 = PlayerSelector.func_82385_b(par0ICommandSender, var6);

                if (var7 != null)
                {
                    var6 = var7;
                }
                else if (PlayerSelector.func_82378_b(var6))
                {
                    throw new PlayerNotFoundException();
                }
            }

            var4.append(var6);
        }

        return var4.toString();
    }

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

    public static boolean doesStringStartWith(String par0Str, String par1Str)
    {
        return par1Str.regionMatches(true, 0, par0Str, 0, par0Str.length());
    }

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

    public boolean isUsernameIndex(int par1)
    {
        return false;
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

    public static void setAdminCommander(IAdminCommand par0IAdminCommand)
    {
        theAdmin = par0IAdminCommand;
    }

    public int compareNameTo(ICommand par1ICommand)
    {
        return this.getCommandName().compareTo(par1ICommand.getCommandName());
    }

    public int compareTo(Object par1Obj)
    {
        return this.compareNameTo((ICommand)par1Obj);
    }
}
