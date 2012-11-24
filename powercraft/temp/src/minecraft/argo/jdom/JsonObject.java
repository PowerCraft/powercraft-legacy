package argo.jdom;

import argo.jdom.JsonNodeType;
import argo.jdom.JsonRootNode;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
final class JsonObject extends JsonRootNode {

   private final Map field_74617_a;


   JsonObject(Map p_i3237_1_) {
      this.field_74617_a = new HashMap(p_i3237_1_);
   }

   public Map getFields() {
      return new HashMap(this.field_74617_a);
   }

   public JsonNodeType func_74616_a() {
      return JsonNodeType.OBJECT;
   }

   public String getText() {
      throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
   }

   public List func_74610_b() {
      throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
   }

   public boolean equals(Object p_equals_1_) {
      if(this == p_equals_1_) {
         return true;
      } else if(p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
         JsonObject var2 = (JsonObject)p_equals_1_;
         return this.field_74617_a.equals(var2.field_74617_a);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.field_74617_a.hashCode();
   }

   public String toString() {
      return "JsonObject fields:[" + this.field_74617_a + "]";
   }
}
