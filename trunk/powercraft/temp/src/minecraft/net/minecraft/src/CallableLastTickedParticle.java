package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.concurrent.Callable;
import net.minecraft.src.EffectRenderer;
import net.minecraft.src.EntityFX;

@SideOnly(Side.CLIENT)
class CallableLastTickedParticle implements Callable {

   // $FF: synthetic field
   final EntityFX field_90041_a;
   // $FF: synthetic field
   final EffectRenderer field_90040_b;


   CallableLastTickedParticle(EffectRenderer p_i7003_1_, EntityFX p_i7003_2_) {
      this.field_90040_b = p_i7003_1_;
      this.field_90041_a = p_i7003_2_;
   }

   public String func_90039_a() {
      return this.field_90041_a != null?this.field_90041_a.toString():null;
   }

   // $FF: synthetic method
   public Object call() {
      return this.func_90039_a();
   }
}
