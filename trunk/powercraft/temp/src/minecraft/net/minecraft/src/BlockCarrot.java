package net.minecraft.src;

import net.minecraft.src.BlockCrops;
import net.minecraft.src.Item;

public class BlockCarrot extends BlockCrops {

   public BlockCarrot(int p_i5101_1_) {
      super(p_i5101_1_, 200);
   }

   public int func_71858_a(int p_71858_1_, int p_71858_2_) {
      if(p_71858_2_ < 7) {
         if(p_71858_2_ == 6) {
            p_71858_2_ = 5;
         }

         return this.field_72059_bZ + (p_71858_2_ >> 1);
      } else {
         return this.field_72059_bZ + 3;
      }
   }

   protected int func_82532_h() {
      return Item.field_82797_bK.field_77779_bT;
   }

   protected int func_82533_j() {
      return Item.field_82797_bK.field_77779_bT;
   }
}
