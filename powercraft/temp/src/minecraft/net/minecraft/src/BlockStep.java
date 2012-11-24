package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.BlockHalfSlab;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;

public class BlockStep extends BlockHalfSlab {

   public static final String[] field_72244_a = new String[]{"stone", "sand", "wood", "cobble", "brick", "smoothStoneBrick"};


   public BlockStep(int p_i4000_1_, boolean p_i4000_2_) {
      super(p_i4000_1_, p_i4000_2_, Material.field_76246_e);
      this.func_71849_a(CreativeTabs.field_78030_b);
   }

   public int func_71858_a(int p_71858_1_, int p_71858_2_) {
      int var3 = p_71858_2_ & 7;
      return var3 == 0?(p_71858_1_ <= 1?6:5):(var3 == 1?(p_71858_1_ == 0?208:(p_71858_1_ == 1?176:192)):(var3 == 2?4:(var3 == 3?16:(var3 == 4?Block.field_72081_al.field_72059_bZ:(var3 == 5?Block.field_72007_bm.field_72059_bZ:6)))));
   }

   public int func_71851_a(int p_71851_1_) {
      return this.func_71858_a(p_71851_1_, 0);
   }

   public int func_71885_a(int p_71885_1_, Random p_71885_2_, int p_71885_3_) {
      return Block.field_72079_ak.field_71990_ca;
   }

   protected ItemStack func_71880_c_(int p_71880_1_) {
      return new ItemStack(Block.field_72079_ak.field_71990_ca, 2, p_71880_1_ & 7);
   }

   public String func_72240_d(int p_72240_1_) {
      if(p_72240_1_ < 0 || p_72240_1_ >= field_72244_a.length) {
         p_72240_1_ = 0;
      }

      return super.func_71917_a() + "." + field_72244_a[p_72240_1_];
   }

   @SideOnly(Side.CLIENT)
   public void func_71879_a(int p_71879_1_, CreativeTabs p_71879_2_, List p_71879_3_) {
      if(p_71879_1_ != Block.field_72085_aj.field_71990_ca) {
         for(int var4 = 0; var4 < 6; ++var4) {
            if(var4 != 2) {
               p_71879_3_.add(new ItemStack(p_71879_1_, 1, var4));
            }
         }

      }
   }

}
