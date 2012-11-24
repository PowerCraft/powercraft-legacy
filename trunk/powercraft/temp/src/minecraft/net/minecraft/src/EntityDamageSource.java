package net.minecraft.src;

import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.StatCollector;

public class EntityDamageSource extends DamageSource {

   protected Entity field_76386_o;


   public EntityDamageSource(String p_i3430_1_, Entity p_i3430_2_) {
      super(p_i3430_1_);
      this.field_76386_o = p_i3430_2_;
   }

   public Entity func_76346_g() {
      return this.field_76386_o;
   }

   public String func_76360_b(EntityPlayer p_76360_1_) {
      return StatCollector.func_74837_a("death." + this.field_76373_n, new Object[]{p_76360_1_.field_71092_bJ, this.field_76386_o.func_70023_ak()});
   }

   public boolean func_76350_n() {
      return this.field_76386_o != null && this.field_76386_o instanceof EntityLiving && !(this.field_76386_o instanceof EntityPlayer);
   }
}
