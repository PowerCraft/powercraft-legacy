package net.minecraft.src;

import net.minecraft.src.Block;
import net.minecraft.src.Material;

public abstract class BlockDirectional extends Block {

   protected BlockDirectional(int p_i3935_1_, int p_i3935_2_, Material p_i3935_3_) {
      super(p_i3935_1_, p_i3935_2_, p_i3935_3_);
   }

   protected BlockDirectional(int p_i3936_1_, Material p_i3936_2_) {
      super(p_i3936_1_, p_i3936_2_);
   }

   public static int func_72217_d(int p_72217_0_) {
      return p_72217_0_ & 3;
   }
}
