package net.minecraft.src;

import java.io.IOException;

class TcpWriterThread extends Thread
{
    final TcpConnection theTcpConnection;

    TcpWriterThread(TcpConnection par1TcpConnection, String par2Str)
    {
        super(par2Str);
        this.theTcpConnection = par1TcpConnection;
    }

    public void run()
    {
        TcpConnection.field_74469_b.getAndIncrement();

        try
        {
            while (TcpConnection.func_74462_a(this.theTcpConnection))
            {
                boolean var1;

                for (var1 = false; TcpConnection.func_74451_d(this.theTcpConnection); var1 = true)
                {
                    ;
                }

                try
                {
                    if (var1 && TcpConnection.func_74453_e(this.theTcpConnection) != null)
                    {
                        TcpConnection.func_74453_e(this.theTcpConnection).flush();
                    }
                }
                catch (IOException var8)
                {
                    if (!TcpConnection.func_74456_f(this.theTcpConnection))
                    {
                        TcpConnection.func_74458_a(this.theTcpConnection, var8);
                    }

                    var8.printStackTrace();
                }

                try
                {
                    sleep(2L);
                }
                catch (InterruptedException var7)
                {
                    ;
                }
            }
        }
        finally
        {
            TcpConnection.field_74469_b.getAndDecrement();
        }
    }
}
