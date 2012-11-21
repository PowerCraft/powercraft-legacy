package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class StatCrafting extends StatBase
{
    private final int itemID;

    public StatCrafting(int par1, String par2Str, int par3)
    {
        super(par1, par2Str);
        this.itemID = par3;
    }

    @SideOnly(Side.CLIENT)
    public int getItemID()
    {
        return this.itemID;
    }
}
