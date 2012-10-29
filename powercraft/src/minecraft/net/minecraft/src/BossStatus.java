package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public final class BossStatus
{
    public static float field_82828_a;
    public static int field_82826_b;
    public static String field_82827_c;
    public static boolean field_82825_d;

    public static void func_82824_a(IBossDisplayData par0IBossDisplayData, boolean par1)
    {
        field_82828_a = (float)par0IBossDisplayData.getDragonHealth() / (float)par0IBossDisplayData.getMaxHealth();
        field_82826_b = 100;
        field_82827_c = par0IBossDisplayData.getEntityName();
        field_82825_d = par1;
    }
}
