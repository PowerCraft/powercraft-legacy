package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelGamemode implements Callable
{
    final WorldInfo field_85109_a;

    CallableLevelGamemode(WorldInfo par1WorldInfo)
    {
        this.field_85109_a = par1WorldInfo;
    }

    public String func_85108_a()
    {
        return String.format("Game mode: %s (ID %d). Hardcore: %b. Cheats: %b", new Object[] {WorldInfo.func_85120_o(this.field_85109_a).getName(), Integer.valueOf(WorldInfo.func_85120_o(this.field_85109_a).getID()), Boolean.valueOf(WorldInfo.func_85117_p(this.field_85109_a)), Boolean.valueOf(WorldInfo.func_85131_q(this.field_85109_a))});
    }

    public Object call()
    {
        return this.func_85108_a();
    }
}
