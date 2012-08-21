package net.minecraft.src;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import net.minecraft.server.MinecraftServer;

public class DedicatedServer extends MinecraftServer implements IServer
{
    private final List field_71341_l = Collections.synchronizedList(new ArrayList());
    private RConThreadQuery field_71342_m;
    private RConThreadMain field_71339_n;
    private PropertyManager field_71340_o;
    private boolean field_71338_p;

    /** The Game Type. */
    private EnumGameType theGameType;
    private NetworkListenThread field_71336_r;
    private boolean field_71335_s = false;

    public DedicatedServer(File par1File)
    {
        super(par1File);
        new DedicatedServerSleepThread(this);
    }

    /**
     * Initialises the server and starts it.
     */
    protected boolean startServer() throws IOException
    {
        DedicatedServerCommandThread var1 = new DedicatedServerCommandThread(this);
        var1.setDaemon(true);
        var1.start();
        ConsoleLogManager.init();
        logger.info("Starting minecraft server version 1.3.2");

        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L)
        {
            logger.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        logger.info("Loading properties");
        this.field_71340_o = new PropertyManager(new File("server.properties"));

        if (this.isSinglePlayer())
        {
            this.func_71189_e("127.0.0.1");
        }
        else
        {
            this.func_71229_d(this.field_71340_o.getBooleanProperty("online-mode", true));
            this.func_71189_e(this.field_71340_o.getStringProperty("server-ip", ""));
        }

        this.func_71251_e(this.field_71340_o.getBooleanProperty("spawn-animals", true));
        this.func_71257_f(this.field_71340_o.getBooleanProperty("spawn-npcs", true));
        this.func_71188_g(this.field_71340_o.getBooleanProperty("pvp", true));
        this.func_71245_h(this.field_71340_o.getBooleanProperty("allow-flight", false));
        this.func_71269_o(this.field_71340_o.getStringProperty("texture-pack", ""));
        this.setMOTD(this.field_71340_o.getStringProperty("motd", "A Minecraft Server"));
        this.field_71338_p = this.field_71340_o.getBooleanProperty("generate-structures", true);
        int var2 = this.field_71340_o.getIntProperty("gamemode", EnumGameType.SURVIVAL.getID());
        this.theGameType = WorldSettings.getGameTypeById(var2);
        logger.info("Default game type: " + this.theGameType);
        InetAddress var3 = null;

        if (this.func_71211_k().length() > 0)
        {
            var3 = InetAddress.getByName(this.func_71211_k());
        }

        if (this.getServerPort() < 0)
        {
            this.func_71208_b(this.field_71340_o.getIntProperty("server-port", 25565));
        }

        logger.info("Generating keypair");
        this.func_71253_a(CryptManager.generateKeyPair());
        logger.info("Starting Minecraft server on " + (this.func_71211_k().length() == 0 ? "*" : this.func_71211_k()) + ":" + this.getServerPort());

        try
        {
            this.field_71336_r = new DedicatedServerListenThread(this, var3, this.getServerPort());
        }
        catch (IOException var15)
        {
            logger.warning("**** FAILED TO BIND TO PORT!");
            logger.log(Level.WARNING, "The exception was: " + var15.toString());
            logger.warning("Perhaps a server is already running on that port?");
            return false;
        }

        if (!this.func_71266_T())
        {
            logger.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            logger.warning("The server will make no attempt to authenticate usernames. Beware.");
            logger.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            logger.warning("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
        }

        this.setConfigurationManager(new DedicatedPlayerList(this));
        long var4 = System.nanoTime();

        if (this.func_71270_I() == null)
        {
            this.func_71261_m(this.field_71340_o.getStringProperty("level-name", "world"));
        }

        String var6 = this.field_71340_o.getStringProperty("level-seed", "");
        String var7 = this.field_71340_o.getStringProperty("level-type", "DEFAULT");
        long var8 = (new Random()).nextLong();

        if (var6.length() > 0)
        {
            try
            {
                long var10 = Long.parseLong(var6);

                if (var10 != 0L)
                {
                    var8 = var10;
                }
            }
            catch (NumberFormatException var14)
            {
                var8 = (long)var6.hashCode();
            }
        }

        WorldType var16 = WorldType.parseWorldType(var7);

        if (var16 == null)
        {
            var16 = WorldType.DEFAULT;
        }

        this.setBuildLimit(this.field_71340_o.getIntProperty("max-build-height", 256));
        this.setBuildLimit((this.getBuildLimit() + 8) / 16 * 16);
        this.setBuildLimit(MathHelper.clamp_int(this.getBuildLimit(), 64, 256));
        this.field_71340_o.setProperty("max-build-height", Integer.valueOf(this.getBuildLimit()));
        logger.info("Preparing level \"" + this.func_71270_I() + "\"");
        this.func_71247_a(this.func_71270_I(), this.func_71270_I(), var8, var16);
        long var11 = System.nanoTime() - var4;
        String var13 = String.format("%.3fs", new Object[] {Double.valueOf((double)var11 / 1.0E9D)});
        logger.info("Done (" + var13 + ")! For help, type \"help\" or \"?\"");

        if (this.field_71340_o.getBooleanProperty("enable-query", false))
        {
            logger.info("Starting GS4 status listener");
            this.field_71342_m = new RConThreadQuery(this);
            this.field_71342_m.startThread();
        }

        if (this.field_71340_o.getBooleanProperty("enable-rcon", false))
        {
            logger.info("Starting remote control listener");
            this.field_71339_n = new RConThreadMain(this);
            this.field_71339_n.startThread();
        }

        return true;
    }

    public boolean func_71225_e()
    {
        return this.field_71338_p;
    }

    public EnumGameType func_71265_f()
    {
        return this.theGameType;
    }

    public int func_71232_g()
    {
        return this.field_71340_o.getIntProperty("difficulty", 1);
    }

    public boolean func_71199_h()
    {
        return this.field_71340_o.getBooleanProperty("hardcore", false);
    }

    protected void func_71228_a(CrashReport par1CrashReport)
    {
        while (this.func_71278_l())
        {
            this.func_71333_ah();

            try
            {
                Thread.sleep(10L);
            }
            catch (InterruptedException var3)
            {
                var3.printStackTrace();
            }
        }
    }

    public CrashReport func_71230_b(CrashReport par1CrashReport)
    {
        par1CrashReport = super.func_71230_b(par1CrashReport);
        par1CrashReport.addCrashSectionCallable("Type", new CallableType(this));
        return par1CrashReport;
    }

    protected void func_71240_o()
    {
        System.exit(0);
    }

    public void func_71190_q()
    {
        super.func_71190_q();
        this.func_71333_ah();
    }

    public boolean func_71255_r()
    {
        return this.field_71340_o.getBooleanProperty("allow-nether", true);
    }

    public boolean func_71193_K()
    {
        return this.field_71340_o.getBooleanProperty("spawn-monsters", true);
    }

    public void func_70000_a(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        par1PlayerUsageSnooper.addData("whitelist_enabled", Boolean.valueOf(this.func_71334_ai().func_72383_n()));
        par1PlayerUsageSnooper.addData("whitelist_count", Integer.valueOf(this.func_71334_ai().getWhiteListedIPs().size()));
        super.func_70000_a(par1PlayerUsageSnooper);
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    public boolean isSnooperEnabled()
    {
        return this.field_71340_o.getBooleanProperty("snooper-enabled", true);
    }

    public void func_71331_a(String par1Str, ICommandSender par2ICommandSender)
    {
        this.field_71341_l.add(new ServerCommand(par1Str, par2ICommandSender));
    }

    public void func_71333_ah()
    {
        while (!this.field_71341_l.isEmpty())
        {
            ServerCommand var1 = (ServerCommand)this.field_71341_l.remove(0);
            this.func_71187_D().func_71556_a(var1.field_73701_b, var1.command);
        }
    }

    public boolean isDedicatedServer()
    {
        return true;
    }

    public DedicatedPlayerList func_71334_ai()
    {
        return (DedicatedPlayerList)super.getConfigurationManager();
    }

    public NetworkListenThread func_71212_ac()
    {
        return this.field_71336_r;
    }

    /**
     * Returns the specified property value as an int, or a default if the property doesn't exist
     */
    public int getIntProperty(String par1Str, int par2)
    {
        return this.field_71340_o.getIntProperty(par1Str, par2);
    }

    /**
     * Returns the specified property value as a String, or a default if the property doesn't exist
     */
    public String getStringProperty(String par1Str, String par2Str)
    {
        return this.field_71340_o.getStringProperty(par1Str, par2Str);
    }

    public boolean func_71332_a(String par1Str, boolean par2)
    {
        return this.field_71340_o.getBooleanProperty(par1Str, par2);
    }

    /**
     * Saves an Object with the given property name
     */
    public void setProperty(String par1Str, Object par2Obj)
    {
        this.field_71340_o.setProperty(par1Str, par2Obj);
    }

    /**
     * Saves all of the server properties to the properties file
     */
    public void saveProperties()
    {
        this.field_71340_o.saveProperties();
    }

    /**
     * Returns the filename where server properties are stored
     */
    public String getSettingsFilename()
    {
        File var1 = this.field_71340_o.getPropertiesFile();
        return var1 != null ? var1.getAbsolutePath() : "No settings file";
    }

    public void func_79001_aj()
    {
        ServerGUI.func_79003_a(this);
        this.field_71335_s = true;
    }

    public boolean func_71279_ae()
    {
        return this.field_71335_s;
    }

    public String func_71206_a(EnumGameType par1EnumGameType, boolean par2)
    {
        return "";
    }

    public ServerConfigurationManager getConfigurationManager()
    {
        return this.func_71334_ai();
    }
}
