package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.server.MinecraftServer;

public class CallablePlayers implements Callable
{
    /** Gets Minecraft Server players. */
    final MinecraftServer minecraftServerPlayers;

    public CallablePlayers(MinecraftServer par1MinecraftServer)
    {
        this.minecraftServerPlayers = par1MinecraftServer;
    }

    public String func_74269_a()
    {
        return MinecraftServer.func_71196_a(this.minecraftServerPlayers).getPlayerListSize() + " / " + MinecraftServer.func_71196_a(this.minecraftServerPlayers).getMaxPlayers() + "; " + MinecraftServer.func_71196_a(this.minecraftServerPlayers).playerEntityList;
    }

    public Object call()
    {
        return this.func_74269_a();
    }
}
