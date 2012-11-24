package argo.jdom;

import argo.jdom.ChainedFunctor;
import argo.jdom.Functor;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public final class JsonNodeSelector {

   final Functor field_74689_a;


   JsonNodeSelector(Functor p_i3231_1_) {
      this.field_74689_a = p_i3231_1_;
   }

   public boolean func_74688_a(Object p_74688_1_) {
      return this.field_74689_a.func_74630_a(p_74688_1_);
   }

   public Object func_74687_b(Object p_74687_1_) {
      return this.field_74689_a.func_74629_b(p_74687_1_);
   }

   public JsonNodeSelector func_74686_a(JsonNodeSelector p_74686_1_) {
      return new JsonNodeSelector(new ChainedFunctor(this, p_74686_1_));
   }

   String func_74685_a() {
      return this.field_74689_a.func_74628_a();
   }

   public String toString() {
      return this.field_74689_a.toString();
   }
}
