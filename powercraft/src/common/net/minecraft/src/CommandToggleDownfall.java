package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class CommandToggleDownfall extends CommandBase
{
    public String getCommandName()
    {
        return "toggledownfall";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        this.reallyToggleDownfall();
        notifyAdmins(par1ICommandSender, "commands.downfall.success", new Object[0]);
    }

    /**
     * Toggle rain and enable thundering.
     */
    protected void reallyToggleDownfall()
    {
        MinecraftServer.getServer().worldServers[0].toggleRain();
        MinecraftServer.getServer().worldServers[0].getWorldInfo().setThundering(true);
    }
}
