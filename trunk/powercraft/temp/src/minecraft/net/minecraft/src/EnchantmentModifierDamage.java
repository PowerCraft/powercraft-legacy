package net.minecraft.src;

import net.minecraft.src.DamageSource;
import net.minecraft.src.Empty3;
import net.minecraft.src.Enchantment;
import net.minecraft.src.IEnchantmentModifier;

final class EnchantmentModifierDamage implements IEnchantmentModifier {

   public int field_77497_a;
   public DamageSource field_77496_b;


   private EnchantmentModifierDamage() {}

   public void func_77493_a(Enchantment p_77493_1_, int p_77493_2_) {
      this.field_77497_a += p_77493_1_.func_77318_a(p_77493_2_, this.field_77496_b);
   }

   // $FF: synthetic method
   EnchantmentModifierDamage(Empty3 p_i3712_1_) {
      this();
   }
}
