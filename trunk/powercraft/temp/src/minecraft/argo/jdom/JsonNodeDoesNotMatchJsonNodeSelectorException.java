package argo.jdom;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class JsonNodeDoesNotMatchJsonNodeSelectorException extends IllegalArgumentException {

   JsonNodeDoesNotMatchJsonNodeSelectorException(String p_i3229_1_) {
      super(p_i3229_1_);
   }
}
