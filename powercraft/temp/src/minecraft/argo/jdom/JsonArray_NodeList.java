package argo.jdom;

import argo.jdom.JsonNode;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;

@SideOnly(Side.CLIENT)
final class JsonArray_NodeList extends ArrayList {

   // $FF: synthetic field
   final Iterable field_74730_a;


   JsonArray_NodeList(Iterable p_i3222_1_) {
      this.field_74730_a = p_i3222_1_;
      Iterator var2 = this.field_74730_a.iterator();

      while(var2.hasNext()) {
         JsonNode var3 = (JsonNode)var2.next();
         this.add(var3);
      }

   }
}
