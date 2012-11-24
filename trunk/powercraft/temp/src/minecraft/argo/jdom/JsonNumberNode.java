package argo.jdom;

import argo.jdom.JsonNode;
import argo.jdom.JsonNodeType;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@SideOnly(Side.CLIENT)
final class JsonNumberNode extends JsonNode {

   private static final Pattern field_74621_a = Pattern.compile("(-?)(0|([1-9]([0-9]*)))(\\.[0-9]+)?((e|E)(\\+|-)?[0-9]+)?");
   private final String field_74620_b;


   JsonNumberNode(String p_i3235_1_) {
      if(p_i3235_1_ == null) {
         throw new NullPointerException("Attempt to construct a JsonNumber with a null value.");
      } else if(!field_74621_a.matcher(p_i3235_1_).matches()) {
         throw new IllegalArgumentException("Attempt to construct a JsonNumber with a String [" + p_i3235_1_ + "] that does not match the JSON number specification.");
      } else {
         this.field_74620_b = p_i3235_1_;
      }
   }

   public JsonNodeType func_74616_a() {
      return JsonNodeType.NUMBER;
   }

   public String getText() {
      return this.field_74620_b;
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
         JsonNumberNode var2 = (JsonNumberNode)p_equals_1_;
         return this.field_74620_b.equals(var2.field_74620_b);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.field_74620_b.hashCode();
   }

   public String toString() {
      return "JsonNumberNode value:[" + this.field_74620_b + "]";
   }

}
