package powercraft.api.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import powercraft.api.interfaces.PC_IWorldGenerator;
import cpw.mods.fml.common.IWorldGenerator;

public class PC_WorldGeneratorRegistry implements IWorldGenerator {
	
	private static List<PC_IWorldGenerator> worldGenerators = new ArrayList<PC_IWorldGenerator>();
	
	public static void register(PC_IWorldGenerator worldGenerator) {
		if (!worldGenerators.contains(worldGenerator)) {
			worldGenerators.add(worldGenerator);
		}
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		for (PC_IWorldGenerator worldGenerator : worldGenerators) {
			worldGenerator.generate(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
		}
	}
	
}
