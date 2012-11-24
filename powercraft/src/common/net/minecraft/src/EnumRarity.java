package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public enum EnumRarity
{
    common(15, "Common"),
    uncommon(14, "Uncommon"),
    rare(11, "Rare"),
    epic(13, "Epic");

    public final int rarityColor;

    public final String rarityName;

    private EnumRarity(int par3, String par4Str)
    {
        this.rarityColor = par3;
        this.rarityName = par4Str;
    }
}
