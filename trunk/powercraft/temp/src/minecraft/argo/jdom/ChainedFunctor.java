package argo.jdom;

import argo.jdom.Functor;
import argo.jdom.JsonNodeDoesNotMatchChainedJsonNodeSelectorException;
import argo.jdom.JsonNodeSelector;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
final class ChainedFunctor implements Functor {

   private final JsonNodeSelector field_74632_a;
   private final JsonNodeSelector field_74631_b;


   ChainedFunctor(JsonNodeSelector p_i3221_1_, JsonNodeSelector p_i3221_2_) {
      this.field_74632_a = p_i3221_1_;
      this.field_74631_b = p_i3221_2_;
   }

   public boolean func_74630_a(Object p_74630_1_) {
      return this.field_74632_a.func_74688_a(p_74630_1_) && this.field_74631_b.func_74688_a(this.field_74632_a.func_74687_b(p_74630_1_));
   }

   public Object func_74629_b(Object p_74629_1_) {
      Object var2;
      try {
         var2 = this.field_74632_a.func_74687_b(p_74629_1_);
      } catch (JsonNodeDoesNotMatchChainedJsonNodeSelectorException var6) {
         throw JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_74698_b(var6, this.field_74632_a);
      }

      try {
         Object var3 = this.field_74631_b.func_74687_b(var2);
         return var3;
      } catch (JsonNodeDoesNotMatchChainedJsonNodeSelectorException var5) {
         throw JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_74699_a(var5, this.field_74632_a);
      }
   }

   public String func_74628_a() {
      return this.field_74631_b.func_74685_a();
   }

   public String toString() {
      return this.field_74632_a.toString() + ", with " + this.field_74631_b.toString();
   }
}
