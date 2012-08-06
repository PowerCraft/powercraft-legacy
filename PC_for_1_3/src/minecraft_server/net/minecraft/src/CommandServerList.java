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
        par1ICommandSender.func_70006_a(par1ICommandSender.translateString("commands.players.list", new Object[] {Integer.valueOf(MinecraftServer.getServer().playersOnline()), Integer.valueOf(MinecraftServer.getServer().getMaxPlayers())}));
        par1ICommandSender.func_70006_a(MinecraftServer.getServer().func_71203_ab().getPlayerList());
    }
}
