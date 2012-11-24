package net.minecraft.src;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityDamageSource;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.StatCollector;

public class EntityDamageSourceIndirect extends EntityDamageSource {

   private Entity field_76387_p;


   public EntityDamageSourceIndirect(String p_i3431_1_, Entity p_i3431_2_, Entity p_i3431_3_) {
      super(p_i3431_1_, p_i3431_2_);
      this.field_76387_p = p_i3431_3_;
   }

   public Entity func_76364_f() {
      return this.field_76386_o;
   }

   public Entity func_76346_g() {
      return this.field_76387_p;
   }

   public String func_76360_b(EntityPlayer p_76360_1_) {
      return StatCollector.func_74837_a("death." + this.field_76373_n, new Object[]{p_76360_1_.field_71092_bJ, this.field_76387_p == null?this.field_76386_o.func_70023_ak():this.field_76387_p.func_70023_ak()});
   }
}
