package argo.jdom;

import argo.jdom.JsonNode;
import argo.jdom.LeafFunctor;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;

@SideOnly(Side.CLIENT)
final class JsonNodeSelectors_Element extends LeafFunctor {

   // $FF: synthetic field
   final int field_74636_a;


   JsonNodeSelectors_Element(int p_i3233_1_) {
      this.field_74636_a = p_i3233_1_;
   }

   public boolean func_74634_a(List p_74634_1_) {
      return p_74634_1_.size() > this.field_74636_a;
   }

   public String func_74628_a() {
      return Integer.toString(this.field_74636_a);
   }

   public JsonNode func_74635_b(List p_74635_1_) {
      return (JsonNode)p_74635_1_.get(this.field_74636_a);
   }

   public String toString() {
      return "an element at index [" + this.field_74636_a + "]";
   }

   // $FF: synthetic method
   public Object func_74633_c(Object p_74633_1_) {
      return this.func_74635_b((List)p_74633_1_);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean func_74630_a(Object p_74630_1_) {
      return this.func_74634_a((List)p_74630_1_);
   }
}
