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

    /**
     * Gets the current player count, maximum player count, and player entity list.
     */
    public String getPlayers()
    {
        return MinecraftServer.getServerConfigurationManager(this.minecraftServerPlayers).getCurrentPlayerCount() + " / " + MinecraftServer.getServerConfigurationManager(this.minecraftServerPlayers).getMaxPlayers() + "; " + MinecraftServer.getServerConfigurationManager(this.minecraftServerPlayers).playerEntityList;
    }

    public Object call()
    {
        return this.getPlayers();
    }
}
