package org.bouncycastle.jce.provider;

import java.security.Permission;
import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
import org.bouncycastle.jcajce.provider.config.ProviderConfigurationPermission;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

class BouncyCastleProviderConfiguration implements ProviderConfiguration {

   private static Permission field_74836_a = new ProviderConfigurationPermission(BouncyCastleProvider.field_74828_a, "threadLocalEcImplicitlyCa");
   private static Permission field_74834_b = new ProviderConfigurationPermission(BouncyCastleProvider.field_74828_a, "ecImplicitlyCa");
   private static Permission field_74835_c = new ProviderConfigurationPermission(BouncyCastleProvider.field_74828_a, "threadLocalDhDefaultParams");
   private static Permission field_74832_d = new ProviderConfigurationPermission(BouncyCastleProvider.field_74828_a, "DhDefaultParams");
   private ThreadLocal field_74833_e = new ThreadLocal();
   private ThreadLocal field_74831_f = new ThreadLocal();


}
