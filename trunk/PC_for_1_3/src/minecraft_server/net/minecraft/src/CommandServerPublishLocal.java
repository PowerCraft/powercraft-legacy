package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class CommandServerPublishLocal extends CommandBase
{
    public String getCommandName()
    {
        return "publish";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        String var3 = MinecraftServer.getServer().func_71206_a(EnumGameType.SURVIVAL, false);

        if (var3 != null)
        {
            func_71522_a(par1ICommandSender, "commands.publish.started", new Object[] {var3});
        }
        else
        {
            func_71522_a(par1ICommandSender, "commands.publish.failed", new Object[0]);
        }
    }
}
