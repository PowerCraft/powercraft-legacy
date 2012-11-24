package argo.jdom;

import argo.jdom.JsonNode;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeFactories;
import argo.jdom.JsonStringNode;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public final class JsonStringNodeBuilder implements JsonNodeBuilder {

   private final String field_74601_a;


   JsonStringNodeBuilder(String p_i3240_1_) {
      this.field_74601_a = p_i3240_1_;
   }

   public JsonStringNode func_74600_a() {
      return JsonNodeFactories.func_74697_a(this.field_74601_a);
   }

   // $FF: synthetic method
   public JsonNode func_74599_b() {
      return this.func_74600_a();
   }
}
