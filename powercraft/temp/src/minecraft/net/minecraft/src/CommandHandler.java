package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.src.CommandBase;
import net.minecraft.src.CommandException;
import net.minecraft.src.CommandNotFoundException;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommand;
import net.minecraft.src.ICommandManager;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.PlayerNotFoundException;
import net.minecraft.src.PlayerSelector;
import net.minecraft.src.WrongUsageException;

public class CommandHandler implements ICommandManager {

   private final Map field_71562_a = new HashMap();
   private final Set field_71561_b = new HashSet();


   public void func_71556_a(ICommandSender p_71556_1_, String p_71556_2_) {
      if(p_71556_2_.startsWith("/")) {
         p_71556_2_ = p_71556_2_.substring(1);
      }

      String[] var3 = p_71556_2_.split(" ");
      String var4 = var3[0];
      var3 = func_71559_a(var3);
      ICommand var5 = (ICommand)this.field_71562_a.get(var4);
      int var6 = this.func_82370_a(var5, var3);

      try {
         if(var5 == null) {
            throw new CommandNotFoundException();
         }

         if(var5.func_71519_b(p_71556_1_)) {
            if(var6 > -1) {
               EntityPlayerMP[] var7 = PlayerSelector.func_82380_c(p_71556_1_, var3[var6]);
               String var8 = var3[var6];
               EntityPlayerMP[] var9 = var7;
               int var10 = var7.length;

               for(int var11 = 0; var11 < var10; ++var11) {
                  EntityPlayerMP var12 = var9[var11];
                  var3[var6] = var12.func_70023_ak();

                  try {
                     var5.func_71515_b(p_71556_1_, var3);
                  } catch (PlayerNotFoundException var14) {
                     p_71556_1_.func_70006_a("\u00a7c" + p_71556_1_.func_70004_a(var14.getMessage(), var14.func_74844_a()));
                  }
               }

               var3[var6] = var8;
            } else {
               var5.func_71515_b(p_71556_1_, var3);
            }
         } else {
            p_71556_1_.func_70006_a("\u00a7cYou do not have permission to use this command.");
         }
      } catch (WrongUsageException var15) {
         p_71556_1_.func_70006_a("\u00a7c" + p_71556_1_.func_70004_a("commands.generic.usage", new Object[]{p_71556_1_.func_70004_a(var15.getMessage(), var15.func_74844_a())}));
      } catch (CommandException var16) {
         p_71556_1_.func_70006_a("\u00a7c" + p_71556_1_.func_70004_a(var16.getMessage(), var16.func_74844_a()));
      } catch (Throwable var17) {
         p_71556_1_.func_70006_a("\u00a7c" + p_71556_1_.func_70004_a("commands.generic.exception", new Object[0]));
         var17.printStackTrace();
      }

   }

   public ICommand func_71560_a(ICommand p_71560_1_) {
      List var2 = p_71560_1_.func_71514_a();
      this.field_71562_a.put(p_71560_1_.func_71517_b(), p_71560_1_);
      this.field_71561_b.add(p_71560_1_);
      if(var2 != null) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            ICommand var5 = (ICommand)this.field_71562_a.get(var4);
            if(var5 == null || !var5.func_71517_b().equals(var4)) {
               this.field_71562_a.put(var4, p_71560_1_);
            }
         }
      }

      return p_71560_1_;
   }

   private static String[] func_71559_a(String[] p_71559_0_) {
      String[] var1 = new String[p_71559_0_.length - 1];

      for(int var2 = 1; var2 < p_71559_0_.length; ++var2) {
         var1[var2 - 1] = p_71559_0_[var2];
      }

      return var1;
   }

   public List func_71558_b(ICommandSender p_71558_1_, String p_71558_2_) {
      String[] var3 = p_71558_2_.split(" ", -1);
      String var4 = var3[0];
      if(var3.length == 1) {
         ArrayList var8 = new ArrayList();
         Iterator var6 = this.field_71562_a.entrySet().iterator();

         while(var6.hasNext()) {
            Entry var7 = (Entry)var6.next();
            if(CommandBase.func_71523_a(var4, (String)var7.getKey()) && ((ICommand)var7.getValue()).func_71519_b(p_71558_1_)) {
               var8.add(var7.getKey());
            }
         }

         return var8;
      } else {
         if(var3.length > 1) {
            ICommand var5 = (ICommand)this.field_71562_a.get(var4);
            if(var5 != null) {
               return var5.func_71516_a(p_71558_1_, func_71559_a(var3));
            }
         }

         return null;
      }
   }

   public List func_71557_a(ICommandSender p_71557_1_) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.field_71561_b.iterator();

      while(var3.hasNext()) {
         ICommand var4 = (ICommand)var3.next();
         if(var4.func_71519_b(p_71557_1_)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public Map func_71555_a() {
      return this.field_71562_a;
   }

   private int func_82370_a(ICommand p_82370_1_, String[] p_82370_2_) {
      if(p_82370_1_ == null) {
         return -1;
      } else {
         for(int var3 = 0; var3 < p_82370_2_.length; ++var3) {
            if(p_82370_1_.func_82358_a(var3) && PlayerSelector.func_82377_a(p_82370_2_[var3])) {
               return var3;
            }
         }

         return -1;
      }
   }
}
