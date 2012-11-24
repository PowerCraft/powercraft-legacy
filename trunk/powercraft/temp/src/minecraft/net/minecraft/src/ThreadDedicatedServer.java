package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.DedicatedServer;

@SideOnly(Side.SERVER)
public final class ThreadDedicatedServer extends Thread {

   // $FF: synthetic field
   final DedicatedServer field_82012_a;


   public ThreadDedicatedServer(DedicatedServer p_i4100_1_) {
      this.field_82012_a = p_i4100_1_;
   }

   public void run() {
      this.field_82012_a.func_71260_j();
   }
}
