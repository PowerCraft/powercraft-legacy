package argo.jdom;

import argo.jdom.JsonNode;
import argo.jdom.JsonNodeType;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public final class JsonStringNode extends JsonNode implements Comparable {

   private final String field_74627_a;


   JsonStringNode(String p_i3239_1_) {
      if(p_i3239_1_ == null) {
         throw new NullPointerException("Attempt to construct a JsonString with a null value.");
      } else {
         this.field_74627_a = p_i3239_1_;
      }
   }

   public JsonNodeType func_74616_a() {
      return JsonNodeType.STRING;
   }

   public String getText() {
      return this.field_74627_a;
   }

   public Map getFields() {
      throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
   }

   public List func_74610_b() {
      throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
   }

   public boolean equals(Object p_equals_1_) {
      if(this == p_equals_1_) {
         return true;
      } else if(p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
         JsonStringNode var2 = (JsonStringNode)p_equals_1_;
         return this.field_74627_a.equals(var2.field_74627_a);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.field_74627_a.hashCode();
   }

   public String toString() {
      return "JsonStringNode value:[" + this.field_74627_a + "]";
   }

   public int func_74626_a(JsonStringNode p_74626_1_) {
      return this.field_74627_a.compareTo(p_74626_1_.field_74627_a);
   }

   // $FF: synthetic method
   public int compareTo(Object p_compareTo_1_) {
      return this.func_74626_a((JsonStringNode)p_compareTo_1_);
   }
}
