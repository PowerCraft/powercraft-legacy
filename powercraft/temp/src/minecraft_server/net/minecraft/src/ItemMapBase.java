package net.minecraft.src;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet;
import net.minecraft.src.World;

public class ItemMapBase extends Item {

   protected ItemMapBase(int p_i3629_1_) {
      super(p_i3629_1_);
   }

   public boolean func_77643_m_() {
      return true;
   }

   public Packet func_77871_c(ItemStack p_77871_1_, World p_77871_2_, EntityPlayer p_77871_3_) {
      return null;
   }
}
