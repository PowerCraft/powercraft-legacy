/**
 * 
 */
package powercraft.api;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import powercraft.api.blocks.PC_IBlock;
import cpw.mods.fml.common.IWorldGenerator;

/**
 * @author Aaron
 *
 */
public class PC_WorldGenerator implements IWorldGenerator {

	/**
	 * @see cpw.mods.fml.common.IWorldGenerator#generate(java.util.Random, int, int, net.minecraft.world.World, net.minecraft.world.chunk.IChunkProvider, net.minecraft.world.chunk.IChunkProvider)
	 */
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		for(int i=0; i<Block.blocksList.length; i++){
			Block block = Block.blocksList[i];
			if(block instanceof PC_IBlock){
				((PC_IBlock)block).generate(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
			}
		}
	}

}
