package net.minecraft.src;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class RConThreadBase implements Runnable
{
    protected boolean field_72619_a = false;
    protected IServer field_72617_b;
    protected Thread field_72618_c;
    protected int field_72615_d = 5;
    protected List field_72616_e = new ArrayList();
    protected List field_72614_f = new ArrayList();

    RConThreadBase(IServer par1IServer)
    {
        this.field_72617_b = par1IServer;

        if (this.field_72617_b.doLogInfoEvent())
        {
            this.func_72606_c("Debugging is enabled, performance maybe reduced!");
        }
    }

    public synchronized void func_72602_a()
    {
        this.field_72618_c = new Thread(this);
        this.field_72618_c.start();
        this.field_72619_a = true;
    }

    public boolean func_72613_c()
    {
        return this.field_72619_a;
    }

    protected void func_72607_a(String par1Str)
    {
        this.field_72617_b.logInfoEvent(par1Str);
    }

    protected void func_72609_b(String par1Str)
    {
        this.field_72617_b.logInfoMessage(par1Str);
    }

    protected void func_72606_c(String par1Str)
    {
        this.field_72617_b.logWarningMessage(par1Str);
    }

    protected void func_72610_d(String par1Str)
    {
        this.field_72617_b.logSevereEvent(par1Str);
    }

    protected int func_72603_d()
    {
        return this.field_72617_b.getPlayerListSize();
    }

    protected void func_72601_a(DatagramSocket par1DatagramSocket)
    {
        this.func_72607_a("registerSocket: " + par1DatagramSocket);
        this.field_72616_e.add(par1DatagramSocket);
    }

    protected boolean func_72604_a(DatagramSocket par1DatagramSocket, boolean par2)
    {
        this.func_72607_a("closeSocket: " + par1DatagramSocket);

        if (null == par1DatagramSocket)
        {
            return false;
        }
        else
        {
            boolean var3 = false;

            if (!par1DatagramSocket.isClosed())
            {
                par1DatagramSocket.close();
                var3 = true;
            }

            if (par2)
            {
                this.field_72616_e.remove(par1DatagramSocket);
            }

            return var3;
        }
    }

    protected boolean func_72608_b(ServerSocket par1ServerSocket)
    {
        return this.func_72605_a(par1ServerSocket, true);
    }

    protected boolean func_72605_a(ServerSocket par1ServerSocket, boolean par2)
    {
        this.func_72607_a("closeSocket: " + par1ServerSocket);

        if (null == par1ServerSocket)
        {
            return false;
        }
        else
        {
            boolean var3 = false;

            try
            {
                if (!par1ServerSocket.isClosed())
                {
                    par1ServerSocket.close();
                    var3 = true;
                }
            }
            catch (IOException var5)
            {
                this.func_72606_c("IO: " + var5.getMessage());
            }

            if (par2)
            {
                this.field_72614_f.remove(par1ServerSocket);
            }

            return var3;
        }
    }

    protected void func_72611_e()
    {
        this.func_72612_a(false);
    }

    protected void func_72612_a(boolean par1)
    {
        int var2 = 0;
        Iterator var3 = this.field_72616_e.iterator();

        while (var3.hasNext())
        {
            DatagramSocket var4 = (DatagramSocket)var3.next();

            if (this.func_72604_a(var4, false))
            {
                ++var2;
            }
        }

        this.field_72616_e.clear();
        var3 = this.field_72614_f.iterator();

        while (var3.hasNext())
        {
            ServerSocket var5 = (ServerSocket)var3.next();

            if (this.func_72605_a(var5, false))
            {
                ++var2;
            }
        }

        this.field_72614_f.clear();

        if (par1 && 0 < var2)
        {
            this.func_72606_c("Force closed " + var2 + " sockets");
        }
    }
}
