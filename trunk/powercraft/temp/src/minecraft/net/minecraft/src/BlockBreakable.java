package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;

public class BlockBreakable extends Block {

   private boolean field_72245_a;


   protected BlockBreakable(int p_i3955_1_, int p_i3955_2_, Material p_i3955_3_, boolean p_i3955_4_) {
      super(p_i3955_1_, p_i3955_2_, p_i3955_3_);
      this.field_72245_a = p_i3955_4_;
   }

   public boolean func_71926_d() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_71877_c(IBlockAccess p_71877_1_, int p_71877_2_, int p_71877_3_, int p_71877_4_, int p_71877_5_) {
      int var6 = p_71877_1_.func_72798_a(p_71877_2_, p_71877_3_, p_71877_4_);
      return !this.field_72245_a && var6 == this.field_71990_ca?false:super.func_71877_c(p_71877_1_, p_71877_2_, p_71877_3_, p_71877_4_, p_71877_5_);
   }
}
