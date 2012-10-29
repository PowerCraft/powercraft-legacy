package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
class GuiBeaconButtonCancel extends GuiBeaconButton
{
    final GuiBeacon field_82260_k;

    public GuiBeaconButtonCancel(GuiBeacon par1, int par2, int par3, int par4)
    {
        super(par2, par3, par4, "/gui/beacon.png", 112, 220);
        this.field_82260_k = par1;
    }

    public void func_82251_b(int par1, int par2)
    {
        this.field_82260_k.drawCreativeTabHoveringText(StatCollector.translateToLocal("gui.cancel"), par1, par2);
    }
}
