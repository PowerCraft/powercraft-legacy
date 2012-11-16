package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelGeneratorOptions implements Callable
{
    final WorldInfo field_85141_a;

    CallableLevelGeneratorOptions(WorldInfo par1WorldInfo)
    {
        this.field_85141_a = par1WorldInfo;
    }

    public String func_85140_a()
    {
        return WorldInfo.func_85130_c(this.field_85141_a);
    }

    public Object call()
    {
        return this.func_85140_a();
    }
}
