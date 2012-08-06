package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class CommandServerList extends CommandBase
{
    public String getCommandName()
    {
        return "list";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        par1ICommandSender.sendChatToPlayer(par1ICommandSender.translateString("commands.players.list", new Object[] {Integer.valueOf(MinecraftServer.getServer().getPlayerListSize()), Integer.valueOf(MinecraftServer.getServer().getMaxPlayers())}));
        par1ICommandSender.sendChatToPlayer(MinecraftServer.getServer().getConfigurationManager().func_72398_c());
    }
}
