package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandServerTp extends CommandBase
{
    public String getCommandName()
    {
        return "tp";
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return par1ICommandSender.translateString("commands.tp.usage", new Object[0]);
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length < 1)
        {
            throw new WrongUsageException("commands.tp.usage", new Object[0]);
        }
        else
        {
            MinecraftServer var3 = MinecraftServer.getServer();
            EntityPlayerMP var4;

            if (par2ArrayOfStr.length != 2 && par2ArrayOfStr.length != 4)
            {
                var4 = (EntityPlayerMP)getCommandSenderAsPlayer(par1ICommandSender);
            }
            else
            {
                var4 = var3.getConfigurationManager().getPlayerForUsername(par2ArrayOfStr[0]);

                if (var4 == null)
                {
                    throw new PlayerNotFoundException();
                }
            }

            if (par2ArrayOfStr.length != 3 && par2ArrayOfStr.length != 4)
            {
                if (par2ArrayOfStr.length == 1 || par2ArrayOfStr.length == 2)
                {
                    EntityPlayerMP var10 = var3.getConfigurationManager().getPlayerForUsername(par2ArrayOfStr[par2ArrayOfStr.length - 1]);

                    if (var10 == null)
                    {
                        throw new PlayerNotFoundException();
                    }

                    var4.serverForThisPlayer.setPlayerLocation(var10.posX, var10.posY, var10.posZ, var10.rotationYaw, var10.rotationPitch);
                    notifyAdmins(par1ICommandSender, "commands.tp.success", new Object[] {var4.getEntityName(), var10.getEntityName()});
                }
            }
            else if (var4.worldObj != null)
            {
                int var5 = par2ArrayOfStr.length - 3;
                int var6 = 30000000;
                int var7 = parseIntBounded(par1ICommandSender, par2ArrayOfStr[var5++], -var6, var6);
                int var8 = parseIntBounded(par1ICommandSender, par2ArrayOfStr[var5++], 0, 256);
                int var9 = parseIntBounded(par1ICommandSender, par2ArrayOfStr[var5++], -var6, var6);
                var4.setPositionAndUpdate((double)((float)var7 + 0.5F), (double)var8, (double)((float)var9 + 0.5F));
                notifyAdmins(par1ICommandSender, "commands.tp.coordinates", new Object[] {var4.getEntityName(), Integer.valueOf(var7), Integer.valueOf(var8), Integer.valueOf(var9)});
            }
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length != 1 && par2ArrayOfStr.length != 2 ? null : getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames());
    }
}
