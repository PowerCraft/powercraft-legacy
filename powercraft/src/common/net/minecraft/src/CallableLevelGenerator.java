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
        return String.format("ID %02d - %s, ver %d. Features enabled: %b", new Object[] {Integer.valueOf(WorldInfo.func_85132_a(this.field_85139_a).func_82747_f()), WorldInfo.func_85132_a(this.field_85139_a).getWorldTypeName(), Integer.valueOf(WorldInfo.func_85132_a(this.field_85139_a).getGeneratorVersion()), Boolean.valueOf(WorldInfo.func_85128_b(this.field_85139_a))});
    }

    public Object call()
    {
        return this.func_85138_a();
    }
}
