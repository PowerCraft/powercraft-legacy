package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

final class CreativeTabTransport extends CreativeTabs
{
    CreativeTabTransport(int par1, String par2Str)
    {
        super(par1, par2Str);
    }

    @SideOnly(Side.CLIENT)

    public int getTabIconItemIndex()
    {
        return Block.railPowered.blockID;
    }
}
