package net.minecraft.src;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerListenThread extends Thread
{
    private static Logger field_71777_a = Logger.getLogger("Minecraft");
    private final List field_71775_b = Collections.synchronizedList(new ArrayList());
    private final HashMap field_71776_c = new HashMap();
    private int field_71773_d = 0;
    private final ServerSocket field_71774_e;
    private NetworkListenThread field_71771_f;
    private final InetAddress field_71772_g;
    private final int field_71778_h;

    public ServerListenThread(NetworkListenThread par1NetworkListenThread, InetAddress par2InetAddress, int par3) throws IOException
    {
        super("Listen thread");
        this.field_71771_f = par1NetworkListenThread;
        this.field_71772_g = par2InetAddress;
        this.field_71778_h = par3;
        this.field_71774_e = new ServerSocket(par3, 0, par2InetAddress);
        this.field_71774_e.setPerformancePreferences(0, 2, 1);
    }

    public void func_71766_a()
    {
        List var1 = this.field_71775_b;

        synchronized (this.field_71775_b)
        {
            for (int var2 = 0; var2 < this.field_71775_b.size(); ++var2)
            {
                NetLoginHandler var3 = (NetLoginHandler)this.field_71775_b.get(var2);

                try
                {
                    var3.tryLogin();
                }
                catch (Exception var6)
                {
                    var3.kickUser("Internal server error");
                    field_71777_a.log(Level.WARNING, "Failed to handle packet: " + var6, var6);
                }

                if (var3.finishedProcessing)
                {
                    this.field_71775_b.remove(var2--);
                }

                var3.field_72538_b.func_74427_a();
            }
        }
    }

    public void run()
    {
        while (this.field_71771_f.isListening)
        {
            try
            {
                Socket var1 = this.field_71774_e.accept();
                InetAddress var2 = var1.getInetAddress();
                long var3 = System.currentTimeMillis();
                HashMap var5 = this.field_71776_c;

                synchronized (this.field_71776_c)
                {
                    if (this.field_71776_c.containsKey(var2) && !func_71770_b(var2) && var3 - ((Long)this.field_71776_c.get(var2)).longValue() < 4000L)
                    {
                        this.field_71776_c.put(var2, Long.valueOf(var3));
                        var1.close();
                        continue;
                    }

                    this.field_71776_c.put(var2, Long.valueOf(var3));
                }

                NetLoginHandler var9 = new NetLoginHandler(this.field_71771_f.getServer(), var1, "Connection #" + this.field_71773_d++);
                this.func_71764_a(var9);
            }
            catch (IOException var8)
            {
                var8.printStackTrace();
            }
        }

        System.out.println("Closing listening thread");
    }

    private void func_71764_a(NetLoginHandler par1NetLoginHandler)
    {
        if (par1NetLoginHandler == null)
        {
            throw new IllegalArgumentException("Got null pendingconnection!");
        }
        else
        {
            List var2 = this.field_71775_b;

            synchronized (this.field_71775_b)
            {
                this.field_71775_b.add(par1NetLoginHandler);
            }
        }
    }

    private static boolean func_71770_b(InetAddress par0InetAddress)
    {
        return "127.0.0.1".equals(par0InetAddress.getHostAddress());
    }

    public void func_71769_a(InetAddress par1InetAddress)
    {
        if (par1InetAddress != null)
        {
            HashMap var2 = this.field_71776_c;

            synchronized (this.field_71776_c)
            {
                this.field_71776_c.remove(par1InetAddress);
            }
        }
    }

    public void func_71768_b()
    {
        try
        {
            this.field_71774_e.close();
        }
        catch (Throwable var2)
        {
            ;
        }
    }
}
