package net.minecraft.src;

import net.minecraft.src.ContainerRepair;
import net.minecraft.src.InventoryBasic;

class InventoryRepair extends InventoryBasic {

   // $FF: synthetic field
   final ContainerRepair field_82346_a;


   InventoryRepair(ContainerRepair p_i5078_1_, String p_i5078_2_, int p_i5078_3_) {
      super(p_i5078_2_, p_i5078_3_);
      this.field_82346_a = p_i5078_1_;
   }

   public void func_70296_d() {
      super.func_70296_d();
      this.field_82346_a.func_75130_a(this);
   }
}
