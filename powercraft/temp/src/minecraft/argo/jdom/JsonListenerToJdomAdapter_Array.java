package argo.jdom;

import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonFieldBuilder;
import argo.jdom.JsonListenerToJdomAdapter;
import argo.jdom.JsonListenerToJdomAdapter_NodeContainer;
import argo.jdom.JsonNodeBuilder;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
class JsonListenerToJdomAdapter_Array implements JsonListenerToJdomAdapter_NodeContainer {

   // $FF: synthetic field
   final JsonArrayNodeBuilder field_74722_a;
   // $FF: synthetic field
   final JsonListenerToJdomAdapter field_74721_b;


   JsonListenerToJdomAdapter_Array(JsonListenerToJdomAdapter p_i3225_1_, JsonArrayNodeBuilder p_i3225_2_) {
      this.field_74721_b = p_i3225_1_;
      this.field_74722_a = p_i3225_2_;
   }

   public void func_74715_a(JsonNodeBuilder p_74715_1_) {
      this.field_74722_a.func_74603_a(p_74715_1_);
   }

   public void func_74716_a(JsonFieldBuilder p_74716_1_) {
      throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to an array.");
   }
}
