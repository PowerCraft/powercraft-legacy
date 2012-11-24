package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.IllegalFormatException;
import java.util.Properties;
import java.util.TreeMap;

public class StringTranslate {

   private static StringTranslate field_74817_a = new StringTranslate("en_US");
   public Properties field_74815_b = new Properties();
   private TreeMap field_74816_c;
   public String field_74813_d;
   private boolean field_74814_e;


   public StringTranslate(String p_i3260_1_) {
      this.func_74807_e();
      this.func_74810_a(p_i3260_1_);
   }

   public static StringTranslate func_74808_a() {
      return field_74817_a;
   }

   private void func_74807_e() {
      TreeMap var1 = new TreeMap();

      try {
         BufferedReader var2 = new BufferedReader(new InputStreamReader(StringTranslate.class.getResourceAsStream("/lang/languages.txt"), "UTF-8"));

         for(String var3 = var2.readLine(); var3 != null; var3 = var2.readLine()) {
            String[] var4 = var3.split("=");
            if(var4 != null && var4.length == 2) {
               var1.put(var4[0], var4[1]);
            }
         }
      } catch (IOException var5) {
         var5.printStackTrace();
         return;
      }

      this.field_74816_c = var1;
      this.field_74816_c.put("en_US", "English (US)");
   }

   public TreeMap func_74806_b() {
      return this.field_74816_c;
   }

   private void func_74812_a(Properties p_74812_1_, String p_74812_2_) throws IOException {
      BufferedReader var3 = new BufferedReader(new InputStreamReader(StringTranslate.class.getResourceAsStream("/lang/" + p_74812_2_ + ".lang"), "UTF-8"));

      for(String var4 = var3.readLine(); var4 != null; var4 = var3.readLine()) {
         var4 = var4.trim();
         if(!var4.startsWith("#")) {
            String[] var5 = var4.split("=");
            if(var5 != null && var5.length == 2) {
               p_74812_1_.setProperty(var5[0], var5[1]);
            }
         }
      }

   }

   public void func_74810_a(String p_74810_1_) {
      if(!p_74810_1_.equals(this.field_74813_d)) {
         Properties var2 = new Properties();

         try {
            this.func_74812_a(var2, "en_US");
         } catch (IOException var8) {
            ;
         }

         this.field_74814_e = false;
         if(!"en_US".equals(p_74810_1_)) {
            try {
               this.func_74812_a(var2, p_74810_1_);
               Enumeration var3 = var2.propertyNames();

               while(var3.hasMoreElements() && !this.field_74814_e) {
                  Object var4 = var3.nextElement();
                  Object var5 = var2.get(var4);
                  if(var5 != null) {
                     String var6 = var5.toString();

                     for(int var7 = 0; var7 < var6.length(); ++var7) {
                        if(var6.charAt(var7) >= 256) {
                           this.field_74814_e = true;
                           break;
                        }
                     }
                  }
               }
            } catch (IOException var9) {
               var9.printStackTrace();
               return;
            }
         }

         this.field_74813_d = p_74810_1_;
         this.field_74815_b = var2;
      }
   }

   @SideOnly(Side.CLIENT)
   public String func_74811_c() {
      return this.field_74813_d;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_74804_d() {
      return this.field_74814_e;
   }

   public String func_74805_b(String p_74805_1_) {
      return this.field_74815_b.getProperty(p_74805_1_, p_74805_1_);
   }

   public String func_74803_a(String p_74803_1_, Object ... p_74803_2_) {
      String var3 = this.field_74815_b.getProperty(p_74803_1_, p_74803_1_);

      try {
         return String.format(var3, p_74803_2_);
      } catch (IllegalFormatException var5) {
         return "Format error: " + var3;
      }
   }

   public String func_74809_c(String p_74809_1_) {
      return this.field_74815_b.getProperty(p_74809_1_ + ".name", "");
   }

   @SideOnly(Side.CLIENT)
   public static boolean func_74802_e(String p_74802_0_) {
      return "ar_SA".equals(p_74802_0_) || "he_IL".equals(p_74802_0_);
   }

}
