package net.minecraft.src;

public class CommandKill extends CommandBase
{
    public String getCommandName()
    {
        return "kill";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        EntityPlayer var3 = getCommandSenderAsPlayer(par1ICommandSender);
        var3.attackEntityFrom(DamageSource.outOfWorld, 1000);
        par1ICommandSender.sendChatToPlayer("Ouch. That look like it hurt.");
    }
}
