package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

final class CreativeTabFood extends CreativeTabs
{
    CreativeTabFood(int par1, String par2Str)
    {
        super(par1, par2Str);
    }

    @SideOnly(Side.CLIENT)

    public int getTabIconItemIndex()
    {
        return Item.appleRed.shiftedIndex;
    }
}
