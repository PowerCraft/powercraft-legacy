package net.minecraft.src;

import net.minecraft.src.BlockRail;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemMinecart extends Item {

   public int field_77841_a;


   public ItemMinecart(int p_i3670_1_, int p_i3670_2_) {
      super(p_i3670_1_);
      this.field_77777_bU = 1;
      this.field_77841_a = p_i3670_2_;
      this.func_77637_a(CreativeTabs.field_78029_e);
   }

   public boolean func_77648_a(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
      int var11 = p_77648_3_.func_72798_a(p_77648_4_, p_77648_5_, p_77648_6_);
      if(BlockRail.func_72184_d(var11)) {
         if(!p_77648_3_.field_72995_K) {
            p_77648_3_.func_72838_d(new EntityMinecart(p_77648_3_, (double)((float)p_77648_4_ + 0.5F), (double)((float)p_77648_5_ + 0.5F), (double)((float)p_77648_6_ + 0.5F), this.field_77841_a));
         }

         --p_77648_1_.field_77994_a;
         return true;
      } else {
         return false;
      }
   }
}
