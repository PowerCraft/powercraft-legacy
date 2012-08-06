package net.minecraft.src;

import java.util.List;
import java.util.regex.Matcher;
import net.minecraft.server.MinecraftServer;

public class CommandServerPardonIp extends CommandBase
{
    public String getCommandName()
    {
        return "pardon-ip";
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return MinecraftServer.getServer().func_71203_ab().func_72363_f().func_73710_b() && super.canCommandSenderUseCommand(par1ICommandSender);
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return par1ICommandSender.translateString("commands.unbanip.usage", new Object[0]);
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length == 1 && par2ArrayOfStr[0].length() > 1)
        {
            Matcher var3 = CommandServerBanIp.field_71545_a.matcher(par2ArrayOfStr[0]);

            if (var3.matches())
            {
                MinecraftServer.getServer().func_71203_ab().func_72363_f().func_73709_b(par2ArrayOfStr[0]);
                func_71522_a(par1ICommandSender, "commands.unbanip.success", new Object[] {par2ArrayOfStr[0]});
            }
            else
            {
                throw new SyntaxErrorException("commands.unbanip.invalid", new Object[0]);
            }
        }
        else
        {
            throw new WrongUsageException("commands.unbanip.usage", new Object[0]);
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length == 1 ? getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().func_71203_ab().func_72363_f().func_73712_c().keySet()) : null;
    }
}
