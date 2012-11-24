package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.awt.Canvas;
import net.minecraft.client.MinecraftApplet;

@SideOnly(Side.CLIENT)
public class CanvasMinecraftApplet extends Canvas {

   // $FF: synthetic field
   final MinecraftApplet field_74422_a;


   public CanvasMinecraftApplet(MinecraftApplet p_i3020_1_) {
      this.field_74422_a = p_i3020_1_;
   }

   public synchronized void addNotify() {
      super.addNotify();
      this.field_74422_a.func_71479_a();
   }

   public synchronized void removeNotify() {
      this.field_74422_a.func_71480_b();
      super.removeNotify();
   }
}
