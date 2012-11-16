package powercraft.core;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.World;
import cpw.mods.fml.common.IWorldGenerator;

public class PC_WorldGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		int posX;
		int posY;
		int posZ;

		for(Block block:Block.blocksList){
			if(block instanceof PC_IWorldSpawnGenerate){
				PC_IWorldSpawnGenerate wsg = (PC_IWorldSpawnGenerate) block;
				int num = wsg.getSpawnsInChunk(random);
				for (int i = 0; i < num; i++) {
					
					int blocksCount = wsg.getBlocksOnSpawnPoint(random);
					PC_CoordI pos = wsg.getSpawnPoint(random);
					pos.x += chunkX * 16;
					pos.y = PC_MathHelper.clamp_int(pos.y, 1, 255);
					pos.z += chunkZ * 16;
		
					new PC_WorldGenMinableMetadata(block.blockID, random.nextInt(8), blocksCount).generate(world, random, pos.x, pos.y, pos.z);
				}
			}
		}
	}

}
