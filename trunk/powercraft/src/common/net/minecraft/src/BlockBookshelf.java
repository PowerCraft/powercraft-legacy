package net.minecraft.src;

import java.util.Random;

public class BlockBookshelf extends Block
{
    public BlockBookshelf(int par1, int par2)
    {
        super(par1, par2, Material.wood);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public int getBlockTextureFromSide(int par1)
    {
        return par1 <= 1 ? 4 : this.blockIndexInTexture;
    }

    public int quantityDropped(Random par1Random)
    {
        return 3;
    }

    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Item.book.shiftedIndex;
    }
}
