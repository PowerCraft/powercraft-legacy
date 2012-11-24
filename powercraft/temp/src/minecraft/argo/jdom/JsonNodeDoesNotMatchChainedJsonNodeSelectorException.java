package argo.jdom;

import argo.jdom.Functor;
import argo.jdom.JsonNodeDoesNotMatchJsonNodeSelectorException;
import argo.jdom.JsonNodeSelector;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.LinkedList;
import java.util.List;

@SideOnly(Side.CLIENT)
public final class JsonNodeDoesNotMatchChainedJsonNodeSelectorException extends JsonNodeDoesNotMatchJsonNodeSelectorException {

   final Functor field_74703_a;
   final List field_74702_b;


   static JsonNodeDoesNotMatchJsonNodeSelectorException func_74701_a(Functor p_74701_0_) {
      return new JsonNodeDoesNotMatchChainedJsonNodeSelectorException(p_74701_0_, new LinkedList());
   }

   static JsonNodeDoesNotMatchJsonNodeSelectorException func_74699_a(JsonNodeDoesNotMatchChainedJsonNodeSelectorException p_74699_0_, JsonNodeSelector p_74699_1_) {
      LinkedList var2 = new LinkedList(p_74699_0_.field_74702_b);
      var2.add(p_74699_1_);
      return new JsonNodeDoesNotMatchChainedJsonNodeSelectorException(p_74699_0_.field_74703_a, var2);
   }

   static JsonNodeDoesNotMatchJsonNodeSelectorException func_74698_b(JsonNodeDoesNotMatchChainedJsonNodeSelectorException p_74698_0_, JsonNodeSelector p_74698_1_) {
      LinkedList var2 = new LinkedList();
      var2.add(p_74698_1_);
      return new JsonNodeDoesNotMatchChainedJsonNodeSelectorException(p_74698_0_.field_74703_a, var2);
   }

   private JsonNodeDoesNotMatchChainedJsonNodeSelectorException(Functor p_i3228_1_, List p_i3228_2_) {
      super("Failed to match any JSON node at [" + func_74700_a(p_i3228_2_) + "]");
      this.field_74703_a = p_i3228_1_;
      this.field_74702_b = p_i3228_2_;
   }

   static String func_74700_a(List p_74700_0_) {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = p_74700_0_.size() - 1; var2 >= 0; --var2) {
         var1.append(((JsonNodeSelector)p_74700_0_.get(var2)).func_74685_a());
         if(var2 != 0) {
            var1.append(".");
         }
      }

      return var1.toString();
   }

   public String toString() {
      return "JsonNodeDoesNotMatchJsonNodeSelectorException{failedNode=" + this.field_74703_a + ", failPath=" + this.field_74702_b + '}';
   }
}
