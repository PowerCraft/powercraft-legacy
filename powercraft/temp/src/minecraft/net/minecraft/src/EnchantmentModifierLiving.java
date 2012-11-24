package net.minecraft.src;

import net.minecraft.src.Empty3;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IEnchantmentModifier;

final class EnchantmentModifierLiving implements IEnchantmentModifier {

   public int field_77495_a;
   public EntityLiving field_77494_b;


   private EnchantmentModifierLiving() {}

   public void func_77493_a(Enchantment p_77493_1_, int p_77493_2_) {
      this.field_77495_a += p_77493_1_.func_77323_a(p_77493_2_, this.field_77494_b);
   }

   // $FF: synthetic method
   EnchantmentModifierLiving(Empty3 p_i3711_1_) {
      this();
   }
}
