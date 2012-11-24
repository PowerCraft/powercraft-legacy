package net.minecraft.src;

import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public class TileEntityRecordPlayer extends TileEntity {

   public ItemStack field_70417_a;


   public void func_70307_a(NBTTagCompound p_70307_1_) {
      super.func_70307_a(p_70307_1_);
      if(p_70307_1_.func_74764_b("RecordItem")) {
         this.field_70417_a = ItemStack.func_77949_a(p_70307_1_.func_74775_l("RecordItem"));
      } else {
         this.field_70417_a = new ItemStack(p_70307_1_.func_74762_e("Record"), 1, 0);
      }

   }

   public void func_70310_b(NBTTagCompound p_70310_1_) {
      super.func_70310_b(p_70310_1_);
      if(this.field_70417_a != null) {
         p_70310_1_.func_74766_a("RecordItem", this.field_70417_a.func_77955_b(new NBTTagCompound()));
         p_70310_1_.func_74768_a("Record", this.field_70417_a.field_77993_c);
      }

   }
}
