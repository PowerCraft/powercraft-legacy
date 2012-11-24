package net.minecraft.src;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.MathHelper;

public class PlayerSelector {

   private static final Pattern field_82389_a = Pattern.compile("^@([parf])(?:\\[([\\w=,-]*)\\])?$");
   private static final Pattern field_82387_b = Pattern.compile("\\G(-?\\w*)(?:$|,)");
   private static final Pattern field_82388_c = Pattern.compile("\\G(\\w{1,2})=(-?\\w+)(?:$|,)");


   public static EntityPlayerMP func_82386_a(ICommandSender p_82386_0_, String p_82386_1_) {
      EntityPlayerMP[] var2 = func_82380_c(p_82386_0_, p_82386_1_);
      return var2 != null && var2.length == 1?var2[0]:null;
   }

   public static String func_82385_b(ICommandSender p_82385_0_, String p_82385_1_) {
      EntityPlayerMP[] var2 = func_82380_c(p_82385_0_, p_82385_1_);
      if(var2 != null && var2.length != 0) {
         String[] var3 = new String[var2.length];

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var3[var4] = var2[var4].func_70023_ak();
         }

         return CommandBase.func_71527_a(var3);
      } else {
         return null;
      }
   }

   public static EntityPlayerMP[] func_82380_c(ICommandSender p_82380_0_, String p_82380_1_) {
      Matcher var2 = field_82389_a.matcher(p_82380_1_);
      if(var2.matches()) {
         Map var3 = func_82381_h(var2.group(2));
         String var4 = var2.group(1);
         int var5 = func_82384_c(var4);
         int var6 = func_82379_d(var4);
         int var7 = func_82375_f(var4);
         int var8 = func_82376_e(var4);
         int var9 = func_82382_g(var4);
         int var10 = EnumGameType.NOT_SET.func_77148_a();
         ChunkCoordinates var11 = p_82380_0_.func_82114_b();
         if(var3.containsKey("rm")) {
            var5 = MathHelper.func_82715_a((String)var3.get("rm"), var5);
         }

         if(var3.containsKey("r")) {
            var6 = MathHelper.func_82715_a((String)var3.get("r"), var6);
         }

         if(var3.containsKey("lm")) {
            var7 = MathHelper.func_82715_a((String)var3.get("lm"), var7);
         }

         if(var3.containsKey("l")) {
            var8 = MathHelper.func_82715_a((String)var3.get("l"), var8);
         }

         if(var3.containsKey("x")) {
            var11.field_71574_a = MathHelper.func_82715_a((String)var3.get("x"), var11.field_71574_a);
         }

         if(var3.containsKey("y")) {
            var11.field_71572_b = MathHelper.func_82715_a((String)var3.get("y"), var11.field_71572_b);
         }

         if(var3.containsKey("z")) {
            var11.field_71573_c = MathHelper.func_82715_a((String)var3.get("z"), var11.field_71573_c);
         }

         if(var3.containsKey("m")) {
            var10 = MathHelper.func_82715_a((String)var3.get("m"), var10);
         }

         if(var3.containsKey("c")) {
            var9 = MathHelper.func_82715_a((String)var3.get("c"), var9);
         }

         List var12;
         if(!var4.equals("p") && !var4.equals("a")) {
            if(!var4.equals("r")) {
               return null;
            } else {
               var12 = MinecraftServer.func_71276_C().func_71203_ab().func_82449_a(var11, var5, var6, 0, var10, var7, var8);
               Collections.shuffle(var12);
               var12 = var12.subList(0, Math.min(var9, var12.size()));
               return var12 != null && !var12.isEmpty()?(EntityPlayerMP[])var12.toArray(new EntityPlayerMP[0]):new EntityPlayerMP[0];
            }
         } else {
            var12 = MinecraftServer.func_71276_C().func_71203_ab().func_82449_a(var11, var5, var6, var9, var10, var7, var8);
            return var12 != null && !var12.isEmpty()?(EntityPlayerMP[])var12.toArray(new EntityPlayerMP[0]):new EntityPlayerMP[0];
         }
      } else {
         return null;
      }
   }

   public static boolean func_82377_a(String p_82377_0_) {
      Matcher var1 = field_82389_a.matcher(p_82377_0_);
      if(var1.matches()) {
         Map var2 = func_82381_h(var1.group(2));
         String var3 = var1.group(1);
         int var4 = func_82382_g(var3);
         if(var2.containsKey("c")) {
            var4 = MathHelper.func_82715_a((String)var2.get("c"), var4);
         }

         return var4 != 1;
      } else {
         return false;
      }
   }

   public static boolean func_82383_a(String p_82383_0_, String p_82383_1_) {
      Matcher var2 = field_82389_a.matcher(p_82383_0_);
      if(!var2.matches()) {
         return false;
      } else {
         String var3 = var2.group(1);
         return p_82383_1_ == null || p_82383_1_.equals(var3);
      }
   }

   public static boolean func_82378_b(String p_82378_0_) {
      return func_82383_a(p_82378_0_, (String)null);
   }

   private static final int func_82384_c(String p_82384_0_) {
      return 0;
   }

   private static final int func_82379_d(String p_82379_0_) {
      return 0;
   }

   private static final int func_82376_e(String p_82376_0_) {
      return Integer.MAX_VALUE;
   }

   private static final int func_82375_f(String p_82375_0_) {
      return 0;
   }

   private static final int func_82382_g(String p_82382_0_) {
      return p_82382_0_.equals("a")?0:1;
   }

   private static Map func_82381_h(String p_82381_0_) {
      HashMap var1 = new HashMap();
      if(p_82381_0_ == null) {
         return var1;
      } else {
         Matcher var2 = field_82387_b.matcher(p_82381_0_);
         int var3 = 0;

         int var4;
         for(var4 = -1; var2.find(); var4 = var2.end()) {
            String var5 = null;
            switch(var3++) {
            case 0:
               var5 = "x";
               break;
            case 1:
               var5 = "y";
               break;
            case 2:
               var5 = "z";
               break;
            case 3:
               var5 = "r";
            }

            if(var5 != null && var2.group(1).length() > 0) {
               var1.put(var5, var2.group(1));
            }
         }

         if(var4 < p_82381_0_.length()) {
            var2 = field_82388_c.matcher(var4 == -1?p_82381_0_:p_82381_0_.substring(var4));

            while(var2.find()) {
               var1.put(var2.group(1), var2.group(2));
            }
         }

         return var1;
      }
   }

}
