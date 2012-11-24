package cpw.mods.fml.common;

import java.util.Random;

import net.minecraft.src.IChunkProvider;
import net.minecraft.src.World;

public interface IWorldGenerator
{
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider);
}
