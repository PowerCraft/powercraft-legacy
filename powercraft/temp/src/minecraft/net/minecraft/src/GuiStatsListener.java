package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.minecraft.src.GuiStatsComponent;

@SideOnly(Side.SERVER)
class GuiStatsListener implements ActionListener {

   // $FF: synthetic field
   final GuiStatsComponent field_79021_a;


   GuiStatsListener(GuiStatsComponent p_i4109_1_) {
      this.field_79021_a = p_i4109_1_;
   }

   public void actionPerformed(ActionEvent p_actionPerformed_1_) {
      GuiStatsComponent.func_79013_a(this.field_79021_a);
   }
}
