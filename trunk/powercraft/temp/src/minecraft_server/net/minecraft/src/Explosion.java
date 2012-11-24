package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class Explosion {

   public boolean field_77286_a = false;
   public boolean field_82755_b = true;
   private int field_77289_h = 16;
   private Random field_77290_i = new Random();
   private World field_77287_j;
   public double field_77284_b;
   public double field_77285_c;
   public double field_77282_d;
   public Entity field_77283_e;
   public float field_77280_f;
   public List field_77281_g = new ArrayList();
   private Map field_77288_k = new HashMap();


   public Explosion(World p_i3727_1_, Entity p_i3727_2_, double p_i3727_3_, double p_i3727_5_, double p_i3727_7_, float p_i3727_9_) {
      this.field_77287_j = p_i3727_1_;
      this.field_77283_e = p_i3727_2_;
      this.field_77280_f = p_i3727_9_;
      this.field_77284_b = p_i3727_3_;
      this.field_77285_c = p_i3727_5_;
      this.field_77282_d = p_i3727_7_;
   }

   public void func_77278_a() {
      float var1 = this.field_77280_f;
      HashSet var2 = new HashSet();

      int var3;
      int var4;
      int var5;
      double var15;
      double var17;
      double var19;
      for(var3 = 0; var3 < this.field_77289_h; ++var3) {
         for(var4 = 0; var4 < this.field_77289_h; ++var4) {
            for(var5 = 0; var5 < this.field_77289_h; ++var5) {
               if(var3 == 0 || var3 == this.field_77289_h - 1 || var4 == 0 || var4 == this.field_77289_h - 1 || var5 == 0 || var5 == this.field_77289_h - 1) {
                  double var6 = (double)((float)var3 / ((float)this.field_77289_h - 1.0F) * 2.0F - 1.0F);
                  double var8 = (double)((float)var4 / ((float)this.field_77289_h - 1.0F) * 2.0F - 1.0F);
                  double var10 = (double)((float)var5 / ((float)this.field_77289_h - 1.0F) * 2.0F - 1.0F);
                  double var12 = Math.sqrt(var6 * var6 + var8 * var8 + var10 * var10);
                  var6 /= var12;
                  var8 /= var12;
                  var10 /= var12;
                  float var14 = this.field_77280_f * (0.7F + this.field_77287_j.field_73012_v.nextFloat() * 0.6F);
                  var15 = this.field_77284_b;
                  var17 = this.field_77285_c;
                  var19 = this.field_77282_d;

                  for(float var21 = 0.3F; var14 > 0.0F; var14 -= var21 * 0.75F) {
                     int var22 = MathHelper.func_76128_c(var15);
                     int var23 = MathHelper.func_76128_c(var17);
                     int var24 = MathHelper.func_76128_c(var19);
                     int var25 = this.field_77287_j.func_72798_a(var22, var23, var24);
                     if(var25 > 0) {
                        Block var26 = Block.field_71973_m[var25];
                        float var27 = this.field_77283_e != null?this.field_77283_e.func_82146_a(this, var26, var22, var23, var24):var26.func_71904_a(this.field_77283_e);
                        var14 -= (var27 + 0.3F) * var21;
                     }

                     if(var14 > 0.0F) {
                        var2.add(new ChunkPosition(var22, var23, var24));
                     }

                     var15 += var6 * (double)var21;
                     var17 += var8 * (double)var21;
                     var19 += var10 * (double)var21;
                  }
               }
            }
         }
      }

      this.field_77281_g.addAll(var2);
      this.field_77280_f *= 2.0F;
      var3 = MathHelper.func_76128_c(this.field_77284_b - (double)this.field_77280_f - 1.0D);
      var4 = MathHelper.func_76128_c(this.field_77284_b + (double)this.field_77280_f + 1.0D);
      var5 = MathHelper.func_76128_c(this.field_77285_c - (double)this.field_77280_f - 1.0D);
      int var28 = MathHelper.func_76128_c(this.field_77285_c + (double)this.field_77280_f + 1.0D);
      int var7 = MathHelper.func_76128_c(this.field_77282_d - (double)this.field_77280_f - 1.0D);
      int var29 = MathHelper.func_76128_c(this.field_77282_d + (double)this.field_77280_f + 1.0D);
      List var9 = this.field_77287_j.func_72839_b(this.field_77283_e, AxisAlignedBB.func_72332_a().func_72299_a((double)var3, (double)var5, (double)var7, (double)var4, (double)var28, (double)var29));
      Vec3 var30 = this.field_77287_j.func_82732_R().func_72345_a(this.field_77284_b, this.field_77285_c, this.field_77282_d);

      for(int var11 = 0; var11 < var9.size(); ++var11) {
         Entity var31 = (Entity)var9.get(var11);
         double var13 = var31.func_70011_f(this.field_77284_b, this.field_77285_c, this.field_77282_d) / (double)this.field_77280_f;
         if(var13 <= 1.0D) {
            var15 = var31.field_70165_t - this.field_77284_b;
            var17 = var31.field_70163_u + (double)var31.func_70047_e() - this.field_77285_c;
            var19 = var31.field_70161_v - this.field_77282_d;
            double var33 = (double)MathHelper.func_76133_a(var15 * var15 + var17 * var17 + var19 * var19);
            if(var33 != 0.0D) {
               var15 /= var33;
               var17 /= var33;
               var19 /= var33;
               double var32 = (double)this.field_77287_j.func_72842_a(var30, var31.field_70121_D);
               double var34 = (1.0D - var13) * var32;
               var31.func_70097_a(DamageSource.field_76378_k, (int)((var34 * var34 + var34) / 2.0D * 8.0D * (double)this.field_77280_f + 1.0D));
               var31.field_70159_w += var15 * var34;
               var31.field_70181_x += var17 * var34;
               var31.field_70179_y += var19 * var34;
               if(var31 instanceof EntityPlayer) {
                  this.field_77288_k.put((EntityPlayer)var31, this.field_77287_j.func_82732_R().func_72345_a(var15 * var34, var17 * var34, var19 * var34));
               }
            }
         }
      }

      this.field_77280_f = var1;
   }

   public void func_77279_a(boolean p_77279_1_) {
      this.field_77287_j.func_72908_a(this.field_77284_b, this.field_77285_c, this.field_77282_d, "random.explode", 4.0F, (1.0F + (this.field_77287_j.field_73012_v.nextFloat() - this.field_77287_j.field_73012_v.nextFloat()) * 0.2F) * 0.7F);
      if(this.field_77280_f >= 2.0F && this.field_82755_b) {
         this.field_77287_j.func_72869_a("hugeexplosion", this.field_77284_b, this.field_77285_c, this.field_77282_d, 1.0D, 0.0D, 0.0D);
      } else {
         this.field_77287_j.func_72869_a("largeexplode", this.field_77284_b, this.field_77285_c, this.field_77282_d, 1.0D, 0.0D, 0.0D);
      }

      Iterator var2;
      ChunkPosition var3;
      int var4;
      int var5;
      int var6;
      int var7;
      if(this.field_82755_b) {
         var2 = this.field_77281_g.iterator();

         while(var2.hasNext()) {
            var3 = (ChunkPosition)var2.next();
            var4 = var3.field_76930_a;
            var5 = var3.field_76928_b;
            var6 = var3.field_76929_c;
            var7 = this.field_77287_j.func_72798_a(var4, var5, var6);
            if(p_77279_1_) {
               double var8 = (double)((float)var4 + this.field_77287_j.field_73012_v.nextFloat());
               double var10 = (double)((float)var5 + this.field_77287_j.field_73012_v.nextFloat());
               double var12 = (double)((float)var6 + this.field_77287_j.field_73012_v.nextFloat());
               double var14 = var8 - this.field_77284_b;
               double var16 = var10 - this.field_77285_c;
               double var18 = var12 - this.field_77282_d;
               double var20 = (double)MathHelper.func_76133_a(var14 * var14 + var16 * var16 + var18 * var18);
               var14 /= var20;
               var16 /= var20;
               var18 /= var20;
               double var22 = 0.5D / (var20 / (double)this.field_77280_f + 0.1D);
               var22 *= (double)(this.field_77287_j.field_73012_v.nextFloat() * this.field_77287_j.field_73012_v.nextFloat() + 0.3F);
               var14 *= var22;
               var16 *= var22;
               var18 *= var22;
               this.field_77287_j.func_72869_a("explode", (var8 + this.field_77284_b * 1.0D) / 2.0D, (var10 + this.field_77285_c * 1.0D) / 2.0D, (var12 + this.field_77282_d * 1.0D) / 2.0D, var14, var16, var18);
               this.field_77287_j.func_72869_a("smoke", var8, var10, var12, var14, var16, var18);
            }

            if(var7 > 0) {
               Block var25 = Block.field_71973_m[var7];
               if(var25.func_85103_a(this)) {
                  var25.func_71914_a(this.field_77287_j, var4, var5, var6, this.field_77287_j.func_72805_g(var4, var5, var6), 0.3F, 0);
               }

               if(this.field_77287_j.func_72930_a(var4, var5, var6, 0, 0, this.field_77287_j.field_72995_K)) {
                  this.field_77287_j.func_72898_h(var4, var5, var6, 0);
               }

               var25.func_71867_k(this.field_77287_j, var4, var5, var6);
            }
         }
      }

      if(this.field_77286_a) {
         var2 = this.field_77281_g.iterator();

         while(var2.hasNext()) {
            var3 = (ChunkPosition)var2.next();
            var4 = var3.field_76930_a;
            var5 = var3.field_76928_b;
            var6 = var3.field_76929_c;
            var7 = this.field_77287_j.func_72798_a(var4, var5, var6);
            int var24 = this.field_77287_j.func_72798_a(var4, var5 - 1, var6);
            if(var7 == 0 && Block.field_71970_n[var24] && this.field_77290_i.nextInt(3) == 0) {
               this.field_77287_j.func_72859_e(var4, var5, var6, Block.field_72067_ar.field_71990_ca);
            }
         }
      }

   }

   public Map func_77277_b() {
      return this.field_77288_k;
   }
}
