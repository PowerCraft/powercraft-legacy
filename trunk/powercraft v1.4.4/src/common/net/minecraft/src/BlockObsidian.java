package net.minecraft.src;

import java.util.Random;

public class BlockObsidian extends BlockStone
{
    public BlockObsidian(int par1, int par2)
    {
        super(par1, par2);
    }

    public int quantityDropped(Random par1Random)
    {
        return 1;
    }

    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Block.obsidian.blockID;
    }
}
