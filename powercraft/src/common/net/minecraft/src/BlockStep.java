package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;
import java.util.Random;

public class BlockStep extends BlockHalfSlab
{
    public static final String[] blockStepTypes = new String[] {"stone", "sand", "wood", "cobble", "brick", "smoothStoneBrick"};

    public BlockStep(int par1, boolean par2)
    {
        super(par1, par2, Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        int var3 = par2 & 7;
        return var3 == 0 ? (par1 <= 1 ? 6 : 5) : (var3 == 1 ? (par1 == 0 ? 208 : (par1 == 1 ? 176 : 192)) : (var3 == 2 ? 4 : (var3 == 3 ? 16 : (var3 == 4 ? Block.brick.blockIndexInTexture : (var3 == 5 ? Block.stoneBrick.blockIndexInTexture : 6)))));
    }

    public int getBlockTextureFromSide(int par1)
    {
        return this.getBlockTextureFromSideAndMetadata(par1, 0);
    }

    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Block.stoneSingleSlab.blockID;
    }

    protected ItemStack createStackedBlock(int par1)
    {
        return new ItemStack(Block.stoneSingleSlab.blockID, 2, par1 & 7);
    }

    public String getFullSlabName(int par1)
    {
        if (par1 < 0 || par1 >= blockStepTypes.length)
        {
            par1 = 0;
        }

        return super.getBlockName() + "." + blockStepTypes[par1];
    }

    @SideOnly(Side.CLIENT)

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        if (par1 != Block.stoneDoubleSlab.blockID)
        {
            for (int var4 = 0; var4 < 6; ++var4)
            {
                if (var4 != 2)
                {
                    par3List.add(new ItemStack(par1, 1, var4));
                }
            }
        }
    }
}
