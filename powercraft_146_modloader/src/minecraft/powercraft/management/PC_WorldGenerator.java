package powercraft.management;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.World;

public class PC_WorldGenerator {

    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        int posX;
        int posY;
        int posZ;

        for (Block block: Block.blocksList)
        {
            if (block instanceof PC_IMSG)
            {
            	PC_IMSG wsg = (PC_IMSG)block;
            	Object o = wsg.msg(PC_Utils.MSG_SPAWNS_IN_CHUNK, random);
            	if(o instanceof Integer){
	                int num = (Integer)o;
	
	                for (int i = 0; i < num; i++)
	                {
	                	o = wsg.msg(PC_Utils.MSG_BLOCKS_ON_SPAWN_POINT, random);
	                	int blocksCount = 2;
	                	if(o instanceof Integer)
	                		blocksCount = (Integer)o;
	                	o = wsg.msg(PC_Utils.MSG_SPAWN_POINT, random);
	                	if(o instanceof PC_VecI){
	                		PC_VecI pos = (PC_VecI)o;
		                    pos.x += chunkX * 16;
		                    pos.y = PC_MathHelper.clamp_int(pos.y, 1, 255);
		                    pos.z += chunkZ * 16;
		                    int metadata = 0;
		                    o = wsg.msg(PC_Utils.MSG_SPAWN_POINT_METADATA, random);
		                    if(o instanceof Integer)
		                    	metadata = (Integer)o;
		                    new PC_WorldGenMinableMetadata(block.blockID, metadata, blocksCount).generate(world, random, pos.x, pos.y, pos.z);
	                	}
	                }
            	}
            }
        }
    }

}
