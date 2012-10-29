package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.server.MinecraftServer;

public class CallablePlayers implements Callable
{
    final MinecraftServer field_82550_a;

    public CallablePlayers(MinecraftServer par1MinecraftServer)
    {
        this.field_82550_a = par1MinecraftServer;
    }

    public String func_82549_a()
    {
        return MinecraftServer.getServerConfigurationManager(this.field_82550_a).getCurrentPlayerCount() + " / " + MinecraftServer.getServerConfigurationManager(this.field_82550_a).getMaxPlayers() + "; " + MinecraftServer.getServerConfigurationManager(this.field_82550_a).playerEntityList;
    }

    public Object call()
    {
        return this.func_82549_a();
    }
}
