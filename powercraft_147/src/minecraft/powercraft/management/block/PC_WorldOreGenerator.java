package powercraft.management.block;

import java.util.Random;

import powercraft.management.PC_MathHelper;
import powercraft.management.PC_VecI;
import powercraft.management.registry.PC_BlockRegistry;
import powercraft.management.registry.PC_MSGRegistry;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class PC_WorldOreGenerator implements IWorldGenerator {
	
	@Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {

        for(PC_Block block:PC_BlockRegistry.getPCBlocks().values()){
        	if(block instanceof PC_BlockOre){
        		PC_BlockOre blockOre = (PC_BlockOre)block;
        		int num = blockOre.getGenOresSpawnsInChunk(random, world, chunkX, chunkZ);
        		for (int i = 0; i < num; i++){
        			int blocksCount = blockOre.getGenOreblocksOnSpawnPoint(random, world, chunkX, chunkZ);
        			PC_VecI pos = blockOre.getGenOresSpawnPoint(random, world, chunkX, chunkZ);
        			pos.y = PC_MathHelper.clamp_int(pos.y, 1, world.getHeight());
        			int metadata = blockOre.getGenOresSpawnMetadata(random, world, chunkX, chunkZ);
        			new PC_WorldGenMinableMetadata(block.blockID, metadata, blocksCount).generate(world, random, pos.x, pos.y, pos.z);
        		}
        	}
        }
        
    }

}
