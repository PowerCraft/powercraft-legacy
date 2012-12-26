package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelGenerator implements Callable
{
    final WorldInfo field_85139_a;

    CallableLevelGenerator(WorldInfo par1WorldInfo)
    {
        this.field_85139_a = par1WorldInfo;
    }

    public String func_85138_a()
    {
        return String.format("ID %02d - %s, ver %d. Features enabled: %b", new Object[] {Integer.valueOf(WorldInfo.getTerrainTypeOfWorld(this.field_85139_a).func_82747_f()), WorldInfo.getTerrainTypeOfWorld(this.field_85139_a).getWorldTypeName(), Integer.valueOf(WorldInfo.getTerrainTypeOfWorld(this.field_85139_a).getGeneratorVersion()), Boolean.valueOf(WorldInfo.getMapFeaturesEnabled(this.field_85139_a))});
    }

    public Object call()
    {
        return this.func_85138_a();
    }
}
