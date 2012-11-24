package net.minecraft.src;

import java.util.List;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.StructureBoundingBox;
import net.minecraft.src.StructureComponent;
import net.minecraft.src.StructureMineshaftPieces;
import net.minecraft.src.TileEntityMobSpawner;
import net.minecraft.src.World;

public class ComponentMineshaftCorridor extends StructureComponent {

   private final boolean field_74958_a;
   private final boolean field_74956_b;
   private boolean field_74957_c;
   private int field_74955_d;


   public ComponentMineshaftCorridor(int p_i3807_1_, Random p_i3807_2_, StructureBoundingBox p_i3807_3_, int p_i3807_4_) {
      super(p_i3807_1_);
      this.field_74885_f = p_i3807_4_;
      this.field_74887_e = p_i3807_3_;
      this.field_74958_a = p_i3807_2_.nextInt(3) == 0;
      this.field_74956_b = !this.field_74958_a && p_i3807_2_.nextInt(23) == 0;
      if(this.field_74885_f != 2 && this.field_74885_f != 0) {
         this.field_74955_d = p_i3807_3_.func_78883_b() / 5;
      } else {
         this.field_74955_d = p_i3807_3_.func_78880_d() / 5;
      }

   }

   public static StructureBoundingBox func_74954_a(List p_74954_0_, Random p_74954_1_, int p_74954_2_, int p_74954_3_, int p_74954_4_, int p_74954_5_) {
      StructureBoundingBox var6 = new StructureBoundingBox(p_74954_2_, p_74954_3_, p_74954_4_, p_74954_2_, p_74954_3_ + 2, p_74954_4_);

      int var7;
      for(var7 = p_74954_1_.nextInt(3) + 2; var7 > 0; --var7) {
         int var8 = var7 * 5;
         switch(p_74954_5_) {
         case 0:
            var6.field_78893_d = p_74954_2_ + 2;
            var6.field_78892_f = p_74954_4_ + (var8 - 1);
            break;
         case 1:
            var6.field_78897_a = p_74954_2_ - (var8 - 1);
            var6.field_78892_f = p_74954_4_ + 2;
            break;
         case 2:
            var6.field_78893_d = p_74954_2_ + 2;
            var6.field_78896_c = p_74954_4_ - (var8 - 1);
            break;
         case 3:
            var6.field_78893_d = p_74954_2_ + (var8 - 1);
            var6.field_78892_f = p_74954_4_ + 2;
         }

         if(StructureComponent.func_74883_a(p_74954_0_, var6) == null) {
            break;
         }
      }

      return var7 > 0?var6:null;
   }

   public void func_74861_a(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_) {
      int var4 = this.func_74877_c();
      int var5 = p_74861_3_.nextInt(4);
      switch(this.field_74885_f) {
      case 0:
         if(var5 <= 1) {
            StructureMineshaftPieces.func_78814_a(p_74861_1_, p_74861_2_, p_74861_3_, this.field_74887_e.field_78897_a, this.field_74887_e.field_78895_b - 1 + p_74861_3_.nextInt(3), this.field_74887_e.field_78892_f + 1, this.field_74885_f, var4);
         } else if(var5 == 2) {
            StructureMineshaftPieces.func_78814_a(p_74861_1_, p_74861_2_, p_74861_3_, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b - 1 + p_74861_3_.nextInt(3), this.field_74887_e.field_78892_f - 3, 1, var4);
         } else {
            StructureMineshaftPieces.func_78814_a(p_74861_1_, p_74861_2_, p_74861_3_, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b - 1 + p_74861_3_.nextInt(3), this.field_74887_e.field_78892_f - 3, 3, var4);
         }
         break;
      case 1:
         if(var5 <= 1) {
            StructureMineshaftPieces.func_78814_a(p_74861_1_, p_74861_2_, p_74861_3_, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b - 1 + p_74861_3_.nextInt(3), this.field_74887_e.field_78896_c, this.field_74885_f, var4);
         } else if(var5 == 2) {
            StructureMineshaftPieces.func_78814_a(p_74861_1_, p_74861_2_, p_74861_3_, this.field_74887_e.field_78897_a, this.field_74887_e.field_78895_b - 1 + p_74861_3_.nextInt(3), this.field_74887_e.field_78896_c - 1, 2, var4);
         } else {
            StructureMineshaftPieces.func_78814_a(p_74861_1_, p_74861_2_, p_74861_3_, this.field_74887_e.field_78897_a, this.field_74887_e.field_78895_b - 1 + p_74861_3_.nextInt(3), this.field_74887_e.field_78892_f + 1, 0, var4);
         }
         break;
      case 2:
         if(var5 <= 1) {
            StructureMineshaftPieces.func_78814_a(p_74861_1_, p_74861_2_, p_74861_3_, this.field_74887_e.field_78897_a, this.field_74887_e.field_78895_b - 1 + p_74861_3_.nextInt(3), this.field_74887_e.field_78896_c - 1, this.field_74885_f, var4);
         } else if(var5 == 2) {
            StructureMineshaftPieces.func_78814_a(p_74861_1_, p_74861_2_, p_74861_3_, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b - 1 + p_74861_3_.nextInt(3), this.field_74887_e.field_78896_c, 1, var4);
         } else {
            StructureMineshaftPieces.func_78814_a(p_74861_1_, p_74861_2_, p_74861_3_, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b - 1 + p_74861_3_.nextInt(3), this.field_74887_e.field_78896_c, 3, var4);
         }
         break;
      case 3:
         if(var5 <= 1) {
            StructureMineshaftPieces.func_78814_a(p_74861_1_, p_74861_2_, p_74861_3_, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b - 1 + p_74861_3_.nextInt(3), this.field_74887_e.field_78896_c, this.field_74885_f, var4);
         } else if(var5 == 2) {
            StructureMineshaftPieces.func_78814_a(p_74861_1_, p_74861_2_, p_74861_3_, this.field_74887_e.field_78893_d - 3, this.field_74887_e.field_78895_b - 1 + p_74861_3_.nextInt(3), this.field_74887_e.field_78896_c - 1, 2, var4);
         } else {
            StructureMineshaftPieces.func_78814_a(p_74861_1_, p_74861_2_, p_74861_3_, this.field_74887_e.field_78893_d - 3, this.field_74887_e.field_78895_b - 1 + p_74861_3_.nextInt(3), this.field_74887_e.field_78892_f + 1, 0, var4);
         }
      }

      if(var4 < 8) {
         int var6;
         int var7;
         if(this.field_74885_f != 2 && this.field_74885_f != 0) {
            for(var6 = this.field_74887_e.field_78897_a + 3; var6 + 3 <= this.field_74887_e.field_78893_d; var6 += 5) {
               var7 = p_74861_3_.nextInt(5);
               if(var7 == 0) {
                  StructureMineshaftPieces.func_78814_a(p_74861_1_, p_74861_2_, p_74861_3_, var6, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c - 1, 2, var4 + 1);
               } else if(var7 == 1) {
                  StructureMineshaftPieces.func_78814_a(p_74861_1_, p_74861_2_, p_74861_3_, var6, this.field_74887_e.field_78895_b, this.field_74887_e.field_78892_f + 1, 0, var4 + 1);
               }
            }
         } else {
            for(var6 = this.field_74887_e.field_78896_c + 3; var6 + 3 <= this.field_74887_e.field_78892_f; var6 += 5) {
               var7 = p_74861_3_.nextInt(5);
               if(var7 == 0) {
                  StructureMineshaftPieces.func_78814_a(p_74861_1_, p_74861_2_, p_74861_3_, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b, var6, 1, var4 + 1);
               } else if(var7 == 1) {
                  StructureMineshaftPieces.func_78814_a(p_74861_1_, p_74861_2_, p_74861_3_, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b, var6, 3, var4 + 1);
               }
            }
         }
      }

   }

   public boolean func_74875_a(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_) {
      if(this.func_74860_a(p_74875_1_, p_74875_3_)) {
         return false;
      } else {
         int var8 = this.field_74955_d * 5 - 1;
         this.func_74884_a(p_74875_1_, p_74875_3_, 0, 0, 0, 2, 1, var8, 0, 0, false);
         this.func_74880_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.8F, 0, 2, 0, 2, 2, var8, 0, 0, false);
         if(this.field_74956_b) {
            this.func_74880_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.6F, 0, 0, 0, 2, 1, var8, Block.field_71955_W.field_71990_ca, 0, false);
         }

         int var9;
         int var10;
         int var11;
         for(var9 = 0; var9 < this.field_74955_d; ++var9) {
            var10 = 2 + var9 * 5;
            this.func_74884_a(p_74875_1_, p_74875_3_, 0, 0, var10, 0, 1, var10, Block.field_72031_aZ.field_71990_ca, 0, false);
            this.func_74884_a(p_74875_1_, p_74875_3_, 2, 0, var10, 2, 1, var10, Block.field_72031_aZ.field_71990_ca, 0, false);
            if(p_74875_2_.nextInt(4) == 0) {
               this.func_74884_a(p_74875_1_, p_74875_3_, 0, 2, var10, 0, 2, var10, Block.field_71988_x.field_71990_ca, 0, false);
               this.func_74884_a(p_74875_1_, p_74875_3_, 2, 2, var10, 2, 2, var10, Block.field_71988_x.field_71990_ca, 0, false);
            } else {
               this.func_74884_a(p_74875_1_, p_74875_3_, 0, 2, var10, 2, 2, var10, Block.field_71988_x.field_71990_ca, 0, false);
            }

            this.func_74876_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 0, 2, var10 - 1, Block.field_71955_W.field_71990_ca, 0);
            this.func_74876_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 2, 2, var10 - 1, Block.field_71955_W.field_71990_ca, 0);
            this.func_74876_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 0, 2, var10 + 1, Block.field_71955_W.field_71990_ca, 0);
            this.func_74876_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 2, 2, var10 + 1, Block.field_71955_W.field_71990_ca, 0);
            this.func_74876_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 0, 2, var10 - 2, Block.field_71955_W.field_71990_ca, 0);
            this.func_74876_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 2, 2, var10 - 2, Block.field_71955_W.field_71990_ca, 0);
            this.func_74876_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 0, 2, var10 + 2, Block.field_71955_W.field_71990_ca, 0);
            this.func_74876_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 2, 2, var10 + 2, Block.field_71955_W.field_71990_ca, 0);
            this.func_74876_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 1, 2, var10 - 1, Block.field_72069_aq.field_71990_ca, 0);
            this.func_74876_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 1, 2, var10 + 1, Block.field_72069_aq.field_71990_ca, 0);
            if(p_74875_2_.nextInt(100) == 0) {
               this.func_74879_a(p_74875_1_, p_74875_3_, p_74875_2_, 2, 0, var10 - 1, StructureMineshaftPieces.func_78816_a(), 3 + p_74875_2_.nextInt(4));
            }

            if(p_74875_2_.nextInt(100) == 0) {
               this.func_74879_a(p_74875_1_, p_74875_3_, p_74875_2_, 0, 0, var10 + 1, StructureMineshaftPieces.func_78816_a(), 3 + p_74875_2_.nextInt(4));
            }

            if(this.field_74956_b && !this.field_74957_c) {
               var11 = this.func_74862_a(0);
               int var12 = var10 - 1 + p_74875_2_.nextInt(3);
               int var13 = this.func_74865_a(1, var12);
               var12 = this.func_74873_b(1, var12);
               if(p_74875_3_.func_78890_b(var13, var11, var12)) {
                  this.field_74957_c = true;
                  p_74875_1_.func_72859_e(var13, var11, var12, Block.field_72065_as.field_71990_ca);
                  TileEntityMobSpawner var14 = (TileEntityMobSpawner)p_74875_1_.func_72796_p(var13, var11, var12);
                  if(var14 != null) {
                     var14.func_70385_a("CaveSpider");
                  }
               }
            }
         }

         for(var9 = 0; var9 <= 2; ++var9) {
            for(var10 = 0; var10 <= var8; ++var10) {
               var11 = this.func_74866_a(p_74875_1_, var9, -1, var10, p_74875_3_);
               if(var11 == 0) {
                  this.func_74864_a(p_74875_1_, Block.field_71988_x.field_71990_ca, 0, var9, -1, var10, p_74875_3_);
               }
            }
         }

         if(this.field_74958_a) {
            for(var9 = 0; var9 <= var8; ++var9) {
               var10 = this.func_74866_a(p_74875_1_, 1, -1, var9, p_74875_3_);
               if(var10 > 0 && Block.field_71970_n[var10]) {
                  this.func_74876_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.7F, 1, 0, var9, Block.field_72056_aG.field_71990_ca, this.func_74863_c(Block.field_72056_aG.field_71990_ca, 0));
               }
            }
         }

         return true;
      }
   }
}
