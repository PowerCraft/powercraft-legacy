package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPig;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemCarrotOnAStick extends Item {

   public ItemCarrotOnAStick(int p_i5082_1_) {
      super(p_i5082_1_);
      this.func_77637_a(CreativeTabs.field_78029_e);
      this.func_77625_d(1);
      this.func_77656_e(25);
   }

   @SideOnly(Side.CLIENT)
   public boolean func_77662_d() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_77629_n_() {
      return true;
   }

   public ItemStack func_77659_a(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
      if(p_77659_3_.func_70115_ae() && p_77659_3_.field_70154_o instanceof EntityPig) {
         EntityPig var4 = (EntityPig)p_77659_3_.field_70154_o;
         if(var4.func_82183_n().func_82633_h() && p_77659_1_.func_77958_k() - p_77659_1_.func_77960_j() >= 7) {
            var4.func_82183_n().func_82632_g();
            p_77659_1_.func_77972_a(7, p_77659_3_);
            if(p_77659_1_.field_77994_a == 0) {
               return new ItemStack(Item.field_77749_aR);
            }
         }
      }

      return p_77659_1_;
   }
}
