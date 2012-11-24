package argo.jdom;

import argo.jdom.JsonArray_NodeList;
import argo.jdom.JsonNodeType;
import argo.jdom.JsonRootNode;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
final class JsonArray extends JsonRootNode {

   private final List field_74619_a;


   JsonArray(Iterable p_i3223_1_) {
      this.field_74619_a = func_74618_a(p_i3223_1_);
   }

   public JsonNodeType func_74616_a() {
      return JsonNodeType.ARRAY;
   }

   public List func_74610_b() {
      return new ArrayList(this.field_74619_a);
   }

   public String getText() {
      throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
   }

   public Map getFields() {
      throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
   }

   public boolean equals(Object p_equals_1_) {
      if(this == p_equals_1_) {
         return true;
      } else if(p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
         JsonArray var2 = (JsonArray)p_equals_1_;
         return this.field_74619_a.equals(var2.field_74619_a);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.field_74619_a.hashCode();
   }

   public String toString() {
      return "JsonArray elements:[" + this.field_74619_a + "]";
   }

   private static List func_74618_a(Iterable p_74618_0_) {
      return new JsonArray_NodeList(p_74618_0_);
   }
}
