package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ChatClickData;
import net.minecraft.src.ChatLine;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiChat;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.StringUtils;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiNewChat extends Gui {

   private final Minecraft field_73772_a;
   private final List field_73770_b = new ArrayList();
   private final List field_73771_c = new ArrayList();
   private int field_73768_d = 0;
   private boolean field_73769_e = false;


   public GuiNewChat(Minecraft p_i3043_1_) {
      this.field_73772_a = p_i3043_1_;
   }

   public void func_73762_a(int p_73762_1_) {
      if(this.field_73772_a.field_71474_y.field_74343_n != 2) {
         byte var2 = 10;
         boolean var3 = false;
         int var4 = 0;
         int var5 = this.field_73771_c.size();
         float var6 = this.field_73772_a.field_71474_y.field_74357_r * 0.9F + 0.1F;
         if(var5 > 0) {
            if(this.func_73760_d()) {
               var2 = 20;
               var3 = true;
            }

            int var7;
            int var9;
            int var12;
            for(var7 = 0; var7 + this.field_73768_d < this.field_73771_c.size() && var7 < var2; ++var7) {
               ChatLine var8 = (ChatLine)this.field_73771_c.get(var7 + this.field_73768_d);
               if(var8 != null) {
                  var9 = p_73762_1_ - var8.func_74540_b();
                  if(var9 < 200 || var3) {
                     double var10 = (double)var9 / 200.0D;
                     var10 = 1.0D - var10;
                     var10 *= 10.0D;
                     if(var10 < 0.0D) {
                        var10 = 0.0D;
                     }

                     if(var10 > 1.0D) {
                        var10 = 1.0D;
                     }

                     var10 *= var10;
                     var12 = (int)(255.0D * var10);
                     if(var3) {
                        var12 = 255;
                     }

                     var12 = (int)((float)var12 * var6);
                     ++var4;
                     if(var12 > 3) {
                        byte var13 = 3;
                        int var14 = -var7 * 9;
                        func_73734_a(var13, var14 - 1, var13 + 320 + 4, var14 + 8, var12 / 2 << 24);
                        GL11.glEnable(3042);
                        String var15 = var8.func_74538_a();
                        if(!this.field_73772_a.field_71474_y.field_74344_o) {
                           var15 = StringUtils.func_76338_a(var15);
                        }

                        this.field_73772_a.field_71466_p.func_78261_a(var15, var13, var14, 16777215 + (var12 << 24));
                     }
                  }
               }
            }

            if(var3) {
               var7 = this.field_73772_a.field_71466_p.field_78288_b;
               GL11.glTranslatef(0.0F, (float)var7, 0.0F);
               int var16 = var5 * var7 + var5;
               var9 = var4 * var7 + var4;
               int var17 = this.field_73768_d * var9 / var5;
               int var11 = var9 * var9 / var16;
               if(var16 != var9) {
                  var12 = var17 > 0?170:96;
                  int var18 = this.field_73769_e?13382451:3355562;
                  func_73734_a(0, -var17, 2, -var17 - var11, var18 + (var12 << 24));
                  func_73734_a(2, -var17, 1, -var17 - var11, 13421772 + (var12 << 24));
               }
            }

         }
      }
   }

   public void func_73761_a() {
      this.field_73771_c.clear();
      this.field_73770_b.clear();
   }

   public void func_73765_a(String p_73765_1_) {
      this.func_73763_a(p_73765_1_, 0);
   }

   public void func_73763_a(String p_73763_1_, int p_73763_2_) {
      boolean var3 = this.func_73760_d();
      boolean var4 = true;
      if(p_73763_2_ != 0) {
         this.func_73759_c(p_73763_2_);
      }

      Iterator var5 = this.field_73772_a.field_71466_p.func_78271_c(p_73763_1_, 320).iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         if(var3 && this.field_73768_d > 0) {
            this.field_73769_e = true;
            this.func_73758_b(1);
         }

         if(!var4) {
            var6 = " " + var6;
         }

         var4 = false;
         this.field_73771_c.add(0, new ChatLine(this.field_73772_a.field_71456_v.func_73834_c(), var6, p_73763_2_));
      }

      while(this.field_73771_c.size() > 100) {
         this.field_73771_c.remove(this.field_73771_c.size() - 1);
      }

   }

   public List func_73756_b() {
      return this.field_73770_b;
   }

   public void func_73767_b(String p_73767_1_) {
      if(this.field_73770_b.isEmpty() || !((String)this.field_73770_b.get(this.field_73770_b.size() - 1)).equals(p_73767_1_)) {
         this.field_73770_b.add(p_73767_1_);
      }

   }

   public void func_73764_c() {
      this.field_73768_d = 0;
      this.field_73769_e = false;
   }

   public void func_73758_b(int p_73758_1_) {
      this.field_73768_d += p_73758_1_;
      int var2 = this.field_73771_c.size();
      if(this.field_73768_d > var2 - 20) {
         this.field_73768_d = var2 - 20;
      }

      if(this.field_73768_d <= 0) {
         this.field_73768_d = 0;
         this.field_73769_e = false;
      }

   }

   public ChatClickData func_73766_a(int p_73766_1_, int p_73766_2_) {
      if(!this.func_73760_d()) {
         return null;
      } else {
         ScaledResolution var3 = new ScaledResolution(this.field_73772_a.field_71474_y, this.field_73772_a.field_71443_c, this.field_73772_a.field_71440_d);
         int var4 = var3.func_78325_e();
         int var5 = p_73766_1_ / var4 - 3;
         int var6 = p_73766_2_ / var4 - 40;
         if(var5 >= 0 && var6 >= 0) {
            int var7 = Math.min(20, this.field_73771_c.size());
            if(var5 <= 320 && var6 < this.field_73772_a.field_71466_p.field_78288_b * var7 + var7) {
               int var8 = var6 / (this.field_73772_a.field_71466_p.field_78288_b + 1) + this.field_73768_d;
               return new ChatClickData(this.field_73772_a.field_71466_p, (ChatLine)this.field_73771_c.get(var8), var5, var6 - (var8 - this.field_73768_d) * this.field_73772_a.field_71466_p.field_78288_b + var8);
            } else {
               return null;
            }
         } else {
            return null;
         }
      }
   }

   public void func_73757_a(String p_73757_1_, Object ... p_73757_2_) {
      this.func_73765_a(StringTranslate.func_74808_a().func_74803_a(p_73757_1_, p_73757_2_));
   }

   public boolean func_73760_d() {
      return this.field_73772_a.field_71462_r instanceof GuiChat;
   }

   public void func_73759_c(int p_73759_1_) {
      Iterator var2 = this.field_73771_c.iterator();

      ChatLine var3;
      do {
         if(!var2.hasNext()) {
            return;
         }

         var3 = (ChatLine)var2.next();
      } while(var3.func_74539_c() != p_73759_1_);

      var2.remove();
   }
}
