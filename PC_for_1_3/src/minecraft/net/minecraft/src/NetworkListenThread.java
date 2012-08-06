package net.minecraft.src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;

public abstract class NetworkListenThread
{
    public static Logger field_71751_a = Logger.getLogger("Minecraft");
    private final MinecraftServer field_71750_c;
    private final List connections = Collections.synchronizedList(new ArrayList());
    public volatile boolean field_71749_b = false;

    public NetworkListenThread(MinecraftServer par1MinecraftServer) throws IOException
    {
        this.field_71750_c = par1MinecraftServer;
        this.field_71749_b = true;
    }

    public void func_71745_a(NetServerHandler par1NetServerHandler)
    {
        this.connections.add(par1NetServerHandler);
    }

    public void stopListening()
    {
        this.field_71749_b = false;
    }

    /**
     * processes packets and pending connections
     */
    public void networkTick()
    {
        for (int var1 = 0; var1 < this.connections.size(); ++var1)
        {
            NetServerHandler var2 = (NetServerHandler)this.connections.get(var1);

            try
            {
                var2.networkTick();
            }
            catch (Exception var4)
            {
                field_71751_a.log(Level.WARNING, "Failed to handle packet: " + var4, var4);
                var2.kickPlayerFromServer("Internal server error");
            }

            if (var2.serverShuttingDown)
            {
                this.connections.remove(var1--);
            }

            var2.theNetworkManager.wakeThreads();
        }
    }

    public MinecraftServer getServer()
    {
        return this.field_71750_c;
    }
}
