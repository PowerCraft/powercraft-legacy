package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;

public class BlockStoneBrick extends Block
{
    public static final String[] STONE_BRICK_TYPES = new String[] {"default", "mossy", "cracked", "chiseled"};
    public BlockStoneBrick(int par1)
    {
        super(par1, 54, Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        switch (par2)
        {
            case 1:
                return 100;

            case 2:
                return 101;

            case 3:
                return 213;

            default:
                return 54;
        }
    }

    public int damageDropped(int par1)
    {
        return par1;
    }

    @SideOnly(Side.CLIENT)

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 4; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
}
