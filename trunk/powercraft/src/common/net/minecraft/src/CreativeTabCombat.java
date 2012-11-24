package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

final class CreativeTabCombat extends CreativeTabs
{
    CreativeTabCombat(int par1, String par2Str)
    {
        super(par1, par2Str);
    }

    @SideOnly(Side.CLIENT)

    public int getTabIconItemIndex()
    {
        return Item.swordGold.shiftedIndex;
    }
}
