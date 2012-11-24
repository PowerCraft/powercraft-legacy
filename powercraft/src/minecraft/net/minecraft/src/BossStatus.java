package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public final class BossStatus
{
    public static float healthScale;
    public static int field_82826_b;
    public static String bossName;
    public static boolean field_82825_d;

    public static void func_82824_a(IBossDisplayData par0IBossDisplayData, boolean par1)
    {
        healthScale = (float)par0IBossDisplayData.getDragonHealth() / (float)par0IBossDisplayData.getMaxHealth();
        field_82826_b = 100;
        bossName = par0IBossDisplayData.getEntityName();
        field_82825_d = par1;
    }
}
