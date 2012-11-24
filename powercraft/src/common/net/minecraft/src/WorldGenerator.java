package net.minecraft.src;

import java.util.Random;

public abstract class WorldGenerator
{
    private final boolean doBlockNotify;
    private int field_82631_b = 1;

    public WorldGenerator()
    {
        this.doBlockNotify = false;
    }

    public WorldGenerator(boolean par1)
    {
        this.doBlockNotify = par1;
    }

    public abstract boolean generate(World var1, Random var2, int var3, int var4, int var5);

    public void setScale(double par1, double par3, double par5) {}

    protected void setBlock(World par1World, int par2, int par3, int par4, int par5)
    {
        this.setBlockAndMetadata(par1World, par2, par3, par4, par5, 0);
    }

    protected void setBlockAndMetadata(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        if (this.doBlockNotify)
        {
            par1World.setBlockAndMetadataWithNotify(par2, par3, par4, par5, par6);
        }
        else
        {
            par1World.setBlockAndMetadata(par2, par3, par4, par5, par6);
        }
    }
}
