package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.src.NetLoginHandler;
import net.minecraft.src.NetworkListenThread;

public class ServerListenThread extends Thread {

   private static Logger field_71777_a = Logger.getLogger("Minecraft");
   private final List field_71775_b = Collections.synchronizedList(new ArrayList());
   private final HashMap field_71776_c = new HashMap();
   private int field_71773_d = 0;
   private final ServerSocket field_71774_e;
   private NetworkListenThread field_71771_f;
   private final InetAddress field_71772_g;
   private final int field_71778_h;


   public ServerListenThread(NetworkListenThread p_i3384_1_, InetAddress p_i3384_2_, int p_i3384_3_) throws IOException {
      super("Listen thread");
      this.field_71771_f = p_i3384_1_;
      this.field_71778_h = p_i3384_3_;
      this.field_71774_e = new ServerSocket(p_i3384_3_, 0, p_i3384_2_);
      this.field_71772_g = p_i3384_2_ == null?this.field_71774_e.getInetAddress():p_i3384_2_;
      this.field_71774_e.setPerformancePreferences(0, 2, 1);
   }

   public void func_71766_a() {
      List var1 = this.field_71775_b;
      synchronized(this.field_71775_b) {
         for(int var2 = 0; var2 < this.field_71775_b.size(); ++var2) {
            NetLoginHandler var3 = (NetLoginHandler)this.field_71775_b.get(var2);

            try {
               var3.func_72532_c();
            } catch (Exception var6) {
               var3.func_72527_a("Internal server error");
               field_71777_a.log(Level.WARNING, "Failed to handle packet for " + var3.func_72528_e() + ": " + var6, var6);
            }

            if(var3.field_72539_c) {
               this.field_71775_b.remove(var2--);
            }

            var3.field_72538_b.func_74427_a();
         }

      }
   }

   public void run() {
      while(this.field_71771_f.field_71749_b) {
         try {
            Socket var1 = this.field_71774_e.accept();
            InetAddress var2 = var1.getInetAddress();
            long var3 = System.currentTimeMillis();
            HashMap var5 = this.field_71776_c;
            synchronized(this.field_71776_c) {
               if(this.field_71776_c.containsKey(var2) && !func_71770_b(var2) && var3 - ((Long)this.field_71776_c.get(var2)).longValue() < 4000L) {
                  this.field_71776_c.put(var2, Long.valueOf(var3));
                  var1.close();
                  continue;
               }

               this.field_71776_c.put(var2, Long.valueOf(var3));
            }

            NetLoginHandler var9 = new NetLoginHandler(this.field_71771_f.func_71746_d(), var1, "Connection #" + this.field_71773_d++);
            this.func_71764_a(var9);
         } catch (IOException var8) {
            var8.printStackTrace();
         }
      }

      System.out.println("Closing listening thread");
   }

   private void func_71764_a(NetLoginHandler p_71764_1_) {
      if(p_71764_1_ == null) {
         throw new IllegalArgumentException("Got null pendingconnection!");
      } else {
         List var2 = this.field_71775_b;
         synchronized(this.field_71775_b) {
            this.field_71775_b.add(p_71764_1_);
         }
      }
   }

   private static boolean func_71770_b(InetAddress p_71770_0_) {
      return "127.0.0.1".equals(p_71770_0_.getHostAddress());
   }

   public void func_71769_a(InetAddress p_71769_1_) {
      if(p_71769_1_ != null) {
         HashMap var2 = this.field_71776_c;
         synchronized(this.field_71776_c) {
            this.field_71776_c.remove(p_71769_1_);
         }
      }

   }

   public void func_71768_b() {
      try {
         this.field_71774_e.close();
      } catch (Throwable var2) {
         ;
      }

   }

   @SideOnly(Side.CLIENT)
   public InetAddress func_71767_c() {
      return this.field_71772_g;
   }

   @SideOnly(Side.CLIENT)
   public int func_71765_d() {
      return this.field_71778_h;
   }

}
