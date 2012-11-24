package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.awt.Component;
import net.minecraft.src.GameSettings;
import org.lwjgl.input.Mouse;

@SideOnly(Side.CLIENT)
public class MouseHelper {

   private final Component field_74376_c;
   private final GameSettings field_85184_d;
   public int field_74377_a;
   public int field_74375_b;


   public MouseHelper(Component p_i6800_1_, GameSettings p_i6800_2_) {
      this.field_74376_c = p_i6800_1_;
      this.field_85184_d = p_i6800_2_;
   }

   public void func_74372_a() {
      Mouse.setGrabbed(true);
      this.field_74377_a = 0;
      this.field_74375_b = 0;
   }

   public void func_74373_b() {
      Mouse.setCursorPosition(this.field_74376_c.getWidth() / 2, this.field_74376_c.getHeight() / 2);
      Mouse.setGrabbed(false);
   }

   public void func_74374_c() {
      this.field_74377_a = Mouse.getDX();
      this.field_74375_b = Mouse.getDY();
   }
}
