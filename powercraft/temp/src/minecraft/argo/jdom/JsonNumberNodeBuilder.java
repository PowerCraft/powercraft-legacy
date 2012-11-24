package argo.jdom;

import argo.jdom.JsonNode;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeFactories;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
final class JsonNumberNodeBuilder implements JsonNodeBuilder {

   private final JsonNode field_74602_a;


   JsonNumberNodeBuilder(String p_i3236_1_) {
      this.field_74602_a = JsonNodeFactories.func_74691_b(p_i3236_1_);
   }

   public JsonNode func_74599_b() {
      return this.field_74602_a;
   }
}
