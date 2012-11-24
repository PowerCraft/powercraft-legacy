package argo.format;

import argo.format.CompactJsonFormatter_JsonNodeType;
import argo.format.JsonEscapedString;
import argo.format.JsonFormatter;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.TreeSet;

@SideOnly(Side.CLIENT)
public final class CompactJsonFormatter implements JsonFormatter {

   public String func_74785_a(JsonRootNode p_74785_1_) {
      StringWriter var2 = new StringWriter();

      try {
         this.func_74787_a(p_74785_1_, var2);
      } catch (IOException var4) {
         throw new RuntimeException("Coding failure in Argo:  StringWriter gave an IOException", var4);
      }

      return var2.toString();
   }

   public void func_74787_a(JsonRootNode p_74787_1_, Writer p_74787_2_) throws IOException {
      this.func_74786_a(p_74787_1_, p_74787_2_);
   }

   private void func_74786_a(JsonNode p_74786_1_, Writer p_74786_2_) throws IOException {
      boolean var3 = true;
      Iterator var4;
      switch(CompactJsonFormatter_JsonNodeType.field_74790_a[p_74786_1_.func_74616_a().ordinal()]) {
      case 1:
         p_74786_2_.append('[');
         var4 = p_74786_1_.func_74610_b().iterator();

         while(var4.hasNext()) {
            JsonNode var6 = (JsonNode)var4.next();
            if(!var3) {
               p_74786_2_.append(',');
            }

            var3 = false;
            this.func_74786_a(var6, p_74786_2_);
         }

         p_74786_2_.append(']');
         break;
      case 2:
         p_74786_2_.append('{');
         var4 = (new TreeSet(p_74786_1_.getFields().keySet())).iterator();

         while(var4.hasNext()) {
            JsonStringNode var5 = (JsonStringNode)var4.next();
            if(!var3) {
               p_74786_2_.append(',');
            }

            var3 = false;
            this.func_74786_a(var5, p_74786_2_);
            p_74786_2_.append(':');
            this.func_74786_a((JsonNode)p_74786_1_.getFields().get(var5), p_74786_2_);
         }

         p_74786_2_.append('}');
         break;
      case 3:
         p_74786_2_.append('\"').append((new JsonEscapedString(p_74786_1_.getText())).toString()).append('\"');
         break;
      case 4:
         p_74786_2_.append(p_74786_1_.getText());
         break;
      case 5:
         p_74786_2_.append("false");
         break;
      case 6:
         p_74786_2_.append("true");
         break;
      case 7:
         p_74786_2_.append("null");
         break;
      default:
         throw new RuntimeException("Coding failure in Argo:  Attempt to format a JsonNode of unknown type [" + p_74786_1_.func_74616_a() + "];");
      }

   }
}
