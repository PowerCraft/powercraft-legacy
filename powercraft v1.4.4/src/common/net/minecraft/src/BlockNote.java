package net.minecraft.src;

public class BlockNote extends BlockContainer
{
    public BlockNote(int par1)
    {
        super(par1, 74, Material.wood);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public int getBlockTextureFromSide(int par1)
    {
        return this.blockIndexInTexture;
    }

    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (par5 > 0)
        {
            boolean var6 = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
            TileEntityNote var7 = (TileEntityNote)par1World.getBlockTileEntity(par2, par3, par4);

            if (var7 != null && var7.previousRedstoneState != var6)
            {
                if (var6)
                {
                    var7.triggerNote(par1World, par2, par3, par4);
                }

                var7.previousRedstoneState = var6;
            }
        }
    }

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityNote var10 = (TileEntityNote)par1World.getBlockTileEntity(par2, par3, par4);

            if (var10 != null)
            {
                var10.changePitch();
                var10.triggerNote(par1World, par2, par3, par4);
            }

            return true;
        }
    }

    public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        if (!par1World.isRemote)
        {
            TileEntityNote var6 = (TileEntityNote)par1World.getBlockTileEntity(par2, par3, par4);

            if (var6 != null)
            {
                var6.triggerNote(par1World, par2, par3, par4);
            }
        }
    }

    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityNote();
    }

    public void onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        float var7 = (float)Math.pow(2.0D, (double)(par6 - 12) / 12.0D);
        String var8 = "harp";

        if (par5 == 1)
        {
            var8 = "bd";
        }

        if (par5 == 2)
        {
            var8 = "snare";
        }

        if (par5 == 3)
        {
            var8 = "hat";
        }

        if (par5 == 4)
        {
            var8 = "bassattack";
        }

        par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.5D, (double)par4 + 0.5D, "note." + var8, 3.0F, var7);
        par1World.spawnParticle("note", (double)par2 + 0.5D, (double)par3 + 1.2D, (double)par4 + 0.5D, (double)par6 / 24.0D, 0.0D, 0.0D);
    }
}
