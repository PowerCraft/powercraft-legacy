package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandServerBanlist extends CommandBase
{
    public String getCommandName()
    {
        return "banlist";
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return (MinecraftServer.getServer().func_71203_ab().func_72363_f().func_73710_b() || MinecraftServer.getServer().func_71203_ab().func_72390_e().func_73710_b()) && super.canCommandSenderUseCommand(par1ICommandSender);
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return par1ICommandSender.translateString("commands.banlist.usage", new Object[0]);
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length >= 1 && par2ArrayOfStr[0].equalsIgnoreCase("ips"))
        {
            par1ICommandSender.func_70006_a(par1ICommandSender.translateString("commands.banlist.ips", new Object[] {Integer.valueOf(MinecraftServer.getServer().func_71203_ab().func_72363_f().func_73712_c().size())}));
            par1ICommandSender.func_70006_a(joinNiceString(MinecraftServer.getServer().func_71203_ab().func_72363_f().func_73712_c().keySet().toArray()));
        }
        else
        {
            par1ICommandSender.func_70006_a(par1ICommandSender.translateString("commands.banlist.players", new Object[] {Integer.valueOf(MinecraftServer.getServer().func_71203_ab().func_72390_e().func_73712_c().size())}));
            par1ICommandSender.func_70006_a(joinNiceString(MinecraftServer.getServer().func_71203_ab().func_72390_e().func_73712_c().keySet().toArray()));
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length == 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"players", "ips"}): null;
    }
}
