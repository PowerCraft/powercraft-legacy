package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.concurrent.Callable;
import net.minecraft.src.IntegratedServer;

@SideOnly(Side.CLIENT)
class CallableType3 implements Callable {

   // $FF: synthetic field
   final IntegratedServer field_76974_a;


   CallableType3(IntegratedServer p_i3120_1_) {
      this.field_76974_a = p_i3120_1_;
   }

   public String func_76973_a() {
      return "Integrated Server (map_client.txt)";
   }

   // $FF: synthetic method
   public Object call() {
      return this.func_76973_a();
   }
}
