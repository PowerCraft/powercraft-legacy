package org.bouncycastle.jce.provider;

import java.security.PrivilegedAction;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

class BouncyCastleProviderAction implements PrivilegedAction {

   // $FF: synthetic field
   final BouncyCastleProvider field_74801_a;


   BouncyCastleProviderAction(BouncyCastleProvider p_i4050_1_) {
      this.field_74801_a = p_i4050_1_;
   }

   public Object run() {
      BouncyCastleProvider.func_74821_a(this.field_74801_a);
      return null;
   }
}
