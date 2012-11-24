package org.bouncycastle.asn1;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;

public class DERObjectIdentifier extends ASN1Primitive {

   String field_71611_a;
   private static ASN1ObjectIdentifier[][] field_71610_b = new ASN1ObjectIdentifier[255][];


   public DERObjectIdentifier(String p_i4037_1_) {
      if(!func_71608_a(p_i4037_1_)) {
         throw new IllegalArgumentException("string " + p_i4037_1_ + " not an OID");
      } else {
         this.field_71611_a = p_i4037_1_;
      }
   }

   public String func_71609_b() {
      return this.field_71611_a;
   }

   public int hashCode() {
      return this.field_71611_a.hashCode();
   }

   boolean func_71607_a(ASN1Primitive p_71607_1_) {
      return !(p_71607_1_ instanceof DERObjectIdentifier)?false:this.field_71611_a.equals(((DERObjectIdentifier)p_71607_1_).field_71611_a);
   }

   public String toString() {
      return this.func_71609_b();
   }

   private static boolean func_71608_a(String p_71608_0_) {
      if(p_71608_0_.length() >= 3 && p_71608_0_.charAt(1) == 46) {
         char var1 = p_71608_0_.charAt(0);
         if(var1 >= 48 && var1 <= 50) {
            boolean var2 = false;

            for(int var3 = p_71608_0_.length() - 1; var3 >= 2; --var3) {
               char var4 = p_71608_0_.charAt(var3);
               if(48 <= var4 && var4 <= 57) {
                  var2 = true;
               } else {
                  if(var4 != 46) {
                     return false;
                  }

                  if(!var2) {
                     return false;
                  }

                  var2 = false;
               }
            }

            return var2;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

}
