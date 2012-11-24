package org.bouncycastle.jcajce.provider.config;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.security.BasicPermission;
import java.security.Permission;
import java.util.StringTokenizer;
import org.bouncycastle.util.Strings;

public class ProviderConfigurationPermission extends BasicPermission {

   private final String field_74843_a;
   private final int field_74842_b;


   @SideOnly(Side.CLIENT)
   public ProviderConfigurationPermission(String p_i4048_1_) {
      super(p_i4048_1_);
      this.field_74843_a = "all";
      this.field_74842_b = 15;
   }

   public ProviderConfigurationPermission(String p_i4049_1_, String p_i4049_2_) {
      super(p_i4049_1_, p_i4049_2_);
      this.field_74843_a = p_i4049_2_;
      this.field_74842_b = this.func_74841_a(p_i4049_2_);
   }

   private int func_74841_a(String p_74841_1_) {
      StringTokenizer var2 = new StringTokenizer(Strings.func_74830_a(p_74841_1_), " ,");
      int var3 = 0;

      while(var2.hasMoreTokens()) {
         String var4 = var2.nextToken();
         if(var4.equals("threadlocalecimplicitlyca")) {
            var3 |= 1;
         } else if(var4.equals("ecimplicitlyca")) {
            var3 |= 2;
         } else if(var4.equals("threadlocaldhdefaultparams")) {
            var3 |= 4;
         } else if(var4.equals("dhdefaultparams")) {
            var3 |= 8;
         } else if(var4.equals("all")) {
            var3 |= 15;
         }
      }

      if(var3 == 0) {
         throw new IllegalArgumentException("unknown permissions passed to mask");
      } else {
         return var3;
      }
   }

   public String getActions() {
      return this.field_74843_a;
   }

   public boolean implies(Permission p_implies_1_) {
      if(!(p_implies_1_ instanceof ProviderConfigurationPermission)) {
         return false;
      } else if(!this.getName().equals(p_implies_1_.getName())) {
         return false;
      } else {
         ProviderConfigurationPermission var2 = (ProviderConfigurationPermission)p_implies_1_;
         return (this.field_74842_b & var2.field_74842_b) == var2.field_74842_b;
      }
   }

   public boolean equals(Object p_equals_1_) {
      if(p_equals_1_ == this) {
         return true;
      } else if(!(p_equals_1_ instanceof ProviderConfigurationPermission)) {
         return false;
      } else {
         ProviderConfigurationPermission var2 = (ProviderConfigurationPermission)p_equals_1_;
         return this.field_74842_b == var2.field_74842_b && this.getName().equals(var2.getName());
      }
   }

   public int hashCode() {
      return this.getName().hashCode() + this.field_74842_b;
   }
}
