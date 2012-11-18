package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public interface IStatType
{
    @SideOnly(Side.CLIENT)

    String format(int var1);
}
