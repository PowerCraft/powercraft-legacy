package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.server.MinecraftServer;

@SideOnly(Side.CLIENT)
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
