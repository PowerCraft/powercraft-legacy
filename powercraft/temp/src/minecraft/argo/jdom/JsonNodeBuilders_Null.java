package argo.jdom;

import argo.jdom.JsonNode;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeFactories;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
final class JsonNodeBuilders_Null implements JsonNodeBuilder {

   public JsonNode func_74599_b() {
      return JsonNodeFactories.func_74696_a();
   }
}
