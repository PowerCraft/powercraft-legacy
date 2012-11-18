package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockRedstoneTorch extends BlockTorch
{
    private boolean torchActive = false;

    private static Map redstoneUpdateInfoCache = new HashMap();

    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        return par1 == 1 ? Block.redstoneWire.getBlockTextureFromSideAndMetadata(par1, par2) : super.getBlockTextureFromSideAndMetadata(par1, par2);
    }

    private boolean checkForBurnout(World par1World, int par2, int par3, int par4, boolean par5)
    {
        if (!redstoneUpdateInfoCache.containsKey(par1World))
        {
            redstoneUpdateInfoCache.put(par1World, new ArrayList());
        }

        List var6 = (List)redstoneUpdateInfoCache.get(par1World);

        if (par5)
        {
            var6.add(new RedstoneUpdateInfo(par2, par3, par4, par1World.getTotalWorldTime()));
        }

        int var7 = 0;

        for (int var8 = 0; var8 < var6.size(); ++var8)
        {
            RedstoneUpdateInfo var9 = (RedstoneUpdateInfo)var6.get(var8);

            if (var9.x == par2 && var9.y == par3 && var9.z == par4)
            {
                ++var7;

                if (var7 >= 8)
                {
                    return true;
                }
            }
        }

        return false;
    }

    protected BlockRedstoneTorch(int par1, int par2, boolean par3)
    {
        super(par1, par2);
        this.torchActive = par3;
        this.setTickRandomly(true);
        this.setCreativeTab((CreativeTabs)null);
    }

    public int tickRate()
    {
        return 2;
    }

    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        if (par1World.getBlockMetadata(par2, par3, par4) == 0)
        {
            super.onBlockAdded(par1World, par2, par3, par4);
        }

        if (this.torchActive)
        {
            par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this.blockID);
            par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, this.blockID);
            par1World.notifyBlocksOfNeighborChange(par2 - 1, par3, par4, this.blockID);
            par1World.notifyBlocksOfNeighborChange(par2 + 1, par3, par4, this.blockID);
            par1World.notifyBlocksOfNeighborChange(par2, par3, par4 - 1, this.blockID);
            par1World.notifyBlocksOfNeighborChange(par2, par3, par4 + 1, this.blockID);
        }
    }

    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        if (this.torchActive)
        {
            par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this.blockID);
            par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, this.blockID);
            par1World.notifyBlocksOfNeighborChange(par2 - 1, par3, par4, this.blockID);
            par1World.notifyBlocksOfNeighborChange(par2 + 1, par3, par4, this.blockID);
            par1World.notifyBlocksOfNeighborChange(par2, par3, par4 - 1, this.blockID);
            par1World.notifyBlocksOfNeighborChange(par2, par3, par4 + 1, this.blockID);
        }
    }

    public boolean isPoweringTo(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (!this.torchActive)
        {
            return false;
        }
        else
        {
            int var6 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
            return var6 == 5 && par5 == 1 ? false : (var6 == 3 && par5 == 3 ? false : (var6 == 4 && par5 == 2 ? false : (var6 == 1 && par5 == 5 ? false : var6 != 2 || par5 != 4)));
        }
    }

    private boolean isIndirectlyPowered(World par1World, int par2, int par3, int par4)
    {
        int var5 = par1World.getBlockMetadata(par2, par3, par4);
        return var5 == 5 && par1World.isBlockIndirectlyProvidingPowerTo(par2, par3 - 1, par4, 0) ? true : (var5 == 3 && par1World.isBlockIndirectlyProvidingPowerTo(par2, par3, par4 - 1, 2) ? true : (var5 == 4 && par1World.isBlockIndirectlyProvidingPowerTo(par2, par3, par4 + 1, 3) ? true : (var5 == 1 && par1World.isBlockIndirectlyProvidingPowerTo(par2 - 1, par3, par4, 4) ? true : var5 == 2 && par1World.isBlockIndirectlyProvidingPowerTo(par2 + 1, par3, par4, 5))));
    }

    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        boolean var6 = this.isIndirectlyPowered(par1World, par2, par3, par4);
        List var7 = (List)redstoneUpdateInfoCache.get(par1World);

        while (var7 != null && !var7.isEmpty() && par1World.getTotalWorldTime() - ((RedstoneUpdateInfo)var7.get(0)).updateTime > 60L)
        {
            var7.remove(0);
        }

        if (this.torchActive)
        {
            if (var6)
            {
                par1World.setBlockAndMetadataWithNotify(par2, par3, par4, Block.torchRedstoneIdle.blockID, par1World.getBlockMetadata(par2, par3, par4));

                if (this.checkForBurnout(par1World, par2, par3, par4, true))
                {
                    par1World.playSoundEffect((double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), "random.fizz", 0.5F, 2.6F + (par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.8F);

                    for (int var8 = 0; var8 < 5; ++var8)
                    {
                        double var9 = (double)par2 + par5Random.nextDouble() * 0.6D + 0.2D;
                        double var11 = (double)par3 + par5Random.nextDouble() * 0.6D + 0.2D;
                        double var13 = (double)par4 + par5Random.nextDouble() * 0.6D + 0.2D;
                        par1World.spawnParticle("smoke", var9, var11, var13, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        }
        else if (!var6 && !this.checkForBurnout(par1World, par2, par3, par4, false))
        {
            par1World.setBlockAndMetadataWithNotify(par2, par3, par4, Block.torchRedstoneActive.blockID, par1World.getBlockMetadata(par2, par3, par4));
        }
    }

    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
        par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate());
    }

    public boolean isIndirectlyPoweringTo(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return par5 == 0 ? this.isPoweringTo(par1IBlockAccess, par2, par3, par4, par5) : false;
    }

    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Block.torchRedstoneActive.blockID;
    }

    public boolean canProvidePower()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)

    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (this.torchActive)
        {
            int var6 = par1World.getBlockMetadata(par2, par3, par4);
            double var7 = (double)((float)par2 + 0.5F) + (double)(par5Random.nextFloat() - 0.5F) * 0.2D;
            double var9 = (double)((float)par3 + 0.7F) + (double)(par5Random.nextFloat() - 0.5F) * 0.2D;
            double var11 = (double)((float)par4 + 0.5F) + (double)(par5Random.nextFloat() - 0.5F) * 0.2D;
            double var13 = 0.2199999988079071D;
            double var15 = 0.27000001072883606D;

            if (var6 == 1)
            {
                par1World.spawnParticle("reddust", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 2)
            {
                par1World.spawnParticle("reddust", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 3)
            {
                par1World.spawnParticle("reddust", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 4)
            {
                par1World.spawnParticle("reddust", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
            }
            else
            {
                par1World.spawnParticle("reddust", var7, var9, var11, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @SideOnly(Side.CLIENT)

    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        return Block.torchRedstoneActive.blockID;
    }
}
