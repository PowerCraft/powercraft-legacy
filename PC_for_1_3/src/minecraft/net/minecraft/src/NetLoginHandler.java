package net.minecraft.src;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import net.minecraft.server.MinecraftServer;

public class NetLoginHandler extends NetHandler
{
    private byte[] field_72536_d;
    public static Logger field_72540_a = Logger.getLogger("Minecraft");
    private static Random field_72537_e = new Random();
    public TcpConnection myTCPConnection;
    public boolean connectionComplete = false;

    /** Reference to the MinecraftServer object. */
    private MinecraftServer mcServer;
    private int connectionTimer = 0;
    private String clientUsername = null;
    private volatile boolean field_72544_i = false;
    private String field_72541_j = "";
    private SecretKey field_72542_k = null;

    public NetLoginHandler(MinecraftServer par1MinecraftServer, Socket par2Socket, String par3Str) throws IOException
    {
        this.mcServer = par1MinecraftServer;
        this.myTCPConnection = new TcpConnection(par2Socket, par3Str, this, par1MinecraftServer.getKeyPair().getPrivate());
        this.myTCPConnection.field_74468_e = 0;
    }

    /**
     * Logs the user in if a login packet is found, otherwise keeps processing network packets unless the timeout has
     * occurred.
     */
    public void tryLogin()
    {
        if (this.field_72544_i)
        {
            this.initializePlayerConnection();
        }

        if (this.connectionTimer++ == 600)
        {
            this.raiseErrorAndDisconnect("Took too long to log in");
        }
        else
        {
            this.myTCPConnection.processReadPackets();
        }
    }

    public void raiseErrorAndDisconnect(String par1Str)
    {
        try
        {
            field_72540_a.info("Disconnecting " + this.getUsernameAndAddress() + ": " + par1Str);
            this.myTCPConnection.addToSendQueue(new Packet255KickDisconnect(par1Str));
            this.myTCPConnection.serverShutdown();
            this.connectionComplete = true;
        }
        catch (Exception var3)
        {
            var3.printStackTrace();
        }
    }

    public void handleClientProtocol(Packet2ClientProtocol par1Packet2ClientProtocol)
    {
        this.clientUsername = par1Packet2ClientProtocol.getUsername();

        if (!this.clientUsername.equals(StringUtils.stripControlCodes(this.clientUsername)))
        {
            this.raiseErrorAndDisconnect("Invalid username!");
        }
        else
        {
            PublicKey var2 = this.mcServer.getKeyPair().getPublic();

            if (par1Packet2ClientProtocol.getProtocolVersion() != 39)
            {
                if (par1Packet2ClientProtocol.getProtocolVersion() > 39)
                {
                    this.raiseErrorAndDisconnect("Outdated server!");
                }
                else
                {
                    this.raiseErrorAndDisconnect("Outdated client!");
                }
            }
            else
            {
                this.field_72541_j = this.mcServer.isServerInOnlineMode() ? Long.toString(field_72537_e.nextLong(), 16) : "-";
                this.field_72536_d = new byte[4];
                field_72537_e.nextBytes(this.field_72536_d);
                this.myTCPConnection.addToSendQueue(new Packet253ServerAuthData(this.field_72541_j, var2, this.field_72536_d));
            }
        }
    }

    public void handleSharedKey(Packet252SharedKey par1Packet252SharedKey)
    {
        PrivateKey var2 = this.mcServer.getKeyPair().getPrivate();
        this.field_72542_k = par1Packet252SharedKey.func_73303_a(var2);

        if (!Arrays.equals(this.field_72536_d, par1Packet252SharedKey.func_73302_b(var2)))
        {
            this.raiseErrorAndDisconnect("Invalid client reply");
        }

        this.myTCPConnection.addToSendQueue(new Packet252SharedKey());
    }

    public void handleClientCommand(Packet205ClientCommand par1Packet205ClientCommand)
    {
        if (par1Packet205ClientCommand.forceRespawn == 0)
        {
            if (this.mcServer.isServerInOnlineMode())
            {
                (new ThreadLoginVerifier(this)).start();
            }
            else
            {
                this.field_72544_i = true;
            }
        }
    }

    public void handleLogin(Packet1Login par1Packet1Login) {}

    /**
     * on success the specified username is connected to the minecraftInstance, otherwise they are packet255'd
     */
    public void initializePlayerConnection()
    {
        String var1 = this.mcServer.getConfigurationManager().allowUserToConnect(this.myTCPConnection.getSocketAddress(), this.clientUsername);

        if (var1 != null)
        {
            this.raiseErrorAndDisconnect(var1);
        }
        else
        {
            EntityPlayerMP var2 = this.mcServer.getConfigurationManager().createPlayerForUser(this.clientUsername);

            if (var2 != null)
            {
                this.mcServer.getConfigurationManager().initializeConnectionToPlayer(this.myTCPConnection, var2);
            }
        }

        this.connectionComplete = true;
    }

    public void handleErrorMessage(String par1Str, Object[] par2ArrayOfObj)
    {
        field_72540_a.info(this.getUsernameAndAddress() + " lost connection");
        this.connectionComplete = true;
    }

    /**
     * Handle a server ping packet.
     */
    public void handleServerPing(Packet254ServerPing par1Packet254ServerPing)
    {
        try
        {
            String var2 = this.mcServer.getMOTD() + "\u00a7" + this.mcServer.getConfigurationManager().getPlayerListSize() + "\u00a7" + this.mcServer.getConfigurationManager().getMaxPlayers();
            InetAddress var3 = null;

            if (this.myTCPConnection.getSocket() != null)
            {
                var3 = this.myTCPConnection.getSocket().getInetAddress();
            }

            this.myTCPConnection.addToSendQueue(new Packet255KickDisconnect(var2));
            this.myTCPConnection.serverShutdown();

            if (var3 != null && this.mcServer.getNetworkThread() instanceof DedicatedServerListenThread)
            {
                ((DedicatedServerListenThread)this.mcServer.getNetworkThread()).func_71761_a(var3);
            }

            this.connectionComplete = true;
        }
        catch (Exception var4)
        {
            var4.printStackTrace();
        }
    }

    public void registerPacket(Packet par1Packet)
    {
        this.raiseErrorAndDisconnect("Protocol error");
    }

    public String getUsernameAndAddress()
    {
        return this.clientUsername != null ? this.clientUsername + " [" + this.myTCPConnection.getSocketAddress().toString() + "]" : this.myTCPConnection.getSocketAddress().toString();
    }

    /**
     * determine if it is a server handler
     */
    public boolean isServerHandler()
    {
        return true;
    }

    static String func_72526_a(NetLoginHandler par0NetLoginHandler)
    {
        return par0NetLoginHandler.field_72541_j;
    }

    static MinecraftServer func_72530_b(NetLoginHandler par0NetLoginHandler)
    {
        return par0NetLoginHandler.mcServer;
    }

    static SecretKey func_72525_c(NetLoginHandler par0NetLoginHandler)
    {
        return par0NetLoginHandler.field_72542_k;
    }

    static String func_72533_d(NetLoginHandler par0NetLoginHandler)
    {
        return par0NetLoginHandler.clientUsername;
    }

    static boolean func_72531_a(NetLoginHandler par0NetLoginHandler, boolean par1)
    {
        return par0NetLoginHandler.field_72544_i = par1;
    }
}
