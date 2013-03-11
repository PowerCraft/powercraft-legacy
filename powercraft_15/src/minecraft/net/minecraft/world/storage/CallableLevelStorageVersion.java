package net.minecraft.world.storage;

import java.util.concurrent.Callable;

class CallableLevelStorageVersion implements Callable
{
    final WorldInfo worldInfoInstance;

    CallableLevelStorageVersion(WorldInfo par1WorldInfo)
    {
        this.worldInfoInstance = par1WorldInfo;
    }

    public String func_85112_a()
    {
        String s = "Unknown?";

        try
        {
            switch (WorldInfo.func_85121_j(this.worldInfoInstance))
            {
                case 19132:
                    s = "McRegion";
                    break;
                case 19133:
                    s = "Anvil";
            }
        }
        catch (Throwable throwable)
        {
            ;
        }

        return String.format("0x%05X - %s", new Object[] {Integer.valueOf(WorldInfo.func_85121_j(this.worldInfoInstance)), s});
    }

    public Object call()
    {
        return this.func_85112_a();
    }
}
