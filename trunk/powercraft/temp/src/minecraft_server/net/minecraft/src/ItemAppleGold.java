package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumRarity;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.World;

public class ItemAppleGold extends ItemFood {

   public ItemAppleGold(int p_i3655_1_, int p_i3655_2_, float p_i3655_3_, boolean p_i3655_4_) {
      super(p_i3655_1_, p_i3655_2_, p_i3655_3_, p_i3655_4_);
      this.func_77627_a(true);
   }

   @SideOnly(Side.CLIENT)
   public boolean func_77636_d(ItemStack p_77636_1_) {
      return p_77636_1_.func_77960_j() > 0;
   }

   @SideOnly(Side.CLIENT)
   public EnumRarity func_77613_e(ItemStack p_77613_1_) {
      return p_77613_1_.func_77960_j() == 0?EnumRarity.rare:EnumRarity.epic;
   }

   protected void func_77849_c(ItemStack p_77849_1_, World p_77849_2_, EntityPlayer p_77849_3_) {
      if(p_77849_1_.func_77960_j() > 0) {
         if(!p_77849_2_.field_72995_K) {
            p_77849_3_.func_70690_d(new PotionEffect(Potion.field_76428_l.field_76415_H, 600, 3));
            p_77849_3_.func_70690_d(new PotionEffect(Potion.field_76429_m.field_76415_H, 6000, 0));
            p_77849_3_.func_70690_d(new PotionEffect(Potion.field_76426_n.field_76415_H, 6000, 0));
         }
      } else {
         super.func_77849_c(p_77849_1_, p_77849_2_, p_77849_3_);
      }

   }

   @SideOnly(Side.CLIENT)
   public void func_77633_a(int p_77633_1_, CreativeTabs p_77633_2_, List p_77633_3_) {
      p_77633_3_.add(new ItemStack(p_77633_1_, 1, 0));
      p_77633_3_.add(new ItemStack(p_77633_1_, 1, 1));
   }
}
