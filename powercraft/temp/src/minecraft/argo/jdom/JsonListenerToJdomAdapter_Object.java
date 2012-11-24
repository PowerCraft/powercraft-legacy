package argo.jdom;

import argo.jdom.JsonFieldBuilder;
import argo.jdom.JsonListenerToJdomAdapter;
import argo.jdom.JsonListenerToJdomAdapter_NodeContainer;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonObjectNodeBuilder;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
class JsonListenerToJdomAdapter_Object implements JsonListenerToJdomAdapter_NodeContainer {

   // $FF: synthetic field
   final JsonObjectNodeBuilder field_74720_a;
   // $FF: synthetic field
   final JsonListenerToJdomAdapter field_74719_b;


   JsonListenerToJdomAdapter_Object(JsonListenerToJdomAdapter p_i3226_1_, JsonObjectNodeBuilder p_i3226_2_) {
      this.field_74719_b = p_i3226_1_;
      this.field_74720_a = p_i3226_2_;
   }

   public void func_74715_a(JsonNodeBuilder p_74715_1_) {
      throw new RuntimeException("Coding failure in Argo:  Attempt to add a node to an object.");
   }

   public void func_74716_a(JsonFieldBuilder p_74716_1_) {
      this.field_74720_a.func_74608_a(p_74716_1_);
   }
}
