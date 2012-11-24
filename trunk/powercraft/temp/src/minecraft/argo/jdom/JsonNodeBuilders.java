package argo.jdom;

import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders_False;
import argo.jdom.JsonNodeBuilders_Null;
import argo.jdom.JsonNodeBuilders_True;
import argo.jdom.JsonNumberNodeBuilder;
import argo.jdom.JsonObjectNodeBuilder;
import argo.jdom.JsonStringNodeBuilder;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public final class JsonNodeBuilders {

   public static JsonNodeBuilder func_74714_a() {
      return new JsonNodeBuilders_Null();
   }

   public static JsonNodeBuilder func_74713_b() {
      return new JsonNodeBuilders_True();
   }

   public static JsonNodeBuilder func_74709_c() {
      return new JsonNodeBuilders_False();
   }

   public static JsonNodeBuilder func_74712_a(String p_74712_0_) {
      return new JsonNumberNodeBuilder(p_74712_0_);
   }

   public static JsonStringNodeBuilder func_74710_b(String p_74710_0_) {
      return new JsonStringNodeBuilder(p_74710_0_);
   }

   public static JsonObjectNodeBuilder func_74711_d() {
      return new JsonObjectNodeBuilder();
   }

   public static JsonArrayNodeBuilder func_74708_e() {
      return new JsonArrayNodeBuilder();
   }
}
