package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public interface IStatStringFormat
{
    /**
     * Formats the strings based on 'IStatStringFormat' interface.
     */
    String formatString(String var1);
}
