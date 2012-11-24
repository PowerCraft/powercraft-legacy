package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class ThreadMinecraftServer extends Thread {

   // $FF: synthetic field
   final MinecraftServer field_82553_a;


   public ThreadMinecraftServer(MinecraftServer p_i5039_1_, String p_i5039_2_) {
      super(p_i5039_2_);
      this.field_82553_a = p_i5039_1_;
   }

   public void run() {
      this.field_82553_a.run();
   }
}
