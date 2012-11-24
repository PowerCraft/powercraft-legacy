package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.src.HttpUtilRunnable;
import net.minecraft.src.IDownloadSuccess;
import net.minecraft.src.IProgressUpdate;

public class HttpUtil {

   public static String func_76179_a(Map p_76179_0_) {
      StringBuilder var1 = new StringBuilder();
      Iterator var2 = p_76179_0_.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if(var1.length() > 0) {
            var1.append('&');
         }

         try {
            var1.append(URLEncoder.encode((String)var3.getKey(), "UTF-8"));
         } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
         }

         if(var3.getValue() != null) {
            var1.append('=');

            try {
               var1.append(URLEncoder.encode(var3.getValue().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException var5) {
               var5.printStackTrace();
            }
         }
      }

      return var1.toString();
   }

   public static String func_76183_a(URL p_76183_0_, Map p_76183_1_, boolean p_76183_2_) {
      return func_76180_a(p_76183_0_, func_76179_a(p_76183_1_), p_76183_2_);
   }

   public static String func_76180_a(URL p_76180_0_, String p_76180_1_, boolean p_76180_2_) {
      try {
         HttpURLConnection var3 = (HttpURLConnection)p_76180_0_.openConnection();
         var3.setRequestMethod("POST");
         var3.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
         var3.setRequestProperty("Content-Length", "" + p_76180_1_.getBytes().length);
         var3.setRequestProperty("Content-Language", "en-US");
         var3.setUseCaches(false);
         var3.setDoInput(true);
         var3.setDoOutput(true);
         DataOutputStream var4 = new DataOutputStream(var3.getOutputStream());
         var4.writeBytes(p_76180_1_);
         var4.flush();
         var4.close();
         BufferedReader var5 = new BufferedReader(new InputStreamReader(var3.getInputStream()));
         StringBuffer var7 = new StringBuffer();

         String var6;
         while((var6 = var5.readLine()) != null) {
            var7.append(var6);
            var7.append('\r');
         }

         var5.close();
         return var7.toString();
      } catch (Exception var8) {
         if(!p_76180_2_) {
            Logger.getLogger("Minecraft").log(Level.SEVERE, "Could not post to " + p_76180_0_, var8);
         }

         return "";
      }
   }

   @SideOnly(Side.CLIENT)
   public static void func_76182_a(File p_76182_0_, String p_76182_1_, IDownloadSuccess p_76182_2_, Map p_76182_3_, int p_76182_4_, IProgressUpdate p_76182_5_) {
      Thread var6 = new Thread(new HttpUtilRunnable(p_76182_5_, p_76182_1_, p_76182_3_, p_76182_0_, p_76182_2_, p_76182_4_));
      var6.setDaemon(true);
      var6.start();
   }

   @SideOnly(Side.CLIENT)
   public static int func_76181_a() throws IOException {
      ServerSocket var0 = null;
      boolean var1 = true;

      int var10;
      try {
         var0 = new ServerSocket(0);
         var10 = var0.getLocalPort();
      } finally {
         try {
            if(var0 != null) {
               var0.close();
            }
         } catch (IOException var8) {
            ;
         }

      }

      return var10;
   }

   @SideOnly(Side.CLIENT)
   public static String[] func_82718_a(String p_82718_0_, String p_82718_1_) {
      HashMap var2 = new HashMap();
      var2.put("user", p_82718_0_);
      var2.put("password", p_82718_1_);
      var2.put("version", Integer.valueOf(13));

      String var3;
      try {
         var3 = func_76183_a(new URL("http://login.minecraft.net/"), var2, false);
      } catch (MalformedURLException var5) {
         var5.printStackTrace();
         return null;
      }

      if(var3 != null && var3.length() != 0) {
         if(!var3.contains(":")) {
            if(var3.trim().equals("Bad login")) {
               System.out.println("Login failed");
            } else if(var3.trim().equals("Old version")) {
               System.out.println("Outdated launcher");
            } else if(var3.trim().equals("User not premium")) {
               System.out.println(var3);
            } else {
               System.out.println(var3);
            }

            return null;
         } else {
            String[] var4 = var3.split(":");
            return new String[]{var4[2], var4[3]};
         }
      } else {
         System.out.println("Can\'t connect to minecraft.net");
         return null;
      }
   }
}
