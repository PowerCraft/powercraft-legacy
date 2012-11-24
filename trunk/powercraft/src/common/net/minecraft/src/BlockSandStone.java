package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;

public class BlockSandStone extends Block
{
    public static final String[] SAND_STONE_TYPES = new String[] {"default", "chiseled", "smooth"};
    public BlockSandStone(int par1)
    {
        super(par1, 192, Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        return par1 != 1 && (par1 != 0 || par2 != 1 && par2 != 2) ? (par1 == 0 ? 208 : (par2 == 1 ? 229 : (par2 == 2 ? 230 : 192))) : 176;
    }

    public int getBlockTextureFromSide(int par1)
    {
        return par1 == 1 ? this.blockIndexInTexture - 16 : (par1 == 0 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture);
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
    }
}
