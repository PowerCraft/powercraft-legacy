package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandServerWhitelist extends CommandBase
{
    public String getCommandName()
    {
        return "whitelist";
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return par1ICommandSender.translateString("commands.whitelist.usage", new Object[0]);
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length >= 1)
        {
            if (par2ArrayOfStr[0].equals("on"))
            {
                MinecraftServer.getServer().func_71203_ab().func_72371_a(true);
                func_71522_a(par1ICommandSender, "commands.whitelist.enabled", new Object[0]);
                return;
            }

            if (par2ArrayOfStr[0].equals("off"))
            {
                MinecraftServer.getServer().func_71203_ab().func_72371_a(false);
                func_71522_a(par1ICommandSender, "commands.whitelist.disabled", new Object[0]);
                return;
            }

            if (par2ArrayOfStr[0].equals("list"))
            {
                par1ICommandSender.func_70006_a(par1ICommandSender.translateString("commands.whitelist.list", new Object[] {Integer.valueOf(MinecraftServer.getServer().func_71203_ab().getWhiteListedIPs().size()), Integer.valueOf(MinecraftServer.getServer().func_71203_ab().func_72373_m().length)}));
                par1ICommandSender.func_70006_a(joinNiceString(MinecraftServer.getServer().func_71203_ab().getWhiteListedIPs().toArray(new String[0])));
                return;
            }

            if (par2ArrayOfStr[0].equals("add"))
            {
                if (par2ArrayOfStr.length < 2)
                {
                    throw new WrongUsageException("commands.whitelist.add.usage", new Object[0]);
                }

                MinecraftServer.getServer().func_71203_ab().addToWhiteList(par2ArrayOfStr[1]);
                func_71522_a(par1ICommandSender, "commands.whitelist.add.success", new Object[] {par2ArrayOfStr[1]});
                return;
            }

            if (par2ArrayOfStr[0].equals("remove"))
            {
                if (par2ArrayOfStr.length < 2)
                {
                    throw new WrongUsageException("commands.whitelist.remove.usage", new Object[0]);
                }

                MinecraftServer.getServer().func_71203_ab().removeFromWhiteList(par2ArrayOfStr[1]);
                func_71522_a(par1ICommandSender, "commands.whitelist.remove.success", new Object[] {par2ArrayOfStr[1]});
                return;
            }

            if (par2ArrayOfStr[0].equals("reload"))
            {
                MinecraftServer.getServer().func_71203_ab().reloadWhiteList();
                func_71522_a(par1ICommandSender, "commands.whitelist.reloaded", new Object[0]);
                return;
            }
        }

        throw new WrongUsageException("commands.whitelist.usage", new Object[0]);
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length == 1)
        {
            return getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"on", "off", "list", "add", "remove", "reload"});
        }
        else
        {
            if (par2ArrayOfStr.length == 2)
            {
                if (par2ArrayOfStr[0].equals("add"))
                {
                    String[] var3 = MinecraftServer.getServer().func_71203_ab().func_72373_m();
                    ArrayList var4 = new ArrayList();
                    String var5 = par2ArrayOfStr[par2ArrayOfStr.length - 1];
                    String[] var6 = var3;
                    int var7 = var3.length;

                    for (int var8 = 0; var8 < var7; ++var8)
                    {
                        String var9 = var6[var8];

                        if (doesStringStartWith(var5, var9) && !MinecraftServer.getServer().func_71203_ab().getWhiteListedIPs().contains(var9))
                        {
                            var4.add(var9);
                        }
                    }

                    return var4;
                }

                if (par2ArrayOfStr[0].equals("remove"))
                {
                    return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().func_71203_ab().getWhiteListedIPs());
                }
            }

            return null;
        }
    }
}
