package net.minecraft.src;

public class CommandShowSeed extends CommandBase
{
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
