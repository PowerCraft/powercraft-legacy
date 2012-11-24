package net.minecraft.src;

import net.minecraft.src.Enchantment;
import net.minecraft.src.EnumEnchantmentType;

public class EnchantmentWaterWorker extends Enchantment {

   public EnchantmentWaterWorker(int p_i3720_1_, int p_i3720_2_) {
      super(p_i3720_1_, p_i3720_2_, EnumEnchantmentType.armor_head);
      this.func_77322_b("waterWorker");
   }

   public int func_77321_a(int p_77321_1_) {
      return 1;
   }

   public int func_77317_b(int p_77317_1_) {
      return this.func_77321_a(p_77317_1_) + 40;
   }

   public int func_77325_b() {
      return 1;
   }
}
