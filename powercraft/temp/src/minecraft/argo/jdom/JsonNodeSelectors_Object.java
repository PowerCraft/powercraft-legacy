package argo.jdom;

import argo.jdom.JsonNode;
import argo.jdom.JsonNodeType;
import argo.jdom.LeafFunctor;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Map;

@SideOnly(Side.CLIENT)
final class JsonNodeSelectors_Object extends LeafFunctor {

   public boolean func_74640_a(JsonNode p_74640_1_) {
      return JsonNodeType.OBJECT == p_74640_1_.func_74616_a();
   }

   public String func_74628_a() {
      return "A short form object";
   }

   public Map func_74639_b(JsonNode p_74639_1_) {
      return p_74639_1_.getFields();
   }

   public String toString() {
      return "an object";
   }

   // $FF: synthetic method
   public Object func_74633_c(Object p_74633_1_) {
      return this.func_74639_b((JsonNode)p_74633_1_);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean func_74630_a(Object p_74630_1_) {
      return this.func_74640_a((JsonNode)p_74630_1_);
   }
}
