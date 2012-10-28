package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
class GuiBeaconButtonConfirm extends GuiBeaconButton
{
    final GuiBeacon field_82264_k;

    public GuiBeaconButtonConfirm(GuiBeacon par1, int par2, int par3, int par4)
    {
        super(par2, par3, par4, "/gui/beacon.png", 90, 220);
        this.field_82264_k = par1;
    }

    public void func_82251_b(int par1, int par2)
    {
        this.field_82264_k.drawCreativeTabHoveringText(StatCollector.translateToLocal("gui.done"), par1, par2);
    }
}
