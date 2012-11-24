package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLvl2 implements Callable
{
    final World theWorld;

    CallableLvl2(World par1World)
    {
        this.theWorld = par1World;
    }

    public String getPlayerEntities()
    {
        return this.theWorld.playerEntities.size() + " total; " + this.theWorld.playerEntities.toString();
    }

    public Object call()
    {
        return this.getPlayerEntities();
    }
}
