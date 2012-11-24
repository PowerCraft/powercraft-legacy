package net.minecraft.src;

import java.io.IOException;
import net.minecraft.src.Chunk;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.World;

public interface IChunkLoader {

   Chunk func_75815_a(World var1, int var2, int var3) throws IOException;

   void func_75816_a(World var1, Chunk var2) throws MinecraftException, IOException;

   void func_75819_b(World var1, Chunk var2);

   void func_75817_a();

   void func_75818_b();
}
