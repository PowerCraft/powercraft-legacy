package net.minecraft.src;

import net.minecraft.src.Enchantment;
import net.minecraft.src.WeightedRandomItem;

public class EnchantmentData extends WeightedRandomItem {

   public final Enchantment field_76302_b;
   public final int field_76303_c;


   public EnchantmentData(Enchantment p_i3713_1_, int p_i3713_2_) {
      super(p_i3713_1_.func_77324_c());
      this.field_76302_b = p_i3713_1_;
      this.field_76303_c = p_i3713_2_;
   }
}
