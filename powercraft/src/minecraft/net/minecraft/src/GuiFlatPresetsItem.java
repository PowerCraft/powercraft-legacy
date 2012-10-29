package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
class GuiFlatPresetsItem
{
    public int field_82911_a;
    public String field_82909_b;
    public String field_82910_c;

    public GuiFlatPresetsItem(int par1, String par2Str, String par3Str)
    {
        this.field_82911_a = par1;
        this.field_82909_b = par2Str;
        this.field_82910_c = par3Str;
    }
}
