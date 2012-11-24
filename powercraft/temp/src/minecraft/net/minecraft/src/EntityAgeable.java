package net.minecraft.src;

import net.minecraft.src.EntityCreature;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public abstract class EntityAgeable extends EntityCreature {

   public EntityAgeable(World p_i3436_1_) {
      super(p_i3436_1_);
   }

   public abstract EntityAgeable func_90011_a(EntityAgeable var1);

   public boolean func_70085_c(EntityPlayer p_70085_1_) {
      ItemStack var2 = p_70085_1_.field_71071_by.func_70448_g();
      if(var2 != null && var2.field_77993_c == Item.field_77815_bC.field_77779_bT && !this.field_70170_p.field_72995_K) {
         Class var3 = EntityList.func_90035_a(var2.func_77960_j());
         if(var3 != null && var3.isAssignableFrom(this.getClass())) {
            EntityAgeable var4 = this.func_90011_a(this);
            if(var4 != null) {
               var4.func_70873_a(-24000);
               var4.func_70012_b(this.field_70165_t, this.field_70163_u, this.field_70161_v, 0.0F, 0.0F);
               this.field_70170_p.func_72838_d(var4);
            }
         }
      }

      return super.func_70085_c(p_70085_1_);
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.field_70180_af.func_75682_a(12, new Integer(0));
   }

   public int func_70874_b() {
      return this.field_70180_af.func_75679_c(12);
   }

   public void func_70873_a(int p_70873_1_) {
      this.field_70180_af.func_75692_b(12, Integer.valueOf(p_70873_1_));
   }

   public void func_70014_b(NBTTagCompound p_70014_1_) {
      super.func_70014_b(p_70014_1_);
      p_70014_1_.func_74768_a("Age", this.func_70874_b());
   }

   public void func_70037_a(NBTTagCompound p_70037_1_) {
      super.func_70037_a(p_70037_1_);
      this.func_70873_a(p_70037_1_.func_74762_e("Age"));
   }

   public void func_70636_d() {
      super.func_70636_d();
      int var1 = this.func_70874_b();
      if(var1 < 0) {
         ++var1;
         this.func_70873_a(var1);
      } else if(var1 > 0) {
         --var1;
         this.func_70873_a(var1);
      }

   }

   public boolean func_70631_g_() {
      return this.func_70874_b() < 0;
   }
}
