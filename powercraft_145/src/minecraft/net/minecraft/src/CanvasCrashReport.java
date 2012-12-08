package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.awt.Canvas;
import java.awt.Dimension;

@SideOnly(Side.CLIENT)
class CanvasCrashReport extends Canvas
{
    public CanvasCrashReport(int par1)
    {
        this.setPreferredSize(new Dimension(par1, par1));
        this.setMinimumSize(new Dimension(par1, par1));
    }
}
