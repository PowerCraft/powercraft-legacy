package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLvl1 implements Callable
{
    /** Gets loaded Entities. */
    final World worldLvl1;

    CallableLvl1(World par1World)
    {
        this.worldLvl1 = par1World;
    }

    public String getWorldEntitiesAsString()
    {
        return this.worldLvl1.loadedEntityList.size() + " total; " + this.worldLvl1.loadedEntityList.toString();
    }

    public Object call()
    {
        return this.getWorldEntitiesAsString();
    }
}
