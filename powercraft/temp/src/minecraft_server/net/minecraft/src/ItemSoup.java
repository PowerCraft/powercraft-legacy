package net.minecraft.src;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemSoup extends ItemFood {

   public ItemSoup(int p_i3624_1_, int p_i3624_2_) {
      super(p_i3624_1_, p_i3624_2_, false);
      this.func_77625_d(1);
   }

   public ItemStack func_77654_b(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_) {
      super.func_77654_b(p_77654_1_, p_77654_2_, p_77654_3_);
      return new ItemStack(Item.field_77670_E);
   }
}
