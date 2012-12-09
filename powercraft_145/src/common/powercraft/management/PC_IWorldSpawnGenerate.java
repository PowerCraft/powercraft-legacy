package powercraft.management;

import java.util.Random;

public interface PC_IWorldSpawnGenerate {

	public int getSpawnsInChunk(Random random);

	public int getBlocksOnSpawnPoint(Random random);

	public PC_VecI getSpawnPoint(Random random);
	
	public int getSpawnPointMetadata(Random random);
	
}
