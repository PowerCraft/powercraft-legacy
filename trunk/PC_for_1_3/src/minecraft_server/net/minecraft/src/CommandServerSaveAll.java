package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class CommandServerSaveAll extends CommandBase
{
    public String getCommandName()
    {
        return "save-all";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        MinecraftServer var3 = MinecraftServer.getServer();
        par1ICommandSender.func_70006_a(par1ICommandSender.translateString("commands.save.start", new Object[0]));

        if (var3.getConfigurationManager() != null)
        {
            var3.getConfigurationManager().savePlayerStates();
        }

        try
        {
            for (int var4 = 0; var4 < var3.theWorldServer.length; ++var4)
            {
                if (var3.theWorldServer[var4] != null)
                {
                    WorldServer var5 = var3.theWorldServer[var4];
                    boolean var6 = var5.levelSaving;
                    var5.levelSaving = false;
                    var5.saveAllChunks(true, (IProgressUpdate)null);
                    var5.levelSaving = var6;
                }
            }
        }
        catch (MinecraftException var7)
        {
            func_71522_a(par1ICommandSender, "commands.save.failed", new Object[] {var7.getMessage()});
            return;
        }

        func_71522_a(par1ICommandSender, "commands.save.success", new Object[0]);
    }
}
