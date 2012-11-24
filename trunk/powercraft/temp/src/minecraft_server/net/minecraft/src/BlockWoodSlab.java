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

public class BlockWoodSlab extends BlockHalfSlab {

   public static final String[] field_72243_a = new String[]{"oak", "spruce", "birch", "jungle"};


   public BlockWoodSlab(int p_i4022_1_, boolean p_i4022_2_) {
      super(p_i4022_1_, p_i4022_2_, Material.field_76245_d);
      this.func_71849_a(CreativeTabs.field_78030_b);
   }

   public int func_71858_a(int p_71858_1_, int p_71858_2_) {
      switch(p_71858_2_ & 7) {
      case 1:
         return 198;
      case 2:
         return 214;
      case 3:
         return 199;
      default:
         return 4;
      }
   }

   public int func_71851_a(int p_71851_1_) {
      return this.func_71858_a(p_71851_1_, 0);
   }

   public int func_71885_a(int p_71885_1_, Random p_71885_2_, int p_71885_3_) {
      return Block.field_72092_bO.field_71990_ca;
   }

   protected ItemStack func_71880_c_(int p_71880_1_) {
      return new ItemStack(Block.field_72092_bO.field_71990_ca, 2, p_71880_1_ & 7);
   }

   public String func_72240_d(int p_72240_1_) {
      if(p_72240_1_ < 0 || p_72240_1_ >= field_72243_a.length) {
         p_72240_1_ = 0;
      }

      return super.func_71917_a() + "." + field_72243_a[p_72240_1_];
   }

   @SideOnly(Side.CLIENT)
   public void func_71879_a(int p_71879_1_, CreativeTabs p_71879_2_, List p_71879_3_) {
      if(p_71879_1_ != Block.field_72090_bN.field_71990_ca) {
         for(int var4 = 0; var4 < 4; ++var4) {
            p_71879_3_.add(new ItemStack(p_71879_1_, 1, var4));
         }

      }
   }

}
