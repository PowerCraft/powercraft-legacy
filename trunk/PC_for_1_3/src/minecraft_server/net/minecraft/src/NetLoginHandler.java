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

    /** The Minecraft logger. */
    public static Logger logger = Logger.getLogger("Minecraft");

    /** The Random object used to generate serverId hex strings. */
    private static Random rand = new Random();
    public TcpConnection field_72538_b;

    /**
     * Returns if the login handler is finished and can be removed. It is set to true on either error or successful
     * login.
     */
    public boolean finishedProcessing = false;

    /** Reference to the MinecraftServer object. */
    private MinecraftServer mcServer;

    /** While waiting to login, if this field ++'s to 600 it will kick you. */
    private int loginTimer = 0;
    private String field_72543_h = null;
    private volatile boolean field_72544_i = false;
    private String field_72541_j = "";
    private SecretKey field_72542_k = null;

    public NetLoginHandler(MinecraftServer par1MinecraftServer, Socket par2Socket, String par3Str) throws IOException
    {
        this.mcServer = par1MinecraftServer;
        this.field_72538_b = new TcpConnection(par2Socket, par3Str, this, par1MinecraftServer.getKeyPair().getPrivate());
        this.field_72538_b.field_74468_e = 0;
    }

    /**
     * Logs the user in if a login packet is found, otherwise keeps processing network packets unless the timeout has
     * occurred.
     */
    public void tryLogin()
    {
        if (this.field_72544_i)
        {
            this.func_72529_d();
        }

        if (this.loginTimer++ == 600)
        {
            this.kickUser("Took too long to log in");
        }
        else
        {
            this.field_72538_b.processReadPackets();
        }
    }

    /**
     * Disconnects the user with the given reason.
     */
    public void kickUser(String par1Str)
    {
        try
        {
            logger.info("Disconnecting " + this.func_72528_e() + ": " + par1Str);
            this.field_72538_b.addToSendQueue(new Packet255KickDisconnect(par1Str));
            this.field_72538_b.serverShutdown();
            this.finishedProcessing = true;
        }
        catch (Exception var3)
        {
            var3.printStackTrace();
        }
    }

    public void func_72500_a(Packet2ClientProtocol par1Packet2ClientProtocol)
    {
        this.field_72543_h = par1Packet2ClientProtocol.getUsername();

        if (!this.field_72543_h.equals(StringUtils.stripControlCodes(this.field_72543_h)))
        {
            this.kickUser("Invalid username!");
        }
        else
        {
            PublicKey var2 = this.mcServer.getKeyPair().getPublic();

            if (par1Packet2ClientProtocol.getProtocolVersion() != 39)
            {
                if (par1Packet2ClientProtocol.getProtocolVersion() > 39)
                {
                    this.kickUser("Outdated server!");
                }
                else
                {
                    this.kickUser("Outdated client!");
                }
            }
            else
            {
                this.field_72541_j = this.mcServer.func_71266_T() ? Long.toString(rand.nextLong(), 16) : "-";
                this.field_72536_d = new byte[4];
                rand.nextBytes(this.field_72536_d);
                this.field_72538_b.addToSendQueue(new Packet253ServerAuthData(this.field_72541_j, var2, this.field_72536_d));
            }
        }
    }

    public void func_72513_a(Packet252SharedKey par1Packet252SharedKey)
    {
        PrivateKey var2 = this.mcServer.getKeyPair().getPrivate();
        this.field_72542_k = par1Packet252SharedKey.func_73303_a(var2);

        if (!Arrays.equals(this.field_72536_d, par1Packet252SharedKey.func_73302_b(var2)))
        {
            this.kickUser("Invalid client reply");
        }

        this.field_72538_b.addToSendQueue(new Packet252SharedKey());
    }

    public void func_72458_a(Packet205ClientCommand par1Packet205ClientCommand)
    {
        if (par1Packet205ClientCommand.field_73447_a == 0)
        {
            if (this.mcServer.func_71266_T())
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

    public void func_72529_d()
    {
        String var1 = this.mcServer.func_71203_ab().func_72399_a(this.field_72538_b.getRemoteAddress(), this.field_72543_h);

        if (var1 != null)
        {
            this.kickUser(var1);
        }
        else
        {
            EntityPlayerMP var2 = this.mcServer.func_71203_ab().func_72366_a(this.field_72543_h);

            if (var2 != null)
            {
                this.mcServer.func_71203_ab().func_72355_a(this.field_72538_b, var2);
            }
        }

        this.finishedProcessing = true;
    }

    public void handleErrorMessage(String par1Str, Object[] par2ArrayOfObj)
    {
        logger.info(this.func_72528_e() + " lost connection");
        this.finishedProcessing = true;
    }

    /**
     * Handle a server ping packet.
     */
    public void handleServerPing(Packet254ServerPing par1Packet254ServerPing)
    {
        try
        {
            String var2 = this.mcServer.func_71273_Y() + "\u00a7" + this.mcServer.func_71203_ab().playersOnline() + "\u00a7" + this.mcServer.func_71203_ab().getMaxPlayers();
            InetAddress var3 = null;

            if (this.field_72538_b.func_74452_g() != null)
            {
                var3 = this.field_72538_b.func_74452_g().getInetAddress();
            }

            this.field_72538_b.addToSendQueue(new Packet255KickDisconnect(var2));
            this.field_72538_b.serverShutdown();

            if (var3 != null && this.mcServer.func_71212_ac() instanceof DedicatedServerListenThread)
            {
                ((DedicatedServerListenThread)this.mcServer.func_71212_ac()).func_71761_a(var3);
            }

            this.finishedProcessing = true;
        }
        catch (Exception var4)
        {
            var4.printStackTrace();
        }
    }

    public void registerPacket(Packet par1Packet)
    {
        this.kickUser("Protocol error");
    }

    public String func_72528_e()
    {
        return this.field_72543_h != null ? this.field_72543_h + " [" + this.field_72538_b.getRemoteAddress().toString() + "]" : this.field_72538_b.getRemoteAddress().toString();
    }

    /**
     * determine if it is a server handler
     */
    public boolean isServerHandler()
    {
        return true;
    }

    /**
     * Returns the server Id randomly generated by this login handler.
     */
    static String getServerId(NetLoginHandler par0NetLoginHandler)
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
        return par0NetLoginHandler.field_72543_h;
    }

    static boolean func_72531_a(NetLoginHandler par0NetLoginHandler, boolean par1)
    {
        return par0NetLoginHandler.field_72544_i = par1;
    }
}
