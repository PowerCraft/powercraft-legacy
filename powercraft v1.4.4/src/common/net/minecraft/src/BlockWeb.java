package net.minecraft.src;

import java.util.Random;

public class BlockWeb extends Block
{
    public BlockWeb(int par1, int par2)
    {
        super(par1, par2, Material.web);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        par5Entity.setInWeb();
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    public int getRenderType()
    {
        return 1;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Item.silk.shiftedIndex;
    }

    protected boolean canSilkHarvest()
    {
        return true;
    }
}
