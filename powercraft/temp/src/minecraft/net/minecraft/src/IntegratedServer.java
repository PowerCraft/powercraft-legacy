package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.File;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CallableIsModded;
import net.minecraft.src.CallableType3;
import net.minecraft.src.CrashReport;
import net.minecraft.src.CryptManager;
import net.minecraft.src.DemoWorldServer;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.IntegratedPlayerList;
import net.minecraft.src.IntegratedServerListenThread;
import net.minecraft.src.NetworkListenThread;
import net.minecraft.src.PlayerUsageSnooper;
import net.minecraft.src.ThreadLanServerPing;
import net.minecraft.src.WorldManager;
import net.minecraft.src.WorldServer;
import net.minecraft.src.WorldServerMulti;
import net.minecraft.src.WorldSettings;
import net.minecraft.src.WorldType;

@SideOnly(Side.CLIENT)
public class IntegratedServer extends MinecraftServer {

   private final Minecraft field_71349_l;
   private final WorldSettings field_71350_m;
   private IntegratedServerListenThread field_71347_n;
   private boolean field_71348_o = false;
   private boolean field_71346_p;
   private ThreadLanServerPing field_71345_q;


   public IntegratedServer(Minecraft p_i3118_1_, String p_i3118_2_, String p_i3118_3_, WorldSettings p_i3118_4_) {
      super(new File(Minecraft.func_71380_b(), "saves"));
      this.func_71224_l(p_i3118_1_.field_71449_j.field_74286_b);
      this.func_71261_m(p_i3118_2_);
      this.func_71246_n(p_i3118_3_);
      this.func_71204_b(p_i3118_1_.func_71355_q());
      this.func_71194_c(p_i3118_4_.func_77167_c());
      this.func_71191_d(256);
      this.func_71210_a(new IntegratedPlayerList(this));
      this.field_71349_l = p_i3118_1_;
      this.field_71350_m = p_i3118_4_;

      try {
         this.field_71347_n = new IntegratedServerListenThread(this);
      } catch (IOException var6) {
         throw new Error();
      }
   }

   protected void func_71247_a(String p_71247_1_, String p_71247_2_, long p_71247_3_, WorldType p_71247_5_, String p_71247_6_) {
      this.func_71237_c(p_71247_1_);
      this.field_71305_c = new WorldServer[3];
      this.field_71312_k = new long[this.field_71305_c.length][100];
      ISaveHandler var7 = this.func_71254_M().func_75804_a(p_71247_1_, true);

      for(int var8 = 0; var8 < this.field_71305_c.length; ++var8) {
         byte var9 = 0;
         if(var8 == 1) {
            var9 = -1;
         }

         if(var8 == 2) {
            var9 = 1;
         }

         if(var8 == 0) {
            if(this.func_71242_L()) {
               this.field_71305_c[var8] = new DemoWorldServer(this, var7, p_71247_2_, var9, this.field_71304_b);
            } else {
               this.field_71305_c[var8] = new WorldServer(this, var7, p_71247_2_, var9, this.field_71350_m, this.field_71304_b);
            }
         } else {
            this.field_71305_c[var8] = new WorldServerMulti(this, var7, p_71247_2_, var9, this.field_71350_m, this.field_71305_c[0], this.field_71304_b);
         }

         this.field_71305_c[var8].func_72954_a(new WorldManager(this, this.field_71305_c[var8]));
         this.func_71203_ab().func_72364_a(this.field_71305_c);
      }

      this.func_71226_c(this.func_71232_g());
      this.func_71222_d();
   }

   protected boolean func_71197_b() throws IOException {
      field_71306_a.info("Starting integrated minecraft server version 1.4.4");
      this.func_71229_d(false);
      this.func_71251_e(true);
      this.func_71257_f(true);
      this.func_71188_g(true);
      this.func_71245_h(true);
      field_71306_a.info("Generating keypair");
      this.func_71253_a(CryptManager.func_75891_b());
      this.func_71247_a(this.func_71270_I(), this.func_71221_J(), this.field_71350_m.func_77160_d(), this.field_71350_m.func_77165_h(), this.field_71350_m.func_82749_j());
      this.func_71205_p(this.func_71214_G() + " - " + this.field_71305_c[0].func_72912_H().func_76065_j());
      return true;
   }

   protected void func_71217_p() {
      boolean var1 = this.field_71348_o;
      this.field_71348_o = this.field_71347_n.func_71752_f();
      if(!var1 && this.field_71348_o) {
         field_71306_a.info("Saving and pausing game...");
         this.func_71203_ab().func_72389_g();
         this.func_71267_a(false);
      }

      if(!this.field_71348_o) {
         super.func_71217_p();
      }

   }

   public boolean func_71225_e() {
      return false;
   }

   public EnumGameType func_71265_f() {
      return this.field_71350_m.func_77162_e();
   }

   public int func_71232_g() {
      return this.field_71349_l.field_71474_y.field_74318_M;
   }

   public boolean func_71199_h() {
      return this.field_71350_m.func_77158_f();
   }

   protected File func_71238_n() {
      return this.field_71349_l.field_71412_D;
   }

   public boolean func_71262_S() {
      return false;
   }

   public IntegratedServerListenThread func_71343_a() {
      return this.field_71347_n;
   }

   protected void func_71228_a(CrashReport p_71228_1_) {
      this.field_71349_l.func_71404_a(p_71228_1_);
   }

   public CrashReport func_71230_b(CrashReport p_71230_1_) {
      p_71230_1_ = super.func_71230_b(p_71230_1_);
      p_71230_1_.func_85056_g().func_71500_a("Type", new CallableType3(this));
      p_71230_1_.func_85056_g().func_71500_a("Is Modded", new CallableIsModded(this));
      return p_71230_1_;
   }

   public void func_70000_a(PlayerUsageSnooper p_70000_1_) {
      super.func_70000_a(p_70000_1_);
      p_70000_1_.func_76472_a("snooper_partner", this.field_71349_l.func_71378_E().func_80006_f());
   }

   public boolean func_70002_Q() {
      return Minecraft.func_71410_x().func_70002_Q();
   }

   public String func_71206_a(EnumGameType p_71206_1_, boolean p_71206_2_) {
      try {
         String var3 = this.field_71347_n.func_71755_c();
         System.out.println("Started on " + var3);
         this.field_71346_p = true;
         this.field_71345_q = new ThreadLanServerPing(this.func_71273_Y(), var3);
         this.field_71345_q.start();
         this.func_71203_ab().func_72357_a(p_71206_1_);
         this.func_71203_ab().func_72387_b(p_71206_2_);
         return var3;
      } catch (IOException var4) {
         return null;
      }
   }

   public void func_71260_j() {
      super.func_71260_j();
      if(this.field_71345_q != null) {
         this.field_71345_q.interrupt();
         this.field_71345_q = null;
      }

   }

   public void func_71263_m() {
      super.func_71263_m();
      if(this.field_71345_q != null) {
         this.field_71345_q.interrupt();
         this.field_71345_q = null;
      }

   }

   public boolean func_71344_c() {
      return this.field_71346_p;
   }

   public void func_71235_a(EnumGameType p_71235_1_) {
      this.func_71203_ab().func_72357_a(p_71235_1_);
   }

   public boolean func_82356_Z() {
      return true;
   }

   // $FF: synthetic method
   public NetworkListenThread func_71212_ac() {
      return this.func_71343_a();
   }
}
