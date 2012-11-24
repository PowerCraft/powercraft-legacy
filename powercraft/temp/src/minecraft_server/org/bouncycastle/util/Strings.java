package org.bouncycastle.util;


public final class Strings {

   public static String func_74830_a(String p_74830_0_) {
      boolean var1 = false;
      char[] var2 = p_74830_0_.toCharArray();

      for(int var3 = 0; var3 != var2.length; ++var3) {
         char var4 = var2[var3];
         if(65 <= var4 && 90 >= var4) {
            var1 = true;
            var2[var3] = (char)(var4 - 65 + 97);
         }
      }

      if(var1) {
         return new String(var2);
      } else {
         return p_74830_0_;
      }
   }
}
