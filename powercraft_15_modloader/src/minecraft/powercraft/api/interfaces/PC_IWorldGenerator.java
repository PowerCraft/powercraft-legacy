package powercraft.api.interfaces;

import java.util.Random;

import net.minecraft.src.IChunkProvider;
import net.minecraft.src.World;

public interface PC_IWorldGenerator {
	
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider);
	
}
