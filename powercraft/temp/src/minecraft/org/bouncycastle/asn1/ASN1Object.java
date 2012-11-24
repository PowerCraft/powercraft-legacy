package org.bouncycastle.asn1;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;

public abstract class ASN1Object implements ASN1Encodable {

   public int hashCode() {
      return this.func_71606_a().hashCode();
   }

   public boolean equals(Object p_equals_1_) {
      if(this == p_equals_1_) {
         return true;
      } else if(!(p_equals_1_ instanceof ASN1Encodable)) {
         return false;
      } else {
         ASN1Encodable var2 = (ASN1Encodable)p_equals_1_;
         return this.func_71606_a().equals(var2.func_71606_a());
      }
   }

   public abstract ASN1Primitive func_71606_a();
}
