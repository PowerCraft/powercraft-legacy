package argo.jdom;

import argo.jdom.JsonNode;
import argo.jdom.JsonNodeType;
import argo.jdom.LeafFunctor;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
final class JsonNodeSelectors_String extends LeafFunctor {

   public boolean func_74645_a(JsonNode p_74645_1_) {
      return JsonNodeType.STRING == p_74645_1_.func_74616_a();
   }

   public String func_74628_a() {
      return "A short form string";
   }

   public String func_74644_b(JsonNode p_74644_1_) {
      return p_74644_1_.getText();
   }

   public String toString() {
      return "a value that is a string";
   }

   // $FF: synthetic method
   public Object func_74633_c(Object p_74633_1_) {
      return this.func_74644_b((JsonNode)p_74633_1_);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean func_74630_a(Object p_74630_1_) {
      return this.func_74645_a((JsonNode)p_74630_1_);
   }
}
