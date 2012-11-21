package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public interface IStatType
{
    @SideOnly(Side.CLIENT)

    /**
     * Formats a given stat for human consumption.
     */
    String format(int var1);
}
