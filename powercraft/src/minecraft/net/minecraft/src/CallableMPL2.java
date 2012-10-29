package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.concurrent.Callable;

@SideOnly(Side.CLIENT)
class CallableMPL2 implements Callable
{
    /** Initialises the WorldClient for CallableMPL2. */
    final WorldClient worldClientMPL2;

    CallableMPL2(WorldClient par1WorldClient)
    {
        this.worldClientMPL2 = par1WorldClient;
    }

    /**
     * Returns the size and contents of the entity spawn queue.
     */
    public String getEntitySpawnQueueCountAndList()
    {
        return WorldClient.getEntitySpawnQueue(this.worldClientMPL2).size() + " total; " + WorldClient.getEntitySpawnQueue(this.worldClientMPL2).toString();
    }

    public Object call()
    {
        return this.getEntitySpawnQueueCountAndList();
    }
}
