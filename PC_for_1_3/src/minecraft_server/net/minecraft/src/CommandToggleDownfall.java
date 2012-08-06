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
        func_71522_a(par1ICommandSender, "commands.downfall.success", new Object[0]);
    }

    protected void func_71554_c()
    {
        MinecraftServer.getServer().worldMngr[0].commandToggleDownfall();
        MinecraftServer.getServer().worldMngr[0].getWorldInfo().setThundering(true);
    }
}
