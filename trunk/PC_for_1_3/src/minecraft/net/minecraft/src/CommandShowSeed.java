package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class CommandShowSeed extends CommandBase
{
    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return MinecraftServer.getServer().isSinglePlayer() || super.canCommandSenderUseCommand(par1ICommandSender);
    }

    public String getCommandName()
    {
        return "seed";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        EntityPlayer var3 = getCommandSenderAsPlayer(par1ICommandSender);
        par1ICommandSender.sendChatToPlayer("Seed: " + var3.worldObj.getSeed());
    }
}
