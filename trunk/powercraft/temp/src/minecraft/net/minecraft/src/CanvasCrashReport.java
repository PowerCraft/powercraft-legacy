package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.awt.Canvas;
import java.awt.Dimension;

@SideOnly(Side.CLIENT)
class CanvasCrashReport extends Canvas {

   public CanvasCrashReport(int p_i3008_1_) {
      this.setPreferredSize(new Dimension(p_i3008_1_, p_i3008_1_));
      this.setMinimumSize(new Dimension(p_i3008_1_, p_i3008_1_));
   }
}
