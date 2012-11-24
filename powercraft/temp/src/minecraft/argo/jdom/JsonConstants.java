package argo.jdom;

import argo.jdom.JsonNode;
import argo.jdom.JsonNodeType;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
final class JsonConstants extends JsonNode {

   static final JsonConstants field_74625_a = new JsonConstants(JsonNodeType.NULL);
   static final JsonConstants field_74623_b = new JsonConstants(JsonNodeType.TRUE);
   static final JsonConstants field_74624_c = new JsonConstants(JsonNodeType.FALSE);
   private final JsonNodeType field_74622_d;


   private JsonConstants(JsonNodeType p_i3224_1_) {
      this.field_74622_d = p_i3224_1_;
   }

   public JsonNodeType func_74616_a() {
      return this.field_74622_d;
   }

   public String getText() {
      throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
   }

   public Map getFields() {
      throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
   }

   public List func_74610_b() {
      throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
   }

}
