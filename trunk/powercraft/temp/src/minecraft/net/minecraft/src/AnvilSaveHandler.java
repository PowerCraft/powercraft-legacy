package net.minecraft.src;

import java.io.File;
import net.minecraft.src.AnvilChunkLoader;
import net.minecraft.src.IChunkLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.RegionFileCache;
import net.minecraft.src.SaveHandler;
import net.minecraft.src.ThreadedFileIOBase;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldProvider;
import net.minecraft.src.WorldProviderEnd;
import net.minecraft.src.WorldProviderHell;

public class AnvilSaveHandler extends SaveHandler {

   public AnvilSaveHandler(File p_i3908_1_, String p_i3908_2_, boolean p_i3908_3_) {
      super(p_i3908_1_, p_i3908_2_, p_i3908_3_);
   }

   public IChunkLoader func_75763_a(WorldProvider p_75763_1_) {
      File var2 = this.func_75765_b();
      File var3;
      if(p_75763_1_ instanceof WorldProviderHell) {
         var3 = new File(var2, "DIM-1");
         var3.mkdirs();
         return new AnvilChunkLoader(var3);
      } else if(p_75763_1_ instanceof WorldProviderEnd) {
         var3 = new File(var2, "DIM1");
         var3.mkdirs();
         return new AnvilChunkLoader(var3);
      } else {
         return new AnvilChunkLoader(var2);
      }
   }

   public void func_75755_a(WorldInfo p_75755_1_, NBTTagCompound p_75755_2_) {
      p_75755_1_.func_76078_e(19133);
      super.func_75755_a(p_75755_1_, p_75755_2_);
   }

   public void func_75759_a() {
      try {
         ThreadedFileIOBase.field_75741_a.func_75734_a();
      } catch (InterruptedException var2) {
         var2.printStackTrace();
      }

      RegionFileCache.func_76551_a();
   }
}
