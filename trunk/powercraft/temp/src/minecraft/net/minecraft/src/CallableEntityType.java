package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityList;

class CallableEntityType implements Callable {

   // $FF: synthetic field
   final Entity field_85155_a;


   CallableEntityType(Entity p_i6811_1_) {
      this.field_85155_a = p_i6811_1_;
   }

   public String func_85154_a() {
      return EntityList.func_75621_b(this.field_85155_a) + " (" + this.field_85155_a.getClass().getCanonicalName() + ")";
   }

   // $FF: synthetic method
   public Object call() {
      return this.func_85154_a();
   }
}
