package net.minecraft.src;

import java.util.Iterator;
import net.minecraft.server.MinecraftServer;

public class WorldManager implements IWorldAccess
{
    private MinecraftServer mcServer;

    private WorldServer theWorldServer;

    public WorldManager(MinecraftServer par1MinecraftServer, WorldServer par2WorldServer)
    {
        this.mcServer = par1MinecraftServer;
        this.theWorldServer = par2WorldServer;
    }

    public void spawnParticle(String par1Str, double par2, double par4, double par6, double par8, double par10, double par12) {}

    public void obtainEntitySkin(Entity par1Entity)
    {
        this.theWorldServer.getEntityTracker().addEntityToTracker(par1Entity);
    }

    public void releaseEntitySkin(Entity par1Entity)
    {
        this.theWorldServer.getEntityTracker().removeEntityFromAllTrackingPlayers(par1Entity);
    }

    public void playSound(String par1Str, double par2, double par4, double par6, float par8, float par9)
    {
        this.mcServer.getConfigurationManager().sendToAllNear(par2, par4, par6, par8 > 1.0F ? (double)(16.0F * par8) : 16.0D, this.theWorldServer.provider.dimensionId, new Packet62LevelSound(par1Str, par2, par4, par6, par8, par9));
    }

    public void func_85102_a(EntityPlayer par1EntityPlayer, String par2Str, double par3, double par5, double par7, float par9, float par10)
    {
        this.mcServer.getConfigurationManager().sendToAllNearExcept(par1EntityPlayer, par3, par5, par7, par9 > 1.0F ? (double)(16.0F * par9) : 16.0D, this.theWorldServer.provider.dimensionId, new Packet62LevelSound(par2Str, par3, par5, par7, par9, par10));
    }

    public void markBlockRangeNeedsUpdate(int par1, int par2, int par3, int par4, int par5, int par6) {}

    public void markBlockNeedsUpdate(int par1, int par2, int par3)
    {
        this.theWorldServer.getPlayerManager().flagChunkForUpdate(par1, par2, par3);
    }

    public void markBlockNeedsUpdate2(int par1, int par2, int par3) {}

    public void playRecord(String par1Str, int par2, int par3, int par4) {}

    public void playAuxSFX(EntityPlayer par1EntityPlayer, int par2, int par3, int par4, int par5, int par6)
    {
        this.mcServer.getConfigurationManager().sendToAllNearExcept(par1EntityPlayer, (double)par3, (double)par4, (double)par5, 64.0D, this.theWorldServer.provider.dimensionId, new Packet61DoorChange(par2, par3, par4, par5, par6, false));
    }

    public void func_82746_a(int par1, int par2, int par3, int par4, int par5)
    {
        this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new Packet61DoorChange(par1, par2, par3, par4, par5, true));
    }

    public void destroyBlockPartially(int par1, int par2, int par3, int par4, int par5)
    {
        Iterator var6 = this.mcServer.getConfigurationManager().playerEntityList.iterator();

        while (var6.hasNext())
        {
            EntityPlayerMP var7 = (EntityPlayerMP)var6.next();

            if (var7 != null && var7.worldObj == this.theWorldServer && var7.entityId != par1)
            {
                double var8 = (double)par2 - var7.posX;
                double var10 = (double)par3 - var7.posY;
                double var12 = (double)par4 - var7.posZ;

                if (var8 * var8 + var10 * var10 + var12 * var12 < 1024.0D)
                {
                    var7.playerNetServerHandler.sendPacketToPlayer(new Packet55BlockDestroy(par1, par2, par3, par4, par5));
                }
            }
        }
    }
}
