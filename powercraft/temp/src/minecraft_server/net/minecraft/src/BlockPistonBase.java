package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.BlockPistonMoving;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Facing;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityPiston;
import net.minecraft.src.World;

public class BlockPistonBase extends Block {

   private boolean field_72119_a;


   public BlockPistonBase(int p_i4025_1_, int p_i4025_2_, boolean p_i4025_3_) {
      super(p_i4025_1_, p_i4025_2_, Material.field_76233_E);
      this.field_72119_a = p_i4025_3_;
      this.func_71884_a(field_71976_h);
      this.func_71848_c(0.5F);
      this.func_71849_a(CreativeTabs.field_78028_d);
   }

   @SideOnly(Side.CLIENT)
   public int func_72118_n() {
      return this.field_72119_a?106:107;
   }

   public int func_71858_a(int p_71858_1_, int p_71858_2_) {
      int var3 = func_72117_e(p_71858_2_);
      return var3 > 5?this.field_72059_bZ:(p_71858_1_ == var3?(!func_72114_f(p_71858_2_) && this.field_72026_ch <= 0.0D && this.field_72023_ci <= 0.0D && this.field_72024_cj <= 0.0D && this.field_72021_ck >= 1.0D && this.field_72022_cl >= 1.0D && this.field_72019_cm >= 1.0D?this.field_72059_bZ:110):(p_71858_1_ == Facing.field_71588_a[var3]?109:108));
   }

   public int func_71857_b() {
      return 16;
   }

   public boolean func_71926_d() {
      return false;
   }

   public boolean func_71903_a(World p_71903_1_, int p_71903_2_, int p_71903_3_, int p_71903_4_, EntityPlayer p_71903_5_, int p_71903_6_, float p_71903_7_, float p_71903_8_, float p_71903_9_) {
      return false;
   }

   public void func_71860_a(World p_71860_1_, int p_71860_2_, int p_71860_3_, int p_71860_4_, EntityLiving p_71860_5_) {
      int var6 = func_72116_b(p_71860_1_, p_71860_2_, p_71860_3_, p_71860_4_, (EntityPlayer)p_71860_5_);
      p_71860_1_.func_72921_c(p_71860_2_, p_71860_3_, p_71860_4_, var6);
      if(!p_71860_1_.field_72995_K) {
         this.func_72110_l(p_71860_1_, p_71860_2_, p_71860_3_, p_71860_4_);
      }

   }

   public void func_71863_a(World p_71863_1_, int p_71863_2_, int p_71863_3_, int p_71863_4_, int p_71863_5_) {
      if(!p_71863_1_.field_72995_K) {
         this.func_72110_l(p_71863_1_, p_71863_2_, p_71863_3_, p_71863_4_);
      }

   }

   public void func_71861_g(World p_71861_1_, int p_71861_2_, int p_71861_3_, int p_71861_4_) {
      if(!p_71861_1_.field_72995_K && p_71861_1_.func_72796_p(p_71861_2_, p_71861_3_, p_71861_4_) == null) {
         this.func_72110_l(p_71861_1_, p_71861_2_, p_71861_3_, p_71861_4_);
      }

   }

   private void func_72110_l(World p_72110_1_, int p_72110_2_, int p_72110_3_, int p_72110_4_) {
      int var5 = p_72110_1_.func_72805_g(p_72110_2_, p_72110_3_, p_72110_4_);
      int var6 = func_72117_e(var5);
      if(var6 != 7) {
         boolean var7 = this.func_72113_e(p_72110_1_, p_72110_2_, p_72110_3_, p_72110_4_, var6);
         if(var7 && !func_72114_f(var5)) {
            if(func_72112_i(p_72110_1_, p_72110_2_, p_72110_3_, p_72110_4_, var6)) {
               p_72110_1_.func_72965_b(p_72110_2_, p_72110_3_, p_72110_4_, this.field_71990_ca, 0, var6);
            }
         } else if(!var7 && func_72114_f(var5)) {
            p_72110_1_.func_72965_b(p_72110_2_, p_72110_3_, p_72110_4_, this.field_71990_ca, 1, var6);
         }

      }
   }

   private boolean func_72113_e(World p_72113_1_, int p_72113_2_, int p_72113_3_, int p_72113_4_, int p_72113_5_) {
      return p_72113_5_ != 0 && p_72113_1_.func_72878_l(p_72113_2_, p_72113_3_ - 1, p_72113_4_, 0)?true:(p_72113_5_ != 1 && p_72113_1_.func_72878_l(p_72113_2_, p_72113_3_ + 1, p_72113_4_, 1)?true:(p_72113_5_ != 2 && p_72113_1_.func_72878_l(p_72113_2_, p_72113_3_, p_72113_4_ - 1, 2)?true:(p_72113_5_ != 3 && p_72113_1_.func_72878_l(p_72113_2_, p_72113_3_, p_72113_4_ + 1, 3)?true:(p_72113_5_ != 5 && p_72113_1_.func_72878_l(p_72113_2_ + 1, p_72113_3_, p_72113_4_, 5)?true:(p_72113_5_ != 4 && p_72113_1_.func_72878_l(p_72113_2_ - 1, p_72113_3_, p_72113_4_, 4)?true:(p_72113_1_.func_72878_l(p_72113_2_, p_72113_3_, p_72113_4_, 0)?true:(p_72113_1_.func_72878_l(p_72113_2_, p_72113_3_ + 2, p_72113_4_, 1)?true:(p_72113_1_.func_72878_l(p_72113_2_, p_72113_3_ + 1, p_72113_4_ - 1, 2)?true:(p_72113_1_.func_72878_l(p_72113_2_, p_72113_3_ + 1, p_72113_4_ + 1, 3)?true:(p_72113_1_.func_72878_l(p_72113_2_ - 1, p_72113_3_ + 1, p_72113_4_, 4)?true:p_72113_1_.func_72878_l(p_72113_2_ + 1, p_72113_3_ + 1, p_72113_4_, 5)))))))))));
   }

   public void func_71883_b(World p_71883_1_, int p_71883_2_, int p_71883_3_, int p_71883_4_, int p_71883_5_, int p_71883_6_) {
      if(p_71883_5_ == 0) {
         p_71883_1_.func_72881_d(p_71883_2_, p_71883_3_, p_71883_4_, p_71883_6_ | 8);
      } else {
         p_71883_1_.func_72881_d(p_71883_2_, p_71883_3_, p_71883_4_, p_71883_6_);
      }

      if(p_71883_5_ == 0) {
         if(this.func_72115_j(p_71883_1_, p_71883_2_, p_71883_3_, p_71883_4_, p_71883_6_)) {
            p_71883_1_.func_72921_c(p_71883_2_, p_71883_3_, p_71883_4_, p_71883_6_ | 8);
            p_71883_1_.func_72908_a((double)p_71883_2_ + 0.5D, (double)p_71883_3_ + 0.5D, (double)p_71883_4_ + 0.5D, "tile.piston.out", 0.5F, p_71883_1_.field_73012_v.nextFloat() * 0.25F + 0.6F);
         } else {
            p_71883_1_.func_72881_d(p_71883_2_, p_71883_3_, p_71883_4_, p_71883_6_);
         }
      } else if(p_71883_5_ == 1) {
         TileEntity var7 = p_71883_1_.func_72796_p(p_71883_2_ + Facing.field_71586_b[p_71883_6_], p_71883_3_ + Facing.field_71587_c[p_71883_6_], p_71883_4_ + Facing.field_71585_d[p_71883_6_]);
         if(var7 instanceof TileEntityPiston) {
            ((TileEntityPiston)var7).func_70339_i();
         }

         p_71883_1_.func_72961_c(p_71883_2_, p_71883_3_, p_71883_4_, Block.field_72095_ac.field_71990_ca, p_71883_6_);
         p_71883_1_.func_72837_a(p_71883_2_, p_71883_3_, p_71883_4_, BlockPistonMoving.func_72297_a(this.field_71990_ca, p_71883_6_, p_71883_6_, false, true));
         if(this.field_72119_a) {
            int var8 = p_71883_2_ + Facing.field_71586_b[p_71883_6_] * 2;
            int var9 = p_71883_3_ + Facing.field_71587_c[p_71883_6_] * 2;
            int var10 = p_71883_4_ + Facing.field_71585_d[p_71883_6_] * 2;
            int var11 = p_71883_1_.func_72798_a(var8, var9, var10);
            int var12 = p_71883_1_.func_72805_g(var8, var9, var10);
            boolean var13 = false;
            if(var11 == Block.field_72095_ac.field_71990_ca) {
               TileEntity var14 = p_71883_1_.func_72796_p(var8, var9, var10);
               if(var14 instanceof TileEntityPiston) {
                  TileEntityPiston var15 = (TileEntityPiston)var14;
                  if(var15.func_70336_c() == p_71883_6_ && var15.func_70341_b()) {
                     var15.func_70339_i();
                     var11 = var15.func_70340_a();
                     var12 = var15.func_70322_n();
                     var13 = true;
                  }
               }
            }

            if(!var13 && var11 > 0 && func_72111_a(var11, p_71883_1_, var8, var9, var10, false) && (Block.field_71973_m[var11].func_71915_e() == 0 || var11 == Block.field_71963_Z.field_71990_ca || var11 == Block.field_71956_V.field_71990_ca)) {
               p_71883_2_ += Facing.field_71586_b[p_71883_6_];
               p_71883_3_ += Facing.field_71587_c[p_71883_6_];
               p_71883_4_ += Facing.field_71585_d[p_71883_6_];
               p_71883_1_.func_72961_c(p_71883_2_, p_71883_3_, p_71883_4_, Block.field_72095_ac.field_71990_ca, var12);
               p_71883_1_.func_72837_a(p_71883_2_, p_71883_3_, p_71883_4_, BlockPistonMoving.func_72297_a(var11, var12, p_71883_6_, false, false));
               p_71883_1_.func_72859_e(var8, var9, var10, 0);
            } else if(!var13) {
               p_71883_1_.func_72859_e(p_71883_2_ + Facing.field_71586_b[p_71883_6_], p_71883_3_ + Facing.field_71587_c[p_71883_6_], p_71883_4_ + Facing.field_71585_d[p_71883_6_], 0);
            }
         } else {
            p_71883_1_.func_72859_e(p_71883_2_ + Facing.field_71586_b[p_71883_6_], p_71883_3_ + Facing.field_71587_c[p_71883_6_], p_71883_4_ + Facing.field_71585_d[p_71883_6_], 0);
         }

         p_71883_1_.func_72908_a((double)p_71883_2_ + 0.5D, (double)p_71883_3_ + 0.5D, (double)p_71883_4_ + 0.5D, "tile.piston.in", 0.5F, p_71883_1_.field_73012_v.nextFloat() * 0.15F + 0.6F);
      }

   }

   public void func_71902_a(IBlockAccess p_71902_1_, int p_71902_2_, int p_71902_3_, int p_71902_4_) {
      int var5 = p_71902_1_.func_72805_g(p_71902_2_, p_71902_3_, p_71902_4_);
      if(func_72114_f(var5)) {
         switch(func_72117_e(var5)) {
         case 0:
            this.func_71905_a(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
            break;
         case 1:
            this.func_71905_a(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
            break;
         case 2:
            this.func_71905_a(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
            break;
         case 3:
            this.func_71905_a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
            break;
         case 4:
            this.func_71905_a(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            break;
         case 5:
            this.func_71905_a(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
         }
      } else {
         this.func_71905_a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   public void func_71919_f() {
      this.func_71905_a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public void func_71871_a(World p_71871_1_, int p_71871_2_, int p_71871_3_, int p_71871_4_, AxisAlignedBB p_71871_5_, List p_71871_6_, Entity p_71871_7_) {
      this.func_71905_a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      super.func_71871_a(p_71871_1_, p_71871_2_, p_71871_3_, p_71871_4_, p_71871_5_, p_71871_6_, p_71871_7_);
   }

   public AxisAlignedBB func_71872_e(World p_71872_1_, int p_71872_2_, int p_71872_3_, int p_71872_4_) {
      this.func_71902_a(p_71872_1_, p_71872_2_, p_71872_3_, p_71872_4_);
      return super.func_71872_e(p_71872_1_, p_71872_2_, p_71872_3_, p_71872_4_);
   }

   public boolean func_71886_c() {
      return false;
   }

   public static int func_72117_e(int p_72117_0_) {
      return p_72117_0_ & 7;
   }

   public static boolean func_72114_f(int p_72114_0_) {
      return (p_72114_0_ & 8) != 0;
   }

   public static int func_72116_b(World p_72116_0_, int p_72116_1_, int p_72116_2_, int p_72116_3_, EntityPlayer p_72116_4_) {
      if(MathHelper.func_76135_e((float)p_72116_4_.field_70165_t - (float)p_72116_1_) < 2.0F && MathHelper.func_76135_e((float)p_72116_4_.field_70161_v - (float)p_72116_3_) < 2.0F) {
         double var5 = p_72116_4_.field_70163_u + 1.82D - (double)p_72116_4_.field_70129_M;
         if(var5 - (double)p_72116_2_ > 2.0D) {
            return 1;
         }

         if((double)p_72116_2_ - var5 > 0.0D) {
            return 0;
         }
      }

      int var7 = MathHelper.func_76128_c((double)(p_72116_4_.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3;
      return var7 == 0?2:(var7 == 1?5:(var7 == 2?3:(var7 == 3?4:0)));
   }

   private static boolean func_72111_a(int p_72111_0_, World p_72111_1_, int p_72111_2_, int p_72111_3_, int p_72111_4_, boolean p_72111_5_) {
      if(p_72111_0_ == Block.field_72089_ap.field_71990_ca) {
         return false;
      } else {
         if(p_72111_0_ != Block.field_71963_Z.field_71990_ca && p_72111_0_ != Block.field_71956_V.field_71990_ca) {
            if(Block.field_71973_m[p_72111_0_].func_71934_m(p_72111_1_, p_72111_2_, p_72111_3_, p_72111_4_) == -1.0F) {
               return false;
            }

            if(Block.field_71973_m[p_72111_0_].func_71915_e() == 2) {
               return false;
            }

            if(!p_72111_5_ && Block.field_71973_m[p_72111_0_].func_71915_e() == 1) {
               return false;
            }
         } else if(func_72114_f(p_72111_1_.func_72805_g(p_72111_2_, p_72111_3_, p_72111_4_))) {
            return false;
         }

         return !(Block.field_71973_m[p_72111_0_] instanceof BlockContainer);
      }
   }

   private static boolean func_72112_i(World p_72112_0_, int p_72112_1_, int p_72112_2_, int p_72112_3_, int p_72112_4_) {
      int var5 = p_72112_1_ + Facing.field_71586_b[p_72112_4_];
      int var6 = p_72112_2_ + Facing.field_71587_c[p_72112_4_];
      int var7 = p_72112_3_ + Facing.field_71585_d[p_72112_4_];
      int var8 = 0;

      while(true) {
         if(var8 < 13) {
            if(var6 <= 0 || var6 >= 255) {
               return false;
            }

            int var9 = p_72112_0_.func_72798_a(var5, var6, var7);
            if(var9 != 0) {
               if(!func_72111_a(var9, p_72112_0_, var5, var6, var7, true)) {
                  return false;
               }

               if(Block.field_71973_m[var9].func_71915_e() != 1) {
                  if(var8 == 12) {
                     return false;
                  }

                  var5 += Facing.field_71586_b[p_72112_4_];
                  var6 += Facing.field_71587_c[p_72112_4_];
                  var7 += Facing.field_71585_d[p_72112_4_];
                  ++var8;
                  continue;
               }
            }
         }

         return true;
      }
   }

   private boolean func_72115_j(World p_72115_1_, int p_72115_2_, int p_72115_3_, int p_72115_4_, int p_72115_5_) {
      int var6 = p_72115_2_ + Facing.field_71586_b[p_72115_5_];
      int var7 = p_72115_3_ + Facing.field_71587_c[p_72115_5_];
      int var8 = p_72115_4_ + Facing.field_71585_d[p_72115_5_];
      int var9 = 0;

      while(true) {
         int var10;
         if(var9 < 13) {
            if(var7 <= 0 || var7 >= 255) {
               return false;
            }

            var10 = p_72115_1_.func_72798_a(var6, var7, var8);
            if(var10 != 0) {
               if(!func_72111_a(var10, p_72115_1_, var6, var7, var8, true)) {
                  return false;
               }

               if(Block.field_71973_m[var10].func_71915_e() != 1) {
                  if(var9 == 12) {
                     return false;
                  }

                  var6 += Facing.field_71586_b[p_72115_5_];
                  var7 += Facing.field_71587_c[p_72115_5_];
                  var8 += Facing.field_71585_d[p_72115_5_];
                  ++var9;
                  continue;
               }

               Block.field_71973_m[var10].func_71897_c(p_72115_1_, var6, var7, var8, p_72115_1_.func_72805_g(var6, var7, var8), 0);
               p_72115_1_.func_72859_e(var6, var7, var8, 0);
            }
         }

         while(var6 != p_72115_2_ || var7 != p_72115_3_ || var8 != p_72115_4_) {
            var9 = var6 - Facing.field_71586_b[p_72115_5_];
            var10 = var7 - Facing.field_71587_c[p_72115_5_];
            int var11 = var8 - Facing.field_71585_d[p_72115_5_];
            int var12 = p_72115_1_.func_72798_a(var9, var10, var11);
            int var13 = p_72115_1_.func_72805_g(var9, var10, var11);
            if(var12 == this.field_71990_ca && var9 == p_72115_2_ && var10 == p_72115_3_ && var11 == p_72115_4_) {
               p_72115_1_.func_72930_a(var6, var7, var8, Block.field_72095_ac.field_71990_ca, p_72115_5_ | (this.field_72119_a?8:0), false);
               p_72115_1_.func_72837_a(var6, var7, var8, BlockPistonMoving.func_72297_a(Block.field_72099_aa.field_71990_ca, p_72115_5_ | (this.field_72119_a?8:0), p_72115_5_, true, false));
            } else {
               p_72115_1_.func_72930_a(var6, var7, var8, Block.field_72095_ac.field_71990_ca, var13, false);
               p_72115_1_.func_72837_a(var6, var7, var8, BlockPistonMoving.func_72297_a(var12, var13, p_72115_5_, true, false));
            }

            var6 = var9;
            var7 = var10;
            var8 = var11;
         }

         return true;
      }
   }
}
