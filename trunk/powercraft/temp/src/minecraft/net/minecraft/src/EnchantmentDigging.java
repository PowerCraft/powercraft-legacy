package net.minecraft.src;

import net.minecraft.src.Enchantment;
import net.minecraft.src.EnumEnchantmentType;

public class EnchantmentDigging extends Enchantment {

   protected EnchantmentDigging(int p_i3708_1_, int p_i3708_2_) {
      super(p_i3708_1_, p_i3708_2_, EnumEnchantmentType.digger);
      this.func_77322_b("digging");
   }

   public int func_77321_a(int p_77321_1_) {
      return 1 + 10 * (p_77321_1_ - 1);
   }

   public int func_77317_b(int p_77317_1_) {
      return super.func_77321_a(p_77317_1_) + 50;
   }

   public int func_77325_b() {
      return 5;
   }
}
