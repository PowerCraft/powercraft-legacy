package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.RenderEngine;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TextureFX {

   public byte[] field_76852_a = new byte[1024];
   public int field_76850_b;
   public boolean field_76851_c = false;
   public int field_76848_d = 0;
   public int field_76849_e = 1;
   public int field_76847_f = 0;


   public TextureFX(int p_i3213_1_) {
      this.field_76850_b = p_i3213_1_;
   }

   public void func_76846_a() {}

   public void func_76845_a(RenderEngine p_76845_1_) {
      if(this.field_76847_f == 0) {
         GL11.glBindTexture(3553, p_76845_1_.func_78341_b("/terrain.png"));
      } else if(this.field_76847_f == 1) {
         GL11.glBindTexture(3553, p_76845_1_.func_78341_b("/gui/items.png"));
      }

   }
}
