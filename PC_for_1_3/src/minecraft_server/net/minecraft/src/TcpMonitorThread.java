package net.minecraft.src;

class TcpMonitorThread extends Thread
{
    final TcpConnection theTcpConnection;

    TcpMonitorThread(TcpConnection par1TcpConnection)
    {
        this.theTcpConnection = par1TcpConnection;
    }

    public void run()
    {
        try
        {
            Thread.sleep(2000L);

            if (TcpConnection.func_74462_a(this.theTcpConnection))
            {
                TcpConnection.func_74461_h(this.theTcpConnection).interrupt();
                this.theTcpConnection.networkShutdown("disconnect.closed", new Object[0]);
            }
        }
        catch (Exception var2)
        {
            var2.printStackTrace();
        }
    }
}
