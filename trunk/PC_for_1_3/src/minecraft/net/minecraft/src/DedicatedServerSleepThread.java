package net.minecraft.src;

class DedicatedServerSleepThread extends Thread
{
    final DedicatedServer field_72451_a;

    DedicatedServerSleepThread(DedicatedServer par1DedicatedServer)
    {
        this.field_72451_a = par1DedicatedServer;
        this.setDaemon(true);
        this.start();
    }

    public void run()
    {
        while (true)
        {
            try
            {
                while (true)
                {
                    Thread.sleep(2147483647L);
                }
            }
            catch (InterruptedException var2)
            {
                ;
            }
        }
    }
}
