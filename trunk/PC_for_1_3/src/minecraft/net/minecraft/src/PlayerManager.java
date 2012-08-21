package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerManager
{
    private final WorldServer theWorldServer;
    private final List field_72699_b = new ArrayList();
    private final LongHashMap allChunkWathers = new LongHashMap();

    /**
     * contains a PlayerInstance for every chunk they can see. the "player instance" cotains a list of all players who
     * can also that chunk
     */
    private final List chunkWathcherWithPlayers = new ArrayList();
    private final int playerViewDistance;
    private final int[][] field_72696_f = new int[][] {{1, 0}, {0, 1}, { -1, 0}, {0, -1}};

    public PlayerManager(WorldServer par1WorldServer, int par2)
    {
        if (par2 > 15)
        {
            throw new IllegalArgumentException("Too big view radius!");
        }
        else if (par2 < 3)
        {
            throw new IllegalArgumentException("Too small view radius!");
        }
        else
        {
            this.playerViewDistance = par2;
            this.theWorldServer = par1WorldServer;
        }
    }

    public WorldServer getWorldServer()
    {
        return this.theWorldServer;
    }

    public void func_72693_b()
    {
        Iterator var1 = this.chunkWathcherWithPlayers.iterator();

        while (var1.hasNext())
        {
            PlayerInstance var2 = (PlayerInstance)var1.next();
            var2.sendChunkUpdate();
        }

        this.chunkWathcherWithPlayers.clear();

        if (this.field_72699_b.isEmpty())
        {
            WorldProvider var3 = this.theWorldServer.provider;

            if (!var3.canRespawnHere())
            {
                this.theWorldServer.theChunkProviderServer.unloadAllChunks();
            }
        }
    }

    private PlayerInstance getOrCreateChunkWatcher(int par1, int par2, boolean par3)
    {
        long var4 = (long)par1 + 2147483647L | (long)par2 + 2147483647L << 32;
        PlayerInstance var6 = (PlayerInstance)this.allChunkWathers.getValueByKey(var4);

        if (var6 == null && par3)
        {
            var6 = new PlayerInstance(this, par1, par2);
            this.allChunkWathers.add(var4, var6);
        }

        return var6;
    }

    /**
     * the "PlayerInstance"/ chunkWatcher will send this chunk to all players who are in line of sight
     */
    public void flagChunkForUpdate(int par1, int par2, int par3)
    {
        int var4 = par1 >> 4;
        int var5 = par3 >> 4;
        PlayerInstance var6 = this.getOrCreateChunkWatcher(var4, var5, false);

        if (var6 != null)
        {
            var6.flagChunkForUpdate(par1 & 15, par2, par3 & 15);
        }
    }

    public void func_72683_a(EntityPlayerMP par1EntityPlayerMP)
    {
        int var2 = (int)par1EntityPlayerMP.posX >> 4;
        int var3 = (int)par1EntityPlayerMP.posZ >> 4;
        par1EntityPlayerMP.field_71131_d = par1EntityPlayerMP.posX;
        par1EntityPlayerMP.field_71132_e = par1EntityPlayerMP.posZ;

        for (int var4 = var2 - this.playerViewDistance; var4 <= var2 + this.playerViewDistance; ++var4)
        {
            for (int var5 = var3 - this.playerViewDistance; var5 <= var3 + this.playerViewDistance; ++var5)
            {
                this.getOrCreateChunkWatcher(var4, var5, true).addPlayerToChunkWatchingList(par1EntityPlayerMP);
            }
        }

        this.field_72699_b.add(par1EntityPlayerMP);
        this.func_72691_b(par1EntityPlayerMP);
    }

    public void func_72691_b(EntityPlayerMP par1EntityPlayerMP)
    {
        ArrayList var2 = new ArrayList(par1EntityPlayerMP.chunksToLoad);
        int var3 = 0;
        int var4 = this.playerViewDistance;
        int var5 = (int)par1EntityPlayerMP.posX >> 4;
        int var6 = (int)par1EntityPlayerMP.posZ >> 4;
        int var7 = 0;
        int var8 = 0;
        ChunkCoordIntPair var9 = PlayerInstance.getChunkLocation(this.getOrCreateChunkWatcher(var5, var6, true));
        par1EntityPlayerMP.chunksToLoad.clear();

        if (var2.contains(var9))
        {
            par1EntityPlayerMP.chunksToLoad.add(var9);
        }

        int var10;

        for (var10 = 1; var10 <= var4 * 2; ++var10)
        {
            for (int var11 = 0; var11 < 2; ++var11)
            {
                int[] var12 = this.field_72696_f[var3++ % 4];

                for (int var13 = 0; var13 < var10; ++var13)
                {
                    var7 += var12[0];
                    var8 += var12[1];
                    var9 = PlayerInstance.getChunkLocation(this.getOrCreateChunkWatcher(var5 + var7, var6 + var8, true));

                    if (var2.contains(var9))
                    {
                        par1EntityPlayerMP.chunksToLoad.add(var9);
                    }
                }
            }
        }

        var3 %= 4;

        for (var10 = 0; var10 < var4 * 2; ++var10)
        {
            var7 += this.field_72696_f[var3][0];
            var8 += this.field_72696_f[var3][1];
            var9 = PlayerInstance.getChunkLocation(this.getOrCreateChunkWatcher(var5 + var7, var6 + var8, true));

            if (var2.contains(var9))
            {
                par1EntityPlayerMP.chunksToLoad.add(var9);
            }
        }
    }

    public void func_72695_c(EntityPlayerMP par1EntityPlayerMP)
    {
        int var2 = (int)par1EntityPlayerMP.field_71131_d >> 4;
        int var3 = (int)par1EntityPlayerMP.field_71132_e >> 4;

        for (int var4 = var2 - this.playerViewDistance; var4 <= var2 + this.playerViewDistance; ++var4)
        {
            for (int var5 = var3 - this.playerViewDistance; var5 <= var3 + this.playerViewDistance; ++var5)
            {
                PlayerInstance var6 = this.getOrCreateChunkWatcher(var4, var5, false);

                if (var6 != null)
                {
                    var6.sendThisChunkToPlayer(par1EntityPlayerMP);
                }
            }
        }

        this.field_72699_b.remove(par1EntityPlayerMP);
    }

    private boolean func_72684_a(int par1, int par2, int par3, int par4, int par5)
    {
        int var6 = par1 - par3;
        int var7 = par2 - par4;
        return var6 >= -par5 && var6 <= par5 ? var7 >= -par5 && var7 <= par5 : false;
    }

    public void func_72685_d(EntityPlayerMP par1EntityPlayerMP)
    {
        int var2 = (int)par1EntityPlayerMP.posX >> 4;
        int var3 = (int)par1EntityPlayerMP.posZ >> 4;
        double var4 = par1EntityPlayerMP.field_71131_d - par1EntityPlayerMP.posX;
        double var6 = par1EntityPlayerMP.field_71132_e - par1EntityPlayerMP.posZ;
        double var8 = var4 * var4 + var6 * var6;

        if (var8 >= 64.0D)
        {
            int var10 = (int)par1EntityPlayerMP.field_71131_d >> 4;
            int var11 = (int)par1EntityPlayerMP.field_71132_e >> 4;
            int var12 = this.playerViewDistance;
            int var13 = var2 - var10;
            int var14 = var3 - var11;

            if (var13 != 0 || var14 != 0)
            {
                for (int var15 = var2 - var12; var15 <= var2 + var12; ++var15)
                {
                    for (int var16 = var3 - var12; var16 <= var3 + var12; ++var16)
                    {
                        if (!this.func_72684_a(var15, var16, var10, var11, var12))
                        {
                            this.getOrCreateChunkWatcher(var15, var16, true).addPlayerToChunkWatchingList(par1EntityPlayerMP);
                        }

                        if (!this.func_72684_a(var15 - var13, var16 - var14, var2, var3, var12))
                        {
                            PlayerInstance var17 = this.getOrCreateChunkWatcher(var15 - var13, var16 - var14, false);

                            if (var17 != null)
                            {
                                var17.sendThisChunkToPlayer(par1EntityPlayerMP);
                            }
                        }
                    }
                }

                this.func_72691_b(par1EntityPlayerMP);
                par1EntityPlayerMP.field_71131_d = par1EntityPlayerMP.posX;
                par1EntityPlayerMP.field_71132_e = par1EntityPlayerMP.posZ;
            }
        }
    }

    public boolean isPlayerWatchingChunk(EntityPlayerMP par1EntityPlayerMP, int par2, int par3)
    {
        PlayerInstance var4 = this.getOrCreateChunkWatcher(par2, par3, false);
        return var4 == null ? false : PlayerInstance.getPlayersInChunk(var4).contains(par1EntityPlayerMP) && !par1EntityPlayerMP.chunksToLoad.contains(PlayerInstance.getChunkLocation(var4));
    }

    public static int func_72686_a(int par0)
    {
        return par0 * 16 - 16;
    }

    static WorldServer getWorldServer(PlayerManager par0PlayerManager)
    {
        return par0PlayerManager.theWorldServer;
    }

    static LongHashMap getChunkWatchers(PlayerManager par0PlayerManager)
    {
        return par0PlayerManager.allChunkWathers;
    }

    static List getChunkWatchersWithPlayers(PlayerManager par0PlayerManager)
    {
        return par0PlayerManager.chunkWathcherWithPlayers;
    }
}
