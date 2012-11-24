package net.minecraft.src;

import net.minecraft.src.BehaviorPotionDispense;
import net.minecraft.src.BehaviorProjectileDispense;
import net.minecraft.src.EntityPotion;
import net.minecraft.src.IPosition;
import net.minecraft.src.IProjectile;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

class BehaviorPotionDispenseLogic extends BehaviorProjectileDispense {

   // $FF: synthetic field
   final ItemStack field_82501_b;
   // $FF: synthetic field
   final BehaviorPotionDispense field_82502_c;


   BehaviorPotionDispenseLogic(BehaviorPotionDispense p_i5007_1_, ItemStack p_i5007_2_) {
      this.field_82502_c = p_i5007_1_;
      this.field_82501_b = p_i5007_2_;
   }

   protected IProjectile func_82499_a(World p_82499_1_, IPosition p_82499_2_) {
      return new EntityPotion(p_82499_1_, p_82499_2_.func_82615_a(), p_82499_2_.func_82617_b(), p_82499_2_.func_82616_c(), this.field_82501_b.func_77946_l());
   }

   protected float func_82498_a() {
      return super.func_82498_a() * 0.5F;
   }

   protected float func_82500_b() {
      return super.func_82500_b() * 1.25F;
   }
}
