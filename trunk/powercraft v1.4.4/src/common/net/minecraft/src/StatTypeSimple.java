package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

final class StatTypeSimple implements IStatType
{
    @SideOnly(Side.CLIENT)

    public String format(int par1)
    {
        return StatBase.getNumberFormat().format((long)par1);
    }
}
