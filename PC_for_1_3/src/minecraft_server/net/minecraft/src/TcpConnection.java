package net.minecraft.src;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.crypto.SecretKey;

public class TcpConnection implements NetworkManager
{
    public static AtomicInteger field_74471_a = new AtomicInteger();
    public static AtomicInteger field_74469_b = new AtomicInteger();
    private Object field_74478_h = new Object();
    private Socket field_74479_i;
    private final SocketAddress field_74476_j;
    private volatile DataInputStream field_74477_k;
    private volatile DataOutputStream field_74474_l;
    private volatile boolean field_74475_m = true;
    private volatile boolean field_74472_n = false;
    private List field_74473_o = Collections.synchronizedList(new ArrayList());
    private List field_74487_p = Collections.synchronizedList(new ArrayList());
    private List field_74486_q = Collections.synchronizedList(new ArrayList());
    private NetHandler field_74485_r;
    private boolean field_74484_s = false;
    private Thread field_74483_t;
    private Thread field_74482_u;
    private String field_74481_v = "";
    private Object[] field_74480_w;
    private int field_74490_x = 0;
    private int field_74489_y = 0;
    public static int[] field_74470_c = new int[256];
    public static int[] field_74467_d = new int[256];
    public int field_74468_e = 0;
    boolean field_74465_f = false;
    boolean field_74466_g = false;
    private SecretKey field_74488_z = null;
    private PrivateKey field_74463_A = null;
    private int field_74464_B = 50;

    public TcpConnection(Socket par1Socket, String par2Str, NetHandler par3NetHandler, PrivateKey par4PrivateKey) throws IOException
    {
        this.field_74463_A = par4PrivateKey;
        this.field_74479_i = par1Socket;
        this.field_74476_j = par1Socket.getRemoteSocketAddress();
        this.field_74485_r = par3NetHandler;

        try
        {
            par1Socket.setSoTimeout(30000);
            par1Socket.setTrafficClass(24);
        }
        catch (SocketException var6)
        {
            System.err.println(var6.getMessage());
        }

        this.field_74477_k = new DataInputStream(par1Socket.getInputStream());
        this.field_74474_l = new DataOutputStream(new BufferedOutputStream(par1Socket.getOutputStream(), 5120));
        this.field_74482_u = new TcpReaderThread(this, par2Str + " read thread");
        this.field_74483_t = new TcpWriterThread(this, par2Str + " write thread");
        this.field_74482_u.start();
        this.field_74483_t.start();
    }

    /**
     * Sets the NetHandler for this NetworkManager. Server-only.
     */
    public void setNetHandler(NetHandler par1NetHandler)
    {
        this.field_74485_r = par1NetHandler;
    }

    /**
     * Adds the packet to the correct send queue (chunk data packets go to a separate queue).
     */
    public void addToSendQueue(Packet par1Packet)
    {
        if (!this.field_74484_s)
        {
            Object var2 = this.field_74478_h;

            synchronized (this.field_74478_h)
            {
                this.field_74489_y += par1Packet.getPacketSize() + 1;

                if (par1Packet.isChunkDataPacket)
                {
                    this.field_74486_q.add(par1Packet);
                }
                else
                {
                    this.field_74487_p.add(par1Packet);
                }
            }
        }
    }

    private boolean func_74459_h()
    {
        boolean var1 = false;

        try
        {
            Packet var2;
            int var10001;
            int[] var10000;

            if (this.field_74468_e == 0 || System.currentTimeMillis() - ((Packet)this.field_74487_p.get(0)).creationTimeMillis >= (long)this.field_74468_e)
            {
                var2 = this.func_74460_a(false);

                if (var2 != null)
                {
                    Packet.writePacket(var2, this.field_74474_l);

                    if (var2 instanceof Packet252SharedKey && !this.field_74466_g)
                    {
                        if (!this.field_74485_r.isServerHandler())
                        {
                            this.field_74488_z = ((Packet252SharedKey)var2).func_73304_d();
                        }

                        this.func_74446_k();
                    }

                    var10000 = field_74467_d;
                    var10001 = var2.getPacketId();
                    var10000[var10001] += var2.getPacketSize() + 1;
                    var1 = true;
                }
            }

            if (this.field_74464_B-- <= 0 && (this.field_74468_e == 0 || System.currentTimeMillis() - ((Packet)this.field_74486_q.get(0)).creationTimeMillis >= (long)this.field_74468_e))
            {
                var2 = this.func_74460_a(true);

                if (var2 != null)
                {
                    Packet.writePacket(var2, this.field_74474_l);
                    var10000 = field_74467_d;
                    var10001 = var2.getPacketId();
                    var10000[var10001] += var2.getPacketSize() + 1;
                    this.field_74464_B = 0;
                    var1 = true;
                }
            }

            return var1;
        }
        catch (Exception var3)
        {
            if (!this.field_74472_n)
            {
                this.func_74455_a(var3);
            }

            return false;
        }
    }

    private Packet func_74460_a(boolean par1)
    {
        Packet var2 = null;
        List var3 = par1 ? this.field_74486_q : this.field_74487_p;
        Object var4 = this.field_74478_h;

        synchronized (this.field_74478_h)
        {
            while (!var3.isEmpty() && var2 == null)
            {
                var2 = (Packet)var3.remove(0);
                this.field_74489_y -= var2.getPacketSize() + 1;

                if (this.func_74454_a(var2, par1))
                {
                    var2 = null;
                }
            }

            return var2;
        }
    }

    private boolean func_74454_a(Packet par1Packet, boolean par2)
    {
        if (!par1Packet.func_73278_e())
        {
            return false;
        }
        else
        {
            List var3 = par2 ? this.field_74486_q : this.field_74487_p;
            Iterator var4 = var3.iterator();
            Packet var5;

            do
            {
                if (!var4.hasNext())
                {
                    return false;
                }

                var5 = (Packet)var4.next();
            }
            while (var5.getPacketId() != par1Packet.getPacketId());

            return par1Packet.func_73268_a(var5);
        }
    }

    public void func_74427_a()
    {
        if (this.field_74482_u != null)
        {
            this.field_74482_u.interrupt();
        }

        if (this.field_74483_t != null)
        {
            this.field_74483_t.interrupt();
        }
    }

    private boolean func_74447_i()
    {
        boolean var1 = false;

        try
        {
            Packet var2 = Packet.readPacket(this.field_74477_k, this.field_74485_r.isServerHandler());

            if (var2 != null)
            {
                if (var2 instanceof Packet252SharedKey && !this.field_74465_f)
                {
                    if (this.field_74485_r.isServerHandler())
                    {
                        this.field_74488_z = ((Packet252SharedKey)var2).func_73303_a(this.field_74463_A);
                    }

                    this.func_74448_j();
                }

                int[] var10000 = field_74470_c;
                int var10001 = var2.getPacketId();
                var10000[var10001] += var2.getPacketSize() + 1;

                if (!this.field_74484_s)
                {
                    if (var2.func_73277_a_() && this.field_74485_r.func_72469_b())
                    {
                        this.field_74490_x = 0;
                        var2.processPacket(this.field_74485_r);
                    }
                    else
                    {
                        this.field_74473_o.add(var2);
                    }
                }

                var1 = true;
            }
            else
            {
                this.networkShutdown("disconnect.endOfStream", new Object[0]);
            }

            return var1;
        }
        catch (Exception var3)
        {
            if (!this.field_74472_n)
            {
                this.func_74455_a(var3);
            }

            return false;
        }
    }

    private void func_74455_a(Exception par1Exception)
    {
        par1Exception.printStackTrace();
        this.networkShutdown("disconnect.genericReason", new Object[] {"Internal exception: " + par1Exception.toString()});
    }

    /**
     * Shuts down the network with the specified reason. Closes all streams and sockets, spawns NetworkMasterThread to
     * stop reading and writing threads.
     */
    public void networkShutdown(String par1Str, Object ... par2ArrayOfObj)
    {
        if (this.field_74475_m)
        {
            this.field_74472_n = true;
            this.field_74481_v = par1Str;
            this.field_74480_w = par2ArrayOfObj;
            this.field_74475_m = false;
            (new TcpMasterThread(this)).start();

            try
            {
                this.field_74477_k.close();
                this.field_74477_k = null;
                this.field_74474_l.close();
                this.field_74474_l = null;
                this.field_74479_i.close();
                this.field_74479_i = null;
            }
            catch (Throwable var4)
            {
                ;
            }
        }
    }

    /**
     * Checks timeouts and processes all pending read packets.
     */
    public void processReadPackets()
    {
        if (this.field_74489_y > 2097152)
        {
            this.networkShutdown("disconnect.overflow", new Object[0]);
        }

        if (this.field_74473_o.isEmpty())
        {
            if (this.field_74490_x++ == 1200)
            {
                this.networkShutdown("disconnect.timeout", new Object[0]);
            }
        }
        else
        {
            this.field_74490_x = 0;
        }

        int var1 = 1000;

        while (!this.field_74473_o.isEmpty() && var1-- >= 0)
        {
            Packet var2 = (Packet)this.field_74473_o.remove(0);
            var2.processPacket(this.field_74485_r);
        }

        this.func_74427_a();

        if (this.field_74472_n && this.field_74473_o.isEmpty())
        {
            this.field_74485_r.handleErrorMessage(this.field_74481_v, this.field_74480_w);
        }
    }

    /**
     * Returns the socket address of the remote side. Server-only.
     */
    public SocketAddress getRemoteAddress()
    {
        return this.field_74476_j;
    }

    /**
     * Shuts down the server. (Only actually used on the server)
     */
    public void serverShutdown()
    {
        if (!this.field_74484_s)
        {
            this.func_74427_a();
            this.field_74484_s = true;
            this.field_74482_u.interrupt();
            (new TcpMonitorThread(this)).start();
        }
    }

    private void func_74448_j() throws IOException
    {
        this.field_74465_f = true;
        this.field_74477_k = new DataInputStream(CryptManager.func_75888_a(this.field_74488_z, this.field_74479_i.getInputStream()));
    }

    private void func_74446_k() throws IOException
    {
        this.field_74474_l.flush();
        this.field_74466_g = true;
        this.field_74474_l = new DataOutputStream(new BufferedOutputStream(CryptManager.func_75897_a(this.field_74488_z, this.field_74479_i.getOutputStream()), 5120));
    }

    /**
     * Returns the number of chunk data packets waiting to be sent.
     */
    public int getNumChunkDataPackets()
    {
        return this.field_74486_q.size();
    }

    public Socket func_74452_g()
    {
        return this.field_74479_i;
    }

    static boolean func_74462_a(TcpConnection par0TcpConnection)
    {
        return par0TcpConnection.field_74475_m;
    }

    static boolean func_74449_b(TcpConnection par0TcpConnection)
    {
        return par0TcpConnection.field_74484_s;
    }

    static boolean func_74450_c(TcpConnection par0TcpConnection)
    {
        return par0TcpConnection.func_74447_i();
    }

    static boolean func_74451_d(TcpConnection par0TcpConnection)
    {
        return par0TcpConnection.func_74459_h();
    }

    static DataOutputStream func_74453_e(TcpConnection par0TcpConnection)
    {
        return par0TcpConnection.field_74474_l;
    }

    static boolean func_74456_f(TcpConnection par0TcpConnection)
    {
        return par0TcpConnection.field_74472_n;
    }

    static void func_74458_a(TcpConnection par0TcpConnection, Exception par1Exception)
    {
        par0TcpConnection.func_74455_a(par1Exception);
    }

    static Thread func_74457_g(TcpConnection par0TcpConnection)
    {
        return par0TcpConnection.field_74482_u;
    }

    static Thread func_74461_h(TcpConnection par0TcpConnection)
    {
        return par0TcpConnection.field_74483_t;
    }
}
