package net.minecraft.src;

class ThreadMonitorConnection extends Thread
{
    /**
     * This was actually an inner class of NetworkManager, so this field is the reference to 'this' NetworkManager.
     */
    final NetworkManager netManager;

    ThreadMonitorConnection(NetworkManager par1NetworkManager)
    {
        netManager = par1NetworkManager;
    }

    public void run()
    {
        try
        {
            Thread.sleep(2000L);

            if (NetworkManager.isRunning(netManager))
            {
                NetworkManager.getWriteThread(netManager).interrupt();
                netManager.networkShutdown("disconnect.closed", new Object[0]);
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
