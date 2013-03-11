package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;

public class BlockCarrot extends BlockCrops
{
    @SideOnly(Side.CLIENT)
    private Icon[] field_94364_a;

    public BlockCarrot(int par1)
    {
        super(par1);
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if (par2 < 7)
        {
            if (par2 == 6)
            {
                par2 = 5;
            }

            return this.field_94364_a[par2 >> 1];
        }
        else
        {
            return this.field_94364_a[3];
        }
    }

    /**
     * Generate a seed ItemStack for this crop.
     */
    protected int getSeedItem()
    {
        return Item.carrot.itemID;
    }

    /**
     * Generate a crop produce ItemStack for this crop.
     */
    protected int getCropItem()
    {
        return Item.carrot.itemID;
    }

    @SideOnly(Side.CLIENT)
    public void func_94332_a(IconRegister par1IconRegister)
    {
        this.field_94364_a = new Icon[4];

        for (int i = 0; i < this.field_94364_a.length; ++i)
        {
            this.field_94364_a[i] = par1IconRegister.func_94245_a("carrots_" + i);
        }
    }
}
