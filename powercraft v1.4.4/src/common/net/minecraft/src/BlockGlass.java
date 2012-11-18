package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Random;

public class BlockGlass extends BlockBreakable
{
    public BlockGlass(int par1, int par2, Material par3Material, boolean par4)
    {
        super(par1, par2, par3Material, par4);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @SideOnly(Side.CLIENT)

    public int getRenderBlockPass()
    {
        return 0;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    protected boolean canSilkHarvest()
    {
        return true;
    }
}
