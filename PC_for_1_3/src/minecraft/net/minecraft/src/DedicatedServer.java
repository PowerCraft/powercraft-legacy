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
    private final List pendingCommandList = Collections.synchronizedList(new ArrayList());
    private RConThreadQuery field_71342_m;
    private RConThreadMain field_71339_n;
    private PropertyManager settings;
    private boolean canSpawnStructures;
    private EnumGameType gameType;
    private NetworkListenThread networkThread;
    private boolean guiIsEnabled = false;

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
        ConsoleLogManager.func_73699_a();
        logger.info("Starting minecraft server version 1.3.2");

        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L)
        {
            logger.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        logger.info("Loading properties");
        this.settings = new PropertyManager(new File("server.properties"));

        if (this.isSinglePlayer())
        {
            this.getHostName("127.0.0.1");
        }
        else
        {
            this.setOnlineMode(this.settings.getOrSetBoolProperty("online-mode", true));
            this.getHostName(this.settings.getOrSetProperty("server-ip", ""));
        }

        this.setSpawnAnimals(this.settings.getOrSetBoolProperty("spawn-animals", true));
        this.setSpawnNpcs(this.settings.getOrSetBoolProperty("spawn-npcs", true));
        this.setAllowPvp(this.settings.getOrSetBoolProperty("pvp", true));
        this.setAllowFlight(this.settings.getOrSetBoolProperty("allow-flight", false));
        this.setTexturePack(this.settings.getOrSetProperty("texture-pack", ""));
        this.setMOTD(this.settings.getOrSetProperty("motd", "A Minecraft Server"));
        this.canSpawnStructures = this.settings.getOrSetBoolProperty("generate-structures", true);
        int var2 = this.settings.getOrSetIntProperty("gamemode", EnumGameType.SURVIVAL.getID());
        this.gameType = WorldSettings.getGameTypeById(var2);
        logger.info("Default game type: " + this.gameType);
        InetAddress var3 = null;

        if (this.getHostname().length() > 0)
        {
            var3 = InetAddress.getByName(this.getHostname());
        }

        if (this.getServerPort() < 0)
        {
            this.setServerPort(this.settings.getOrSetIntProperty("server-port", 25565));
        }

        logger.info("Generating keypair");
        this.setKeyPair(CryptManager.createNewKeyPair());
        logger.info("Starting Minecraft server on " + (this.getHostname().length() == 0 ? "*" : this.getHostname()) + ":" + this.getServerPort());

        try
        {
            this.networkThread = new DedicatedServerListenThread(this, var3, this.getServerPort());
        }
        catch (IOException var15)
        {
            logger.warning("**** FAILED TO BIND TO PORT!");
            logger.log(Level.WARNING, "The exception was: " + var15.toString());
            logger.warning("Perhaps a server is already running on that port?");
            return false;
        }

        if (!this.isServerInOnlineMode())
        {
            logger.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            logger.warning("The server will make no attempt to authenticate usernames. Beware.");
            logger.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            logger.warning("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
        }

        this.setConfigurationManager(new DedicatedPlayerList(this));
        long var4 = System.nanoTime();

        if (this.getFolderName() == null)
        {
            this.setFolderName(this.settings.getOrSetProperty("level-name", "world"));
        }

        String var6 = this.settings.getOrSetProperty("level-seed", "");
        String var7 = this.settings.getOrSetProperty("level-type", "DEFAULT");
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

        this.setBuildLimit(this.settings.getOrSetIntProperty("max-build-height", 256));
        this.setBuildLimit((this.getBuildLimit() + 8) / 16 * 16);
        this.setBuildLimit(MathHelper.clamp_int(this.getBuildLimit(), 64, 256));
        this.settings.setArbitraryProperty("max-build-height", Integer.valueOf(this.getBuildLimit()));
        logger.info("Preparing level \"" + this.getFolderName() + "\"");
        this.loadAllDimensions(this.getFolderName(), this.getFolderName(), var8, var16);
        long var11 = System.nanoTime() - var4;
        String var13 = String.format("%.3fs", new Object[] {Double.valueOf((double)var11 / 1.0E9D)});
        logger.info("Done (" + var13 + ")! For help, type \"help\" or \"?\"");

        if (this.settings.getOrSetBoolProperty("enable-query", false))
        {
            logger.info("Starting GS4 status listener");
            this.field_71342_m = new RConThreadQuery(this);
            this.field_71342_m.func_72602_a();
        }

        if (this.settings.getOrSetBoolProperty("enable-rcon", false))
        {
            logger.info("Starting remote control listener");
            this.field_71339_n = new RConThreadMain(this);
            this.field_71339_n.func_72602_a();
        }

        return true;
    }

    public boolean canStructuresSpawn()
    {
        return this.canSpawnStructures;
    }

    public EnumGameType getGameType()
    {
        return this.gameType;
    }

    /**
     * defaults to "1" for the dedicated server
     */
    public int getDifficulty()
    {
        return this.settings.getOrSetIntProperty("difficulty", 1);
    }

    /**
     * defaults to false
     */
    public boolean isHardcore()
    {
        return this.settings.getOrSetBoolProperty("hardcore", false);
    }

    /**
     * called on exit from the main run loop
     */
    protected void finalTick(CrashReport par1CrashReport)
    {
        while (this.isServerRunning())
        {
            this.executePendingCommands();

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

    /**
     * iterates the worldServers and adds their info also
     */
    public CrashReport addServerInfoToCrashReport(CrashReport par1CrashReport)
    {
        par1CrashReport = super.addServerInfoToCrashReport(par1CrashReport);
        par1CrashReport.addCrashSectionCallable("Type", new CallableType(this));
        return par1CrashReport;
    }

    /**
     * directly calls system.exit, instantly killing the program
     */
    protected void systemExitNow()
    {
        System.exit(0);
    }

    public void updateTimeLightAndEntities()
    {
        super.updateTimeLightAndEntities();
        this.executePendingCommands();
    }

    public boolean getAllowNether()
    {
        return this.settings.getOrSetBoolProperty("allow-nether", true);
    }

    public boolean allowSpawnMonsters()
    {
        return this.settings.getOrSetBoolProperty("spawn-monsters", true);
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        par1PlayerUsageSnooper.addData("whitelist_enabled", Boolean.valueOf(this.getDedicatedPlayerList().isWhiteListEnabled()));
        par1PlayerUsageSnooper.addData("whitelist_count", Integer.valueOf(this.getDedicatedPlayerList().getIPWhiteList().size()));
        super.addServerStatsToSnooper(par1PlayerUsageSnooper);
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    public boolean isSnooperEnabled()
    {
        return this.settings.getOrSetBoolProperty("snooper-enabled", true);
    }

    public void addPendingCommand(String par1Str, ICommandSender par2ICommandSender)
    {
        this.pendingCommandList.add(new ServerCommand(par1Str, par2ICommandSender));
    }

    public void executePendingCommands()
    {
        while (!this.pendingCommandList.isEmpty())
        {
            ServerCommand var1 = (ServerCommand)this.pendingCommandList.remove(0);
            this.getCommandManager().executeCommand(var1.sender, var1.command);
        }
    }

    public boolean isDedicatedServer()
    {
        return true;
    }

    public DedicatedPlayerList getDedicatedPlayerList()
    {
        return (DedicatedPlayerList)super.getConfigurationManager();
    }

    public NetworkListenThread getNetworkThread()
    {
        return this.networkThread;
    }

    public int getOrSetIntProperty(String par1Str, int par2)
    {
        return this.settings.getOrSetIntProperty(par1Str, par2);
    }

    public String getOrSetProperty(String par1Str, String par2Str)
    {
        return this.settings.getOrSetProperty(par1Str, par2Str);
    }

    public boolean getOrSetBoolProperty(String par1Str, boolean par2)
    {
        return this.settings.getOrSetBoolProperty(par1Str, par2);
    }

    public void setArbitraryProperty(String par1Str, Object par2Obj)
    {
        this.settings.setArbitraryProperty(par1Str, par2Obj);
    }

    public void saveSettingsToFile()
    {
        this.settings.saveSettingsToFile();
    }

    public String getSettingsFilePath()
    {
        File var1 = this.settings.getFile();
        return var1 != null ? var1.getAbsolutePath() : "No settings file";
    }

    public boolean getGuiEnabled()
    {
        return this.guiIsEnabled;
    }

    /**
     * does nothing on dedicated. on integrated, sets commandsAllowedForAll and gameType and allows external connections
     */
    public String shareToLAN(EnumGameType par1EnumGameType, boolean par2)
    {
        return "";
    }

    public ServerConfigurationManager getConfigurationManager()
    {
        return this.getDedicatedPlayerList();
    }
}
