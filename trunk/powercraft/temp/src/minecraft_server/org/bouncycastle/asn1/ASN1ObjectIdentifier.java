package org.bouncycastle.asn1;

import org.bouncycastle.asn1.DERObjectIdentifier;

public class ASN1ObjectIdentifier extends DERObjectIdentifier {

   public ASN1ObjectIdentifier(String p_i4036_1_) {
      super(p_i4036_1_);
   }

   public ASN1ObjectIdentifier func_71612_a(String p_71612_1_) {
      return new ASN1ObjectIdentifier(this.func_71609_b() + "." + p_71612_1_);
   }
}
