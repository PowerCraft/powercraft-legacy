package net.minecraft.src;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPig;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class ItemSaddle extends Item {

   public ItemSaddle(int p_i3679_1_) {
      super(p_i3679_1_);
      this.field_77777_bU = 1;
      this.func_77637_a(CreativeTabs.field_78029_e);
   }

   public boolean func_77646_a(ItemStack p_77646_1_, EntityLiving p_77646_2_) {
      if(p_77646_2_ instanceof EntityPig) {
         EntityPig var3 = (EntityPig)p_77646_2_;
         if(!var3.func_70901_n() && !var3.func_70631_g_()) {
            var3.func_70900_e(true);
            --p_77646_1_.field_77994_a;
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean func_77644_a(ItemStack p_77644_1_, EntityLiving p_77644_2_, EntityLiving p_77644_3_) {
      this.func_77646_a(p_77644_1_, p_77644_2_);
      return true;
   }
}
