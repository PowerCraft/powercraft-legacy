package net.minecraft.src;

public class BlockCommandBlock extends BlockContainer
{
    public BlockCommandBlock(int par1)
    {
        super(par1, 184, Material.iron);
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityCommandBlock();
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.isRemote)
        {
            boolean var6 = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
            int var7 = par1World.getBlockMetadata(par2, par3, par4);
            boolean var8 = (var7 & 1) != 0;

            if (var6 && !var8)
            {
                TileEntity var9 = par1World.getBlockTileEntity(par2, par3, par4);

                if (var9 != null && var9 instanceof TileEntityCommandBlock)
                {
                    ((TileEntityCommandBlock)var9).func_82351_a(par1World);
                }

                par1World.setBlockMetadata(par2, par3, par4, var7 | 1);
            }
            else if (!var6 && var8)
            {
                par1World.setBlockMetadata(par2, par3, par4, var7 & -2);
            }
        }
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        TileEntityCommandBlock var10 = (TileEntityCommandBlock)par1World.getBlockTileEntity(par2, par3, par4);

        if (var10 != null)
        {
            par5EntityPlayer.displayGUIEditSign(var10);
        }

        return true;
    }
}
