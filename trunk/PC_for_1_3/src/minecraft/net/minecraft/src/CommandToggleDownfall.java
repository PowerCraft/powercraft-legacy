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
        this.func_71554_c();
        notifyAdmins(par1ICommandSender, "commands.downfall.success", new Object[0]);
    }

    protected void func_71554_c()
    {
        MinecraftServer.getServer().theWorldServer[0].toggleRain();
        MinecraftServer.getServer().theWorldServer[0].getWorldInfo().setThundering(true);
    }
}
