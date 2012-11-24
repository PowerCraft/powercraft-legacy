package argo.jdom;

import argo.jdom.Functor;
import argo.jdom.JsonNodeDoesNotMatchChainedJsonNodeSelectorException;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
abstract class LeafFunctor implements Functor {

   public final Object func_74629_b(Object p_74629_1_) {
      if(!this.func_74630_a(p_74629_1_)) {
         throw JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_74701_a(this);
      } else {
         return this.func_74633_c(p_74629_1_);
      }
   }

   protected abstract Object func_74633_c(Object var1);
}
