package net.minecraft.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ColorizerWater
{
    private static int[] waterBuffer = new int[65536];

    public static void setWaterBiomeColorizer(int[] par0ArrayOfInteger)
    {
        waterBuffer = par0ArrayOfInteger;
    }
}
