package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.server.MinecraftServer;

public class CallablePlayers implements Callable
{
    final MinecraftServer field_74270_a;

    public CallablePlayers(MinecraftServer par1MinecraftServer)
    {
        this.field_74270_a = par1MinecraftServer;
    }

    public String func_74269_a()
    {
        return MinecraftServer.func_71196_a(this.field_74270_a).playersOnline() + " / " + MinecraftServer.func_71196_a(this.field_74270_a).getMaxPlayers() + "; " + MinecraftServer.func_71196_a(this.field_74270_a).playerEntities;
    }

    public Object call()
    {
        return this.func_74269_a();
    }
}
