package argo.jdom;

import argo.format.CompactJsonFormatter;
import argo.format.JsonFormatter;
import argo.jdom.JsonNodeDoesNotMatchChainedJsonNodeSelectorException;
import argo.jdom.JsonNodeDoesNotMatchJsonNodeSelectorException;
import argo.jdom.JsonRootNode;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public final class JsonNodeDoesNotMatchPathElementsException extends JsonNodeDoesNotMatchJsonNodeSelectorException {

   private static final JsonFormatter field_74707_a = new CompactJsonFormatter();


   static JsonNodeDoesNotMatchPathElementsException func_74704_a(JsonNodeDoesNotMatchChainedJsonNodeSelectorException p_74704_0_, Object[] p_74704_1_, JsonRootNode p_74704_2_) {
      return new JsonNodeDoesNotMatchPathElementsException(p_74704_0_, p_74704_1_, p_74704_2_);
   }

   private JsonNodeDoesNotMatchPathElementsException(JsonNodeDoesNotMatchChainedJsonNodeSelectorException p_i3230_1_, Object[] p_i3230_2_, JsonRootNode p_i3230_3_) {
      super(func_74706_b(p_i3230_1_, p_i3230_2_, p_i3230_3_));
   }

   private static String func_74706_b(JsonNodeDoesNotMatchChainedJsonNodeSelectorException p_74706_0_, Object[] p_74706_1_, JsonRootNode p_74706_2_) {
      return "Failed to find " + p_74706_0_.field_74703_a.toString() + " at [" + JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_74700_a(p_74706_0_.field_74702_b) + "] while resolving [" + func_74705_a(p_74706_1_) + "] in " + field_74707_a.func_74785_a(p_74706_2_) + ".";
   }

   private static String func_74705_a(Object[] p_74705_0_) {
      StringBuilder var1 = new StringBuilder();
      boolean var2 = true;
      Object[] var3 = p_74705_0_;
      int var4 = p_74705_0_.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = var3[var5];
         if(!var2) {
            var1.append(".");
         }

         var2 = false;
         if(var6 instanceof String) {
            var1.append("\"").append(var6).append("\"");
         } else {
            var1.append(var6);
         }
      }

      return var1.toString();
   }

}
