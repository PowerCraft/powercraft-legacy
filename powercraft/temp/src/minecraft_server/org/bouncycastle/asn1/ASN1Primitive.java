package org.bouncycastle.asn1;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;

public abstract class ASN1Primitive extends ASN1Object {

   public final boolean equals(Object p_equals_1_) {
      return this == p_equals_1_?true:p_equals_1_ instanceof ASN1Encodable && this.func_71607_a(((ASN1Encodable)p_equals_1_).func_71606_a());
   }

   public ASN1Primitive func_71606_a() {
      return this;
   }

   public abstract int hashCode();

   abstract boolean func_71607_a(ASN1Primitive var1);
}
