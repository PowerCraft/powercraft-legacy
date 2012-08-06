package net.minecraft.src;

class TcpMasterThread extends Thread
{
    final TcpConnection theTcpConnection;

    TcpMasterThread(TcpConnection par1TcpConnection)
    {
        this.theTcpConnection = par1TcpConnection;
    }

    @SuppressWarnings("deprecation")
    public void run()
    {
        try
        {
            Thread.sleep(5000L);

            if (TcpConnection.func_74457_g(this.theTcpConnection).isAlive())
            {
                try
                {
                    TcpConnection.func_74457_g(this.theTcpConnection).stop();
                }
                catch (Throwable var3)
                {
                    ;
                }
            }

            if (TcpConnection.func_74461_h(this.theTcpConnection).isAlive())
            {
                try
                {
                    TcpConnection.func_74461_h(this.theTcpConnection).stop();
                }
                catch (Throwable var2)
                {
                    ;
                }
            }
        }
        catch (InterruptedException var4)
        {
            var4.printStackTrace();
        }
    }
}
