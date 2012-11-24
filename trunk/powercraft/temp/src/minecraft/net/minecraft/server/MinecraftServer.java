package net.minecraft.server;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.src.AnvilSaveConverter;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BehaviorArrowDispense;
import net.minecraft.src.BehaviorBucketEmptyDispense;
import net.minecraft.src.BehaviorBucketFullDispense;
import net.minecraft.src.BehaviorDispenseBoat;
import net.minecraft.src.BehaviorDispenseFireball;
import net.minecraft.src.BehaviorDispenseMinecart;
import net.minecraft.src.BehaviorEggDispense;
import net.minecraft.src.BehaviorExpBottleDispense;
import net.minecraft.src.BehaviorMobEggDispense;
import net.minecraft.src.BehaviorPotionDispense;
import net.minecraft.src.BehaviorSnowballDispense;
import net.minecraft.src.BlockDispenser;
import net.minecraft.src.CallableIsServerModded;
import net.minecraft.src.CallableServerMemoryStats;
import net.minecraft.src.CallableServerProfiler;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.CommandBase;
import net.minecraft.src.ConvertingProgressUpdate;
import net.minecraft.src.CrashReport;
import net.minecraft.src.DedicatedServer;
import net.minecraft.src.DemoWorldServer;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.ICommandManager;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.IPlayerUsage;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.IUpdatePlayerListBox;
import net.minecraft.src.Item;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.NetworkListenThread;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet4UpdateTime;
import net.minecraft.src.PlayerUsageSnooper;
import net.minecraft.src.Profiler;
import net.minecraft.src.RConConsoleSource;
import net.minecraft.src.ReportedException;
import net.minecraft.src.ServerCommandManager;
import net.minecraft.src.ServerConfigurationManager;
import net.minecraft.src.StatList;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.StringUtils;
import net.minecraft.src.ThreadDedicatedServer;
import net.minecraft.src.ThreadMinecraftServer;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldManager;
import net.minecraft.src.WorldServer;
import net.minecraft.src.WorldServerMulti;
import net.minecraft.src.WorldSettings;
import net.minecraft.src.WorldType;

public abstract class MinecraftServer implements ICommandSender, Runnable, IPlayerUsage {

   public static Logger field_71306_a = Logger.getLogger("Minecraft");
   private static MinecraftServer field_71309_l = null;
   private final ISaveFormat field_71310_m;
   private final PlayerUsageSnooper field_71307_n = new PlayerUsageSnooper("server", this);
   private final File field_71308_o;
   private final List field_71322_p = new ArrayList();
   private final ICommandManager field_71321_q;
   public final Profiler field_71304_b = new Profiler();
   private String field_71320_r;
   private int field_71319_s = -1;
   public WorldServer[] field_71305_c;
   private ServerConfigurationManager field_71318_t;
   private boolean field_71317_u = true;
   private boolean field_71316_v = false;
   private int field_71315_w = 0;
   public String field_71302_d;
   public int field_71303_e;
   private boolean field_71325_x;
   private boolean field_71324_y;
   private boolean field_71323_z;
   private boolean field_71284_A;
   private boolean field_71285_B;
   private String field_71286_C;
   private int field_71280_D;
   private long field_71281_E;
   private long field_71282_F;
   private long field_71283_G;
   private long field_71291_H;
   public final long[] field_71300_f = new long[100];
   public final long[] field_71301_g = new long[100];
   public final long[] field_71313_h = new long[100];
   public final long[] field_71314_i = new long[100];
   public final long[] field_71311_j = new long[100];
   public long[][] field_71312_k;
   private KeyPair field_71292_I;
   private String field_71293_J;
   private String field_71294_K;
   @SideOnly(Side.CLIENT)
   private String field_71287_L;
   private boolean field_71288_M;
   private boolean field_71289_N;
   private boolean field_71290_O;
   private String field_71297_P = "";
   private boolean field_71296_Q = false;
   private long field_71299_R;
   private String field_71298_S;
   private boolean field_71295_T;


   public MinecraftServer(File p_i3375_1_) {
      field_71309_l = this;
      this.field_71308_o = p_i3375_1_;
      this.field_71321_q = new ServerCommandManager();
      this.field_71310_m = new AnvilSaveConverter(p_i3375_1_);
      this.func_82355_al();
   }

   private void func_82355_al() {
      BlockDispenser.field_82527_a.func_82595_a(Item.field_77704_l, new BehaviorArrowDispense(this));
      BlockDispenser.field_82527_a.func_82595_a(Item.field_77764_aP, new BehaviorEggDispense(this));
      BlockDispenser.field_82527_a.func_82595_a(Item.field_77768_aD, new BehaviorSnowballDispense(this));
      BlockDispenser.field_82527_a.func_82595_a(Item.field_77809_bD, new BehaviorExpBottleDispense(this));
      BlockDispenser.field_82527_a.func_82595_a(Item.field_77726_bs, new BehaviorPotionDispense(this));
      BlockDispenser.field_82527_a.func_82595_a(Item.field_77815_bC, new BehaviorMobEggDispense(this));
      BlockDispenser.field_82527_a.func_82595_a(Item.field_77811_bE, new BehaviorDispenseFireball(this));
      BehaviorDispenseMinecart var1 = new BehaviorDispenseMinecart(this);
      BlockDispenser.field_82527_a.func_82595_a(Item.field_77773_az, var1);
      BlockDispenser.field_82527_a.func_82595_a(Item.field_77762_aN, var1);
      BlockDispenser.field_82527_a.func_82595_a(Item.field_77763_aO, var1);
      BlockDispenser.field_82527_a.func_82595_a(Item.field_77769_aE, new BehaviorDispenseBoat(this));
      BehaviorBucketFullDispense var2 = new BehaviorBucketFullDispense(this);
      BlockDispenser.field_82527_a.func_82595_a(Item.field_77775_ay, var2);
      BlockDispenser.field_82527_a.func_82595_a(Item.field_77786_ax, var2);
      BlockDispenser.field_82527_a.func_82595_a(Item.field_77788_aw, new BehaviorBucketEmptyDispense(this));
   }

   protected abstract boolean func_71197_b() throws IOException;

   protected void func_71237_c(String p_71237_1_) {
      if(this.func_71254_M().func_75801_b(p_71237_1_)) {
         field_71306_a.info("Converting map!");
         this.func_71192_d("menu.convertingLevel");
         this.func_71254_M().func_75805_a(p_71237_1_, new ConvertingProgressUpdate(this));
      }

   }

   protected synchronized void func_71192_d(String p_71192_1_) {
      this.field_71298_S = p_71192_1_;
   }

   @SideOnly(Side.CLIENT)
   public synchronized String func_71195_b_() {
      return this.field_71298_S;
   }

   protected void func_71247_a(String p_71247_1_, String p_71247_2_, long p_71247_3_, WorldType p_71247_5_, String p_71247_6_) {
      this.func_71237_c(p_71247_1_);
      this.func_71192_d("menu.loadingLevel");
      this.field_71305_c = new WorldServer[3];
      this.field_71312_k = new long[this.field_71305_c.length][100];
      ISaveHandler var7 = this.field_71310_m.func_75804_a(p_71247_1_, true);
      WorldInfo var9 = var7.func_75757_d();
      WorldSettings var8;
      if(var9 == null) {
         var8 = new WorldSettings(p_71247_3_, this.func_71265_f(), this.func_71225_e(), this.func_71199_h(), p_71247_5_);
         var8.func_82750_a(p_71247_6_);
      } else {
         var8 = new WorldSettings(var9);
      }

      if(this.field_71289_N) {
         var8.func_77159_a();
      }

      for(int var10 = 0; var10 < this.field_71305_c.length; ++var10) {
         byte var11 = 0;
         if(var10 == 1) {
            var11 = -1;
         }

         if(var10 == 2) {
            var11 = 1;
         }

         if(var10 == 0) {
            if(this.func_71242_L()) {
               this.field_71305_c[var10] = new DemoWorldServer(this, var7, p_71247_2_, var11, this.field_71304_b);
            } else {
               this.field_71305_c[var10] = new WorldServer(this, var7, p_71247_2_, var11, var8, this.field_71304_b);
            }
         } else {
            this.field_71305_c[var10] = new WorldServerMulti(this, var7, p_71247_2_, var11, var8, this.field_71305_c[0], this.field_71304_b);
         }

         this.field_71305_c[var10].func_72954_a(new WorldManager(this, this.field_71305_c[var10]));
         if(!this.func_71264_H()) {
            this.field_71305_c[var10].func_72912_H().func_76060_a(this.func_71265_f());
         }

         this.field_71318_t.func_72364_a(this.field_71305_c);
      }

      this.func_71226_c(this.func_71232_g());
      this.func_71222_d();
   }

   protected void func_71222_d() {
      int var5 = 0;
      this.func_71192_d("menu.generatingTerrain");
      byte var6 = 0;
      field_71306_a.info("Preparing start region for level " + var6);
      WorldServer var7 = this.field_71305_c[var6];
      ChunkCoordinates var8 = var7.func_72861_E();
      long var9 = System.currentTimeMillis();

      for(int var11 = -192; var11 <= 192 && this.func_71278_l(); var11 += 16) {
         for(int var12 = -192; var12 <= 192 && this.func_71278_l(); var12 += 16) {
            long var13 = System.currentTimeMillis();
            if(var13 - var9 > 1000L) {
               this.func_71216_a_("Preparing spawn area", var5 * 100 / 625);
               var9 = var13;
            }

            ++var5;
            var7.field_73059_b.func_73158_c(var8.field_71574_a + var11 >> 4, var8.field_71573_c + var12 >> 4);
         }
      }

      this.func_71243_i();
   }

   public abstract boolean func_71225_e();

   public abstract EnumGameType func_71265_f();

   public abstract int func_71232_g();

   public abstract boolean func_71199_h();

   protected void func_71216_a_(String p_71216_1_, int p_71216_2_) {
      this.field_71302_d = p_71216_1_;
      this.field_71303_e = p_71216_2_;
      field_71306_a.info(p_71216_1_ + ": " + p_71216_2_ + "%");
   }

   protected void func_71243_i() {
      this.field_71302_d = null;
      this.field_71303_e = 0;
   }

   protected void func_71267_a(boolean p_71267_1_) {
      if(!this.field_71290_O) {
         WorldServer[] var2 = this.field_71305_c;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WorldServer var5 = var2[var4];
            if(var5 != null) {
               if(!p_71267_1_) {
                  field_71306_a.info("Saving chunks for level \'" + var5.func_72912_H().func_76065_j() + "\'/" + var5.field_73011_w.func_80007_l());
               }

               try {
                  var5.func_73044_a(true, (IProgressUpdate)null);
               } catch (MinecraftException var7) {
                  field_71306_a.warning(var7.getMessage());
               }
            }
         }

      }
   }

   public void func_71260_j() {
      if(!this.field_71290_O) {
         field_71306_a.info("Stopping server");
         if(this.func_71212_ac() != null) {
            this.func_71212_ac().func_71744_a();
         }

         if(this.field_71318_t != null) {
            field_71306_a.info("Saving players");
            this.field_71318_t.func_72389_g();
            this.field_71318_t.func_72392_r();
         }

         field_71306_a.info("Saving worlds");
         this.func_71267_a(false);

         for(int var1 = 0; var1 < this.field_71305_c.length; ++var1) {
            WorldServer var2 = this.field_71305_c[var1];
            var2.func_73041_k();
         }

         if(this.field_71307_n != null && this.field_71307_n.func_76468_d()) {
            this.field_71307_n.func_76470_e();
         }

      }
   }

   public String func_71211_k() {
      return this.field_71320_r;
   }

   public void func_71189_e(String p_71189_1_) {
      this.field_71320_r = p_71189_1_;
   }

   public boolean func_71278_l() {
      return this.field_71317_u;
   }

   public void func_71263_m() {
      this.field_71317_u = false;
   }

   public void run() {
      try {
         if(this.func_71197_b()) {
            long var1 = System.currentTimeMillis();

            for(long var50 = 0L; this.field_71317_u; this.field_71296_Q = true) {
               long var5 = System.currentTimeMillis();
               long var7 = var5 - var1;
               if(var7 > 2000L && var1 - this.field_71299_R >= 15000L) {
                  field_71306_a.warning("Can\'t keep up! Did the system time change, or is the server overloaded?");
                  var7 = 2000L;
                  this.field_71299_R = var1;
               }

               if(var7 < 0L) {
                  field_71306_a.warning("Time ran backwards! Did the system time change?");
                  var7 = 0L;
               }

               var50 += var7;
               var1 = var5;
               if(this.field_71305_c[0].func_73056_e()) {
                  this.func_71217_p();
                  var50 = 0L;
               } else {
                  while(var50 > 50L) {
                     var50 -= 50L;
                     this.func_71217_p();
                  }
               }

               Thread.sleep(1L);
            }
         } else {
            this.func_71228_a((CrashReport)null);
         }
      } catch (Throwable var48) {
         var48.printStackTrace();
         field_71306_a.log(Level.SEVERE, "Encountered an unexpected exception " + var48.getClass().getSimpleName(), var48);
         CrashReport var2 = null;
         if(var48 instanceof ReportedException) {
            var2 = this.func_71230_b(((ReportedException)var48).func_71575_a());
         } else {
            var2 = this.func_71230_b(new CrashReport("Exception in server tick loop", var48));
         }

         File var3 = new File(new File(this.func_71238_n(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");
         if(var2.func_71508_a(var3)) {
            field_71306_a.severe("This crash report has been saved to: " + var3.getAbsolutePath());
         } else {
            field_71306_a.severe("We were unable to save this crash report to disk.");
         }

         this.func_71228_a(var2);
      } finally {
         try {
            this.func_71260_j();
            this.field_71316_v = true;
         } catch (Throwable var46) {
            var46.printStackTrace();
         } finally {
            this.func_71240_o();
         }

      }

   }

   protected File func_71238_n() {
      return new File(".");
   }

   protected void func_71228_a(CrashReport p_71228_1_) {}

   protected void func_71240_o() {}

   public void func_71217_p() {
      long var1 = System.nanoTime();
      AxisAlignedBB.func_72332_a().func_72298_a();
      ++this.field_71315_w;
      if(this.field_71295_T) {
         this.field_71295_T = false;
         this.field_71304_b.field_76327_a = true;
         this.field_71304_b.func_76317_a();
      }

      this.field_71304_b.func_76320_a("root");
      this.func_71190_q();
      if(this.field_71315_w % 900 == 0) {
         this.field_71304_b.func_76320_a("save");
         this.field_71318_t.func_72389_g();
         this.func_71267_a(true);
         this.field_71304_b.func_76319_b();
      }

      this.field_71304_b.func_76320_a("tallying");
      this.field_71311_j[this.field_71315_w % 100] = System.nanoTime() - var1;
      this.field_71300_f[this.field_71315_w % 100] = Packet.field_73290_p - this.field_71281_E;
      this.field_71281_E = Packet.field_73290_p;
      this.field_71301_g[this.field_71315_w % 100] = Packet.field_73289_q - this.field_71282_F;
      this.field_71282_F = Packet.field_73289_q;
      this.field_71313_h[this.field_71315_w % 100] = Packet.field_73292_n - this.field_71283_G;
      this.field_71283_G = Packet.field_73292_n;
      this.field_71314_i[this.field_71315_w % 100] = Packet.field_73293_o - this.field_71291_H;
      this.field_71291_H = Packet.field_73293_o;
      this.field_71304_b.func_76319_b();
      this.field_71304_b.func_76320_a("snooper");
      if(!this.field_71307_n.func_76468_d() && this.field_71315_w > 100) {
         this.field_71307_n.func_76463_a();
      }

      if(this.field_71315_w % 6000 == 0) {
         this.field_71307_n.func_76471_b();
      }

      this.field_71304_b.func_76319_b();
      this.field_71304_b.func_76319_b();
   }

   public void func_71190_q() {
      this.field_71304_b.func_76320_a("levels");

      int var1;
      for(var1 = 0; var1 < this.field_71305_c.length; ++var1) {
         long var2 = System.nanoTime();
         if(var1 == 0 || this.func_71255_r()) {
            WorldServer var4 = this.field_71305_c[var1];
            this.field_71304_b.func_76320_a(var4.func_72912_H().func_76065_j());
            this.field_71304_b.func_76320_a("pools");
            var4.func_82732_R().func_72343_a();
            this.field_71304_b.func_76319_b();
            if(this.field_71315_w % 20 == 0) {
               this.field_71304_b.func_76320_a("timeSync");
               this.field_71318_t.func_72396_a(new Packet4UpdateTime(var4.func_82737_E(), var4.func_72820_D()), var4.field_73011_w.field_76574_g);
               this.field_71304_b.func_76319_b();
            }

            this.field_71304_b.func_76320_a("tick");

            CrashReport var6;
            try {
               var4.func_72835_b();
            } catch (Throwable var8) {
               var6 = CrashReport.func_85055_a(var8, "Exception ticking world");
               var4.func_72914_a(var6);
               throw new ReportedException(var6);
            }

            try {
               var4.func_72939_s();
            } catch (Throwable var7) {
               var6 = CrashReport.func_85055_a(var7, "Exception ticking world entities");
               var4.func_72914_a(var6);
               throw new ReportedException(var6);
            }

            this.field_71304_b.func_76319_b();
            this.field_71304_b.func_76320_a("tracker");
            var4.func_73039_n().func_72788_a();
            this.field_71304_b.func_76319_b();
            this.field_71304_b.func_76319_b();
         }

         this.field_71312_k[var1][this.field_71315_w % 100] = System.nanoTime() - var2;
      }

      this.field_71304_b.func_76318_c("connection");
      this.func_71212_ac().func_71747_b();
      this.field_71304_b.func_76318_c("players");
      this.field_71318_t.func_72374_b();
      this.field_71304_b.func_76318_c("tickables");

      for(var1 = 0; var1 < this.field_71322_p.size(); ++var1) {
         ((IUpdatePlayerListBox)this.field_71322_p.get(var1)).func_73660_a();
      }

      this.field_71304_b.func_76319_b();
   }

   public boolean func_71255_r() {
      return true;
   }

   public void func_71256_s() {
      (new ThreadMinecraftServer(this, "Server thread")).start();
   }

   public File func_71209_f(String p_71209_1_) {
      return new File(this.func_71238_n(), p_71209_1_);
   }

   public void func_71244_g(String p_71244_1_) {
      field_71306_a.info(p_71244_1_);
   }

   public void func_71236_h(String p_71236_1_) {
      field_71306_a.warning(p_71236_1_);
   }

   public WorldServer func_71218_a(int p_71218_1_) {
      return p_71218_1_ == -1?this.field_71305_c[1]:(p_71218_1_ == 1?this.field_71305_c[2]:this.field_71305_c[0]);
   }

   @SideOnly(Side.SERVER)
   public void func_82010_a(IUpdatePlayerListBox p_82010_1_) {
      this.field_71322_p.add(p_82010_1_);
   }

   public String func_71277_t() {
      return this.field_71320_r;
   }

   public int func_71234_u() {
      return this.field_71319_s;
   }

   public String func_71274_v() {
      return this.field_71286_C;
   }

   public String func_71249_w() {
      return "1.4.4";
   }

   public int func_71233_x() {
      return this.field_71318_t.func_72394_k();
   }

   public int func_71275_y() {
      return this.field_71318_t.func_72352_l();
   }

   public String[] func_71213_z() {
      return this.field_71318_t.func_72369_d();
   }

   public String func_71258_A() {
      return "";
   }

   public String func_71252_i(String p_71252_1_) {
      RConConsoleSource.field_70010_a.func_70007_b();
      this.field_71321_q.func_71556_a(RConConsoleSource.field_70010_a, p_71252_1_);
      return RConConsoleSource.field_70010_a.func_70008_c();
   }

   public boolean func_71239_B() {
      return false;
   }

   public void func_71201_j(String p_71201_1_) {
      field_71306_a.log(Level.SEVERE, p_71201_1_);
   }

   public void func_71198_k(String p_71198_1_) {
      if(this.func_71239_B()) {
         field_71306_a.log(Level.INFO, p_71198_1_);
      }

   }

   public String getServerModName() {
      return "vanilla";
   }

   public CrashReport func_71230_b(CrashReport p_71230_1_) {
      p_71230_1_.func_85056_g().func_71500_a("Profiler Position", new CallableIsServerModded(this));
      if(this.field_71305_c != null && this.field_71305_c.length > 0 && this.field_71305_c[0] != null) {
         p_71230_1_.func_85056_g().func_71500_a("Vec3 Pool Size", new CallableServerProfiler(this));
      }

      if(this.field_71318_t != null) {
         p_71230_1_.func_85056_g().func_71500_a("Player Count", new CallableServerMemoryStats(this));
      }

      return p_71230_1_;
   }

   public List func_71248_a(ICommandSender p_71248_1_, String p_71248_2_) {
      ArrayList var3 = new ArrayList();
      if(p_71248_2_.startsWith("/")) {
         p_71248_2_ = p_71248_2_.substring(1);
         boolean var10 = !p_71248_2_.contains(" ");
         List var11 = this.field_71321_q.func_71558_b(p_71248_1_, p_71248_2_);
         if(var11 != null) {
            Iterator var12 = var11.iterator();

            while(var12.hasNext()) {
               String var13 = (String)var12.next();
               if(var10) {
                  var3.add("/" + var13);
               } else {
                  var3.add(var13);
               }
            }
         }

         return var3;
      } else {
         String[] var4 = p_71248_2_.split(" ", -1);
         String var5 = var4[var4.length - 1];
         String[] var6 = this.field_71318_t.func_72369_d();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String var9 = var6[var8];
            if(CommandBase.func_71523_a(var5, var9)) {
               var3.add(var9);
            }
         }

         return var3;
      }
   }

   public static MinecraftServer func_71276_C() {
      return field_71309_l;
   }

   public String func_70005_c_() {
      return "Server";
   }

   public void func_70006_a(String p_70006_1_) {
      field_71306_a.info(StringUtils.func_76338_a(p_70006_1_));
   }

   public boolean func_70003_b(int p_70003_1_, String p_70003_2_) {
      return true;
   }

   public String func_70004_a(String p_70004_1_, Object ... p_70004_2_) {
      return StringTranslate.func_74808_a().func_74803_a(p_70004_1_, p_70004_2_);
   }

   public ICommandManager func_71187_D() {
      return this.field_71321_q;
   }

   public KeyPair func_71250_E() {
      return this.field_71292_I;
   }

   public int func_71215_F() {
      return this.field_71319_s;
   }

   public void func_71208_b(int p_71208_1_) {
      this.field_71319_s = p_71208_1_;
   }

   public String func_71214_G() {
      return this.field_71293_J;
   }

   public void func_71224_l(String p_71224_1_) {
      this.field_71293_J = p_71224_1_;
   }

   public boolean func_71264_H() {
      return this.field_71293_J != null;
   }

   public String func_71270_I() {
      return this.field_71294_K;
   }

   public void func_71261_m(String p_71261_1_) {
      this.field_71294_K = p_71261_1_;
   }

   @SideOnly(Side.CLIENT)
   public void func_71246_n(String p_71246_1_) {
      this.field_71287_L = p_71246_1_;
   }

   @SideOnly(Side.CLIENT)
   public String func_71221_J() {
      return this.field_71287_L;
   }

   public void func_71253_a(KeyPair p_71253_1_) {
      this.field_71292_I = p_71253_1_;
   }

   public void func_71226_c(int p_71226_1_) {
      for(int var2 = 0; var2 < this.field_71305_c.length; ++var2) {
         WorldServer var3 = this.field_71305_c[var2];
         if(var3 != null) {
            if(var3.func_72912_H().func_76093_s()) {
               var3.field_73013_u = 3;
               var3.func_72891_a(true, true);
            } else if(this.func_71264_H()) {
               var3.field_73013_u = p_71226_1_;
               var3.func_72891_a(var3.field_73013_u > 0, true);
            } else {
               var3.field_73013_u = p_71226_1_;
               var3.func_72891_a(this.func_71193_K(), this.field_71324_y);
            }
         }
      }

   }

   protected boolean func_71193_K() {
      return true;
   }

   public boolean func_71242_L() {
      return this.field_71288_M;
   }

   public void func_71204_b(boolean p_71204_1_) {
      this.field_71288_M = p_71204_1_;
   }

   public void func_71194_c(boolean p_71194_1_) {
      this.field_71289_N = p_71194_1_;
   }

   public ISaveFormat func_71254_M() {
      return this.field_71310_m;
   }

   public void func_71272_O() {
      this.field_71290_O = true;
      this.func_71254_M().func_75800_d();

      for(int var1 = 0; var1 < this.field_71305_c.length; ++var1) {
         WorldServer var2 = this.field_71305_c[var1];
         if(var2 != null) {
            var2.func_73041_k();
         }
      }

      this.func_71254_M().func_75802_e(this.field_71305_c[0].func_72860_G().func_75760_g());
      this.func_71263_m();
   }

   public String func_71202_P() {
      return this.field_71297_P;
   }

   public void func_71269_o(String p_71269_1_) {
      this.field_71297_P = p_71269_1_;
   }

   public void func_70000_a(PlayerUsageSnooper p_70000_1_) {
      p_70000_1_.func_76472_a("whitelist_enabled", Boolean.valueOf(false));
      p_70000_1_.func_76472_a("whitelist_count", Integer.valueOf(0));
      p_70000_1_.func_76472_a("players_current", Integer.valueOf(this.func_71233_x()));
      p_70000_1_.func_76472_a("players_max", Integer.valueOf(this.func_71275_y()));
      p_70000_1_.func_76472_a("players_seen", Integer.valueOf(this.field_71318_t.func_72373_m().length));
      p_70000_1_.func_76472_a("uses_auth", Boolean.valueOf(this.field_71325_x));
      p_70000_1_.func_76472_a("gui_state", this.func_71279_ae()?"enabled":"disabled");
      p_70000_1_.func_76472_a("avg_tick_ms", Integer.valueOf((int)(MathHelper.func_76127_a(this.field_71311_j) * 1.0E-6D)));
      p_70000_1_.func_76472_a("avg_sent_packet_count", Integer.valueOf((int)MathHelper.func_76127_a(this.field_71300_f)));
      p_70000_1_.func_76472_a("avg_sent_packet_size", Integer.valueOf((int)MathHelper.func_76127_a(this.field_71301_g)));
      p_70000_1_.func_76472_a("avg_rec_packet_count", Integer.valueOf((int)MathHelper.func_76127_a(this.field_71313_h)));
      p_70000_1_.func_76472_a("avg_rec_packet_size", Integer.valueOf((int)MathHelper.func_76127_a(this.field_71314_i)));
      int var2 = 0;

      for(int var3 = 0; var3 < this.field_71305_c.length; ++var3) {
         if(this.field_71305_c[var3] != null) {
            WorldServer var4 = this.field_71305_c[var3];
            WorldInfo var5 = var4.func_72912_H();
            p_70000_1_.func_76472_a("world[" + var2 + "][dimension]", Integer.valueOf(var4.field_73011_w.field_76574_g));
            p_70000_1_.func_76472_a("world[" + var2 + "][mode]", var5.func_76077_q());
            p_70000_1_.func_76472_a("world[" + var2 + "][difficulty]", Integer.valueOf(var4.field_73013_u));
            p_70000_1_.func_76472_a("world[" + var2 + "][hardcore]", Boolean.valueOf(var5.func_76093_s()));
            p_70000_1_.func_76472_a("world[" + var2 + "][generator_name]", var5.func_76067_t().func_77127_a());
            p_70000_1_.func_76472_a("world[" + var2 + "][generator_version]", Integer.valueOf(var5.func_76067_t().func_77131_c()));
            p_70000_1_.func_76472_a("world[" + var2 + "][height]", Integer.valueOf(this.field_71280_D));
            p_70000_1_.func_76472_a("world[" + var2 + "][chunks_loaded]", Integer.valueOf(var4.func_72863_F().func_73152_e()));
            ++var2;
         }
      }

      p_70000_1_.func_76472_a("worlds", Integer.valueOf(var2));
   }

   public void func_70001_b(PlayerUsageSnooper p_70001_1_) {
      p_70001_1_.func_76472_a("singleplayer", Boolean.valueOf(this.func_71264_H()));
      p_70001_1_.func_76472_a("server_brand", this.getServerModName());
      p_70001_1_.func_76472_a("gui_supported", GraphicsEnvironment.isHeadless()?"headless":"supported");
      p_70001_1_.func_76472_a("dedicated", Boolean.valueOf(this.func_71262_S()));
   }

   public boolean func_70002_Q() {
      return true;
   }

   public int func_71227_R() {
      return 16;
   }

   public abstract boolean func_71262_S();

   public boolean func_71266_T() {
      return this.field_71325_x;
   }

   public void func_71229_d(boolean p_71229_1_) {
      this.field_71325_x = p_71229_1_;
   }

   public boolean func_71268_U() {
      return this.field_71324_y;
   }

   public void func_71251_e(boolean p_71251_1_) {
      this.field_71324_y = p_71251_1_;
   }

   public boolean func_71220_V() {
      return this.field_71323_z;
   }

   public void func_71257_f(boolean p_71257_1_) {
      this.field_71323_z = p_71257_1_;
   }

   public boolean func_71219_W() {
      return this.field_71284_A;
   }

   public void func_71188_g(boolean p_71188_1_) {
      this.field_71284_A = p_71188_1_;
   }

   public boolean func_71231_X() {
      return this.field_71285_B;
   }

   public void func_71245_h(boolean p_71245_1_) {
      this.field_71285_B = p_71245_1_;
   }

   public abstract boolean func_82356_Z();

   public String func_71273_Y() {
      return this.field_71286_C;
   }

   public void func_71205_p(String p_71205_1_) {
      this.field_71286_C = p_71205_1_;
   }

   public int func_71207_Z() {
      return this.field_71280_D;
   }

   public void func_71191_d(int p_71191_1_) {
      this.field_71280_D = p_71191_1_;
   }

   public boolean func_71241_aa() {
      return this.field_71316_v;
   }

   public ServerConfigurationManager func_71203_ab() {
      return this.field_71318_t;
   }

   public void func_71210_a(ServerConfigurationManager p_71210_1_) {
      this.field_71318_t = p_71210_1_;
   }

   public void func_71235_a(EnumGameType p_71235_1_) {
      for(int var2 = 0; var2 < this.field_71305_c.length; ++var2) {
         func_71276_C().field_71305_c[var2].func_72912_H().func_76060_a(p_71235_1_);
      }

   }

   public abstract NetworkListenThread func_71212_ac();

   @SideOnly(Side.CLIENT)
   public boolean func_71200_ad() {
      return this.field_71296_Q;
   }

   public boolean func_71279_ae() {
      return false;
   }

   public abstract String func_71206_a(EnumGameType var1, boolean var2);

   public int func_71259_af() {
      return this.field_71315_w;
   }

   public void func_71223_ag() {
      this.field_71295_T = true;
   }

   @SideOnly(Side.CLIENT)
   public PlayerUsageSnooper func_80003_ah() {
      return this.field_71307_n;
   }

   public ChunkCoordinates func_82114_b() {
      return new ChunkCoordinates(0, 0, 0);
   }

   public int func_82357_ak() {
      return 16;
   }

   // $FF: synthetic method
   public static ServerConfigurationManager func_71196_a(MinecraftServer p_71196_0_) {
      return p_71196_0_.field_71318_t;
   }

   @SideOnly(Side.SERVER)
   public static void main(String[] p_main_0_) {
      StatList.func_75919_a();

      try {
         boolean var1 = !GraphicsEnvironment.isHeadless();
         String var2 = null;
         String var3 = ".";
         String var4 = null;
         boolean var5 = false;
         boolean var6 = false;
         int var7 = -1;

         for(int var8 = 0; var8 < p_main_0_.length; ++var8) {
            String var9 = p_main_0_[var8];
            String var10 = var8 == p_main_0_.length - 1?null:p_main_0_[var8 + 1];
            boolean var11 = false;
            if(!var9.equals("nogui") && !var9.equals("--nogui")) {
               if(var9.equals("--port") && var10 != null) {
                  var11 = true;

                  try {
                     var7 = Integer.parseInt(var10);
                  } catch (NumberFormatException var13) {
                     ;
                  }
               } else if(var9.equals("--singleplayer") && var10 != null) {
                  var11 = true;
                  var2 = var10;
               } else if(var9.equals("--universe") && var10 != null) {
                  var11 = true;
                  var3 = var10;
               } else if(var9.equals("--world") && var10 != null) {
                  var11 = true;
                  var4 = var10;
               } else if(var9.equals("--demo")) {
                  var5 = true;
               } else if(var9.equals("--bonusChest")) {
                  var6 = true;
               }
            } else {
               var1 = false;
            }

            if(var11) {
               ++var8;
            }
         }

         DedicatedServer var15 = new DedicatedServer(new File(var3));
         if(var2 != null) {
            var15.func_71224_l(var2);
         }

         if(var4 != null) {
            var15.func_71261_m(var4);
         }

         if(var7 >= 0) {
            var15.func_71208_b(var7);
         }

         if(var5) {
            var15.func_71204_b(true);
         }

         if(var6) {
            var15.func_71194_c(true);
         }

         if(var1) {
            var15.func_82011_an();
         }

         var15.func_71256_s();
         Runtime.getRuntime().addShutdownHook(new ThreadDedicatedServer(var15));
      } catch (Exception var14) {
         field_71306_a.log(Level.SEVERE, "Failed to start the minecraft server", var14);
      }

   }
}
