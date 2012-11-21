package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

final class StatTypeDistance implements IStatType
{
    @SideOnly(Side.CLIENT)

    /**
     * Formats a given stat for human consumption.
     */
    public String format(int par1)
    {
        double var2 = (double)par1 / 100.0D;
        double var4 = var2 / 1000.0D;
        return var4 > 0.5D ? StatBase.getDecimalFormat().format(var4) + " km" : (var2 > 0.5D ? StatBase.getDecimalFormat().format(var2) + " m" : par1 + " cm");
    }
}
