package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class ItemSimpleFoiled extends Item {

   public ItemSimpleFoiled(int p_i5087_1_) {
      super(p_i5087_1_);
   }

   @SideOnly(Side.CLIENT)
   public boolean func_77636_d(ItemStack p_77636_1_) {
      return true;
   }
}
