package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Random;

public class BlockPistonMoving extends BlockContainer
{
    public BlockPistonMoving(int par1)
    {
        super(par1, Material.piston);
        this.setHardness(-1.0F);
    }

    public TileEntity createNewTileEntity(World par1World)
    {
        return null;
    }

    public void onBlockAdded(World par1World, int par2, int par3, int par4) {}

    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntity var7 = par1World.getBlockTileEntity(par2, par3, par4);

        if (var7 instanceof TileEntityPiston)
        {
            ((TileEntityPiston)var7).clearPistonTileEntity();
        }
        else
        {
            super.breakBlock(par1World, par2, par3, par4, par5, par6);
        }
    }

    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return false;
    }

    public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5)
    {
        return false;
    }

    public int getRenderType()
    {
        return -1;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (!par1World.isRemote && par1World.getBlockTileEntity(par2, par3, par4) == null)
        {
            par1World.setBlockWithNotify(par2, par3, par4, 0);
            return true;
        }
        else
        {
            return false;
        }
    }

    public int idDropped(int par1, Random par2Random, int par3)
    {
        return 0;
    }

    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
        if (!par1World.isRemote)
        {
            TileEntityPiston var8 = this.getTileEntityAtLocation(par1World, par2, par3, par4);

            if (var8 != null)
            {
                Block.blocksList[var8.getStoredBlockID()].dropBlockAsItem(par1World, par2, par3, par4, var8.getBlockMetadata(), 0);
            }
        }
    }

    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.isRemote && par1World.getBlockTileEntity(par2, par3, par4) == null)
        {
            ;
        }
    }

    public static TileEntity getTileEntity(int par0, int par1, int par2, boolean par3, boolean par4)
    {
        return new TileEntityPiston(par0, par1, par2, par3, par4);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        TileEntityPiston var5 = this.getTileEntityAtLocation(par1World, par2, par3, par4);

        if (var5 == null)
        {
            return null;
        }
        else
        {
            float var6 = var5.getProgress(0.0F);

            if (var5.isExtending())
            {
                var6 = 1.0F - var6;
            }

            return this.getAxisAlignedBB(par1World, par2, par3, par4, var5.getStoredBlockID(), var6, var5.getPistonOrientation());
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        TileEntityPiston var5 = this.getTileEntityAtLocation(par1IBlockAccess, par2, par3, par4);

        if (var5 != null)
        {
            Block var6 = Block.blocksList[var5.getStoredBlockID()];

            if (var6 == null || var6 == this)
            {
                return;
            }

            var6.setBlockBoundsBasedOnState(par1IBlockAccess, par2, par3, par4);
            float var7 = var5.getProgress(0.0F);

            if (var5.isExtending())
            {
                var7 = 1.0F - var7;
            }

            int var8 = var5.getPistonOrientation();
            this.minX = var6.getBlockBoundsMinX() - (double)((float)Facing.offsetsXForSide[var8] * var7);
            this.minY = var6.getBlockBoundsMinY() - (double)((float)Facing.offsetsYForSide[var8] * var7);
            this.minZ = var6.getBlockBoundsMinZ() - (double)((float)Facing.offsetsZForSide[var8] * var7);
            this.maxX = var6.getBlockBoundsMaxX() - (double)((float)Facing.offsetsXForSide[var8] * var7);
            this.maxY = var6.getBlockBoundsMaxY() - (double)((float)Facing.offsetsYForSide[var8] * var7);
            this.maxZ = var6.getBlockBoundsMaxZ() - (double)((float)Facing.offsetsZForSide[var8] * var7);
        }
    }

    public AxisAlignedBB getAxisAlignedBB(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
        if (par5 != 0 && par5 != this.blockID)
        {
            AxisAlignedBB var8 = Block.blocksList[par5].getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);

            if (var8 == null)
            {
                return null;
            }
            else
            {
                if (Facing.offsetsXForSide[par7] < 0)
                {
                    var8.minX -= (double)((float)Facing.offsetsXForSide[par7] * par6);
                }
                else
                {
                    var8.maxX -= (double)((float)Facing.offsetsXForSide[par7] * par6);
                }

                if (Facing.offsetsYForSide[par7] < 0)
                {
                    var8.minY -= (double)((float)Facing.offsetsYForSide[par7] * par6);
                }
                else
                {
                    var8.maxY -= (double)((float)Facing.offsetsYForSide[par7] * par6);
                }

                if (Facing.offsetsZForSide[par7] < 0)
                {
                    var8.minZ -= (double)((float)Facing.offsetsZForSide[par7] * par6);
                }
                else
                {
                    var8.maxZ -= (double)((float)Facing.offsetsZForSide[par7] * par6);
                }

                return var8;
            }
        }
        else
        {
            return null;
        }
    }

    private TileEntityPiston getTileEntityAtLocation(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        TileEntity var5 = par1IBlockAccess.getBlockTileEntity(par2, par3, par4);
        return var5 instanceof TileEntityPiston ? (TileEntityPiston)var5 : null;
    }

    @SideOnly(Side.CLIENT)

    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        return 0;
    }
}
