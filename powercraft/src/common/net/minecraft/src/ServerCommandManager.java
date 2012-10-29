package net.minecraft.src;

import java.util.Iterator;
import net.minecraft.server.MinecraftServer;

public class ServerCommandManager extends CommandHandler implements IAdminCommand
{
    public ServerCommandManager()
    {
        this.registerCommand(new CommandTime());
        this.registerCommand(new CommandGameMode());
        this.registerCommand(new CommandDefaultGameMode());
        this.registerCommand(new CommandKill());
        this.registerCommand(new CommandToggleDownfall());
        this.registerCommand(new CommandXP());
        this.registerCommand(new CommandServerTp());
        this.registerCommand(new CommandGive());
        this.registerCommand(new CommandServerEmote());
        this.registerCommand(new CommandShowSeed());
        this.registerCommand(new CommandHelp());
        this.registerCommand(new CommandDebug());
        this.registerCommand(new CommandServerMessage());

        if (MinecraftServer.getServer().isDedicatedServer())
        {
            this.registerCommand(new CommandServerOp());
            this.registerCommand(new CommandServerDeop());
            this.registerCommand(new CommandServerStop());
            this.registerCommand(new CommandServerSaveAll());
            this.registerCommand(new CommandServerSaveOff());
            this.registerCommand(new CommandServerSaveOn());
            this.registerCommand(new CommandServerBanIp());
            this.registerCommand(new CommandServerPardonIp());
            this.registerCommand(new CommandServerBan());
            this.registerCommand(new CommandServerBanlist());
            this.registerCommand(new CommandServerPardon());
            this.registerCommand(new CommandServerKick());
            this.registerCommand(new CommandServerList());
            this.registerCommand(new CommandServerSay());
            this.registerCommand(new CommandServerWhitelist());
        }
        else
        {
            this.registerCommand(new CommandServerPublishLocal());
        }

        CommandBase.setAdminCommander(this);
    }

    /**
     * Sends a message to the admins of the server from a given CommandSender with the given resource string and given
     * extra srings. If the int par2 is even or zero, the original sender is also notified.
     */
    public void notifyAdmins(ICommandSender par1ICommandSender, int par2, String par3Str, Object ... par4ArrayOfObj)
    {
        Iterator var5 = MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator();

        while (var5.hasNext())
        {
            EntityPlayerMP var6 = (EntityPlayerMP)var5.next();

            if (var6 != par1ICommandSender && MinecraftServer.getServer().getConfigurationManager().areCommandsAllowed(var6.username))
            {
                var6.sendChatToPlayer("\u00a77\u00a7o[" + par1ICommandSender.getCommandSenderName() + ": " + var6.translateString(par3Str, par4ArrayOfObj) + "]");
            }
        }

        if (par1ICommandSender != MinecraftServer.getServer())
        {
            MinecraftServer.logger.info("[" + par1ICommandSender.getCommandSenderName() + ": " + MinecraftServer.getServer().translateString(par3Str, par4ArrayOfObj) + "]");
        }

        if ((par2 & 1) != 1)
        {
            par1ICommandSender.sendChatToPlayer(par1ICommandSender.translateString(par3Str, par4ArrayOfObj));
        }
    }
}
