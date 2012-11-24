package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RailLogic;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class BlockRail extends Block {

   private final boolean field_72186_a;


   public static final boolean func_72180_d_(World p_72180_0_, int p_72180_1_, int p_72180_2_, int p_72180_3_) {
      int var4 = p_72180_0_.func_72798_a(p_72180_1_, p_72180_2_, p_72180_3_);
      return var4 == Block.field_72056_aG.field_71990_ca || var4 == Block.field_71954_T.field_71990_ca || var4 == Block.field_71953_U.field_71990_ca;
   }

   public static final boolean func_72184_d(int p_72184_0_) {
      return p_72184_0_ == Block.field_72056_aG.field_71990_ca || p_72184_0_ == Block.field_71954_T.field_71990_ca || p_72184_0_ == Block.field_71953_U.field_71990_ca;
   }

   protected BlockRail(int p_i3984_1_, int p_i3984_2_, boolean p_i3984_3_) {
      super(p_i3984_1_, p_i3984_2_, Material.field_76265_p);
      this.field_72186_a = p_i3984_3_;
      this.func_71905_a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
      this.func_71849_a(CreativeTabs.field_78029_e);
   }

   public boolean func_72183_n() {
      return this.field_72186_a;
   }

   public AxisAlignedBB func_71872_e(World p_71872_1_, int p_71872_2_, int p_71872_3_, int p_71872_4_) {
      return null;
   }

   public boolean func_71926_d() {
      return false;
   }

   public MovingObjectPosition func_71878_a(World p_71878_1_, int p_71878_2_, int p_71878_3_, int p_71878_4_, Vec3 p_71878_5_, Vec3 p_71878_6_) {
      this.func_71902_a(p_71878_1_, p_71878_2_, p_71878_3_, p_71878_4_);
      return super.func_71878_a(p_71878_1_, p_71878_2_, p_71878_3_, p_71878_4_, p_71878_5_, p_71878_6_);
   }

   public void func_71902_a(IBlockAccess p_71902_1_, int p_71902_2_, int p_71902_3_, int p_71902_4_) {
      int var5 = p_71902_1_.func_72805_g(p_71902_2_, p_71902_3_, p_71902_4_);
      if(var5 >= 2 && var5 <= 5) {
         this.func_71905_a(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
      } else {
         this.func_71905_a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
      }

   }

   public int func_71858_a(int p_71858_1_, int p_71858_2_) {
      if(this.field_72186_a) {
         if(this.field_71990_ca == Block.field_71954_T.field_71990_ca && (p_71858_2_ & 8) == 0) {
            return this.field_72059_bZ - 16;
         }
      } else if(p_71858_2_ >= 6) {
         return this.field_72059_bZ - 16;
      }

      return this.field_72059_bZ;
   }

   public boolean func_71886_c() {
      return false;
   }

   public int func_71857_b() {
      return 9;
   }

   public int func_71925_a(Random p_71925_1_) {
      return 1;
   }

   public boolean func_71930_b(World p_71930_1_, int p_71930_2_, int p_71930_3_, int p_71930_4_) {
      return p_71930_1_.func_72797_t(p_71930_2_, p_71930_3_ - 1, p_71930_4_);
   }

   public void func_71861_g(World p_71861_1_, int p_71861_2_, int p_71861_3_, int p_71861_4_) {
      if(!p_71861_1_.field_72995_K) {
         this.func_72181_a(p_71861_1_, p_71861_2_, p_71861_3_, p_71861_4_, true);
         if(this.field_71990_ca == Block.field_71954_T.field_71990_ca) {
            this.func_71863_a(p_71861_1_, p_71861_2_, p_71861_3_, p_71861_4_, this.field_71990_ca);
         }
      }

   }

   public void func_71863_a(World p_71863_1_, int p_71863_2_, int p_71863_3_, int p_71863_4_, int p_71863_5_) {
      if(!p_71863_1_.field_72995_K) {
         int var6 = p_71863_1_.func_72805_g(p_71863_2_, p_71863_3_, p_71863_4_);
         int var7 = var6;
         if(this.field_72186_a) {
            var7 = var6 & 7;
         }

         boolean var8 = false;
         if(!p_71863_1_.func_72797_t(p_71863_2_, p_71863_3_ - 1, p_71863_4_)) {
            var8 = true;
         }

         if(var7 == 2 && !p_71863_1_.func_72797_t(p_71863_2_ + 1, p_71863_3_, p_71863_4_)) {
            var8 = true;
         }

         if(var7 == 3 && !p_71863_1_.func_72797_t(p_71863_2_ - 1, p_71863_3_, p_71863_4_)) {
            var8 = true;
         }

         if(var7 == 4 && !p_71863_1_.func_72797_t(p_71863_2_, p_71863_3_, p_71863_4_ - 1)) {
            var8 = true;
         }

         if(var7 == 5 && !p_71863_1_.func_72797_t(p_71863_2_, p_71863_3_, p_71863_4_ + 1)) {
            var8 = true;
         }

         if(var8) {
            this.func_71897_c(p_71863_1_, p_71863_2_, p_71863_3_, p_71863_4_, p_71863_1_.func_72805_g(p_71863_2_, p_71863_3_, p_71863_4_), 0);
            p_71863_1_.func_72859_e(p_71863_2_, p_71863_3_, p_71863_4_, 0);
         } else if(this.field_71990_ca == Block.field_71954_T.field_71990_ca) {
            boolean var9 = p_71863_1_.func_72864_z(p_71863_2_, p_71863_3_, p_71863_4_);
            var9 = var9 || this.func_72182_a(p_71863_1_, p_71863_2_, p_71863_3_, p_71863_4_, var6, true, 0) || this.func_72182_a(p_71863_1_, p_71863_2_, p_71863_3_, p_71863_4_, var6, false, 0);
            boolean var10 = false;
            if(var9 && (var6 & 8) == 0) {
               p_71863_1_.func_72921_c(p_71863_2_, p_71863_3_, p_71863_4_, var7 | 8);
               var10 = true;
            } else if(!var9 && (var6 & 8) != 0) {
               p_71863_1_.func_72921_c(p_71863_2_, p_71863_3_, p_71863_4_, var7);
               var10 = true;
            }

            if(var10) {
               p_71863_1_.func_72898_h(p_71863_2_, p_71863_3_ - 1, p_71863_4_, this.field_71990_ca);
               if(var7 == 2 || var7 == 3 || var7 == 4 || var7 == 5) {
                  p_71863_1_.func_72898_h(p_71863_2_, p_71863_3_ + 1, p_71863_4_, this.field_71990_ca);
               }
            }
         } else if(p_71863_5_ > 0 && Block.field_71973_m[p_71863_5_].func_71853_i() && !this.field_72186_a && RailLogic.func_73650_a(new RailLogic(this, p_71863_1_, p_71863_2_, p_71863_3_, p_71863_4_)) == 3) {
            this.func_72181_a(p_71863_1_, p_71863_2_, p_71863_3_, p_71863_4_, false);
         }

      }
   }

   private void func_72181_a(World p_72181_1_, int p_72181_2_, int p_72181_3_, int p_72181_4_, boolean p_72181_5_) {
      if(!p_72181_1_.field_72995_K) {
         (new RailLogic(this, p_72181_1_, p_72181_2_, p_72181_3_, p_72181_4_)).func_73652_a(p_72181_1_.func_72864_z(p_72181_2_, p_72181_3_, p_72181_4_), p_72181_5_);
      }
   }

   private boolean func_72182_a(World p_72182_1_, int p_72182_2_, int p_72182_3_, int p_72182_4_, int p_72182_5_, boolean p_72182_6_, int p_72182_7_) {
      if(p_72182_7_ >= 8) {
         return false;
      } else {
         int var8 = p_72182_5_ & 7;
         boolean var9 = true;
         switch(var8) {
         case 0:
            if(p_72182_6_) {
               ++p_72182_4_;
            } else {
               --p_72182_4_;
            }
            break;
         case 1:
            if(p_72182_6_) {
               --p_72182_2_;
            } else {
               ++p_72182_2_;
            }
            break;
         case 2:
            if(p_72182_6_) {
               --p_72182_2_;
            } else {
               ++p_72182_2_;
               ++p_72182_3_;
               var9 = false;
            }

            var8 = 1;
            break;
         case 3:
            if(p_72182_6_) {
               --p_72182_2_;
               ++p_72182_3_;
               var9 = false;
            } else {
               ++p_72182_2_;
            }

            var8 = 1;
            break;
         case 4:
            if(p_72182_6_) {
               ++p_72182_4_;
            } else {
               --p_72182_4_;
               ++p_72182_3_;
               var9 = false;
            }

            var8 = 0;
            break;
         case 5:
            if(p_72182_6_) {
               ++p_72182_4_;
               ++p_72182_3_;
               var9 = false;
            } else {
               --p_72182_4_;
            }

            var8 = 0;
         }

         return this.func_72185_a(p_72182_1_, p_72182_2_, p_72182_3_, p_72182_4_, p_72182_6_, p_72182_7_, var8)?true:var9 && this.func_72185_a(p_72182_1_, p_72182_2_, p_72182_3_ - 1, p_72182_4_, p_72182_6_, p_72182_7_, var8);
      }
   }

   private boolean func_72185_a(World p_72185_1_, int p_72185_2_, int p_72185_3_, int p_72185_4_, boolean p_72185_5_, int p_72185_6_, int p_72185_7_) {
      int var8 = p_72185_1_.func_72798_a(p_72185_2_, p_72185_3_, p_72185_4_);
      if(var8 == Block.field_71954_T.field_71990_ca) {
         int var9 = p_72185_1_.func_72805_g(p_72185_2_, p_72185_3_, p_72185_4_);
         int var10 = var9 & 7;
         if(p_72185_7_ == 1 && (var10 == 0 || var10 == 4 || var10 == 5)) {
            return false;
         }

         if(p_72185_7_ == 0 && (var10 == 1 || var10 == 2 || var10 == 3)) {
            return false;
         }

         if((var9 & 8) != 0) {
            if(p_72185_1_.func_72864_z(p_72185_2_, p_72185_3_, p_72185_4_)) {
               return true;
            }

            return this.func_72182_a(p_72185_1_, p_72185_2_, p_72185_3_, p_72185_4_, var9, p_72185_5_, p_72185_6_ + 1);
         }
      }

      return false;
   }

   public int func_71915_e() {
      return 0;
   }

   // $FF: synthetic method
   static boolean func_72179_a(BlockRail p_72179_0_) {
      return p_72179_0_.field_72186_a;
   }
}
