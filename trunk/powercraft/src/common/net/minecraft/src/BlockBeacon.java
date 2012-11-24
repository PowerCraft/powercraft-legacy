package net.minecraft.src;

public class BlockBeacon extends BlockContainer
{
    public BlockBeacon(int par1)
    {
        super(par1, 41, Material.glass);
        this.setHardness(3.0F);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityBeacon();
    }

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityBeacon var10 = (TileEntityBeacon)par1World.getBlockTileEntity(par2, par3, par4);

            if (var10 != null)
            {
                par5EntityPlayer.displayGUIBeacon(var10);
            }

            return true;
        }
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return 34;
    }
}
