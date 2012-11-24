package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.src.CrashReport;

public class CallableMinecraftVersion implements Callable {

   // $FF: synthetic field
   final CrashReport field_71494_a;


   public CallableMinecraftVersion(CrashReport p_i3244_1_) {
      this.field_71494_a = p_i3244_1_;
   }

   public String func_71493_a() {
      return "1.4.4";
   }

   // $FF: synthetic method
   public Object call() {
      return this.func_71493_a();
   }
}
