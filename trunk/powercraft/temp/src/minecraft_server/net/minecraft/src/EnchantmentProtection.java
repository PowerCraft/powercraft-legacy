package net.minecraft.src;

import net.minecraft.src.DamageSource;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EnumEnchantmentType;

public class EnchantmentProtection extends Enchantment {

   private static final String[] field_77354_A = new String[]{"all", "fire", "fall", "explosion", "projectile"};
   private static final int[] field_77355_B = new int[]{1, 10, 5, 5, 3};
   private static final int[] field_77357_C = new int[]{11, 8, 6, 8, 6};
   private static final int[] field_77353_D = new int[]{20, 12, 10, 12, 15};
   public final int field_77356_a;


   public EnchantmentProtection(int p_i3718_1_, int p_i3718_2_, int p_i3718_3_) {
      super(p_i3718_1_, p_i3718_2_, EnumEnchantmentType.armor);
      this.field_77356_a = p_i3718_3_;
      if(p_i3718_3_ == 2) {
         this.field_77351_y = EnumEnchantmentType.armor_feet;
      }

   }

   public int func_77321_a(int p_77321_1_) {
      return field_77355_B[this.field_77356_a] + (p_77321_1_ - 1) * field_77357_C[this.field_77356_a];
   }

   public int func_77317_b(int p_77317_1_) {
      return this.func_77321_a(p_77317_1_) + field_77353_D[this.field_77356_a];
   }

   public int func_77325_b() {
      return 4;
   }

   public int func_77318_a(int p_77318_1_, DamageSource p_77318_2_) {
      if(p_77318_2_.func_76357_e()) {
         return 0;
      } else {
         int var3 = (6 + p_77318_1_ * p_77318_1_) / 2;
         return this.field_77356_a == 0?var3:(this.field_77356_a == 1 && p_77318_2_.func_76347_k()?var3:(this.field_77356_a == 2 && p_77318_2_ == DamageSource.field_76379_h?var3 * 2:((this.field_77356_a != 3 || p_77318_2_ != DamageSource.field_76378_k) && p_77318_2_ != DamageSource.field_76375_l?(this.field_77356_a == 4 && p_77318_2_.func_76352_a()?var3:0):var3)));
      }
   }

   public String func_77320_a() {
      return "enchantment.protect." + field_77354_A[this.field_77356_a];
   }

   public boolean func_77326_a(Enchantment p_77326_1_) {
      if(p_77326_1_ instanceof EnchantmentProtection) {
         EnchantmentProtection var2 = (EnchantmentProtection)p_77326_1_;
         return var2.field_77356_a == this.field_77356_a?false:this.field_77356_a == 2 || var2.field_77356_a == 2;
      } else {
         return super.func_77326_a(p_77326_1_);
      }
   }

}
