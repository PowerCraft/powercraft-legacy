package net.minecraft.src;

import java.io.IOException;
import java.net.InetAddress;
import net.minecraft.server.MinecraftServer;

public class IntegratedServerListenThread extends NetworkListenThread
{
    private final MemoryConnection field_71760_c = new MemoryConnection((NetHandler)null);
    private MemoryConnection field_71758_d;
    private String field_71759_e;
    private boolean field_71756_f = false;
    private ServerListenThread myServerListenThread;

    public IntegratedServerListenThread(IntegratedServer par1IntegratedServer) throws IOException
    {
        super(par1IntegratedServer);
    }

    public void func_71754_a(MemoryConnection par1MemoryConnection, String par2Str)
    {
        this.field_71758_d = par1MemoryConnection;
        this.field_71759_e = par2Str;
    }

    public String func_71755_c() throws IOException
    {
        if (this.myServerListenThread == null)
        {
            int var1 = -1;

            try
            {
                var1 = HttpUtil.func_76181_a();
            }
            catch (IOException var4)
            {
                ;
            }

            if (var1 <= 0)
            {
                var1 = 25564;
            }

            try
            {
                this.myServerListenThread = new ServerListenThread(this, InetAddress.getLocalHost(), var1);
                this.myServerListenThread.start();
            }
            catch (IOException var3)
            {
                throw var3;
            }
        }

        return this.myServerListenThread.getInetAddress().getHostAddress() + ":" + this.myServerListenThread.func_71765_d();
    }

    public void stopListening()
    {
        super.stopListening();

        if (this.myServerListenThread != null)
        {
            System.out.println("Stopping server connection");
            this.myServerListenThread.func_71768_b();
            this.myServerListenThread.interrupt();
            this.myServerListenThread = null;
        }
    }

    /**
     * processes packets and pending connections
     */
    public void networkTick()
    {
        if (this.field_71758_d != null)
        {
            EntityPlayerMP var1 = this.func_71753_e().getConfigurationManager().createPlayerForUser(this.field_71759_e);

            if (var1 != null)
            {
                this.field_71760_c.pairWith(this.field_71758_d);
                this.field_71756_f = true;
                this.func_71753_e().getConfigurationManager().initializeConnectionToPlayer(this.field_71760_c, var1);
            }

            this.field_71758_d = null;
            this.field_71759_e = null;
        }

        if (this.myServerListenThread != null)
        {
            this.myServerListenThread.processPendingConnections();
        }

        super.networkTick();
    }

    public IntegratedServer func_71753_e()
    {
        return (IntegratedServer)super.getServer();
    }

    public boolean func_71752_f()
    {
        return this.field_71756_f && this.field_71760_c.getPairedConnection().isConnectionActive() && this.field_71760_c.getPairedConnection().isGamePaused();
    }

    public MinecraftServer getServer()
    {
        return this.func_71753_e();
    }
}
