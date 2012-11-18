package net.minecraft.src;

import java.util.List;

public interface IChunkProvider
{
    boolean chunkExists(int var1, int var2);

    Chunk provideChunk(int var1, int var2);

    Chunk loadChunk(int var1, int var2);

    void populate(IChunkProvider var1, int var2, int var3);

    boolean saveChunks(boolean var1, IProgressUpdate var2);

    boolean unload100OldestChunks();

    boolean canSave();

    String makeString();

    List getPossibleCreatures(EnumCreatureType var1, int var2, int var3, int var4);

    ChunkPosition findClosestStructure(World var1, String var2, int var3, int var4, int var5);

    int getLoadedChunkCount();

    void func_82695_e(int var1, int var2);
}
