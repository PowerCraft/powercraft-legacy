package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockLockedChest extends Block {

   protected BlockLockedChest(int p_i3967_1_) {
      super(p_i3967_1_, Material.field_76245_d);
      this.field_72059_bZ = 26;
   }

   @SideOnly(Side.CLIENT)
   public int func_71895_b(IBlockAccess p_71895_1_, int p_71895_2_, int p_71895_3_, int p_71895_4_, int p_71895_5_) {
      if(p_71895_5_ == 1) {
         return this.field_72059_bZ - 1;
      } else if(p_71895_5_ == 0) {
         return this.field_72059_bZ - 1;
      } else {
         int var6 = p_71895_1_.func_72798_a(p_71895_2_, p_71895_3_, p_71895_4_ - 1);
         int var7 = p_71895_1_.func_72798_a(p_71895_2_, p_71895_3_, p_71895_4_ + 1);
         int var8 = p_71895_1_.func_72798_a(p_71895_2_ - 1, p_71895_3_, p_71895_4_);
         int var9 = p_71895_1_.func_72798_a(p_71895_2_ + 1, p_71895_3_, p_71895_4_);
         byte var10 = 3;
         if(Block.field_71970_n[var6] && !Block.field_71970_n[var7]) {
            var10 = 3;
         }

         if(Block.field_71970_n[var7] && !Block.field_71970_n[var6]) {
            var10 = 2;
         }

         if(Block.field_71970_n[var8] && !Block.field_71970_n[var9]) {
            var10 = 5;
         }

         if(Block.field_71970_n[var9] && !Block.field_71970_n[var8]) {
            var10 = 4;
         }

         return p_71895_5_ == var10?this.field_72059_bZ + 1:this.field_72059_bZ;
      }
   }

   public int func_71851_a(int p_71851_1_) {
      return p_71851_1_ == 1?this.field_72059_bZ - 1:(p_71851_1_ == 0?this.field_72059_bZ - 1:(p_71851_1_ == 3?this.field_72059_bZ + 1:this.field_72059_bZ));
   }

   public boolean func_71930_b(World p_71930_1_, int p_71930_2_, int p_71930_3_, int p_71930_4_) {
      return true;
   }

   public void func_71847_b(World p_71847_1_, int p_71847_2_, int p_71847_3_, int p_71847_4_, Random p_71847_5_) {
      p_71847_1_.func_72859_e(p_71847_2_, p_71847_3_, p_71847_4_, 0);
   }
}
