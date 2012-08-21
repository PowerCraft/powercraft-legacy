package net.minecraft.server;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.src.AnvilSaveConverter;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.CallableIsServerModded;
import net.minecraft.src.CallablePlayers;
import net.minecraft.src.CallableServerProfiler;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.CommandBase;
import net.minecraft.src.ConvertProgressUpdater;
import net.minecraft.src.CrashReport;
import net.minecraft.src.DemoWorldServer;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.ICommandManager;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.IPlayerUsage;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.IUpdatePlayerListBox;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.NetworkListenThread;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet4UpdateTime;
import net.minecraft.src.PlayerUsageSnooper;
import net.minecraft.src.Profiler;
import net.minecraft.src.RConConsoleSource;
import net.minecraft.src.ReportedException;
import net.minecraft.src.ServerCommandManager;
import net.minecraft.src.ServerConfigurationManager;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.StringUtils;
import net.minecraft.src.ThreadServerApplication;
import net.minecraft.src.Vec3;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldManager;
import net.minecraft.src.WorldServer;
import net.minecraft.src.WorldServerMulti;
import net.minecraft.src.WorldSettings;
import net.minecraft.src.WorldType;

public abstract class MinecraftServer implements Runnable, IPlayerUsage, ICommandSender
{
    /** The logging system. */
    public static Logger logger = Logger.getLogger("Minecraft");

    /** Instance of Minecraft Server. */
    private static MinecraftServer mcServer = null;
    private final ISaveFormat anvilConverterForAnvilFile;

    /** The PlayerUsageSnooper instance. */
    private final PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("server", this);
    private final File anvilFile;

    /** List of names of players who are online. */
    private final List playersOnline = new ArrayList();
    private final ICommandManager commandManager;
    public final Profiler theProfiler = new Profiler();

    /** The server's hostname. */
    private String hostname;
    private int serverPort = -1;

    /** The server world instances. */
    public WorldServer[] theWorldServer;

    /** The ServerConfigurationManager instance. */
    private ServerConfigurationManager serverConfigManager;
    private boolean serverShouldContinueRunning = true;

    /** Indicates to other classes that the server is safely stopped. */
    private boolean serverStopped = false;

    /** incremented every tick */
    private int tickCounter = 0;

    /**
     * The task the server is currently working on(and will output on outputPercentRemaining).
     */
    public String currentTask;

    /** The percentage of the current task finished so far. */
    public int percentDone;

    /** True if the server is in online mode. */
    private boolean onlineMode;
    private boolean canAnimalsSpawn;
    private boolean canNPCsSpawn;

    /** Indicates whether PvP is active on the server or not. */
    private boolean pvpEnabled;

    /** Determines if flight is allowed or not. */
    private boolean allowFlight;

    /** The server MOTD string. */
    private String motd;

    /** Maximum build height. */
    private int buildLimit;
    private long lastSentPacketID;
    private long lastSentPacketSize;
    private long lastRecievedID;
    private long lastRecievedSize;
    public final long[] sentPacketCountArray = new long[100];
    public final long[] sentPacketSizeArray = new long[100];
    public final long[] recievedPacketCountArray = new long[100];
    public final long[] recievedPacketSizeArray = new long[100];
    public final long[] tickTimeArray = new long[100];

    /** stats are [dimension][tick%100] system.nanoTime is stored. */
    public long[][] timeOfLastDimenstionTick;
    private KeyPair serverKeyPair;

    /** Username of the server owner (for integrated servers) */
    private String serverOwner;
    private String folderName;
    private String worldName;
    private boolean isDemo;
    private boolean enableBonusChest;

    /**
     * if this is set, there is no need to save chunks or stop the server, because that is already being done.
     */
    private boolean worldIsBeingDeleted;
    private String texturePack = "";
    private boolean serverIsRunning = false;

    /**
     * set when the client is warned for "can'tKeepUp", only trigger again after 15 seconds
     */
    private long timeOfLastWarning;
    private String userMessage;
    private boolean startProfiling;

    public MinecraftServer(File par1File)
    {
        mcServer = this;
        this.anvilFile = par1File;
        this.commandManager = new ServerCommandManager();
        this.anvilConverterForAnvilFile = new AnvilSaveConverter(par1File);
    }

    /**
     * Initialises the server and starts it.
     */
    protected abstract boolean startServer() throws IOException;

    protected void convertMapIfNeeded(String par1Str)
    {
        if (this.getActiveAnvilConverter().isOldMapFormat(par1Str))
        {
            logger.info("Converting map!");
            this.setUserMessage("menu.convertingLevel");
            this.getActiveAnvilConverter().convertMapFormat(par1Str, new ConvertProgressUpdater(this));
        }
    }

    /**
     * typically menu.convertingLevel, menu.loadingLevel,  saving, or others
     */
    protected synchronized void setUserMessage(String par1Str)
    {
        this.userMessage = par1Str;
    }

    public synchronized String getUserMessage()
    {
        return this.userMessage;
    }

    protected void loadAllDimensions(String par1Str, String par2Str, long par3, WorldType par5WorldType)
    {
        this.convertMapIfNeeded(par1Str);
        this.setUserMessage("menu.loadingLevel");
        this.theWorldServer = new WorldServer[3];
        this.timeOfLastDimenstionTick = new long[this.theWorldServer.length][100];
        ISaveHandler var6 = this.anvilConverterForAnvilFile.getSaveLoader(par1Str, true);
        WorldInfo var8 = var6.loadWorldInfo();
        WorldSettings var7;

        if (var8 == null)
        {
            var7 = new WorldSettings(par3, this.getGameType(), this.canStructuresSpawn(), this.isHardcore(), par5WorldType);
        }
        else
        {
            var7 = new WorldSettings(var8);
        }

        if (this.enableBonusChest)
        {
            var7.enableBonusChest();
        }

        for (int var9 = 0; var9 < this.theWorldServer.length; ++var9)
        {
            byte var10 = 0;

            if (var9 == 1)
            {
                var10 = -1;
            }

            if (var9 == 2)
            {
                var10 = 1;
            }

            if (var9 == 0)
            {
                if (this.isDemo())
                {
                    this.theWorldServer[var9] = new DemoWorldServer(this, var6, par2Str, var10, this.theProfiler);
                }
                else
                {
                    this.theWorldServer[var9] = new WorldServer(this, var6, par2Str, var10, var7, this.theProfiler);
                }
            }
            else
            {
                this.theWorldServer[var9] = new WorldServerMulti(this, var6, par2Str, var10, var7, this.theWorldServer[0], this.theProfiler);
            }

            this.theWorldServer[var9].addWorldAccess(new WorldManager(this, this.theWorldServer[var9]));

            if (!this.isSinglePlayer())
            {
                this.theWorldServer[var9].getWorldInfo().setGameType(this.getGameType());
            }

            this.serverConfigManager.func_72364_a(this.theWorldServer);
        }

        this.setDifficultyForAllDimensions(this.getDifficulty());
        this.initialWorldChunkLoad();
    }

    protected void initialWorldChunkLoad()
    {
        short var1 = 196;
        long var2 = System.currentTimeMillis();
        this.setUserMessage("menu.generatingTerrain");

        for (int var4 = 0; var4 < 1; ++var4)
        {
            logger.info("Preparing start region for level " + var4);
            WorldServer var5 = this.theWorldServer[var4];
            ChunkCoordinates var6 = var5.getSpawnPoint();

            for (int var7 = -var1; var7 <= var1 && this.isServerRunning(); var7 += 16)
            {
                for (int var8 = -var1; var8 <= var1 && this.isServerRunning(); var8 += 16)
                {
                    long var9 = System.currentTimeMillis();

                    if (var9 < var2)
                    {
                        var2 = var9;
                    }

                    if (var9 > var2 + 1000L)
                    {
                        int var11 = (var1 * 2 + 1) * (var1 * 2 + 1);
                        int var12 = (var7 + var1) * (var1 * 2 + 1) + var8 + 1;
                        this.outputPercentRemaining("Preparing spawn area", var12 * 100 / var11);
                        var2 = var9;
                    }

                    var5.theChunkProviderServer.loadChunk(var6.posX + var7 >> 4, var6.posZ + var8 >> 4);

                    while (var5.updatingLighting() && this.isServerRunning())
                    {
                        ;
                    }
                }
            }
        }

        this.clearCurrentTask();
    }

    public abstract boolean canStructuresSpawn();

    public abstract EnumGameType getGameType();

    /**
     * defaults to "1" for the dedicated server
     */
    public abstract int getDifficulty();

    /**
     * defaults to false
     */
    public abstract boolean isHardcore();

    /**
     * Used to display a percent remaining given text and the percentage.
     */
    protected void outputPercentRemaining(String par1Str, int par2)
    {
        this.currentTask = par1Str;
        this.percentDone = par2;
        logger.info(par1Str + ": " + par2 + "%");
    }

    /**
     * Set current task to null and set its percentage to 0.
     */
    protected void clearCurrentTask()
    {
        this.currentTask = null;
        this.percentDone = 0;
    }

    /**
     * par1 indicates if a log message should be output
     */
    protected void saveAllDimensions(boolean par1)
    {
        if (!this.worldIsBeingDeleted)
        {
            WorldServer[] var2 = this.theWorldServer;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                WorldServer var5 = var2[var4];

                if (var5 != null)
                {
                    if (!par1)
                    {
                        logger.info("Saving chunks for level \'" + var5.getWorldInfo().getWorldName() + "\'/" + var5.provider.func_80007_l());
                    }

                    try
                    {
                        var5.saveAllChunks(true, (IProgressUpdate)null);
                    }
                    catch (MinecraftException var7)
                    {
                        logger.warning(var7.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Saves all necessary data as preparation for stopping the server.
     */
    public void stopServer()
    {
        if (!this.worldIsBeingDeleted)
        {
            logger.info("Stopping server");

            if (this.getNetworkThread() != null)
            {
                this.getNetworkThread().stopListening();
            }

            if (this.serverConfigManager != null)
            {
                logger.info("Saving players");
                this.serverConfigManager.saveAllPlayerData();
                this.serverConfigManager.removeAllPlayers();
            }

            logger.info("Saving worlds");
            this.saveAllDimensions(false);
            WorldServer[] var1 = this.theWorldServer;
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3)
            {
                WorldServer var4 = var1[var3];
                var4.flush();
            }

            if (this.usageSnooper != null && this.usageSnooper.isSnooperRunning())
            {
                this.usageSnooper.stopSnooper();
            }
        }
    }

    public String getHostname()
    {
        return this.hostname;
    }

    public void getHostName(String par1Str)
    {
        this.hostname = par1Str;
    }

    public boolean isServerRunning()
    {
        return this.serverShouldContinueRunning;
    }

    /**
     * sets serverRunning to false
     */
    public void setServerStopping()
    {
        this.serverShouldContinueRunning = false;
    }

    public void run()
    {
        try
        {
            if (this.startServer())
            {
                long var1 = System.currentTimeMillis();

                for (long var50 = 0L; this.serverShouldContinueRunning; this.serverIsRunning = true)
                {
                    long var5 = System.currentTimeMillis();
                    long var7 = var5 - var1;

                    if (var7 > 2000L && var1 - this.timeOfLastWarning >= 15000L)
                    {
                        logger.warning("Can\'t keep up! Did the system time change, or is the server overloaded?");
                        var7 = 2000L;
                        this.timeOfLastWarning = var1;
                    }

                    if (var7 < 0L)
                    {
                        logger.warning("Time ran backwards! Did the system time change?");
                        var7 = 0L;
                    }

                    var50 += var7;
                    var1 = var5;

                    if (this.theWorldServer[0].areAllPlayersAsleep())
                    {
                        this.tick();
                        var50 = 0L;
                    }
                    else
                    {
                        while (var50 > 50L)
                        {
                            var50 -= 50L;
                            this.tick();
                        }
                    }

                    Thread.sleep(1L);
                }
            }
            else
            {
                this.finalTick((CrashReport)null);
            }
        }
        catch (Throwable var48)
        {
            var48.printStackTrace();
            logger.log(Level.SEVERE, "Encountered an unexpected exception " + var48.getClass().getSimpleName(), var48);
            CrashReport var2 = null;

            if (var48 instanceof ReportedException)
            {
                var2 = this.addServerInfoToCrashReport(((ReportedException)var48).func_71575_a());
            }
            else
            {
                var2 = this.addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", var48));
            }

            File var3 = new File(new File(this.getDataDirectory(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

            if (var2.saveToFile(var3))
            {
                logger.severe("This crash report has been saved to: " + var3.getAbsolutePath());
            }
            else
            {
                logger.severe("We were unable to save this crash report to disk.");
            }

            this.finalTick(var2);
        }
        finally
        {
            try
            {
                this.stopServer();
                this.serverStopped = true;
            }
            catch (Throwable var46)
            {
                var46.printStackTrace();
            }
            finally
            {
                this.systemExitNow();
            }
        }
    }

    protected File getDataDirectory()
    {
        return new File(".");
    }

    /**
     * called on exit from the main run loop
     */
    protected void finalTick(CrashReport par1CrashReport) {}

    /**
     * directly calls system.exit, instantly killing the program
     */
    protected void systemExitNow() {}

    /**
     * main function called by run() every loop
     */
    public void tick()
    {
        long var1 = System.nanoTime();
        AxisAlignedBB.getAABBPool().cleanPool();
        Vec3.getVec3Pool().clear();
        ++this.tickCounter;

        if (this.startProfiling)
        {
            this.startProfiling = false;
            this.theProfiler.profilingEnabled = true;
            this.theProfiler.clearProfiling();
        }

        this.theProfiler.startSection("root");
        this.updateTimeLightAndEntities();

        if (this.tickCounter % 900 == 0)
        {
            this.theProfiler.startSection("save");
            this.serverConfigManager.saveAllPlayerData();
            this.saveAllDimensions(true);
            this.theProfiler.endSection();
        }

        this.theProfiler.startSection("tallying");
        this.tickTimeArray[this.tickCounter % 100] = System.nanoTime() - var1;
        this.sentPacketCountArray[this.tickCounter % 100] = Packet.sentID - this.lastSentPacketID;
        this.lastSentPacketID = Packet.sentID;
        this.sentPacketSizeArray[this.tickCounter % 100] = Packet.sentSize - this.lastSentPacketSize;
        this.lastSentPacketSize = Packet.sentSize;
        this.recievedPacketCountArray[this.tickCounter % 100] = Packet.recievedID - this.lastRecievedID;
        this.lastRecievedID = Packet.recievedID;
        this.recievedPacketSizeArray[this.tickCounter % 100] = Packet.recievedSize - this.lastRecievedSize;
        this.lastRecievedSize = Packet.recievedSize;
        this.theProfiler.endSection();
        this.theProfiler.startSection("snooper");

        if (!this.usageSnooper.isSnooperRunning() && this.tickCounter > 100)
        {
            this.usageSnooper.startSnooper();
        }

        if (this.tickCounter % 6000 == 0)
        {
            this.usageSnooper.addMemoryStatsToSnooper();
        }

        this.theProfiler.endSection();
        this.theProfiler.endSection();
    }

    public void updateTimeLightAndEntities()
    {
        this.theProfiler.startSection("levels");

        for (int var1 = 0; var1 < this.theWorldServer.length; ++var1)
        {
            long var2 = System.nanoTime();

            if (var1 == 0 || this.getAllowNether())
            {
                WorldServer var4 = this.theWorldServer[var1];
                this.theProfiler.startSection(var4.getWorldInfo().getWorldName());

                if (this.tickCounter % 20 == 0)
                {
                    this.theProfiler.startSection("timeSync");
                    this.serverConfigManager.sendPacketToAllPlayersInDimension(new Packet4UpdateTime(var4.getWorldTime()), var4.provider.worldType);
                    this.theProfiler.endSection();
                }

                this.theProfiler.startSection("tick");
                var4.tick();
                this.theProfiler.endStartSection("lights");

                while (true)
                {
                    if (!var4.updatingLighting())
                    {
                        this.theProfiler.endSection();
                        var4.updateEntities();
                        this.theProfiler.startSection("tracker");
                        var4.getEntityTracker().processOutstandingEntries();
                        this.theProfiler.endSection();
                        this.theProfiler.endSection();
                        break;
                    }
                }
            }

            this.timeOfLastDimenstionTick[var1][this.tickCounter % 100] = System.nanoTime() - var2;
        }

        this.theProfiler.endStartSection("connection");
        this.getNetworkThread().networkTick();
        this.theProfiler.endStartSection("players");
        this.serverConfigManager.sendPlayerInfoToAllPlayers();
        this.theProfiler.endStartSection("tickables");
        Iterator var5 = this.playersOnline.iterator();

        while (var5.hasNext())
        {
            IUpdatePlayerListBox var6 = (IUpdatePlayerListBox)var5.next();
            var6.func_73660_a();
        }

        this.theProfiler.endSection();
    }

    public boolean getAllowNether()
    {
        return true;
    }

    public void startServerThread()
    {
        (new ThreadServerApplication(this, "Server thread")).start();
    }

    /**
     * Returns a File object from the specified string.
     */
    public File getFile(String par1Str)
    {
        return new File(this.getDataDirectory(), par1Str);
    }

    public void logInfoMessage(String par1Str)
    {
        logger.info(par1Str);
    }

    public void logWarningMessage(String par1Str)
    {
        logger.warning(par1Str);
    }

    public WorldServer worldServerForDimension(int par1)
    {
        return par1 == -1 ? this.theWorldServer[1] : (par1 == 1 ? this.theWorldServer[2] : this.theWorldServer[0]);
    }

    public String getHostName()
    {
        return this.hostname;
    }

    /**
     * never used. Can not be called "getServerPort" is already taken
     */
    public int getMyServerPort()
    {
        return this.serverPort;
    }

    /**
     * minecraftServer.getMOTD is used in 2 places instead (it is a non-virtual function which returns the same thing)
     */
    public String getServerMOTD()
    {
        return this.motd;
    }

    public String getMinecraftVersion()
    {
        return "1.3.2";
    }

    public int getPlayerListSize()
    {
        return this.serverConfigManager.getPlayerListSize();
    }

    public int getMaxPlayers()
    {
        return this.serverConfigManager.getMaxPlayers();
    }

    public String[] getAllUsernames()
    {
        return this.serverConfigManager.getAllUsernames();
    }

    /**
     * rename this when a patch comes out which uses it
     */
    public String returnAnEmptyString()
    {
        return "";
    }

    public String executeCommand(String par1Str)
    {
        RConConsoleSource.consoleBuffer.clearChatBuffer();
        this.commandManager.executeCommand(RConConsoleSource.consoleBuffer, par1Str);
        return RConConsoleSource.consoleBuffer.getChatBuffer();
    }

    public boolean doLogInfoEvent()
    {
        return false;
    }

    public void logSevereEvent(String par1Str)
    {
        logger.log(Level.SEVERE, par1Str);
    }

    public void logInfoEvent(String par1Str)
    {
        if (this.doLogInfoEvent())
        {
            logger.log(Level.INFO, par1Str);
        }
    }

    public String getServerModName()
    {
        return "vanilla";
    }

    /**
     * iterates the worldServers and adds their info also
     */
    public CrashReport addServerInfoToCrashReport(CrashReport par1CrashReport)
    {
        par1CrashReport.addCrashSectionCallable("Is Modded", new CallableIsServerModded(this));
        par1CrashReport.addCrashSectionCallable("Profiler Position", new CallableServerProfiler(this));

        if (this.serverConfigManager != null)
        {
            par1CrashReport.addCrashSectionCallable("Player Count", new CallablePlayers(this));
        }

        if (this.theWorldServer != null)
        {
            WorldServer[] var2 = this.theWorldServer;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                WorldServer var5 = var2[var4];

                if (var5 != null)
                {
                    var5.addWorldInfoToCrashReport(par1CrashReport);
                }
            }
        }

        return par1CrashReport;
    }

    /**
     * if par2 begins with / then it searches for commands, otherwise it returns users
     */
    public List getPossibleCompletions(ICommandSender par1ICommandSender, String par2Str)
    {
        ArrayList var3 = new ArrayList();

        if (par2Str.startsWith("/"))
        {
            par2Str = par2Str.substring(1);
            boolean var10 = !par2Str.contains(" ");
            List var11 = this.commandManager.getPossibleCommands(par1ICommandSender, par2Str);

            if (var11 != null)
            {
                Iterator var12 = var11.iterator();

                while (var12.hasNext())
                {
                    String var13 = (String)var12.next();

                    if (var10)
                    {
                        var3.add("/" + var13);
                    }
                    else
                    {
                        var3.add(var13);
                    }
                }
            }

            return var3;
        }
        else
        {
            String[] var4 = par2Str.split(" ", -1);
            String var5 = var4[var4.length - 1];
            String[] var6 = this.serverConfigManager.getAllUsernames();
            int var7 = var6.length;

            for (int var8 = 0; var8 < var7; ++var8)
            {
                String var9 = var6[var8];

                if (CommandBase.doesStringStartWith(var5, var9))
                {
                    var3.add(var9);
                }
            }

            return var3;
        }
    }

    /**
     * Gets mcServer.
     */
    public static MinecraftServer getServer()
    {
        return mcServer;
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getCommandSenderName()
    {
        return "Server";
    }

    public void sendChatToPlayer(String par1Str)
    {
        logger.info(StringUtils.stripControlCodes(par1Str));
    }

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    public boolean canCommandSenderUseCommand(String par1Str)
    {
        return true;
    }

    /**
     * Translates and formats the given string key with the given arguments.
     */
    public String translateString(String par1Str, Object ... par2ArrayOfObj)
    {
        return StringTranslate.getInstance().translateKeyFormat(par1Str, par2ArrayOfObj);
    }

    public ICommandManager getCommandManager()
    {
        return this.commandManager;
    }

    /**
     * Gets KeyPair instanced in MinecraftServer.
     */
    public KeyPair getKeyPair()
    {
        return this.serverKeyPair;
    }

    /**
     * Gets serverPort.
     */
    public int getServerPort()
    {
        return this.serverPort;
    }

    public void setServerPort(int par1)
    {
        this.serverPort = par1;
    }

    /**
     * Returns the username of the server owner (for integrated servers)
     */
    public String getServerOwner()
    {
        return this.serverOwner;
    }

    /**
     * Sets the username of the owner of this server (in the case of an integrated server)
     */
    public void setServerOwner(String par1Str)
    {
        this.serverOwner = par1Str;
    }

    public boolean isSinglePlayer()
    {
        return this.serverOwner != null;
    }

    public String getFolderName()
    {
        return this.folderName;
    }

    public void setFolderName(String par1Str)
    {
        this.folderName = par1Str;
    }

    public void setWorldName(String par1Str)
    {
        this.worldName = par1Str;
    }

    public String getWorldName()
    {
        return this.worldName;
    }

    public void setKeyPair(KeyPair par1KeyPair)
    {
        this.serverKeyPair = par1KeyPair;
    }

    public void setDifficultyForAllDimensions(int par1)
    {
        for (int var2 = 0; var2 < this.theWorldServer.length; ++var2)
        {
            WorldServer var3 = this.theWorldServer[var2];

            if (var3 != null)
            {
                if (var3.getWorldInfo().isHardcoreModeEnabled())
                {
                    var3.difficultySetting = 3;
                    var3.setAllowedSpawnTypes(true, true);
                }
                else if (this.isSinglePlayer())
                {
                    var3.difficultySetting = par1;
                    var3.setAllowedSpawnTypes(var3.difficultySetting > 0, true);
                }
                else
                {
                    var3.difficultySetting = par1;
                    var3.setAllowedSpawnTypes(this.allowSpawnMonsters(), this.canAnimalsSpawn);
                }
            }
        }
    }

    protected boolean allowSpawnMonsters()
    {
        return true;
    }

    /**
     * Gets whether this is a demo or not.
     */
    public boolean isDemo()
    {
        return this.isDemo;
    }

    /**
     * Sets whether this is a demo or not.
     */
    public void setDemo(boolean par1)
    {
        this.isDemo = par1;
    }

    public void canCreateBonusChest(boolean par1)
    {
        this.enableBonusChest = par1;
    }

    public ISaveFormat getActiveAnvilConverter()
    {
        return this.anvilConverterForAnvilFile;
    }

    /**
     * WARNING : directly calls
     * getActiveAnvilConverter().deleteWorldDirectory(dimensionServerList[0].getSaveHandler().getSaveDirectoryName());
     */
    public void deleteWorldAndStopServer()
    {
        this.worldIsBeingDeleted = true;
        this.getActiveAnvilConverter().flushCache();

        for (int var1 = 0; var1 < this.theWorldServer.length; ++var1)
        {
            WorldServer var2 = this.theWorldServer[var1];

            if (var2 != null)
            {
                var2.flush();
            }
        }

        this.getActiveAnvilConverter().deleteWorldDirectory(this.theWorldServer[0].getSaveHandler().getSaveDirectoryName());
        this.setServerStopping();
    }

    public String getTexturePack()
    {
        return this.texturePack;
    }

    public void setTexturePack(String par1Str)
    {
        this.texturePack = par1Str;
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        par1PlayerUsageSnooper.addData("whitelist_enabled", Boolean.valueOf(false));
        par1PlayerUsageSnooper.addData("whitelist_count", Integer.valueOf(0));
        par1PlayerUsageSnooper.addData("players_current", Integer.valueOf(this.getPlayerListSize()));
        par1PlayerUsageSnooper.addData("players_max", Integer.valueOf(this.getMaxPlayers()));
        par1PlayerUsageSnooper.addData("players_seen", Integer.valueOf(this.serverConfigManager.getAvailablePlayerDat().length));
        par1PlayerUsageSnooper.addData("uses_auth", Boolean.valueOf(this.onlineMode));
        par1PlayerUsageSnooper.addData("gui_state", this.getGuiEnabled() ? "enabled" : "disabled");
        par1PlayerUsageSnooper.addData("avg_tick_ms", Integer.valueOf((int)(MathHelper.average(this.tickTimeArray) * 1.0E-6D)));
        par1PlayerUsageSnooper.addData("avg_sent_packet_count", Integer.valueOf((int)MathHelper.average(this.sentPacketCountArray)));
        par1PlayerUsageSnooper.addData("avg_sent_packet_size", Integer.valueOf((int)MathHelper.average(this.sentPacketSizeArray)));
        par1PlayerUsageSnooper.addData("avg_rec_packet_count", Integer.valueOf((int)MathHelper.average(this.recievedPacketCountArray)));
        par1PlayerUsageSnooper.addData("avg_rec_packet_size", Integer.valueOf((int)MathHelper.average(this.recievedPacketSizeArray)));
        int var2 = 0;

        for (int var3 = 0; var3 < this.theWorldServer.length; ++var3)
        {
            if (this.theWorldServer[var3] != null)
            {
                WorldServer var4 = this.theWorldServer[var3];
                WorldInfo var5 = var4.getWorldInfo();
                par1PlayerUsageSnooper.addData("world[" + var2 + "][dimension]", Integer.valueOf(var4.provider.worldType));
                par1PlayerUsageSnooper.addData("world[" + var2 + "][mode]", var5.getGameType());
                par1PlayerUsageSnooper.addData("world[" + var2 + "][difficulty]", Integer.valueOf(var4.difficultySetting));
                par1PlayerUsageSnooper.addData("world[" + var2 + "][hardcore]", Boolean.valueOf(var5.isHardcoreModeEnabled()));
                par1PlayerUsageSnooper.addData("world[" + var2 + "][generator_name]", var5.getTerrainType().getWorldTypeName());
                par1PlayerUsageSnooper.addData("world[" + var2 + "][generator_version]", Integer.valueOf(var5.getTerrainType().getGeneratorVersion()));
                par1PlayerUsageSnooper.addData("world[" + var2 + "][height]", Integer.valueOf(this.buildLimit));
                par1PlayerUsageSnooper.addData("world[" + var2 + "][chunks_loaded]", Integer.valueOf(var4.getChunkProvider().getLoadedChunkCount()));
                ++var2;
            }
        }

        par1PlayerUsageSnooper.addData("worlds", Integer.valueOf(var2));
    }

    public void addServerTypeToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        par1PlayerUsageSnooper.addData("singleplayer", Boolean.valueOf(this.isSinglePlayer()));
        par1PlayerUsageSnooper.addData("server_brand", this.getServerModName());
        par1PlayerUsageSnooper.addData("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
        par1PlayerUsageSnooper.addData("dedicated", Boolean.valueOf(this.isDedicatedServer()));
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    public boolean isSnooperEnabled()
    {
        return true;
    }

    /**
     * this is checked to be 16 on reception of the packet, and the packet is ignored otherwise
     */
    public int textureFlag()
    {
        return 16;
    }

    public abstract boolean isDedicatedServer();

    public boolean isServerInOnlineMode()
    {
        return this.onlineMode;
    }

    public void setOnlineMode(boolean par1)
    {
        this.onlineMode = par1;
    }

    public boolean getCanSpawnAnimals()
    {
        return this.canAnimalsSpawn;
    }

    public void setSpawnAnimals(boolean par1)
    {
        this.canAnimalsSpawn = par1;
    }

    public boolean getCanNPCsSpawn()
    {
        return this.canNPCsSpawn;
    }

    public void setSpawnNpcs(boolean par1)
    {
        this.canNPCsSpawn = par1;
    }

    public boolean isPVPEnabled()
    {
        return this.pvpEnabled;
    }

    public void setAllowPvp(boolean par1)
    {
        this.pvpEnabled = par1;
    }

    public boolean isFlightAllowed()
    {
        return this.allowFlight;
    }

    public void setAllowFlight(boolean par1)
    {
        this.allowFlight = par1;
    }

    public String getMOTD()
    {
        return this.motd;
    }

    public void setMOTD(String par1Str)
    {
        this.motd = par1Str;
    }

    public int getBuildLimit()
    {
        return this.buildLimit;
    }

    public void setBuildLimit(int par1)
    {
        this.buildLimit = par1;
    }

    public boolean isServerStopped()
    {
        return this.serverStopped;
    }

    public ServerConfigurationManager getConfigurationManager()
    {
        return this.serverConfigManager;
    }

    public void setConfigurationManager(ServerConfigurationManager par1ServerConfigurationManager)
    {
        this.serverConfigManager = par1ServerConfigurationManager;
    }

    /**
     * sets the game type for all dimensions
     */
    public void setGameType(EnumGameType par1EnumGameType)
    {
        for (int var2 = 0; var2 < this.theWorldServer.length; ++var2)
        {
            getServer().theWorldServer[var2].getWorldInfo().setGameType(par1EnumGameType);
        }
    }

    public abstract NetworkListenThread getNetworkThread();

    public boolean serverIsInRunLoop()
    {
        return this.serverIsRunning;
    }

    public boolean getGuiEnabled()
    {
        return false;
    }

    /**
     * does nothing on dedicated. on integrated, sets commandsAllowedForAll and gameType and allows external connections
     */
    public abstract String shareToLAN(EnumGameType var1, boolean var2);

    public int getTickCounter()
    {
        return this.tickCounter;
    }

    public void enableProfiling()
    {
        this.startProfiling = true;
    }

    public PlayerUsageSnooper func_80003_ah()
    {
        return this.usageSnooper;
    }

    public static ServerConfigurationManager func_71196_a(MinecraftServer par0MinecraftServer)
    {
        return par0MinecraftServer.serverConfigManager;
    }
}
