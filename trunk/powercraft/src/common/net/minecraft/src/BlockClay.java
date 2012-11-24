package net.minecraft.src;

import java.util.Random;

public class BlockClay extends Block
{
    public BlockClay(int par1, int par2)
    {
        super(par1, par2, Material.clay);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Item.clay.shiftedIndex;
    }

    public int quantityDropped(Random par1Random)
    {
        return 4;
    }
}
