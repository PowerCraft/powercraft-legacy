package argo.jdom;

import argo.jdom.JsonNodeDoesNotMatchChainedJsonNodeSelectorException;
import argo.jdom.JsonNodeDoesNotMatchPathElementsException;
import argo.jdom.JsonNodeFactories;
import argo.jdom.JsonNodeSelector;
import argo.jdom.JsonNodeSelectors;
import argo.jdom.JsonNodeType;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public abstract class JsonNode {

   public abstract JsonNodeType func_74616_a();

   public abstract String getText();

   public abstract Map getFields();

   public abstract List func_74610_b();

   public final String getStringValue(Object ... p_getStringValue_1_) {
      return (String)this.func_74613_a(JsonNodeSelectors.func_74682_a(p_getStringValue_1_), this, p_getStringValue_1_);
   }

   public final List getArrayNode(Object ... p_getArrayNode_1_) {
      return (List)this.func_74613_a(JsonNodeSelectors.func_74683_b(p_getArrayNode_1_), this, p_getArrayNode_1_);
   }

   private Object func_74613_a(JsonNodeSelector p_74613_1_, JsonNode p_74613_2_, Object[] p_74613_3_) throws JsonNodeDoesNotMatchPathElementsException {
      try {
         return p_74613_1_.func_74687_b(p_74613_2_);
      } catch (JsonNodeDoesNotMatchChainedJsonNodeSelectorException var5) {
         throw JsonNodeDoesNotMatchPathElementsException.func_74704_a(var5, p_74613_3_, JsonNodeFactories.func_74695_a(new JsonNode[]{p_74613_2_}));
      }
   }
}
