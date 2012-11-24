package powercraft.core;

import java.util.Random;

public interface PC_IWorldSpawnGenerate
{
    public int getSpawnsInChunk(Random random);

    public int getBlocksOnSpawnPoint(Random random);

    public PC_CoordI getSpawnPoint(Random random);
}
