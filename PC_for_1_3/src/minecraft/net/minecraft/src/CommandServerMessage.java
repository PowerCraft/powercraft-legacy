package net.minecraft.src;

import java.util.Arrays;
import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandServerMessage extends CommandBase
{
    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return (!MinecraftServer.getServer().isSinglePlayer() || MinecraftServer.getServer().getPlayerListSize() > 1) && super.canCommandSenderUseCommand(par1ICommandSender);
    }

    public List getCommandAliases()
    {
        return Arrays.asList(new String[] {"w", "msg"});
    }

    public String getCommandName()
    {
        return "tell";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length < 2)
        {
            throw new WrongUsageException("commands.message.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP var3 = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(par2ArrayOfStr[0]);

            if (var3 == null)
            {
                throw new PlayerNotFoundException();
            }
            else if (var3 == par1ICommandSender)
            {
                throw new PlayerNotFoundException("commands.message.sameTarget", new Object[0]);
            }
            else
            {
                String var4 = joinString(par2ArrayOfStr, 1);
                var3.sendChatToPlayer("\u00a77\u00a7o" + var3.translateString("commands.message.display.incoming", new Object[] {par1ICommandSender.getCommandSenderName(), var4}));
                par1ICommandSender.sendChatToPlayer("\u00a77\u00a7o" + par1ICommandSender.translateString("commands.message.display.outgoing", new Object[] {var3.getCommandSenderName(), var4}));
            }
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames());
    }
}
