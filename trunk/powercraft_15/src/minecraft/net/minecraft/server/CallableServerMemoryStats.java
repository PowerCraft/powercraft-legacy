package net.minecraft.server;

import java.util.concurrent.Callable;

public class CallableServerMemoryStats implements Callable
{
    /** Reference to the MinecraftServer object. */
    final MinecraftServer mcServer;

    public CallableServerMemoryStats(MinecraftServer par1)
    {
        this.mcServer = par1;
    }

    public String func_96556_a()
    {
        return MinecraftServer.getServerConfigurationManager(this.mcServer).getCurrentPlayerCount() + " / " + MinecraftServer.getServerConfigurationManager(this.mcServer).getMaxPlayers() + "; " + MinecraftServer.getServerConfigurationManager(this.mcServer).playerEntityList;
    }

    public Object call()
    {
        return this.func_96556_a();
    }
}
