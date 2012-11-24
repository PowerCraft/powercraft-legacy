package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Collection;
import java.util.Iterator;
import net.minecraft.src.Container;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.StatCollector;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public abstract class InventoryEffectRenderer extends GuiContainer {

   private boolean field_74222_o;


   public InventoryEffectRenderer(Container p_i3084_1_) {
      super(p_i3084_1_);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      if(!this.field_73882_e.field_71439_g.func_70651_bq().isEmpty()) {
         this.field_74198_m = 160 + (this.field_73880_f - this.field_74194_b - 200) / 2;
         this.field_74222_o = true;
      }

   }

   public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
      super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
      if(this.field_74222_o) {
         this.func_74221_h();
      }

   }

   private void func_74221_h() {
      int var1 = this.field_74198_m - 124;
      int var2 = this.field_74197_n;
      Collection var4 = this.field_73882_e.field_71439_g.func_70651_bq();
      if(!var4.isEmpty()) {
         int var5 = this.field_73882_e.field_71446_o.func_78341_b("/gui/inventory.png");
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDisable(2896);
         int var6 = 33;
         if(var4.size() > 5) {
            var6 = 132 / (var4.size() - 1);
         }

         for(Iterator var7 = this.field_73882_e.field_71439_g.func_70651_bq().iterator(); var7.hasNext(); var2 += var6) {
            PotionEffect var8 = (PotionEffect)var7.next();
            Potion var9 = Potion.field_76425_a[var8.func_76456_a()];
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_73882_e.field_71446_o.func_78342_b(var5);
            this.func_73729_b(var1, var2, 0, 166, 140, 32);
            if(var9.func_76400_d()) {
               int var10 = var9.func_76392_e();
               this.func_73729_b(var1 + 6, var2 + 7, 0 + var10 % 8 * 18, 198 + var10 / 8 * 18, 18, 18);
            }

            String var12 = StatCollector.func_74838_a(var9.func_76393_a());
            if(var8.func_76458_c() == 1) {
               var12 = var12 + " II";
            } else if(var8.func_76458_c() == 2) {
               var12 = var12 + " III";
            } else if(var8.func_76458_c() == 3) {
               var12 = var12 + " IV";
            }

            this.field_73886_k.func_78261_a(var12, var1 + 10 + 18, var2 + 6, 16777215);
            String var11 = Potion.func_76389_a(var8);
            this.field_73886_k.func_78261_a(var11, var1 + 10 + 18, var2 + 6 + 10, 8355711);
         }

      }
   }
}
