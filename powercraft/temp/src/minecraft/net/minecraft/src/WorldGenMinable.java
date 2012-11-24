package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;

public class WorldGenMinable extends WorldGenerator {

   private int field_76542_a;
   private int field_76541_b;


   public WorldGenMinable(int p_i3796_1_, int p_i3796_2_) {
      this.field_76542_a = p_i3796_1_;
      this.field_76541_b = p_i3796_2_;
   }

   public boolean func_76484_a(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_) {
      float var6 = p_76484_2_.nextFloat() * 3.1415927F;
      double var7 = (double)((float)(p_76484_3_ + 8) + MathHelper.func_76126_a(var6) * (float)this.field_76541_b / 8.0F);
      double var9 = (double)((float)(p_76484_3_ + 8) - MathHelper.func_76126_a(var6) * (float)this.field_76541_b / 8.0F);
      double var11 = (double)((float)(p_76484_5_ + 8) + MathHelper.func_76134_b(var6) * (float)this.field_76541_b / 8.0F);
      double var13 = (double)((float)(p_76484_5_ + 8) - MathHelper.func_76134_b(var6) * (float)this.field_76541_b / 8.0F);
      double var15 = (double)(p_76484_4_ + p_76484_2_.nextInt(3) - 2);
      double var17 = (double)(p_76484_4_ + p_76484_2_.nextInt(3) - 2);

      for(int var19 = 0; var19 <= this.field_76541_b; ++var19) {
         double var20 = var7 + (var9 - var7) * (double)var19 / (double)this.field_76541_b;
         double var22 = var15 + (var17 - var15) * (double)var19 / (double)this.field_76541_b;
         double var24 = var11 + (var13 - var11) * (double)var19 / (double)this.field_76541_b;
         double var26 = p_76484_2_.nextDouble() * (double)this.field_76541_b / 16.0D;
         double var28 = (double)(MathHelper.func_76126_a((float)var19 * 3.1415927F / (float)this.field_76541_b) + 1.0F) * var26 + 1.0D;
         double var30 = (double)(MathHelper.func_76126_a((float)var19 * 3.1415927F / (float)this.field_76541_b) + 1.0F) * var26 + 1.0D;
         int var32 = MathHelper.func_76128_c(var20 - var28 / 2.0D);
         int var33 = MathHelper.func_76128_c(var22 - var30 / 2.0D);
         int var34 = MathHelper.func_76128_c(var24 - var28 / 2.0D);
         int var35 = MathHelper.func_76128_c(var20 + var28 / 2.0D);
         int var36 = MathHelper.func_76128_c(var22 + var30 / 2.0D);
         int var37 = MathHelper.func_76128_c(var24 + var28 / 2.0D);

         for(int var38 = var32; var38 <= var35; ++var38) {
            double var39 = ((double)var38 + 0.5D - var20) / (var28 / 2.0D);
            if(var39 * var39 < 1.0D) {
               for(int var41 = var33; var41 <= var36; ++var41) {
                  double var42 = ((double)var41 + 0.5D - var22) / (var30 / 2.0D);
                  if(var39 * var39 + var42 * var42 < 1.0D) {
                     for(int var44 = var34; var44 <= var37; ++var44) {
                        double var45 = ((double)var44 + 0.5D - var24) / (var28 / 2.0D);
                        if(var39 * var39 + var42 * var42 + var45 * var45 < 1.0D && p_76484_1_.func_72798_a(var38, var41, var44) == Block.field_71981_t.field_71990_ca) {
                           p_76484_1_.func_72822_b(var38, var41, var44, this.field_76542_a);
                        }
                     }
                  }
               }
            }
         }
      }

      return true;
   }
}
