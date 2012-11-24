package net.minecraft.src;

import java.io.File;
import net.minecraft.src.IChunkLoader;
import net.minecraft.src.IPlayerFileData;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldProvider;

public interface ISaveHandler {

   WorldInfo func_75757_d();

   void func_75762_c() throws MinecraftException;

   IChunkLoader func_75763_a(WorldProvider var1);

   void func_75755_a(WorldInfo var1, NBTTagCompound var2);

   void func_75761_a(WorldInfo var1);

   IPlayerFileData func_75756_e();

   void func_75759_a();

   File func_75758_b(String var1);

   String func_75760_g();
}
