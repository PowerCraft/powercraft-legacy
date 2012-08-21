package net.minecraft.src;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.minecraft.server.MinecraftServer;

public class CommandHelp extends CommandBase
{
    public String getCommandName()
    {
        return "help";
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return par1ICommandSender.translateString("commands.help.usage", new Object[0]);
    }

    public List getCommandAliases()
    {
        return Arrays.asList(new String[] {"?"});
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        List var3 = this.func_71534_d(par1ICommandSender);
        byte var4 = 7;
        int var5 = var3.size() / var4;
        boolean var6 = false;
        ICommand var9;
        int var11;

        try
        {
            var11 = par2ArrayOfStr.length == 0 ? 0 : parseIntBounded(par1ICommandSender, par2ArrayOfStr[0], 1, var5 + 1) - 1;
        }
        catch (NumberInvalidException var10)
        {
            Map var8 = this.func_71535_c();
            var9 = (ICommand)var8.get(par2ArrayOfStr[0]);

            if (var9 != null)
            {
                throw new WrongUsageException(var9.getCommandUsage(par1ICommandSender), new Object[0]);
            }

            throw new CommandNotFoundException();
        }

        int var7 = Math.min((var11 + 1) * var4, var3.size());
        par1ICommandSender.sendChatToPlayer("\u00a72" + par1ICommandSender.translateString("commands.help.header", new Object[] {Integer.valueOf(var11 + 1), Integer.valueOf(var5 + 1)}));

        for (int var12 = var11 * var4; var12 < var7; ++var12)
        {
            var9 = (ICommand)var3.get(var12);
            par1ICommandSender.sendChatToPlayer(var9.getCommandUsage(par1ICommandSender));
        }

        if (var11 == 0 && par1ICommandSender instanceof EntityPlayer)
        {
            par1ICommandSender.sendChatToPlayer("\u00a7a" + par1ICommandSender.translateString("commands.help.footer", new Object[0]));
        }
    }

    protected List func_71534_d(ICommandSender par1ICommandSender)
    {
        List var2 = MinecraftServer.getServer().getCommandManager().getPossibleCommands(par1ICommandSender);
        Collections.sort(var2);
        return var2;
    }

    protected Map func_71535_c()
    {
        return MinecraftServer.getServer().getCommandManager().getCommands();
    }
}
