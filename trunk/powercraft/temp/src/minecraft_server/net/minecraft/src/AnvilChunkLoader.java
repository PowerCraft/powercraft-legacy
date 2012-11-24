package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.src.AnvilChunkLoaderPending;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityList;
import net.minecraft.src.ExtendedBlockStorage;
import net.minecraft.src.IChunkLoader;
import net.minecraft.src.IThreadedFileIO;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NextTickListEntry;
import net.minecraft.src.NibbleArray;
import net.minecraft.src.RegionFileCache;
import net.minecraft.src.ThreadedFileIOBase;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class AnvilChunkLoader implements IThreadedFileIO, IChunkLoader {

   private List field_75828_a = new ArrayList();
   private Set field_75826_b = new HashSet();
   private Object field_75827_c = new Object();
   final File field_75825_d;


   public AnvilChunkLoader(File p_i3779_1_) {
      this.field_75825_d = p_i3779_1_;
   }

   public Chunk func_75815_a(World p_75815_1_, int p_75815_2_, int p_75815_3_) throws IOException {
      NBTTagCompound var4 = null;
      ChunkCoordIntPair var5 = new ChunkCoordIntPair(p_75815_2_, p_75815_3_);
      Object var6 = this.field_75827_c;
      synchronized(this.field_75827_c) {
         if(this.field_75826_b.contains(var5)) {
            for(int var7 = 0; var7 < this.field_75828_a.size(); ++var7) {
               if(((AnvilChunkLoaderPending)this.field_75828_a.get(var7)).field_76548_a.equals(var5)) {
                  var4 = ((AnvilChunkLoaderPending)this.field_75828_a.get(var7)).field_76547_b;
                  break;
               }
            }
         }
      }

      if(var4 == null) {
         DataInputStream var10 = RegionFileCache.func_76549_c(this.field_75825_d, p_75815_2_, p_75815_3_);
         if(var10 == null) {
            return null;
         }

         var4 = CompressedStreamTools.func_74794_a(var10);
      }

      return this.func_75822_a(p_75815_1_, p_75815_2_, p_75815_3_, var4);
   }

   protected Chunk func_75822_a(World p_75822_1_, int p_75822_2_, int p_75822_3_, NBTTagCompound p_75822_4_) {
      if(!p_75822_4_.func_74764_b("Level")) {
         System.out.println("Chunk file at " + p_75822_2_ + "," + p_75822_3_ + " is missing level data, skipping");
         return null;
      } else if(!p_75822_4_.func_74775_l("Level").func_74764_b("Sections")) {
         System.out.println("Chunk file at " + p_75822_2_ + "," + p_75822_3_ + " is missing block data, skipping");
         return null;
      } else {
         Chunk var5 = this.func_75823_a(p_75822_1_, p_75822_4_.func_74775_l("Level"));
         if(!var5.func_76600_a(p_75822_2_, p_75822_3_)) {
            System.out.println("Chunk file at " + p_75822_2_ + "," + p_75822_3_ + " is in the wrong location; relocating. (Expected " + p_75822_2_ + ", " + p_75822_3_ + ", got " + var5.field_76635_g + ", " + var5.field_76647_h + ")");
            p_75822_4_.func_74768_a("xPos", p_75822_2_);
            p_75822_4_.func_74768_a("zPos", p_75822_3_);
            var5 = this.func_75823_a(p_75822_1_, p_75822_4_.func_74775_l("Level"));
         }

         return var5;
      }
   }

   public void func_75816_a(World p_75816_1_, Chunk p_75816_2_) throws MinecraftException, IOException {
      p_75816_1_.func_72906_B();

      try {
         NBTTagCompound var3 = new NBTTagCompound();
         NBTTagCompound var4 = new NBTTagCompound();
         var3.func_74782_a("Level", var4);
         this.func_75820_a(p_75816_2_, p_75816_1_, var4);
         this.func_75824_a(p_75816_2_.func_76632_l(), var3);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   protected void func_75824_a(ChunkCoordIntPair p_75824_1_, NBTTagCompound p_75824_2_) {
      Object var3 = this.field_75827_c;
      synchronized(this.field_75827_c) {
         if(this.field_75826_b.contains(p_75824_1_)) {
            for(int var4 = 0; var4 < this.field_75828_a.size(); ++var4) {
               if(((AnvilChunkLoaderPending)this.field_75828_a.get(var4)).field_76548_a.equals(p_75824_1_)) {
                  this.field_75828_a.set(var4, new AnvilChunkLoaderPending(p_75824_1_, p_75824_2_));
                  return;
               }
            }
         }

         this.field_75828_a.add(new AnvilChunkLoaderPending(p_75824_1_, p_75824_2_));
         this.field_75826_b.add(p_75824_1_);
         ThreadedFileIOBase.field_75741_a.func_75735_a(this);
      }
   }

   public boolean func_75814_c() {
      AnvilChunkLoaderPending var1 = null;
      Object var2 = this.field_75827_c;
      synchronized(this.field_75827_c) {
         if(this.field_75828_a.isEmpty()) {
            return false;
         }

         var1 = (AnvilChunkLoaderPending)this.field_75828_a.remove(0);
         this.field_75826_b.remove(var1.field_76548_a);
      }

      if(var1 != null) {
         try {
            this.func_75821_a(var1);
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return true;
   }

   private void func_75821_a(AnvilChunkLoaderPending p_75821_1_) throws IOException {
      DataOutputStream var2 = RegionFileCache.func_76552_d(this.field_75825_d, p_75821_1_.field_76548_a.field_77276_a, p_75821_1_.field_76548_a.field_77275_b);
      CompressedStreamTools.func_74800_a(p_75821_1_.field_76547_b, var2);
      var2.close();
   }

   public void func_75819_b(World p_75819_1_, Chunk p_75819_2_) {}

   public void func_75817_a() {}

   public void func_75818_b() {}

   private void func_75820_a(Chunk p_75820_1_, World p_75820_2_, NBTTagCompound p_75820_3_) {
      p_75820_3_.func_74768_a("xPos", p_75820_1_.field_76635_g);
      p_75820_3_.func_74768_a("zPos", p_75820_1_.field_76647_h);
      p_75820_3_.func_74772_a("LastUpdate", p_75820_2_.func_82737_E());
      p_75820_3_.func_74783_a("HeightMap", p_75820_1_.field_76634_f);
      p_75820_3_.func_74757_a("TerrainPopulated", p_75820_1_.field_76646_k);
      ExtendedBlockStorage[] var4 = p_75820_1_.func_76587_i();
      NBTTagList var5 = new NBTTagList("Sections");
      ExtendedBlockStorage[] var6 = var4;
      int var7 = var4.length;

      NBTTagCompound var10;
      for(int var8 = 0; var8 < var7; ++var8) {
         ExtendedBlockStorage var9 = var6[var8];
         if(var9 != null) {
            var10 = new NBTTagCompound();
            var10.func_74774_a("Y", (byte)(var9.func_76662_d() >> 4 & 255));
            var10.func_74773_a("Blocks", var9.func_76658_g());
            if(var9.func_76660_i() != null) {
               var10.func_74773_a("Add", var9.func_76660_i().field_76585_a);
            }

            var10.func_74773_a("Data", var9.func_76669_j().field_76585_a);
            var10.func_74773_a("SkyLight", var9.func_76671_l().field_76585_a);
            var10.func_74773_a("BlockLight", var9.func_76661_k().field_76585_a);
            var5.func_74742_a(var10);
         }
      }

      p_75820_3_.func_74782_a("Sections", var5);
      p_75820_3_.func_74773_a("Biomes", p_75820_1_.func_76605_m());
      p_75820_1_.field_76644_m = false;
      NBTTagList var15 = new NBTTagList();

      Iterator var17;
      for(var7 = 0; var7 < p_75820_1_.field_76645_j.length; ++var7) {
         var17 = p_75820_1_.field_76645_j[var7].iterator();

         while(var17.hasNext()) {
            Entity var19 = (Entity)var17.next();
            p_75820_1_.field_76644_m = true;
            var10 = new NBTTagCompound();
            if(var19.func_70039_c(var10)) {
               var15.func_74742_a(var10);
            }
         }
      }

      p_75820_3_.func_74782_a("Entities", var15);
      NBTTagList var16 = new NBTTagList();
      var17 = p_75820_1_.field_76648_i.values().iterator();

      while(var17.hasNext()) {
         TileEntity var21 = (TileEntity)var17.next();
         var10 = new NBTTagCompound();
         var21.func_70310_b(var10);
         var16.func_74742_a(var10);
      }

      p_75820_3_.func_74782_a("TileEntities", var16);
      List var18 = p_75820_2_.func_72920_a(p_75820_1_, false);
      if(var18 != null) {
         long var20 = p_75820_2_.func_82737_E();
         NBTTagList var11 = new NBTTagList();
         Iterator var12 = var18.iterator();

         while(var12.hasNext()) {
            NextTickListEntry var13 = (NextTickListEntry)var12.next();
            NBTTagCompound var14 = new NBTTagCompound();
            var14.func_74768_a("i", var13.field_77179_d);
            var14.func_74768_a("x", var13.field_77183_a);
            var14.func_74768_a("y", var13.field_77181_b);
            var14.func_74768_a("z", var13.field_77182_c);
            var14.func_74768_a("t", (int)(var13.field_77180_e - var20));
            var11.func_74742_a(var14);
         }

         p_75820_3_.func_74782_a("TileTicks", var11);
      }

   }

   private Chunk func_75823_a(World p_75823_1_, NBTTagCompound p_75823_2_) {
      int var3 = p_75823_2_.func_74762_e("xPos");
      int var4 = p_75823_2_.func_74762_e("zPos");
      Chunk var5 = new Chunk(p_75823_1_, var3, var4);
      var5.field_76634_f = p_75823_2_.func_74759_k("HeightMap");
      var5.field_76646_k = p_75823_2_.func_74767_n("TerrainPopulated");
      NBTTagList var6 = p_75823_2_.func_74761_m("Sections");
      byte var7 = 16;
      ExtendedBlockStorage[] var8 = new ExtendedBlockStorage[var7];

      for(int var9 = 0; var9 < var6.func_74745_c(); ++var9) {
         NBTTagCompound var10 = (NBTTagCompound)var6.func_74743_b(var9);
         byte var11 = var10.func_74771_c("Y");
         ExtendedBlockStorage var12 = new ExtendedBlockStorage(var11 << 4);
         var12.func_76664_a(var10.func_74770_j("Blocks"));
         if(var10.func_74764_b("Add")) {
            var12.func_76673_a(new NibbleArray(var10.func_74770_j("Add"), 4));
         }

         var12.func_76668_b(new NibbleArray(var10.func_74770_j("Data"), 4));
         var12.func_76666_d(new NibbleArray(var10.func_74770_j("SkyLight"), 4));
         var12.func_76659_c(new NibbleArray(var10.func_74770_j("BlockLight"), 4));
         var12.func_76672_e();
         var8[var11] = var12;
      }

      var5.func_76602_a(var8);
      if(p_75823_2_.func_74764_b("Biomes")) {
         var5.func_76616_a(p_75823_2_.func_74770_j("Biomes"));
      }

      NBTTagList var14 = p_75823_2_.func_74761_m("Entities");
      if(var14 != null) {
         for(int var17 = 0; var17 < var14.func_74745_c(); ++var17) {
            NBTTagCompound var16 = (NBTTagCompound)var14.func_74743_b(var17);
            Entity var18 = EntityList.func_75615_a(var16, p_75823_1_);
            var5.field_76644_m = true;
            if(var18 != null) {
               var5.func_76612_a(var18);
            }
         }
      }

      NBTTagList var15 = p_75823_2_.func_74761_m("TileEntities");
      if(var15 != null) {
         for(int var21 = 0; var21 < var15.func_74745_c(); ++var21) {
            NBTTagCompound var20 = (NBTTagCompound)var15.func_74743_b(var21);
            TileEntity var13 = TileEntity.func_70317_c(var20);
            if(var13 != null) {
               var5.func_76620_a(var13);
            }
         }
      }

      if(p_75823_2_.func_74764_b("TileTicks")) {
         NBTTagList var19 = p_75823_2_.func_74761_m("TileTicks");
         if(var19 != null) {
            for(int var22 = 0; var22 < var19.func_74745_c(); ++var22) {
               NBTTagCompound var23 = (NBTTagCompound)var19.func_74743_b(var22);
               p_75823_1_.func_72892_b(var23.func_74762_e("x"), var23.func_74762_e("y"), var23.func_74762_e("z"), var23.func_74762_e("i"), var23.func_74762_e("t"));
            }
         }
      }

      return var5;
   }
}
