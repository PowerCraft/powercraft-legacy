package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelWeather implements Callable
{
    final WorldInfo field_85111_a;

    CallableLevelWeather(WorldInfo par1WorldInfo)
    {
        this.field_85111_a = par1WorldInfo;
    }

    public String func_85110_a()
    {
        return String.format("Rain time: %d (now: %b), thunder time: %d (now: %b)", new Object[] {Integer.valueOf(WorldInfo.func_85119_k(this.field_85111_a)), Boolean.valueOf(WorldInfo.func_85127_l(this.field_85111_a)), Integer.valueOf(WorldInfo.func_85133_m(this.field_85111_a)), Boolean.valueOf(WorldInfo.func_85116_n(this.field_85111_a))});
    }

    public Object call()
    {
        return this.func_85110_a();
    }
}
