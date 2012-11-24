package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.concurrent.Callable;

@SideOnly(Side.CLIENT)
class CallableMPL2 implements Callable
{
    /** Reference to the WorldClient object. */
    final WorldClient theWorldClient;

    CallableMPL2(WorldClient par1WorldClient)
    {
        this.theWorldClient = par1WorldClient;
    }

    /**
     * Returns the size and contents of the entity spawn queue.
     */
    public String getEntitySpawnQueueCountAndList()
    {
        return WorldClient.getEntitySpawnQueue(this.theWorldClient).size() + " total; " + WorldClient.getEntitySpawnQueue(this.theWorldClient).toString();
    }

    public Object call()
    {
        return this.getEntitySpawnQueueCountAndList();
    }
}
