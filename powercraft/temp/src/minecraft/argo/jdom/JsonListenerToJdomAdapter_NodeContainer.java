package argo.jdom;

import argo.jdom.JsonFieldBuilder;
import argo.jdom.JsonNodeBuilder;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
interface JsonListenerToJdomAdapter_NodeContainer {

   void func_74715_a(JsonNodeBuilder var1);

   void func_74716_a(JsonFieldBuilder var1);
}
