package net.minecraft.src;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.Profiler;
import net.minecraft.src.WorldServer;
import net.minecraft.src.WorldSettings;
import net.minecraft.src.WorldType;

public class DemoWorldServer extends WorldServer {

   private static final long field_73072_L = (long)"North Carolina".hashCode();
   public static final WorldSettings field_73071_a = (new WorldSettings(field_73072_L, EnumGameType.SURVIVAL, true, false, WorldType.field_77137_b)).func_77159_a();


   public DemoWorldServer(MinecraftServer p_i3386_1_, ISaveHandler p_i3386_2_, String p_i3386_3_, int p_i3386_4_, Profiler p_i3386_5_) {
      super(p_i3386_1_, p_i3386_2_, p_i3386_3_, p_i3386_4_, field_73071_a, p_i3386_5_);
   }

}
