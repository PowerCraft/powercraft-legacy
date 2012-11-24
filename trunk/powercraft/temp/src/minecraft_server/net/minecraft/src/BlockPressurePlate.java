package net.minecraft.src;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockFence;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMobType;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockPressurePlate extends Block {

   private EnumMobType field_72194_a;


   protected BlockPressurePlate(int p_i3981_1_, int p_i3981_2_, EnumMobType p_i3981_3_, Material p_i3981_4_) {
      super(p_i3981_1_, p_i3981_2_, p_i3981_4_);
      this.field_72194_a = p_i3981_3_;
      this.func_71849_a(CreativeTabs.field_78028_d);
      this.func_71907_b(true);
      float var5 = 0.0625F;
      this.func_71905_a(var5, 0.0F, var5, 1.0F - var5, 0.03125F, 1.0F - var5);
   }

   public int func_71859_p_() {
      return 20;
   }

   public AxisAlignedBB func_71872_e(World p_71872_1_, int p_71872_2_, int p_71872_3_, int p_71872_4_) {
      return null;
   }

   public boolean func_71926_d() {
      return false;
   }

   public boolean func_71886_c() {
      return false;
   }

   public boolean func_71918_c(IBlockAccess p_71918_1_, int p_71918_2_, int p_71918_3_, int p_71918_4_) {
      return true;
   }

   public boolean func_71930_b(World p_71930_1_, int p_71930_2_, int p_71930_3_, int p_71930_4_) {
      return p_71930_1_.func_72797_t(p_71930_2_, p_71930_3_ - 1, p_71930_4_) || BlockFence.func_72249_c(p_71930_1_.func_72798_a(p_71930_2_, p_71930_3_ - 1, p_71930_4_));
   }

   public void func_71863_a(World p_71863_1_, int p_71863_2_, int p_71863_3_, int p_71863_4_, int p_71863_5_) {
      boolean var6 = false;
      if(!p_71863_1_.func_72797_t(p_71863_2_, p_71863_3_ - 1, p_71863_4_) && !BlockFence.func_72249_c(p_71863_1_.func_72798_a(p_71863_2_, p_71863_3_ - 1, p_71863_4_))) {
         var6 = true;
      }

      if(var6) {
         this.func_71897_c(p_71863_1_, p_71863_2_, p_71863_3_, p_71863_4_, p_71863_1_.func_72805_g(p_71863_2_, p_71863_3_, p_71863_4_), 0);
         p_71863_1_.func_72859_e(p_71863_2_, p_71863_3_, p_71863_4_, 0);
      }

   }

   public void func_71847_b(World p_71847_1_, int p_71847_2_, int p_71847_3_, int p_71847_4_, Random p_71847_5_) {
      if(!p_71847_1_.field_72995_K) {
         if(p_71847_1_.func_72805_g(p_71847_2_, p_71847_3_, p_71847_4_) != 0) {
            this.func_72193_l(p_71847_1_, p_71847_2_, p_71847_3_, p_71847_4_);
         }
      }
   }

   public void func_71869_a(World p_71869_1_, int p_71869_2_, int p_71869_3_, int p_71869_4_, Entity p_71869_5_) {
      if(!p_71869_1_.field_72995_K) {
         if(p_71869_1_.func_72805_g(p_71869_2_, p_71869_3_, p_71869_4_) != 1) {
            this.func_72193_l(p_71869_1_, p_71869_2_, p_71869_3_, p_71869_4_);
         }
      }
   }

   private void func_72193_l(World p_72193_1_, int p_72193_2_, int p_72193_3_, int p_72193_4_) {
      boolean var5 = p_72193_1_.func_72805_g(p_72193_2_, p_72193_3_, p_72193_4_) == 1;
      boolean var6 = false;
      float var7 = 0.125F;
      List var8 = null;
      if(this.field_72194_a == EnumMobType.everything) {
         var8 = p_72193_1_.func_72839_b((Entity)null, AxisAlignedBB.func_72332_a().func_72299_a((double)((float)p_72193_2_ + var7), (double)p_72193_3_, (double)((float)p_72193_4_ + var7), (double)((float)(p_72193_2_ + 1) - var7), (double)p_72193_3_ + 0.25D, (double)((float)(p_72193_4_ + 1) - var7)));
      }

      if(this.field_72194_a == EnumMobType.mobs) {
         var8 = p_72193_1_.func_72872_a(EntityLiving.class, AxisAlignedBB.func_72332_a().func_72299_a((double)((float)p_72193_2_ + var7), (double)p_72193_3_, (double)((float)p_72193_4_ + var7), (double)((float)(p_72193_2_ + 1) - var7), (double)p_72193_3_ + 0.25D, (double)((float)(p_72193_4_ + 1) - var7)));
      }

      if(this.field_72194_a == EnumMobType.players) {
         var8 = p_72193_1_.func_72872_a(EntityPlayer.class, AxisAlignedBB.func_72332_a().func_72299_a((double)((float)p_72193_2_ + var7), (double)p_72193_3_, (double)((float)p_72193_4_ + var7), (double)((float)(p_72193_2_ + 1) - var7), (double)p_72193_3_ + 0.25D, (double)((float)(p_72193_4_ + 1) - var7)));
      }

      if(!var8.isEmpty()) {
         Iterator var9 = var8.iterator();

         while(var9.hasNext()) {
            Entity var10 = (Entity)var9.next();
            if(!var10.func_82144_au()) {
               var6 = true;
               break;
            }
         }
      }

      if(var6 && !var5) {
         p_72193_1_.func_72921_c(p_72193_2_, p_72193_3_, p_72193_4_, 1);
         p_72193_1_.func_72898_h(p_72193_2_, p_72193_3_, p_72193_4_, this.field_71990_ca);
         p_72193_1_.func_72898_h(p_72193_2_, p_72193_3_ - 1, p_72193_4_, this.field_71990_ca);
         p_72193_1_.func_72909_d(p_72193_2_, p_72193_3_, p_72193_4_, p_72193_2_, p_72193_3_, p_72193_4_);
         p_72193_1_.func_72908_a((double)p_72193_2_ + 0.5D, (double)p_72193_3_ + 0.1D, (double)p_72193_4_ + 0.5D, "random.click", 0.3F, 0.6F);
      }

      if(!var6 && var5) {
         p_72193_1_.func_72921_c(p_72193_2_, p_72193_3_, p_72193_4_, 0);
         p_72193_1_.func_72898_h(p_72193_2_, p_72193_3_, p_72193_4_, this.field_71990_ca);
         p_72193_1_.func_72898_h(p_72193_2_, p_72193_3_ - 1, p_72193_4_, this.field_71990_ca);
         p_72193_1_.func_72909_d(p_72193_2_, p_72193_3_, p_72193_4_, p_72193_2_, p_72193_3_, p_72193_4_);
         p_72193_1_.func_72908_a((double)p_72193_2_ + 0.5D, (double)p_72193_3_ + 0.1D, (double)p_72193_4_ + 0.5D, "random.click", 0.3F, 0.5F);
      }

      if(var6) {
         p_72193_1_.func_72836_a(p_72193_2_, p_72193_3_, p_72193_4_, this.field_71990_ca, this.func_71859_p_());
      }

   }

   public void func_71852_a(World p_71852_1_, int p_71852_2_, int p_71852_3_, int p_71852_4_, int p_71852_5_, int p_71852_6_) {
      if(p_71852_6_ > 0) {
         p_71852_1_.func_72898_h(p_71852_2_, p_71852_3_, p_71852_4_, this.field_71990_ca);
         p_71852_1_.func_72898_h(p_71852_2_, p_71852_3_ - 1, p_71852_4_, this.field_71990_ca);
      }

      super.func_71852_a(p_71852_1_, p_71852_2_, p_71852_3_, p_71852_4_, p_71852_5_, p_71852_6_);
   }

   public void func_71902_a(IBlockAccess p_71902_1_, int p_71902_2_, int p_71902_3_, int p_71902_4_) {
      boolean var5 = p_71902_1_.func_72805_g(p_71902_2_, p_71902_3_, p_71902_4_) == 1;
      float var6 = 0.0625F;
      if(var5) {
         this.func_71905_a(var6, 0.0F, var6, 1.0F - var6, 0.03125F, 1.0F - var6);
      } else {
         this.func_71905_a(var6, 0.0F, var6, 1.0F - var6, 0.0625F, 1.0F - var6);
      }

   }

   public boolean func_71865_a(IBlockAccess p_71865_1_, int p_71865_2_, int p_71865_3_, int p_71865_4_, int p_71865_5_) {
      return p_71865_1_.func_72805_g(p_71865_2_, p_71865_3_, p_71865_4_) > 0;
   }

   public boolean func_71855_c(IBlockAccess p_71855_1_, int p_71855_2_, int p_71855_3_, int p_71855_4_, int p_71855_5_) {
      return p_71855_1_.func_72805_g(p_71855_2_, p_71855_3_, p_71855_4_) == 0?false:p_71855_5_ == 1;
   }

   public boolean func_71853_i() {
      return true;
   }

   public void func_71919_f() {
      float var1 = 0.5F;
      float var2 = 0.125F;
      float var3 = 0.5F;
      this.func_71905_a(0.5F - var1, 0.5F - var2, 0.5F - var3, 0.5F + var1, 0.5F + var2, 0.5F + var3);
   }

   public int func_71915_e() {
      return 1;
   }
}
