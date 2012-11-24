package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.awt.BorderLayout;
import java.awt.Canvas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
import net.minecraft.src.CrashReport;
import net.minecraft.src.PanelCrashReport;

@SideOnly(Side.CLIENT)
public class MinecraftAppletImpl extends Minecraft {

   // $FF: synthetic field
   final MinecraftApplet field_71478_O;


   public MinecraftAppletImpl(MinecraftApplet p_i3013_1_, Canvas p_i3013_2_, MinecraftApplet p_i3013_3_, int p_i3013_4_, int p_i3013_5_, boolean p_i3013_6_) {
      super(p_i3013_2_, p_i3013_3_, p_i3013_4_, p_i3013_5_, p_i3013_6_);
      this.field_71478_O = p_i3013_1_;
   }

   public void func_71406_c(CrashReport p_71406_1_) {
      this.field_71478_O.removeAll();
      this.field_71478_O.setLayout(new BorderLayout());
      this.field_71478_O.add(new PanelCrashReport(p_71406_1_), "Center");
      this.field_71478_O.validate();
   }
}
