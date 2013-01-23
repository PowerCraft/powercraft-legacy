package net.minecraft.server;

import java.util.concurrent.Callable;

public class CallableServerMemoryStats implements Callable
{
    final MinecraftServer field_92023_a;

    public CallableServerMemoryStats(MinecraftServer par1MinecraftServer)
    {
        this.field_92023_a = par1MinecraftServer;
    }

    public String func_92022_a()
    {
        return MinecraftServer.getServerConfigurationManager(this.field_92023_a).getCurrentPlayerCount() + " / " + MinecraftServer.getServerConfigurationManager(this.field_92023_a).getMaxPlayers() + "; " + MinecraftServer.getServerConfigurationManager(this.field_92023_a).playerEntityList;
    }

    public Object call()
    {
        return this.func_92022_a();
    }
}
