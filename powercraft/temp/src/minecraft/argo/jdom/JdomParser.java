package argo.jdom;

import argo.jdom.JsonListenerToJdomAdapter;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;
import argo.saj.SajParser;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

@SideOnly(Side.CLIENT)
public final class JdomParser {

   public JsonRootNode func_74788_a(Reader p_74788_1_) throws IOException, InvalidSyntaxException {
      JsonListenerToJdomAdapter var2 = new JsonListenerToJdomAdapter();
      (new SajParser()).func_74552_a(p_74788_1_, var2);
      return var2.func_74660_a();
   }

   public JsonRootNode parse(String p_parse_1_) throws InvalidSyntaxException {
      try {
         JsonRootNode var2 = this.func_74788_a(new StringReader(p_parse_1_));
         return var2;
      } catch (IOException var4) {
         throw new RuntimeException("Coding failure in Argo:  StringWriter gave an IOException", var4);
      }
   }
}
