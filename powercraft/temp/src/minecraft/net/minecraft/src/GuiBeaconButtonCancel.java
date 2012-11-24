package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.GuiBeacon;
import net.minecraft.src.GuiBeaconButton;
import net.minecraft.src.StatCollector;

@SideOnly(Side.CLIENT)
class GuiBeaconButtonCancel extends GuiBeaconButton {

   // $FF: synthetic field
   final GuiBeacon field_82260_k;


   public GuiBeaconButtonCancel(GuiBeacon p_i5010_1_, int p_i5010_2_, int p_i5010_3_, int p_i5010_4_) {
      super(p_i5010_2_, p_i5010_3_, p_i5010_4_, "/gui/beacon.png", 112, 220);
      this.field_82260_k = p_i5010_1_;
   }

   public void func_82251_b(int p_82251_1_, int p_82251_2_) {
      this.field_82260_k.func_74190_a(StatCollector.func_74838_a("gui.cancel"), p_82251_1_, p_82251_2_);
   }
}
