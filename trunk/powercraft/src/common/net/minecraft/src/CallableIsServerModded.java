package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.server.MinecraftServer;

public class CallableIsServerModded implements Callable
{
    final MinecraftServer field_82557_a;

    public CallableIsServerModded(MinecraftServer par1MinecraftServer)
    {
        this.field_82557_a = par1MinecraftServer;
    }

    public String func_82556_a()
    {
        String var1 = this.field_82557_a.getServerModName();
        return !var1.equals("vanilla") ? "Definitely; \'" + var1 + "\'" : "Unknown (can\'t tell)";
    }

    public Object call()
    {
        return this.func_82556_a();
    }
}
