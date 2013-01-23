package net.minecraft.src;

import java.util.List;

public class ItemCoal extends Item
{
    public ItemCoal(int par1)
    {
        super(par1);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    public String getItemNameIS(ItemStack par1ItemStack)
    {
        return par1ItemStack.getItemDamage() == 1 ? "item.charcoal" : "item.coal";
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }
}
