package argo.jdom;

import argo.jdom.JsonNode;
import argo.jdom.JsonNodeType;
import argo.jdom.LeafFunctor;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;

@SideOnly(Side.CLIENT)
final class JsonNodeSelectors_Array extends LeafFunctor {

   public boolean func_74638_a(JsonNode p_74638_1_) {
      return JsonNodeType.ARRAY == p_74638_1_.func_74616_a();
   }

   public String func_74628_a() {
      return "A short form array";
   }

   public List func_74637_b(JsonNode p_74637_1_) {
      return p_74637_1_.func_74610_b();
   }

   public String toString() {
      return "an array";
   }

   // $FF: synthetic method
   public Object func_74633_c(Object p_74633_1_) {
      return this.func_74637_b((JsonNode)p_74633_1_);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean func_74630_a(Object p_74630_1_) {
      return this.func_74638_a((JsonNode)p_74630_1_);
   }
}
