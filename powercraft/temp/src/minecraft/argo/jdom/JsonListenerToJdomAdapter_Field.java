package argo.jdom;

import argo.jdom.JsonFieldBuilder;
import argo.jdom.JsonListenerToJdomAdapter;
import argo.jdom.JsonListenerToJdomAdapter_NodeContainer;
import argo.jdom.JsonNodeBuilder;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
class JsonListenerToJdomAdapter_Field implements JsonListenerToJdomAdapter_NodeContainer {

   // $FF: synthetic field
   final JsonFieldBuilder field_74718_a;
   // $FF: synthetic field
   final JsonListenerToJdomAdapter field_74717_b;


   JsonListenerToJdomAdapter_Field(JsonListenerToJdomAdapter p_i3227_1_, JsonFieldBuilder p_i3227_2_) {
      this.field_74717_b = p_i3227_1_;
      this.field_74718_a = p_i3227_2_;
   }

   public void func_74715_a(JsonNodeBuilder p_74715_1_) {
      this.field_74718_a.func_74723_b(p_74715_1_);
   }

   public void func_74716_a(JsonFieldBuilder p_74716_1_) {
      throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to a field.");
   }
}
