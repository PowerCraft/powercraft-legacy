package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

final class CreativeTabSearch extends CreativeTabs
{
    CreativeTabSearch(int par1, String par2Str)
    {
        super(par1, par2Str);
    }

    @SideOnly(Side.CLIENT)

    public int getTabIconItemIndex()
    {
        return Item.compass.shiftedIndex;
    }
}