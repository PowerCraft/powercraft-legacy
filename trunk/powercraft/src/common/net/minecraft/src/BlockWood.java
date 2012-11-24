package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;

public class BlockWood extends Block
{
    public static final String[] woodType = new String[] {"oak", "spruce", "birch", "jungle"};

    public BlockWood(int par1)
    {
        super(par1, 4, Material.wood);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        switch (par2)
        {
            case 1:
                return 198;

            case 2:
                return 214;

            case 3:
                return 199;

            default:
                return 4;
        }
    }

    public int damageDropped(int par1)
    {
        return par1;
    }

    @SideOnly(Side.CLIENT)

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));
    }
}
