package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockPane extends Block {

   private int field_72164_a;
   private final boolean field_72163_b;


   protected BlockPane(int p_i4005_1_, int p_i4005_2_, int p_i4005_3_, Material p_i4005_4_, boolean p_i4005_5_) {
      super(p_i4005_1_, p_i4005_2_, p_i4005_4_);
      this.field_72164_a = p_i4005_3_;
      this.field_72163_b = p_i4005_5_;
      this.func_71849_a(CreativeTabs.field_78031_c);
   }

   public int func_71885_a(int p_71885_1_, Random p_71885_2_, int p_71885_3_) {
      return !this.field_72163_b?0:super.func_71885_a(p_71885_1_, p_71885_2_, p_71885_3_);
   }

   public boolean func_71926_d() {
      return false;
   }

   public boolean func_71886_c() {
      return false;
   }

   public int func_71857_b() {
      return 18;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_71877_c(IBlockAccess p_71877_1_, int p_71877_2_, int p_71877_3_, int p_71877_4_, int p_71877_5_) {
      int var6 = p_71877_1_.func_72798_a(p_71877_2_, p_71877_3_, p_71877_4_);
      return var6 == this.field_71990_ca?false:super.func_71877_c(p_71877_1_, p_71877_2_, p_71877_3_, p_71877_4_, p_71877_5_);
   }

   public void func_71871_a(World p_71871_1_, int p_71871_2_, int p_71871_3_, int p_71871_4_, AxisAlignedBB p_71871_5_, List p_71871_6_, Entity p_71871_7_) {
      boolean var8 = this.func_72161_e(p_71871_1_.func_72798_a(p_71871_2_, p_71871_3_, p_71871_4_ - 1));
      boolean var9 = this.func_72161_e(p_71871_1_.func_72798_a(p_71871_2_, p_71871_3_, p_71871_4_ + 1));
      boolean var10 = this.func_72161_e(p_71871_1_.func_72798_a(p_71871_2_ - 1, p_71871_3_, p_71871_4_));
      boolean var11 = this.func_72161_e(p_71871_1_.func_72798_a(p_71871_2_ + 1, p_71871_3_, p_71871_4_));
      if((!var10 || !var11) && (var10 || var11 || var8 || var9)) {
         if(var10 && !var11) {
            this.func_71905_a(0.0F, 0.0F, 0.4375F, 0.5F, 1.0F, 0.5625F);
            super.func_71871_a(p_71871_1_, p_71871_2_, p_71871_3_, p_71871_4_, p_71871_5_, p_71871_6_, p_71871_7_);
         } else if(!var10 && var11) {
            this.func_71905_a(0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
            super.func_71871_a(p_71871_1_, p_71871_2_, p_71871_3_, p_71871_4_, p_71871_5_, p_71871_6_, p_71871_7_);
         }
      } else {
         this.func_71905_a(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
         super.func_71871_a(p_71871_1_, p_71871_2_, p_71871_3_, p_71871_4_, p_71871_5_, p_71871_6_, p_71871_7_);
      }

      if((!var8 || !var9) && (var10 || var11 || var8 || var9)) {
         if(var8 && !var9) {
            this.func_71905_a(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F);
            super.func_71871_a(p_71871_1_, p_71871_2_, p_71871_3_, p_71871_4_, p_71871_5_, p_71871_6_, p_71871_7_);
         } else if(!var8 && var9) {
            this.func_71905_a(0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F);
            super.func_71871_a(p_71871_1_, p_71871_2_, p_71871_3_, p_71871_4_, p_71871_5_, p_71871_6_, p_71871_7_);
         }
      } else {
         this.func_71905_a(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
         super.func_71871_a(p_71871_1_, p_71871_2_, p_71871_3_, p_71871_4_, p_71871_5_, p_71871_6_, p_71871_7_);
      }

   }

   public void func_71919_f() {
      this.func_71905_a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public void func_71902_a(IBlockAccess p_71902_1_, int p_71902_2_, int p_71902_3_, int p_71902_4_) {
      float var5 = 0.4375F;
      float var6 = 0.5625F;
      float var7 = 0.4375F;
      float var8 = 0.5625F;
      boolean var9 = this.func_72161_e(p_71902_1_.func_72798_a(p_71902_2_, p_71902_3_, p_71902_4_ - 1));
      boolean var10 = this.func_72161_e(p_71902_1_.func_72798_a(p_71902_2_, p_71902_3_, p_71902_4_ + 1));
      boolean var11 = this.func_72161_e(p_71902_1_.func_72798_a(p_71902_2_ - 1, p_71902_3_, p_71902_4_));
      boolean var12 = this.func_72161_e(p_71902_1_.func_72798_a(p_71902_2_ + 1, p_71902_3_, p_71902_4_));
      if((!var11 || !var12) && (var11 || var12 || var9 || var10)) {
         if(var11 && !var12) {
            var5 = 0.0F;
         } else if(!var11 && var12) {
            var6 = 1.0F;
         }
      } else {
         var5 = 0.0F;
         var6 = 1.0F;
      }

      if((!var9 || !var10) && (var11 || var12 || var9 || var10)) {
         if(var9 && !var10) {
            var7 = 0.0F;
         } else if(!var9 && var10) {
            var8 = 1.0F;
         }
      } else {
         var7 = 0.0F;
         var8 = 1.0F;
      }

      this.func_71905_a(var5, 0.0F, var7, var6, 1.0F, var8);
   }

   @SideOnly(Side.CLIENT)
   public int func_72162_n() {
      return this.field_72164_a;
   }

   public final boolean func_72161_e(int p_72161_1_) {
      return Block.field_71970_n[p_72161_1_] || p_72161_1_ == this.field_71990_ca || p_72161_1_ == Block.field_71946_M.field_71990_ca;
   }

   protected boolean func_71906_q_() {
      return true;
   }

   protected ItemStack func_71880_c_(int p_71880_1_) {
      return new ItemStack(this.field_71990_ca, 1, p_71880_1_);
   }
}
