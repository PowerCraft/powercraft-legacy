package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.Item;
import net.minecraft.src.TextureFX;

@SideOnly(Side.CLIENT)
public class TextureCompassFX extends TextureFX {

   private Minecraft field_76865_g;
   private int[] field_76867_h = new int[256];
   public double field_76868_i;
   public double field_76866_j;
   public static TextureCompassFX field_82391_c;


   public TextureCompassFX(Minecraft p_i3212_1_) {
      super(Item.field_77750_aQ.func_77617_a(0));
      this.field_76865_g = p_i3212_1_;
      this.field_76847_f = 1;

      try {
         BufferedImage var2 = ImageIO.read(Minecraft.class.getResource("/gui/items.png"));
         int var3 = this.field_76850_b % 16 * 16;
         int var4 = this.field_76850_b / 16 * 16;
         var2.getRGB(var3, var4, 16, 16, this.field_76867_h, 0, 16);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      field_82391_c = this;
   }

   public void func_76846_a() {
      if(this.field_76865_g.field_71441_e != null && this.field_76865_g.field_71439_g != null) {
         func_82390_a(this.field_76865_g.field_71439_g.field_70165_t, this.field_76865_g.field_71439_g.field_70161_v, (double)this.field_76865_g.field_71439_g.field_70177_z, false, false);
      } else {
         func_82390_a(0.0D, 0.0D, 0.0D, true, false);
      }

   }

   public static void func_82390_a(double p_82390_0_, double p_82390_2_, double p_82390_4_, boolean p_82390_6_, boolean p_82390_7_) {
      int[] var8 = field_82391_c.field_76867_h;
      byte[] var9 = field_82391_c.field_76852_a;

      int var17;
      int var16;
      for(int var10 = 0; var10 < 256; ++var10) {
         int var11 = var8[var10] >> 24 & 255;
         int var12 = var8[var10] >> 16 & 255;
         int var13 = var8[var10] >> 8 & 255;
         int var14 = var8[var10] >> 0 & 255;
         if(field_82391_c.field_76851_c) {
            int var15 = (var12 * 30 + var13 * 59 + var14 * 11) / 100;
            var16 = (var12 * 30 + var13 * 70) / 100;
            var17 = (var12 * 30 + var14 * 70) / 100;
            var12 = var15;
            var13 = var16;
            var14 = var17;
         }

         var9[var10 * 4 + 0] = (byte)var12;
         var9[var10 * 4 + 1] = (byte)var13;
         var9[var10 * 4 + 2] = (byte)var14;
         var9[var10 * 4 + 3] = (byte)var11;
      }

      double var27 = 0.0D;
      if(field_82391_c.field_76865_g.field_71441_e != null && !p_82390_6_) {
         ChunkCoordinates var29 = field_82391_c.field_76865_g.field_71441_e.func_72861_E();
         double var28 = (double)var29.field_71574_a - p_82390_0_;
         double var32 = (double)var29.field_71573_c - p_82390_2_;
         var27 = (p_82390_4_ - 90.0D) * 3.141592653589793D / 180.0D - Math.atan2(var32, var28);
         if(!field_82391_c.field_76865_g.field_71441_e.field_73011_w.func_76569_d()) {
            var27 = Math.random() * 3.1415927410125732D * 2.0D;
         }
      }

      double var30;
      if(p_82390_7_) {
         field_82391_c.field_76868_i = var27;
      } else {
         for(var30 = var27 - field_82391_c.field_76868_i; var30 < -3.141592653589793D; var30 += 6.283185307179586D) {
            ;
         }

         while(var30 >= 3.141592653589793D) {
            var30 -= 6.283185307179586D;
         }

         if(var30 < -1.0D) {
            var30 = -1.0D;
         }

         if(var30 > 1.0D) {
            var30 = 1.0D;
         }

         field_82391_c.field_76866_j += var30 * 0.1D;
         field_82391_c.field_76866_j *= 0.8D;
         field_82391_c.field_76868_i += field_82391_c.field_76866_j;
      }

      var30 = Math.sin(field_82391_c.field_76868_i);
      double var31 = Math.cos(field_82391_c.field_76868_i);

      int var19;
      int var18;
      int var21;
      int var20;
      short var23;
      int var22;
      int var25;
      int var24;
      int var26;
      for(var16 = -4; var16 <= 4; ++var16) {
         var17 = (int)(8.5D + var31 * (double)var16 * 0.3D);
         var18 = (int)(7.5D - var30 * (double)var16 * 0.3D * 0.5D);
         var19 = var18 * 16 + var17;
         var20 = 100;
         var21 = 100;
         var22 = 100;
         var23 = 255;
         if(field_82391_c.field_76851_c) {
            var24 = (var20 * 30 + var21 * 59 + var22 * 11) / 100;
            var25 = (var20 * 30 + var21 * 70) / 100;
            var26 = (var20 * 30 + var22 * 70) / 100;
            var20 = var24;
            var21 = var25;
            var22 = var26;
         }

         var9[var19 * 4 + 0] = (byte)var20;
         var9[var19 * 4 + 1] = (byte)var21;
         var9[var19 * 4 + 2] = (byte)var22;
         var9[var19 * 4 + 3] = (byte)var23;
      }

      for(var16 = -8; var16 <= 16; ++var16) {
         var17 = (int)(8.5D + var30 * (double)var16 * 0.3D);
         var18 = (int)(7.5D + var31 * (double)var16 * 0.3D * 0.5D);
         var19 = var18 * 16 + var17;
         var20 = var16 >= 0?255:100;
         var21 = var16 >= 0?20:100;
         var22 = var16 >= 0?20:100;
         var23 = 255;
         if(field_82391_c.field_76851_c) {
            var24 = (var20 * 30 + var21 * 59 + var22 * 11) / 100;
            var25 = (var20 * 30 + var21 * 70) / 100;
            var26 = (var20 * 30 + var22 * 70) / 100;
            var20 = var24;
            var21 = var25;
            var22 = var26;
         }

         var9[var19 * 4 + 0] = (byte)var20;
         var9[var19 * 4 + 1] = (byte)var21;
         var9[var19 * 4 + 2] = (byte)var22;
         var9[var19 * 4 + 3] = (byte)var23;
      }

   }
}
