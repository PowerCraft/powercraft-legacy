package net.minecraft.src;

import net.minecraft.src.Enchantment;
import net.minecraft.src.EnumEnchantmentType;

public class EnchantmentArrowInfinite extends Enchantment {

   public EnchantmentArrowInfinite(int p_i3704_1_, int p_i3704_2_) {
      super(p_i3704_1_, p_i3704_2_, EnumEnchantmentType.bow);
      this.func_77322_b("arrowInfinite");
   }

   public int func_77321_a(int p_77321_1_) {
      return 20;
   }

   public int func_77317_b(int p_77317_1_) {
      return 50;
   }

   public int func_77325_b() {
      return 1;
   }
}
