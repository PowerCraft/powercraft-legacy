package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class CommandServerPublishLocal extends CommandBase
{
    public String getCommandName()
    {
        return "publish";
    }

    public int func_82362_a()
    {
        return 4;
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        String var3 = MinecraftServer.getServer().shareToLAN(EnumGameType.SURVIVAL, false);

        if (var3 != null)
        {
            notifyAdmins(par1ICommandSender, "commands.publish.started", new Object[] {var3});
        }
        else
        {
            notifyAdmins(par1ICommandSender, "commands.publish.failed", new Object[0]);
        }
    }
}
