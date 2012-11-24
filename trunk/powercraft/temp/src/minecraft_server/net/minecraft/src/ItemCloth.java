package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Block;
import net.minecraft.src.BlockCloth;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemDye;
import net.minecraft.src.ItemStack;

public class ItemCloth extends ItemBlock {

   public ItemCloth(int p_i3626_1_) {
      super(p_i3626_1_);
      this.func_77656_e(0);
      this.func_77627_a(true);
   }

   @SideOnly(Side.CLIENT)
   public int func_77617_a(int p_77617_1_) {
      return Block.field_72101_ab.func_71858_a(2, BlockCloth.func_72238_e_(p_77617_1_));
   }

   public int func_77647_b(int p_77647_1_) {
      return p_77647_1_;
   }

   public String func_77667_c(ItemStack p_77667_1_) {
      return super.func_77658_a() + "." + ItemDye.field_77860_a[BlockCloth.func_72238_e_(p_77667_1_.func_77960_j())];
   }
}
