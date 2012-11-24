package net.minecraft.src;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class RConThreadBase implements Runnable
{
    protected boolean running = false;

    protected IServer server;

    protected Thread rconThread;
    protected int field_72615_d = 5;

    protected List socketList = new ArrayList();

    protected List serverSocketList = new ArrayList();

    RConThreadBase(IServer par1IServer)
    {
        this.server = par1IServer;

        if (this.server.isDebuggingEnabled())
        {
            this.logWarning("Debugging is enabled, performance maybe reduced!");
        }
    }

    public synchronized void startThread()
    {
        this.rconThread = new Thread(this);
        this.rconThread.start();
        this.running = true;
    }

    public boolean isRunning()
    {
        return this.running;
    }

    protected void logDebug(String par1Str)
    {
        this.server.logDebug(par1Str);
    }

    protected void logInfo(String par1Str)
    {
        this.server.logInfo(par1Str);
    }

    protected void logWarning(String par1Str)
    {
        this.server.logWarning(par1Str);
    }

    protected void logSevere(String par1Str)
    {
        this.server.logSevere(par1Str);
    }

    protected int getNumberOfPlayers()
    {
        return this.server.getCurrentPlayerCount();
    }

    protected void registerSocket(DatagramSocket par1DatagramSocket)
    {
        this.logDebug("registerSocket: " + par1DatagramSocket);
        this.socketList.add(par1DatagramSocket);
    }

    protected boolean closeSocket(DatagramSocket par1DatagramSocket, boolean par2)
    {
        this.logDebug("closeSocket: " + par1DatagramSocket);

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
                this.socketList.remove(par1DatagramSocket);
            }

            return var3;
        }
    }

    protected boolean closeServerSocket(ServerSocket par1ServerSocket)
    {
        return this.closeServerSocket_do(par1ServerSocket, true);
    }

    protected boolean closeServerSocket_do(ServerSocket par1ServerSocket, boolean par2)
    {
        this.logDebug("closeSocket: " + par1ServerSocket);

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
                this.logWarning("IO: " + var5.getMessage());
            }

            if (par2)
            {
                this.serverSocketList.remove(par1ServerSocket);
            }

            return var3;
        }
    }

    protected void closeAllSockets()
    {
        this.closeAllSockets_do(false);
    }

    protected void closeAllSockets_do(boolean par1)
    {
        int var2 = 0;
        Iterator var3 = this.socketList.iterator();

        while (var3.hasNext())
        {
            DatagramSocket var4 = (DatagramSocket)var3.next();

            if (this.closeSocket(var4, false))
            {
                ++var2;
            }
        }

        this.socketList.clear();
        var3 = this.serverSocketList.iterator();

        while (var3.hasNext())
        {
            ServerSocket var5 = (ServerSocket)var3.next();

            if (this.closeServerSocket_do(var5, false))
            {
                ++var2;
            }
        }

        this.serverSocketList.clear();

        if (par1 && 0 < var2)
        {
            this.logWarning("Force closed " + var2 + " sockets");
        }
    }
}
