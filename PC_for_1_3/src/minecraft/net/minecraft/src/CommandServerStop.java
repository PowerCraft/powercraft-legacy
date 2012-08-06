package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class CommandServerStop extends CommandBase
{
    public String getCommandName()
    {
        return "stop";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        notifyAdmins(par1ICommandSender, "commands.stop.start", new Object[0]);
        MinecraftServer.getServer().setServerStopping();
    }
}
