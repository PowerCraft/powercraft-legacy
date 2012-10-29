package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class ColorizerWater
{
    private static int[] waterBuffer = new int[65536];

    public static void setWaterBiomeColorizer(int[] par0ArrayOfInteger)
    {
        waterBuffer = par0ArrayOfInteger;
    }
}
