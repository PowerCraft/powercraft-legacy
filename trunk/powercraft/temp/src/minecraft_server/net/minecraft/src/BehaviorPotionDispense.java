package net.minecraft.src;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BehaviorDefaultDispenseItem;
import net.minecraft.src.BehaviorPotionDispenseLogic;
import net.minecraft.src.IBehaviorDispenseItem;
import net.minecraft.src.IBlockSource;
import net.minecraft.src.ItemPotion;
import net.minecraft.src.ItemStack;

public class BehaviorPotionDispense implements IBehaviorDispenseItem {

   private final BehaviorDefaultDispenseItem field_82484_c;
   // $FF: synthetic field
   final MinecraftServer field_74272_a;


   public BehaviorPotionDispense(MinecraftServer p_i5008_1_) {
      this.field_74272_a = p_i5008_1_;
      this.field_82484_c = new BehaviorDefaultDispenseItem();
   }

   public ItemStack func_82482_a(IBlockSource p_82482_1_, ItemStack p_82482_2_) {
      return ItemPotion.func_77831_g(p_82482_2_.func_77960_j())?(new BehaviorPotionDispenseLogic(this, p_82482_2_)).func_82482_a(p_82482_1_, p_82482_2_):this.field_82484_c.func_82482_a(p_82482_1_, p_82482_2_);
   }
}
