package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Hashtable;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

@SideOnly(Side.CLIENT)
public class ServerAddress {

   private final String field_78866_a;
   private final int field_78865_b;


   private ServerAddress(String p_i3096_1_, int p_i3096_2_) {
      this.field_78866_a = p_i3096_1_;
      this.field_78865_b = p_i3096_2_;
   }

   public String func_78861_a() {
      return this.field_78866_a;
   }

   public int func_78864_b() {
      return this.field_78865_b;
   }

   public static ServerAddress func_78860_a(String p_78860_0_) {
      if(p_78860_0_ == null) {
         return null;
      } else {
         String[] var1 = p_78860_0_.split(":");
         if(p_78860_0_.startsWith("[")) {
            int var2 = p_78860_0_.indexOf("]");
            if(var2 > 0) {
               String var3 = p_78860_0_.substring(1, var2);
               String var4 = p_78860_0_.substring(var2 + 1).trim();
               if(var4.startsWith(":") && var4.length() > 0) {
                  var4 = var4.substring(1);
                  var1 = new String[]{var3, var4};
               } else {
                  var1 = new String[]{var3};
               }
            }
         }

         if(var1.length > 2) {
            var1 = new String[]{p_78860_0_};
         }

         String var5 = var1[0];
         int var6 = var1.length > 1?func_78862_a(var1[1], 25565):25565;
         if(var6 == 25565) {
            String[] var7 = func_78863_b(var5);
            var5 = var7[0];
            var6 = func_78862_a(var7[1], 25565);
         }

         return new ServerAddress(var5, var6);
      }
   }

   private static String[] func_78863_b(String p_78863_0_) {
      try {
         Hashtable var1 = new Hashtable();
         var1.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
         var1.put("java.naming.provider.url", "dns:");
         InitialDirContext var2 = new InitialDirContext(var1);
         Attributes var3 = var2.getAttributes("_minecraft._tcp." + p_78863_0_, new String[]{"SRV"});
         String[] var4 = var3.get("srv").get().toString().split(" ", 4);
         return new String[]{var4[3], var4[2]};
      } catch (Throwable var5) {
         return new String[]{p_78863_0_, Integer.toString(25565)};
      }
   }

   private static int func_78862_a(String p_78862_0_, int p_78862_1_) {
      try {
         return Integer.parseInt(p_78862_0_.trim());
      } catch (Exception var3) {
         return p_78862_1_;
      }
   }
}
