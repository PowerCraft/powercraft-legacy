package argo.jdom;

import argo.jdom.JsonNode;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonStringNode;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
final class JsonFieldBuilder {

   private JsonNodeBuilder field_74729_a;
   private JsonNodeBuilder field_74728_b;


   static JsonFieldBuilder func_74727_a() {
      return new JsonFieldBuilder();
   }

   JsonFieldBuilder func_74726_a(JsonNodeBuilder p_74726_1_) {
      this.field_74729_a = p_74726_1_;
      return this;
   }

   JsonFieldBuilder func_74723_b(JsonNodeBuilder p_74723_1_) {
      this.field_74728_b = p_74723_1_;
      return this;
   }

   JsonStringNode func_74724_b() {
      return (JsonStringNode)this.field_74729_a.func_74599_b();
   }

   JsonNode func_74725_c() {
      return this.field_74728_b.func_74599_b();
   }
}
