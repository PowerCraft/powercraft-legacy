package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;

public class BlockCloth extends Block
{
    public BlockCloth()
    {
        super(35, 64, Material.cloth);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if (par2 == 0)
        {
            return this.blockIndexInTexture;
        }
        else
        {
            par2 = ~(par2 & 15);
            return 113 + ((par2 & 8) >> 3) + (par2 & 7) * 16;
        }
    }

    public int damageDropped(int par1)
    {
        return par1;
    }

    public static int getBlockFromDye(int par0)
    {
        return ~par0 & 15;
    }

    public static int getDyeFromBlock(int par0)
    {
        return ~par0 & 15;
    }

    @SideOnly(Side.CLIENT)

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 16; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
}
