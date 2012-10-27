package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLvl2 implements Callable
{
    /** Gets loaded Entities. */
    final World worldLvl2;

    CallableLvl2(World par1World)
    {
        this.worldLvl2 = par1World;
    }

    /**
     * Returns the size and contents of the player entity list.
     */
    public String getPlayerEntities()
    {
        return this.worldLvl2.playerEntities.size() + " total; " + this.worldLvl2.playerEntities.toString();
    }

    public Object call()
    {
        return this.getPlayerEntities();
    }
}
