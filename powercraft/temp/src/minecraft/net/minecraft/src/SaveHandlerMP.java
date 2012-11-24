package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.File;
import net.minecraft.src.IChunkLoader;
import net.minecraft.src.IPlayerFileData;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldProvider;

@SideOnly(Side.CLIENT)
public class SaveHandlerMP implements ISaveHandler {

   public WorldInfo func_75757_d() {
      return null;
   }

   public void func_75762_c() throws MinecraftException {}

   public IChunkLoader func_75763_a(WorldProvider p_75763_1_) {
      return null;
   }

   public void func_75755_a(WorldInfo p_75755_1_, NBTTagCompound p_75755_2_) {}

   public void func_75761_a(WorldInfo p_75761_1_) {}

   public IPlayerFileData func_75756_e() {
      return null;
   }

   public void func_75759_a() {}

   public File func_75758_b(String p_75758_1_) {
      return null;
   }

   public String func_75760_g() {
      return "none";
   }
}
