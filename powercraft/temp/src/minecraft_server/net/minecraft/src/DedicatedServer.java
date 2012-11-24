package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CallableServerType;
import net.minecraft.src.CallableType;
import net.minecraft.src.ConsoleLogManager;
import net.minecraft.src.CrashReport;
import net.minecraft.src.CryptManager;
import net.minecraft.src.DedicatedPlayerList;
import net.minecraft.src.DedicatedServerCommandThread;
import net.minecraft.src.DedicatedServerListenThread;
import net.minecraft.src.DedicatedServerSleepThread;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.IServer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NetworkListenThread;
import net.minecraft.src.PlayerUsageSnooper;
import net.minecraft.src.PropertyManager;
import net.minecraft.src.RConThreadMain;
import net.minecraft.src.RConThreadQuery;
import net.minecraft.src.ServerCommand;
import net.minecraft.src.ServerConfigurationManager;
import net.minecraft.src.ServerGUI;
import net.minecraft.src.WorldSettings;
import net.minecraft.src.WorldType;

public class DedicatedServer extends MinecraftServer implements IServer {

   private final List field_71341_l = Collections.synchronizedList(new ArrayList());
   private RConThreadQuery field_71342_m;
   private RConThreadMain field_71339_n;
   private PropertyManager field_71340_o;
   private boolean field_71338_p;
   private EnumGameType field_71337_q;
   private NetworkListenThread field_71336_r;
   private boolean field_71335_s = false;


   public DedicatedServer(File p_i3382_1_) {
      super(p_i3382_1_);
      new DedicatedServerSleepThread(this);
   }

   protected boolean func_71197_b() throws IOException {
      DedicatedServerCommandThread var1 = new DedicatedServerCommandThread(this);
      var1.setDaemon(true);
      var1.start();
      ConsoleLogManager.func_73699_a();
      field_71306_a.info("Starting minecraft server version 1.4.4");
      if(Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
         field_71306_a.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
      }

      field_71306_a.info("Loading properties");
      this.field_71340_o = new PropertyManager(new File("server.properties"));
      if(this.func_71264_H()) {
         this.func_71189_e("127.0.0.1");
      } else {
         this.func_71229_d(this.field_71340_o.func_73670_a("online-mode", true));
         this.func_71189_e(this.field_71340_o.func_73671_a("server-ip", ""));
      }

      this.func_71251_e(this.field_71340_o.func_73670_a("spawn-animals", true));
      this.func_71257_f(this.field_71340_o.func_73670_a("spawn-npcs", true));
      this.func_71188_g(this.field_71340_o.func_73670_a("pvp", true));
      this.func_71245_h(this.field_71340_o.func_73670_a("allow-flight", false));
      this.func_71269_o(this.field_71340_o.func_73671_a("texture-pack", ""));
      this.func_71205_p(this.field_71340_o.func_73671_a("motd", "A Minecraft Server"));
      if(this.field_71340_o.func_73669_a("difficulty", 1) < 0) {
         this.field_71340_o.func_73667_a("difficulty", Integer.valueOf(0));
      } else if(this.field_71340_o.func_73669_a("difficulty", 1) > 3) {
         this.field_71340_o.func_73667_a("difficulty", Integer.valueOf(3));
      }

      this.field_71338_p = this.field_71340_o.func_73670_a("generate-structures", true);
      int var2 = this.field_71340_o.func_73669_a("gamemode", EnumGameType.SURVIVAL.func_77148_a());
      this.field_71337_q = WorldSettings.func_77161_a(var2);
      field_71306_a.info("Default game type: " + this.field_71337_q);
      InetAddress var3 = null;
      if(this.func_71211_k().length() > 0) {
         var3 = InetAddress.getByName(this.func_71211_k());
      }

      if(this.func_71215_F() < 0) {
         this.func_71208_b(this.field_71340_o.func_73669_a("server-port", 25565));
      }

      field_71306_a.info("Generating keypair");
      this.func_71253_a(CryptManager.func_75891_b());
      field_71306_a.info("Starting Minecraft server on " + (this.func_71211_k().length() == 0?"*":this.func_71211_k()) + ":" + this.func_71215_F());

      try {
         this.field_71336_r = new DedicatedServerListenThread(this, var3, this.func_71215_F());
      } catch (IOException var16) {
         field_71306_a.warning("**** FAILED TO BIND TO PORT!");
         field_71306_a.log(Level.WARNING, "The exception was: " + var16.toString());
         field_71306_a.warning("Perhaps a server is already running on that port?");
         return false;
      }

      if(!this.func_71266_T()) {
         field_71306_a.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
         field_71306_a.warning("The server will make no attempt to authenticate usernames. Beware.");
         field_71306_a.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
         field_71306_a.warning("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
      }

      this.func_71210_a(new DedicatedPlayerList(this));
      long var4 = System.nanoTime();
      if(this.func_71270_I() == null) {
         this.func_71261_m(this.field_71340_o.func_73671_a("level-name", "world"));
      }

      String var6 = this.field_71340_o.func_73671_a("level-seed", "");
      String var7 = this.field_71340_o.func_73671_a("level-type", "DEFAULT");
      String var8 = this.field_71340_o.func_73671_a("generator-settings", "");
      long var9 = (new Random()).nextLong();
      if(var6.length() > 0) {
         try {
            long var11 = Long.parseLong(var6);
            if(var11 != 0L) {
               var9 = var11;
            }
         } catch (NumberFormatException var15) {
            var9 = (long)var6.hashCode();
         }
      }

      WorldType var17 = WorldType.func_77130_a(var7);
      if(var17 == null) {
         var17 = WorldType.field_77137_b;
      }

      this.func_71191_d(this.field_71340_o.func_73669_a("max-build-height", 256));
      this.func_71191_d((this.func_71207_Z() + 8) / 16 * 16);
      this.func_71191_d(MathHelper.func_76125_a(this.func_71207_Z(), 64, 256));
      this.field_71340_o.func_73667_a("max-build-height", Integer.valueOf(this.func_71207_Z()));
      field_71306_a.info("Preparing level \"" + this.func_71270_I() + "\"");
      this.func_71247_a(this.func_71270_I(), this.func_71270_I(), var9, var17, var8);
      long var12 = System.nanoTime() - var4;
      String var14 = String.format("%.3fs", new Object[]{Double.valueOf((double)var12 / 1.0E9D)});
      field_71306_a.info("Done (" + var14 + ")! For help, type \"help\" or \"?\"");
      if(this.field_71340_o.func_73670_a("enable-query", false)) {
         field_71306_a.info("Starting GS4 status listener");
         this.field_71342_m = new RConThreadQuery(this);
         this.field_71342_m.func_72602_a();
      }

      if(this.field_71340_o.func_73670_a("enable-rcon", false)) {
         field_71306_a.info("Starting remote control listener");
         this.field_71339_n = new RConThreadMain(this);
         this.field_71339_n.func_72602_a();
      }

      return true;
   }

   public boolean func_71225_e() {
      return this.field_71338_p;
   }

   public EnumGameType func_71265_f() {
      return this.field_71337_q;
   }

   public int func_71232_g() {
      return this.field_71340_o.func_73669_a("difficulty", 1);
   }

   public boolean func_71199_h() {
      return this.field_71340_o.func_73670_a("hardcore", false);
   }

   protected void func_71228_a(CrashReport p_71228_1_) {
      while(this.func_71278_l()) {
         this.func_71333_ah();

         try {
            Thread.sleep(10L);
         } catch (InterruptedException var3) {
            var3.printStackTrace();
         }
      }

   }

   public CrashReport func_71230_b(CrashReport p_71230_1_) {
      p_71230_1_ = super.func_71230_b(p_71230_1_);
      p_71230_1_.func_85056_g().func_71500_a("Is Modded", new CallableType(this));
      p_71230_1_.func_85056_g().func_71500_a("Type", new CallableServerType(this));
      return p_71230_1_;
   }

   protected void func_71240_o() {
      System.exit(0);
   }

   protected void func_71190_q() {
      super.func_71190_q();
      this.func_71333_ah();
   }

   public boolean func_71255_r() {
      return this.field_71340_o.func_73670_a("allow-nether", true);
   }

   public boolean func_71193_K() {
      return this.field_71340_o.func_73670_a("spawn-monsters", true);
   }

   public void func_70000_a(PlayerUsageSnooper p_70000_1_) {
      p_70000_1_.func_76472_a("whitelist_enabled", Boolean.valueOf(this.func_71334_ai().func_72383_n()));
      p_70000_1_.func_76472_a("whitelist_count", Integer.valueOf(this.func_71334_ai().func_72388_h().size()));
      super.func_70000_a(p_70000_1_);
   }

   public boolean func_70002_Q() {
      return this.field_71340_o.func_73670_a("snooper-enabled", true);
   }

   public void func_71331_a(String p_71331_1_, ICommandSender p_71331_2_) {
      this.field_71341_l.add(new ServerCommand(p_71331_1_, p_71331_2_));
   }

   public void func_71333_ah() {
      while(!this.field_71341_l.isEmpty()) {
         ServerCommand var1 = (ServerCommand)this.field_71341_l.remove(0);
         this.func_71187_D().func_71556_a(var1.field_73701_b, var1.field_73702_a);
      }

   }

   public boolean func_71262_S() {
      return true;
   }

   public DedicatedPlayerList func_71334_ai() {
      return (DedicatedPlayerList)super.func_71203_ab();
   }

   public NetworkListenThread func_71212_ac() {
      return this.field_71336_r;
   }

   public int func_71327_a(String p_71327_1_, int p_71327_2_) {
      return this.field_71340_o.func_73669_a(p_71327_1_, p_71327_2_);
   }

   public String func_71330_a(String p_71330_1_, String p_71330_2_) {
      return this.field_71340_o.func_73671_a(p_71330_1_, p_71330_2_);
   }

   public boolean func_71332_a(String p_71332_1_, boolean p_71332_2_) {
      return this.field_71340_o.func_73670_a(p_71332_1_, p_71332_2_);
   }

   public void func_71328_a(String p_71328_1_, Object p_71328_2_) {
      this.field_71340_o.func_73667_a(p_71328_1_, p_71328_2_);
   }

   public void func_71326_a() {
      this.field_71340_o.func_73668_b();
   }

   public String func_71329_c() {
      File var1 = this.field_71340_o.func_73665_c();
      return var1 != null?var1.getAbsolutePath():"No settings file";
   }

   public boolean func_71279_ae() {
      return this.field_71335_s;
   }

   public String func_71206_a(EnumGameType p_71206_1_, boolean p_71206_2_) {
      return "";
   }

   public boolean func_82356_Z() {
      return this.field_71340_o.func_73670_a("enable-command-block", false);
   }

   public int func_82357_ak() {
      return this.field_71340_o.func_73669_a("spawn-protection", super.func_82357_ak());
   }

   // $FF: synthetic method
   public ServerConfigurationManager func_71203_ab() {
      return this.func_71334_ai();
   }

   @SideOnly(Side.SERVER)
   public void func_82011_an() {
      ServerGUI.func_79003_a(this);
      this.field_71335_s = true;
   }
}
