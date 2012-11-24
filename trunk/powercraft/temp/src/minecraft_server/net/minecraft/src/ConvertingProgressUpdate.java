package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.IProgressUpdate;

public class ConvertingProgressUpdate implements IProgressUpdate {

   private long field_82309_b;
   // $FF: synthetic field
   final MinecraftServer field_82310_a;


   public ConvertingProgressUpdate(MinecraftServer p_i5038_1_) {
      this.field_82310_a = p_i5038_1_;
      this.field_82309_b = System.currentTimeMillis();
   }

   public void func_73720_a(String p_73720_1_) {}

   public void func_73718_a(int p_73718_1_) {
      if(System.currentTimeMillis() - this.field_82309_b >= 1000L) {
         this.field_82309_b = System.currentTimeMillis();
         MinecraftServer.field_71306_a.info("Converting... " + p_73718_1_ + "%");
      }

   }

   @SideOnly(Side.CLIENT)
   public void func_73721_b(String p_73721_1_) {}

   @SideOnly(Side.CLIENT)
   public void func_73717_a() {}

   public void func_73719_c(String p_73719_1_) {}
}
