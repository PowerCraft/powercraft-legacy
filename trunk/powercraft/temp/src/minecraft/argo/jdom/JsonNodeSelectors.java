package argo.jdom;

import argo.jdom.ChainedFunctor;
import argo.jdom.JsonNodeFactories;
import argo.jdom.JsonNodeSelector;
import argo.jdom.JsonNodeSelectors_Array;
import argo.jdom.JsonNodeSelectors_Element;
import argo.jdom.JsonNodeSelectors_Field;
import argo.jdom.JsonNodeSelectors_Object;
import argo.jdom.JsonNodeSelectors_String;
import argo.jdom.JsonStringNode;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Arrays;

@SideOnly(Side.CLIENT)
public final class JsonNodeSelectors {

   public static JsonNodeSelector func_74682_a(Object ... p_74682_0_) {
      return func_74677_a(p_74682_0_, new JsonNodeSelector(new JsonNodeSelectors_String()));
   }

   public static JsonNodeSelector func_74683_b(Object ... p_74683_0_) {
      return func_74677_a(p_74683_0_, new JsonNodeSelector(new JsonNodeSelectors_Array()));
   }

   public static JsonNodeSelector func_74681_c(Object ... p_74681_0_) {
      return func_74677_a(p_74681_0_, new JsonNodeSelector(new JsonNodeSelectors_Object()));
   }

   public static JsonNodeSelector func_74675_a(String p_74675_0_) {
      return func_74680_a(JsonNodeFactories.func_74697_a(p_74675_0_));
   }

   public static JsonNodeSelector func_74680_a(JsonStringNode p_74680_0_) {
      return new JsonNodeSelector(new JsonNodeSelectors_Field(p_74680_0_));
   }

   public static JsonNodeSelector func_74684_b(String p_74684_0_) {
      return func_74681_c(new Object[0]).func_74686_a(func_74675_a(p_74684_0_));
   }

   public static JsonNodeSelector func_74678_a(int p_74678_0_) {
      return new JsonNodeSelector(new JsonNodeSelectors_Element(p_74678_0_));
   }

   public static JsonNodeSelector func_74679_b(int p_74679_0_) {
      return func_74683_b(new Object[0]).func_74686_a(func_74678_a(p_74679_0_));
   }

   private static JsonNodeSelector func_74677_a(Object[] p_74677_0_, JsonNodeSelector p_74677_1_) {
      JsonNodeSelector var2 = p_74677_1_;

      for(int var3 = p_74677_0_.length - 1; var3 >= 0; --var3) {
         if(p_74677_0_[var3] instanceof Integer) {
            var2 = func_74676_a(func_74679_b(((Integer)p_74677_0_[var3]).intValue()), var2);
         } else {
            if(!(p_74677_0_[var3] instanceof String)) {
               throw new IllegalArgumentException("Element [" + p_74677_0_[var3] + "] of path elements" + " [" + Arrays.toString(p_74677_0_) + "] was of illegal type [" + p_74677_0_[var3].getClass().getCanonicalName() + "]; only Integer and String are valid.");
            }

            var2 = func_74676_a(func_74684_b((String)p_74677_0_[var3]), var2);
         }
      }

      return var2;
   }

   private static JsonNodeSelector func_74676_a(JsonNodeSelector p_74676_0_, JsonNodeSelector p_74676_1_) {
      return new JsonNodeSelector(new ChainedFunctor(p_74676_0_, p_74676_1_));
   }
}
