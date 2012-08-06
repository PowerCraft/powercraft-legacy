package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandXP extends CommandBase
{
    public String getCommandName()
    {
        return "xp";
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return par1ICommandSender.translateString("commands.xp.usage", new Object[0]);
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length > 0)
        {
            int var4 = parseIntBounded(par1ICommandSender, par2ArrayOfStr[0], 0, 5000);
            EntityPlayer var3;

            if (par2ArrayOfStr.length > 1)
            {
                var3 = this.func_71543_a(par2ArrayOfStr[1]);
            }
            else
            {
                var3 = getCommandSenderAsPlayer(par1ICommandSender);
            }

            var3.addExperience(var4);
            notifyAdmins(par1ICommandSender, "commands.xp.success", new Object[] {Integer.valueOf(var4), var3.getEntityName()});
        }
        else
        {
            throw new WrongUsageException("commands.xp.usage", new Object[0]);
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length == 2 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, this.func_71542_c()) : null;
    }

    protected EntityPlayer func_71543_a(String par1Str)
    {
        EntityPlayerMP var2 = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(par1Str);

        if (var2 == null)
        {
            throw new PlayerNotFoundException();
        }
        else
        {
            return var2;
        }
    }

    protected String[] func_71542_c()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }
}
