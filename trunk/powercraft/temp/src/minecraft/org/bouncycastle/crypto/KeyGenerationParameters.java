package org.bouncycastle.crypto;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.security.SecureRandom;

@SideOnly(Side.CLIENT)
public class KeyGenerationParameters {

   private SecureRandom field_71845_a;
   private int field_71844_b;


   public KeyGenerationParameters(SecureRandom p_i4040_1_, int p_i4040_2_) {
      this.field_71845_a = p_i4040_1_;
      this.field_71844_b = p_i4040_2_;
   }

   public SecureRandom func_71843_a() {
      return this.field_71845_a;
   }

   public int func_71842_b() {
      return this.field_71844_b;
   }
}
