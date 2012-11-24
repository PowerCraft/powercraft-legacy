package net.minecraft.src;

import net.minecraft.src.ContainerEnchantment;
import net.minecraft.src.InventoryBasic;

class SlotEnchantmentTable extends InventoryBasic {

   // $FF: synthetic field
   final ContainerEnchantment field_70484_a;


   SlotEnchantmentTable(ContainerEnchantment p_i3604_1_, String p_i3604_2_, int p_i3604_3_) {
      super(p_i3604_2_, p_i3604_3_);
      this.field_70484_a = p_i3604_1_;
   }

   public int func_70297_j_() {
      return 1;
   }

   public void func_70296_d() {
      super.func_70296_d();
      this.field_70484_a.func_75130_a(this);
   }
}
