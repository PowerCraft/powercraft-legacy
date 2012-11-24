package argo.jdom;

import argo.jdom.JsonFieldBuilder;
import argo.jdom.JsonObjectNodeBuilder;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.HashMap;
import java.util.Iterator;

@SideOnly(Side.CLIENT)
class JsonObjectNodeBuilder_List extends HashMap {

   // $FF: synthetic field
   final JsonObjectNodeBuilder field_74598_a;


   JsonObjectNodeBuilder_List(JsonObjectNodeBuilder p_i3238_1_) {
      this.field_74598_a = p_i3238_1_;
      Iterator var2 = JsonObjectNodeBuilder.func_74607_a(this.field_74598_a).iterator();

      while(var2.hasNext()) {
         JsonFieldBuilder var3 = (JsonFieldBuilder)var2.next();
         this.put(var3.func_74724_b(), var3.func_74725_c());
      }

   }
}
