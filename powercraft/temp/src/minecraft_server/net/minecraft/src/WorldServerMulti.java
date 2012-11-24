package net.minecraft.src;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.DerivedWorldInfo;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.Profiler;
import net.minecraft.src.WorldServer;
import net.minecraft.src.WorldSettings;

public class WorldServerMulti extends WorldServer {

   public WorldServerMulti(MinecraftServer p_i3388_1_, ISaveHandler p_i3388_2_, String p_i3388_3_, int p_i3388_4_, WorldSettings p_i3388_5_, WorldServer p_i3388_6_, Profiler p_i3388_7_) {
      super(p_i3388_1_, p_i3388_2_, p_i3388_3_, p_i3388_4_, p_i3388_5_, p_i3388_7_);
      this.field_72988_C = p_i3388_6_.field_72988_C;
      this.field_72986_A = new DerivedWorldInfo(p_i3388_6_.func_72912_H());
   }

   protected void func_73042_a() throws MinecraftException {}
}
