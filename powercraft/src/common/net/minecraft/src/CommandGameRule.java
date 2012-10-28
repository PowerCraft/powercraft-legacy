package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandGameRule extends CommandBase
{
    public String getCommandName()
    {
        return "gamerule";
    }

    public int func_82362_a()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return par1ICommandSender.translateString("commands.gamerule.usage", new Object[0]);
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        String var6;

        if (par2ArrayOfStr.length == 2)
        {
            var6 = par2ArrayOfStr[0];
            String var7 = par2ArrayOfStr[1];
            GameRules var8 = this.func_82366_d();

            if (var8.func_82765_e(var6))
            {
                var8.func_82764_b(var6, var7);
                notifyAdmins(par1ICommandSender, "commands.gamerule.success", new Object[0]);
            }
            else
            {
                notifyAdmins(par1ICommandSender, "commands.gamerule.norule", new Object[] {var6});
            }
        }
        else if (par2ArrayOfStr.length == 1)
        {
            var6 = par2ArrayOfStr[0];
            GameRules var4 = this.func_82366_d();

            if (var4.func_82765_e(var6))
            {
                String var5 = var4.func_82767_a(var6);
                par1ICommandSender.sendChatToPlayer(var6 + " = " + var5);
            }
            else
            {
                notifyAdmins(par1ICommandSender, "commands.gamerule.norule", new Object[] {var6});
            }
        }
        else if (par2ArrayOfStr.length == 0)
        {
            GameRules var3 = this.func_82366_d();
            par1ICommandSender.sendChatToPlayer(joinNiceString(var3.func_82763_b()));
        }
        else
        {
            throw new WrongUsageException("commands.gamerule.usage", new Object[0]);
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length == 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, this.func_82366_d().func_82763_b()) : (par2ArrayOfStr.length == 2 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"true", "false"}): null);
    }

    private GameRules func_82366_d()
    {
        return MinecraftServer.getServer().worldServerForDimension(0).func_82736_K();
    }
}
