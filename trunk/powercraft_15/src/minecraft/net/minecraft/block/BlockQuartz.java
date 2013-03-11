package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockQuartz extends Block
{
    public static final String[] field_94420_a = new String[] {"default", "chiseled", "lines"};
    private static final String[] field_94418_b = new String[] {"quartzblock_side", "quartzblock_chiseled", "quartzblock_lines", null, null};
    @SideOnly(Side.CLIENT)
    private Icon[] field_94419_c;
    @SideOnly(Side.CLIENT)
    private Icon field_94414_cO;
    @SideOnly(Side.CLIENT)
    private Icon field_94415_cP;
    @SideOnly(Side.CLIENT)
    private Icon field_94416_cQ;
    @SideOnly(Side.CLIENT)
    private Icon field_94417_cR;

    public BlockQuartz(int par1)
    {
        super(par1, Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if (par2 != 2 && par2 != 3 && par2 != 4)
        {
            if (par1 != 1 && (par1 != 0 || par2 != 1))
            {
                if (par1 == 0)
                {
                    return this.field_94417_cR;
                }
                else
                {
                    if (par2 < 0 || par2 >= this.field_94419_c.length)
                    {
                        par2 = 0;
                    }

                    return this.field_94419_c[par2];
                }
            }
            else
            {
                return par2 == 1 ? this.field_94414_cO : this.field_94416_cQ;
            }
        }
        else
        {
            return par2 == 2 && (par1 == 1 || par1 == 0) ? this.field_94415_cP : (par2 == 3 && (par1 == 5 || par1 == 4) ? this.field_94415_cP : (par2 == 4 && (par1 == 2 || par1 == 3) ? this.field_94415_cP : this.field_94419_c[par2]));
        }
    }

    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
        if (par9 == 2)
        {
            switch (par5)
            {
                case 0:
                case 1:
                    par9 = 2;
                    break;
                case 2:
                case 3:
                    par9 = 4;
                    break;
                case 4:
                case 5:
                    par9 = 3;
            }
        }

        return par9;
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int par1)
    {
        return par1 != 3 && par1 != 4 ? par1 : 2;
    }

    /**
     * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage
     * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
     */
    protected ItemStack createStackedBlock(int par1)
    {
        return par1 != 3 && par1 != 4 ? super.createStackedBlock(par1) : new ItemStack(this.blockID, 1, 2);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 39;
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
    }

    @SideOnly(Side.CLIENT)
    public void func_94332_a(IconRegister par1IconRegister)
    {
        this.field_94419_c = new Icon[field_94418_b.length];

        for (int i = 0; i < this.field_94419_c.length; ++i)
        {
            if (field_94418_b[i] == null)
            {
                this.field_94419_c[i] = this.field_94419_c[i - 1];
            }
            else
            {
                this.field_94419_c[i] = par1IconRegister.func_94245_a(field_94418_b[i]);
            }
        }

        this.field_94416_cQ = par1IconRegister.func_94245_a("quartzblock_top");
        this.field_94414_cO = par1IconRegister.func_94245_a("quartzblock_chiseled_top");
        this.field_94415_cP = par1IconRegister.func_94245_a("quartzblock_lines_top");
        this.field_94417_cR = par1IconRegister.func_94245_a("quartzblock_bottom");
    }
}
