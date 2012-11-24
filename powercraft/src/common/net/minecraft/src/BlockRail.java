package net.minecraft.src;

import java.util.Random;

import net.minecraftforge.common.ForgeDirection;
import static net.minecraftforge.common.ForgeDirection.*;

public class BlockRail extends Block
{
    private final boolean isPowered;

    private int renderType = 9;

    public void setRenderType(int value)
    {
        renderType = value;
    }

    public static final boolean isRailBlockAt(World par0World, int par1, int par2, int par3)
    {
        int var4 = par0World.getBlockId(par1, par2, par3);
        return isRailBlock(var4);
    }

    public static final boolean isRailBlock(int par0)
    {
        return Block.blocksList[par0] instanceof BlockRail;
    }

    protected BlockRail(int par1, int par2, boolean par3)
    {
        super(par1, par2, Material.circuits);
        this.isPowered = par3;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    public boolean isPowered()
    {
        return this.isPowered;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
    {
        this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);

        if (var5 >= 2 && var5 <= 5)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        }
    }

    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if (this.isPowered)
        {
            if (this.blockID == Block.railPowered.blockID && (par2 & 8) == 0)
            {
                return this.blockIndexInTexture - 16;
            }
        }
        else if (par2 >= 6)
        {
            return this.blockIndexInTexture - 16;
        }

        return this.blockIndexInTexture;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return renderType;
    }

    public int quantityDropped(Random par1Random)
    {
        return 1;
    }

    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return par1World.isBlockSolidOnSide(par2, par3 - 1, par4, UP);
    }

    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote)
        {
            this.refreshTrackShape(par1World, par2, par3, par4, true);

            if (this.blockID == Block.railPowered.blockID)
            {
                this.onNeighborBlockChange(par1World, par2, par3, par4, this.blockID);
            }
        }
    }

    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.isRemote)
        {
            int var6 = par1World.getBlockMetadata(par2, par3, par4);
            int var7 = var6;

            if (this.isPowered)
            {
                var7 = var6 & 7;
            }

            boolean var8 = false;

            if (!par1World.isBlockSolidOnSide(par2, par3 - 1, par4, UP))
            {
                var8 = true;
            }

            if (var7 == 2 && !par1World.isBlockSolidOnSide(par2 + 1, par3, par4, UP))
            {
                var8 = true;
            }

            if (var7 == 3 && !par1World.isBlockSolidOnSide(par2 - 1, par3, par4, UP))
            {
                var8 = true;
            }

            if (var7 == 4 && !par1World.isBlockSolidOnSide(par2, par3, par4 - 1, UP))
            {
                var8 = true;
            }

            if (var7 == 5 && !par1World.isBlockSolidOnSide(par2, par3, par4 + 1, UP))
            {
                var8 = true;
            }

            if (var8)
            {
                this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
                par1World.setBlockWithNotify(par2, par3, par4, 0);
            }
            else if (this.blockID == Block.railPowered.blockID)
            {
                boolean var9 = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
                var9 = var9 || this.isNeighborRailPowered(par1World, par2, par3, par4, var6, true, 0) || this.isNeighborRailPowered(par1World, par2, par3, par4, var6, false, 0);
                boolean var10 = false;

                if (var9 && (var6 & 8) == 0)
                {
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, var7 | 8);
                    var10 = true;
                }
                else if (!var9 && (var6 & 8) != 0)
                {
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, var7);
                    var10 = true;
                }

                if (var10)
                {
                    par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this.blockID);

                    if (var7 == 2 || var7 == 3 || var7 == 4 || var7 == 5)
                    {
                        par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, this.blockID);
                    }
                }
            }
            else if (par5 > 0 && Block.blocksList[par5].canProvidePower() && !this.isPowered && RailLogic.getAdjacentTracks(new RailLogic(this, par1World, par2, par3, par4)) == 3)
            {
                this.refreshTrackShape(par1World, par2, par3, par4, false);
            }
        }
    }

    private void refreshTrackShape(World par1World, int par2, int par3, int par4, boolean par5)
    {
        if (!par1World.isRemote)
        {
            (new RailLogic(this, par1World, par2, par3, par4)).refreshTrackShape(par1World.isBlockIndirectlyGettingPowered(par2, par3, par4), par5);
        }
    }

    private boolean isNeighborRailPowered(World par1World, int par2, int par3, int par4, int par5, boolean par6, int par7)
    {
        if (par7 >= 8)
        {
            return false;
        }
        else
        {
            int var8 = par5 & 7;
            boolean var9 = true;

            switch (var8)
            {
                case 0:
                    if (par6)
                    {
                        ++par4;
                    }
                    else
                    {
                        --par4;
                    }

                    break;

                case 1:
                    if (par6)
                    {
                        --par2;
                    }
                    else
                    {
                        ++par2;
                    }

                    break;

                case 2:
                    if (par6)
                    {
                        --par2;
                    }
                    else
                    {
                        ++par2;
                        ++par3;
                        var9 = false;
                    }

                    var8 = 1;
                    break;

                case 3:
                    if (par6)
                    {
                        --par2;
                        ++par3;
                        var9 = false;
                    }
                    else
                    {
                        ++par2;
                    }

                    var8 = 1;
                    break;

                case 4:
                    if (par6)
                    {
                        ++par4;
                    }
                    else
                    {
                        --par4;
                        ++par3;
                        var9 = false;
                    }

                    var8 = 0;
                    break;

                case 5:
                    if (par6)
                    {
                        ++par4;
                        ++par3;
                        var9 = false;
                    }
                    else
                    {
                        --par4;
                    }

                    var8 = 0;
            }

            return this.isRailPassingPower(par1World, par2, par3, par4, par6, par7, var8) ? true : var9 && this.isRailPassingPower(par1World, par2, par3 - 1, par4, par6, par7, var8);
        }
    }

    private boolean isRailPassingPower(World par1World, int par2, int par3, int par4, boolean par5, int par6, int par7)
    {
        int var8 = par1World.getBlockId(par2, par3, par4);

        if (var8 == Block.railPowered.blockID)
        {
            int var9 = par1World.getBlockMetadata(par2, par3, par4);
            int var10 = var9 & 7;

            if (par7 == 1 && (var10 == 0 || var10 == 4 || var10 == 5))
            {
                return false;
            }

            if (par7 == 0 && (var10 == 1 || var10 == 2 || var10 == 3))
            {
                return false;
            }

            if ((var9 & 8) != 0)
            {
                if (par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
                {
                    return true;
                }

                return this.isNeighborRailPowered(par1World, par2, par3, par4, var9, par5, par6 + 1);
            }
        }

        return false;
    }

    public int getMobilityFlag()
    {
        return 0;
    }

    @Deprecated
    static boolean isPoweredBlockRail(BlockRail par0BlockRail)
    {
        return par0BlockRail.isPowered;
    }

    public boolean isFlexibleRail(World world, int y, int x, int z)
    {
        return !isPowered;
    }

    public boolean canMakeSlopes(World world, int x, int y, int z)
    {
        return true;
    }

    public int getBasicRailMetadata(IBlockAccess world, EntityMinecart cart, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);

        if (isPowered)
        {
            meta = meta & 7;
        }

        return meta;
    }

    public float getRailMaxSpeed(World world, EntityMinecart cart, int y, int x, int z)
    {
        return 0.4f;
    }

    public void onMinecartPass(World world, EntityMinecart cart, int y, int x, int z)
    {
    }

    public boolean hasPowerBit(World world, int x, int y, int z)
    {
        return isPowered;
    }
}
