package net.minecraft.src;

import java.net.SocketAddress;
import net.minecraft.server.MinecraftServer;

public class IntegratedPlayerList extends ServerConfigurationManager
{
    private NBTTagCompound tagsForLastWrittenPlayer = null;

    public IntegratedPlayerList(IntegratedServer par1IntegratedServer)
    {
        super(par1IntegratedServer);
        this.viewDistance = 10;
    }

    /**
     * also stores the NBTTags if this is an intergratedPlayerList
     */
    protected void writePlayerData(EntityPlayerMP par1EntityPlayerMP)
    {
        if (par1EntityPlayerMP.getCommandSenderName().equals(this.getIntegratedServer().getServerOwner()))
        {
            this.tagsForLastWrittenPlayer = new NBTTagCompound();
            par1EntityPlayerMP.writeToNBT(this.tagsForLastWrittenPlayer);
        }

        super.writePlayerData(par1EntityPlayerMP);
    }

    /**
     * checks ban-lists, then white-lists, then space for the server. Returns null on success, or an error message
     */
    public String allowUserToConnect(SocketAddress par1SocketAddress, String par2Str)
    {
        return par2Str.equalsIgnoreCase(this.getIntegratedServer().getServerOwner()) ? "That name is already taken." : super.allowUserToConnect(par1SocketAddress, par2Str);
    }

    /**
     * get the associated Integrated Server
     */
    public IntegratedServer getIntegratedServer()
    {
        return (IntegratedServer)super.getServerInstance();
    }

    /**
     * gets the tags created in the last writePlayerData call
     */
    public NBTTagCompound getTagsFromLastWrite()
    {
        return this.tagsForLastWrittenPlayer;
    }

    public MinecraftServer getServerInstance()
    {
        return this.getIntegratedServer();
    }
}
