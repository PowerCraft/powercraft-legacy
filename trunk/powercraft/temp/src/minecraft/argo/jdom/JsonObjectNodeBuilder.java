package argo.jdom;

import argo.jdom.JsonFieldBuilder;
import argo.jdom.JsonNode;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeFactories;
import argo.jdom.JsonObjectNodeBuilder_List;
import argo.jdom.JsonRootNode;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.LinkedList;
import java.util.List;

@SideOnly(Side.CLIENT)
public final class JsonObjectNodeBuilder implements JsonNodeBuilder {

   private final List field_74609_a = new LinkedList();


   public JsonObjectNodeBuilder func_74608_a(JsonFieldBuilder p_74608_1_) {
      this.field_74609_a.add(p_74608_1_);
      return this;
   }

   public JsonRootNode func_74606_a() {
      return JsonNodeFactories.func_74692_a(new JsonObjectNodeBuilder_List(this));
   }

   // $FF: synthetic method
   public JsonNode func_74599_b() {
      return this.func_74606_a();
   }

   // $FF: synthetic method
   static List func_74607_a(JsonObjectNodeBuilder p_74607_0_) {
      return p_74607_0_.field_74609_a;
   }
}
