package net.minecraft.src;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.DedicatedServerListenThread;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet1Login;
import net.minecraft.src.Packet205ClientCommand;
import net.minecraft.src.Packet252SharedKey;
import net.minecraft.src.Packet253ServerAuthData;
import net.minecraft.src.Packet254ServerPing;
import net.minecraft.src.Packet255KickDisconnect;
import net.minecraft.src.Packet2ClientProtocol;
import net.minecraft.src.ServerConfigurationManager;
import net.minecraft.src.StringUtils;
import net.minecraft.src.TcpConnection;
import net.minecraft.src.ThreadLoginVerifier;

public class NetLoginHandler extends NetHandler {

   private byte[] field_72536_d;
   public static Logger field_72540_a = Logger.getLogger("Minecraft");
   private static Random field_72537_e = new Random();
   public TcpConnection field_72538_b;
   public boolean field_72539_c = false;
   private MinecraftServer field_72534_f;
   private int field_72535_g = 0;
   public String field_72543_h = null;
   private volatile boolean field_72544_i = false;
   private String field_72541_j = "";
   private SecretKey field_72542_k = null;


   public NetLoginHandler(MinecraftServer p_i3400_1_, Socket p_i3400_2_, String p_i3400_3_) throws IOException {
      this.field_72534_f = p_i3400_1_;
      this.field_72538_b = new TcpConnection(p_i3400_2_, p_i3400_3_, this, p_i3400_1_.func_71250_E().getPrivate());
      this.field_72538_b.field_74468_e = 0;
   }

   public void func_72532_c() {
      if(this.field_72544_i) {
         this.func_72529_d();
      }

      if(this.field_72535_g++ == 600) {
         this.func_72527_a("Took too long to log in");
      } else {
         this.field_72538_b.func_74428_b();
      }

   }

   public void func_72527_a(String p_72527_1_) {
      try {
         field_72540_a.info("Disconnecting " + this.func_72528_e() + ": " + p_72527_1_);
         this.field_72538_b.func_74429_a(new Packet255KickDisconnect(p_72527_1_));
         this.field_72538_b.func_74423_d();
         this.field_72539_c = true;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void func_72500_a(Packet2ClientProtocol p_72500_1_) {
      this.field_72543_h = p_72500_1_.func_73454_f();
      if(!this.field_72543_h.equals(StringUtils.func_76338_a(this.field_72543_h))) {
         this.func_72527_a("Invalid username!");
      } else {
         PublicKey var2 = this.field_72534_f.func_71250_E().getPublic();
         if(p_72500_1_.func_73453_d() != 49) {
            if(p_72500_1_.func_73453_d() > 49) {
               this.func_72527_a("Outdated server!");
            } else {
               this.func_72527_a("Outdated client!");
            }

         } else {
            this.field_72541_j = this.field_72534_f.func_71266_T()?Long.toString(field_72537_e.nextLong(), 16):"-";
            this.field_72536_d = new byte[4];
            field_72537_e.nextBytes(this.field_72536_d);
            this.field_72538_b.func_74429_a(new Packet253ServerAuthData(this.field_72541_j, var2, this.field_72536_d));
         }
      }
   }

   public void func_72513_a(Packet252SharedKey p_72513_1_) {
      PrivateKey var2 = this.field_72534_f.func_71250_E().getPrivate();
      this.field_72542_k = p_72513_1_.func_73303_a(var2);
      if(!Arrays.equals(this.field_72536_d, p_72513_1_.func_73302_b(var2))) {
         this.func_72527_a("Invalid client reply");
      }

      this.field_72538_b.func_74429_a(new Packet252SharedKey());
   }

   public void func_72458_a(Packet205ClientCommand p_72458_1_) {
      if(p_72458_1_.field_73447_a == 0) {
         if(this.field_72534_f.func_71266_T()) {
            (new ThreadLoginVerifier(this)).start();
         } else {
            this.field_72544_i = true;
         }
      }

   }

   public void func_72455_a(Packet1Login p_72455_1_) {}

   public void func_72529_d() {
      String var1 = this.field_72534_f.func_71203_ab().func_72399_a(this.field_72538_b.func_74430_c(), this.field_72543_h);
      if(var1 != null) {
         this.func_72527_a(var1);
      } else {
         EntityPlayerMP var2 = this.field_72534_f.func_71203_ab().func_72366_a(this.field_72543_h);
         if(var2 != null) {
            this.field_72534_f.func_71203_ab().func_72355_a(this.field_72538_b, var2);
         }
      }

      this.field_72539_c = true;
   }

   public void func_72515_a(String p_72515_1_, Object[] p_72515_2_) {
      field_72540_a.info(this.func_72528_e() + " lost connection");
      this.field_72539_c = true;
   }

   public void func_72467_a(Packet254ServerPing p_72467_1_) {
      try {
         ServerConfigurationManager var2 = this.field_72534_f.func_71203_ab();
         String var3 = null;
         if(p_72467_1_.field_82559_a == 1) {
            List var4 = Arrays.asList(new Serializable[]{Integer.valueOf(1), Integer.valueOf(49), this.field_72534_f.func_71249_w(), this.field_72534_f.func_71273_Y(), Integer.valueOf(var2.func_72394_k()), Integer.valueOf(var2.func_72352_l())});

            Object var6;
            for(Iterator var5 = var4.iterator(); var5.hasNext(); var3 = var3 + var6.toString().replaceAll("\u0000", "")) {
               var6 = var5.next();
               if(var3 == null) {
                  var3 = "\u00a7";
               } else {
                  var3 = var3 + "\u0000";
               }
            }
         } else {
            var3 = this.field_72534_f.func_71273_Y() + "\u00a7" + var2.func_72394_k() + "\u00a7" + var2.func_72352_l();
         }

         InetAddress var8 = null;
         if(this.field_72538_b.func_74452_g() != null) {
            var8 = this.field_72538_b.func_74452_g().getInetAddress();
         }

         this.field_72538_b.func_74429_a(new Packet255KickDisconnect(var3));
         this.field_72538_b.func_74423_d();
         if(var8 != null && this.field_72534_f.func_71212_ac() instanceof DedicatedServerListenThread) {
            ((DedicatedServerListenThread)this.field_72534_f.func_71212_ac()).func_71761_a(var8);
         }

         this.field_72539_c = true;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public void func_72509_a(Packet p_72509_1_) {
      this.func_72527_a("Protocol error");
   }

   public String func_72528_e() {
      return this.field_72543_h != null?this.field_72543_h + " [" + this.field_72538_b.func_74430_c().toString() + "]":this.field_72538_b.func_74430_c().toString();
   }

   public boolean func_72489_a() {
      return true;
   }

   // $FF: synthetic method
   static String func_72526_a(NetLoginHandler p_72526_0_) {
      return p_72526_0_.field_72541_j;
   }

   // $FF: synthetic method
   static MinecraftServer func_72530_b(NetLoginHandler p_72530_0_) {
      return p_72530_0_.field_72534_f;
   }

   // $FF: synthetic method
   static SecretKey func_72525_c(NetLoginHandler p_72525_0_) {
      return p_72525_0_.field_72542_k;
   }

   // $FF: synthetic method
   static String func_72533_d(NetLoginHandler p_72533_0_) {
      return p_72533_0_.field_72543_h;
   }

   // $FF: synthetic method
   public static boolean func_72531_a(NetLoginHandler p_72531_0_, boolean p_72531_1_) {
      return p_72531_0_.field_72544_i = p_72531_1_;
   }

}
