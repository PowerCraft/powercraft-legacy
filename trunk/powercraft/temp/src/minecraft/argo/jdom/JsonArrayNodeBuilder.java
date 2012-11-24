package argo.jdom;

import argo.jdom.JsonNode;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeFactories;
import argo.jdom.JsonRootNode;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@SideOnly(Side.CLIENT)
public final class JsonArrayNodeBuilder implements JsonNodeBuilder {

   private final List field_74605_a = new LinkedList();


   public JsonArrayNodeBuilder func_74603_a(JsonNodeBuilder p_74603_1_) {
      this.field_74605_a.add(p_74603_1_);
      return this;
   }

   public JsonRootNode func_74604_a() {
      LinkedList var1 = new LinkedList();
      Iterator var2 = this.field_74605_a.iterator();

      while(var2.hasNext()) {
         JsonNodeBuilder var3 = (JsonNodeBuilder)var2.next();
         var1.add(var3.func_74599_b());
      }

      return JsonNodeFactories.func_74690_a(var1);
   }

   // $FF: synthetic method
   public JsonNode func_74599_b() {
      return this.func_74604_a();
   }
}
