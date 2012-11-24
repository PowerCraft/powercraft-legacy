package argo.jdom;

import argo.jdom.JsonNode;
import argo.jdom.JsonStringNode;
import argo.jdom.LeafFunctor;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Map;

@SideOnly(Side.CLIENT)
final class JsonNodeSelectors_Field extends LeafFunctor {

   // $FF: synthetic field
   final JsonStringNode field_74643_a;


   JsonNodeSelectors_Field(JsonStringNode p_i3232_1_) {
      this.field_74643_a = p_i3232_1_;
   }

   public boolean func_74641_a(Map p_74641_1_) {
      return p_74641_1_.containsKey(this.field_74643_a);
   }

   public String func_74628_a() {
      return "\"" + this.field_74643_a.getText() + "\"";
   }

   public JsonNode func_74642_b(Map p_74642_1_) {
      return (JsonNode)p_74642_1_.get(this.field_74643_a);
   }

   public String toString() {
      return "a field called [\"" + this.field_74643_a.getText() + "\"]";
   }

   // $FF: synthetic method
   public Object func_74633_c(Object p_74633_1_) {
      return this.func_74642_b((Map)p_74633_1_);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean func_74630_a(Object p_74630_1_) {
      return this.func_74641_a((Map)p_74630_1_);
   }
}
