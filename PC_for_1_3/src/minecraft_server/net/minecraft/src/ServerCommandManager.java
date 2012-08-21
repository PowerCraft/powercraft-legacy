package net.minecraft.src;

import java.util.Iterator;
import net.minecraft.server.MinecraftServer;

public class ServerCommandManager extends CommandHandler implements IAdminCommand
{
    public ServerCommandManager()
    {
        this.func_71560_a(new CommandTime());
        this.func_71560_a(new CommandGameMode());
        this.func_71560_a(new CommandDefaultGameMode());
        this.func_71560_a(new CommandKill());
        this.func_71560_a(new CommandToggleDownfall());
        this.func_71560_a(new CommandXP());
        this.func_71560_a(new CommandServerTp());
        this.func_71560_a(new CommandGive());
        this.func_71560_a(new CommandServerEmote());
        this.func_71560_a(new CommandShowSeed());
        this.func_71560_a(new CommandHelp());
        this.func_71560_a(new CommandDebug());
        this.func_71560_a(new CommandServerMessage());

        if (MinecraftServer.getServer().isDedicatedServer())
        {
            this.func_71560_a(new CommandServerOp());
            this.func_71560_a(new CommandServerDeop());
            this.func_71560_a(new CommandServerStop());
            this.func_71560_a(new CommandServerSaveAll());
            this.func_71560_a(new CommandServerSaveOff());
            this.func_71560_a(new CommandServerSaveOn());
            this.func_71560_a(new CommandServerBanIp());
            this.func_71560_a(new CommandServerPardonIp());
            this.func_71560_a(new CommandServerBan());
            this.func_71560_a(new CommandServerBanlist());
            this.func_71560_a(new CommandServerPardon());
            this.func_71560_a(new CommandServerKick());
            this.func_71560_a(new CommandServerList());
            this.func_71560_a(new CommandServerSay());
            this.func_71560_a(new CommandServerWhitelist());
        }
        else
        {
            this.func_71560_a(new CommandServerPublishLocal());
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

            if (var6 != par1ICommandSender && MinecraftServer.getServer().getConfigurationManager().isOp(var6.username))
            {
                var6.func_70006_a("\u00a77\u00a7o[" + par1ICommandSender.getCommandSenderName() + ": " + var6.translateString(par3Str, par4ArrayOfObj) + "]");
            }
        }

        if (par1ICommandSender != MinecraftServer.getServer())
        {
            MinecraftServer.logger.info("[" + par1ICommandSender.getCommandSenderName() + ": " + MinecraftServer.getServer().translateString(par3Str, par4ArrayOfObj) + "]");
        }

        if ((par2 & 1) != 1)
        {
            par1ICommandSender.func_70006_a(par1ICommandSender.translateString(par3Str, par4ArrayOfObj));
        }
    }
}
