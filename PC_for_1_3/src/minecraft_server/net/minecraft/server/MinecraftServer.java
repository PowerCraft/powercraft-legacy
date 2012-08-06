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
import net.minecraft.src.DedicatedServer;
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
import net.minecraft.src.StatList;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.StringUtils;
import net.minecraft.src.ThreadDedicatedServer;
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
    private final ISaveFormat field_71310_m;

    /** The PlayerUsageSnooper instance. */
    private final PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("server", this);
    private final File field_71308_o;

    /** List of names of players who are online. */
    private final List playersOnline = new ArrayList();
    private final ICommandManager field_71321_q;
    public final Profiler field_71304_b = new Profiler();

    /** The server's hostname */
    private String hostname;

    /** The server's port */
    private int serverPort = -1;

    /** The server world manager. */
    public WorldServer[] worldMngr;

    /** The ServerConfigurationManager instance. */
    private ServerConfigurationManager serverConfigManager;

    /**
     * Indicates whether the server is running or not. Set to false to initiate a shutdown.
     */
    private boolean serverRunning = true;

    /** Indicates to other classes that the server is safely stopped. */
    private boolean serverStopped = false;
    private int deathTime = 0;

    /**
     * the task the server is currently working on(and will output on ouputPercentRemaining)
     */
    public String currentTask;

    /** the percentage of the current task finished so far */
    public int percentDone;

    /** True if the server is in online mode. */
    private boolean onlineMode;

    /** True if server has animals turned on */
    private boolean spawnPeacefulMobs;
    private boolean field_71323_z;

    /** Indicates whether PvP is active on the server or not. */
    private boolean pvpEnabled;

    /** Determines if flight is Allowed or not */
    private boolean allowFlight;

    /** The server MOTD string. */
    private String motd;

    /** Maximum build height */
    private int buildLimit;
    private long field_71281_E;
    private long field_71282_F;
    private long field_71283_G;
    private long field_71291_H;
    public final long[] field_71300_f = new long[100];
    public final long[] field_71301_g = new long[100];
    public final long[] field_71313_h = new long[100];
    public final long[] field_71314_i = new long[100];
    public final long[] field_71311_j = new long[100];
    public long[][] field_71312_k;
    private KeyPair field_71292_I;

    /** Username of the server owner (for integrated servers) */
    private String serverOwner;
    private String field_71294_K;
    private boolean field_71288_M;
    private boolean field_71289_N;
    private boolean field_71290_O;
    private String field_71297_P = "";
    private boolean field_71296_Q = false;
    private long field_71299_R;
    private String field_71298_S;
    private boolean field_71295_T;

    public MinecraftServer(File par1File)
    {
        mcServer = this;
        this.field_71308_o = par1File;
        this.field_71321_q = new ServerCommandManager();
        this.field_71310_m = new AnvilSaveConverter(par1File);
    }

    /**
     * Initialises the server and starts it.
     */
    protected abstract boolean startServer() throws IOException;

    protected void func_71237_c(String par1Str)
    {
        if (this.func_71254_M().isOldMapFormat(par1Str))
        {
            logger.info("Converting map!");
            this.func_71192_d("menu.convertingLevel");
            this.func_71254_M().convertMapFormat(par1Str, new ConvertProgressUpdater(this));
        }
    }

    protected synchronized void func_71192_d(String par1Str)
    {
        this.field_71298_S = par1Str;
    }

    protected void func_71247_a(String par1Str, String par2Str, long par3, WorldType par5WorldType)
    {
        this.func_71237_c(par1Str);
        this.func_71192_d("menu.loadingLevel");
        this.worldMngr = new WorldServer[3];
        this.field_71312_k = new long[this.worldMngr.length][100];
        ISaveHandler var6 = this.field_71310_m.getSaveLoader(par1Str, true);
        WorldInfo var8 = var6.loadWorldInfo();
        WorldSettings var7;

        if (var8 == null)
        {
            var7 = new WorldSettings(par3, this.func_71265_f(), this.func_71225_e(), this.func_71199_h(), par5WorldType);
        }
        else
        {
            var7 = new WorldSettings(var8);
        }

        if (this.field_71289_N)
        {
            var7.enableBonusChest();
        }

        for (int var9 = 0; var9 < this.worldMngr.length; ++var9)
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
                if (this.func_71242_L())
                {
                    this.worldMngr[var9] = new DemoWorldServer(this, var6, par2Str, var10, this.field_71304_b);
                }
                else
                {
                    this.worldMngr[var9] = new WorldServer(this, var6, par2Str, var10, var7, this.field_71304_b);
                }
            }
            else
            {
                this.worldMngr[var9] = new WorldServerMulti(this, var6, par2Str, var10, var7, this.worldMngr[0], this.field_71304_b);
            }

            this.worldMngr[var9].addWorldAccess(new WorldManager(this, this.worldMngr[var9]));

            if (!this.func_71264_H())
            {
                this.worldMngr[var9].getWorldInfo().setGameType(this.func_71265_f());
            }

            this.serverConfigManager.setPlayerManager(this.worldMngr);
        }

        this.func_71226_c(this.func_71232_g());
        this.func_71222_d();
    }

    protected void func_71222_d()
    {
        short var1 = 196;
        long var2 = System.currentTimeMillis();
        this.func_71192_d("menu.generatingTerrain");

        for (int var4 = 0; var4 < 1; ++var4)
        {
            logger.info("Preparing start region for level " + var4);
            WorldServer var5 = this.worldMngr[var4];
            ChunkCoordinates var6 = var5.getSpawnPoint();

            for (int var7 = -var1; var7 <= var1 && this.func_71278_l(); var7 += 16)
            {
                for (int var8 = -var1; var8 <= var1 && this.func_71278_l(); var8 += 16)
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

                    while (var5.updatingLighting() && this.func_71278_l())
                    {
                        ;
                    }
                }
            }
        }

        this.clearCurrentTask();
    }

    public abstract boolean func_71225_e();

    public abstract EnumGameType func_71265_f();

    public abstract int func_71232_g();

    public abstract boolean func_71199_h();

    /**
     * used to display a percent remaining given text and the percentage
     */
    protected void outputPercentRemaining(String par1Str, int par2)
    {
        this.currentTask = par1Str;
        this.percentDone = par2;
        logger.info(par1Str + ": " + par2 + "%");
    }

    /**
     * set current task to null and set its percentage to 0
     */
    protected void clearCurrentTask()
    {
        this.currentTask = null;
        this.percentDone = 0;
    }

    protected void func_71267_a(boolean par1)
    {
        if (!this.field_71290_O)
        {
            WorldServer[] var2 = this.worldMngr;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                WorldServer var5 = var2[var4];

                if (var5 != null)
                {
                    if (!par1)
                    {
                        logger.info("Saving chunks for level \'" + var5.getWorldInfo().func_76065_j() + "\'/" + var5.provider);
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
        if (!this.field_71290_O)
        {
            logger.info("Stopping server");

            if (this.func_71212_ac() != null)
            {
                this.func_71212_ac().stopListening();
            }

            if (this.serverConfigManager != null)
            {
                logger.info("Saving players");
                this.serverConfigManager.savePlayerStates();
                this.serverConfigManager.func_72392_r();
            }

            logger.info("Saving worlds");
            this.func_71267_a(false);
            WorldServer[] var1 = this.worldMngr;
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3)
            {
                WorldServer var4 = var1[var3];
                var4.flush();
            }

            if (this.usageSnooper != null && this.usageSnooper.func_76468_d())
            {
                this.usageSnooper.func_76470_e();
            }
        }
    }

    public String func_71211_k()
    {
        return this.hostname;
    }

    public void func_71189_e(String par1Str)
    {
        this.hostname = par1Str;
    }

    public boolean func_71278_l()
    {
        return this.serverRunning;
    }

    /**
     * Sets the serverRunning variable to false, in order to get the server to shut down.
     */
    public void initiateShutdown()
    {
        this.serverRunning = false;
    }

    public void run()
    {
        try
        {
            if (this.startServer())
            {
                long var1 = System.currentTimeMillis();

                for (long var50 = 0L; this.serverRunning; this.field_71296_Q = true)
                {
                    long var5 = System.currentTimeMillis();
                    long var7 = var5 - var1;

                    if (var7 > 2000L && var1 - this.field_71299_R >= 15000L)
                    {
                        logger.warning("Can\'t keep up! Did the system time change, or is the server overloaded?");
                        var7 = 2000L;
                        this.field_71299_R = var1;
                    }

                    if (var7 < 0L)
                    {
                        logger.warning("Time ran backwards! Did the system time change?");
                        var7 = 0L;
                    }

                    var50 += var7;
                    var1 = var5;

                    if (this.worldMngr[0].func_73056_e())
                    {
                        this.func_71217_p();
                        var50 = 0L;
                    }
                    else
                    {
                        while (var50 > 50L)
                        {
                            var50 -= 50L;
                            this.func_71217_p();
                        }
                    }

                    Thread.sleep(1L);
                }
            }
            else
            {
                this.func_71228_a((CrashReport)null);
            }
        }
        catch (Throwable var48)
        {
            var48.printStackTrace();
            logger.log(Level.SEVERE, "Encountered an unexpected exception " + var48.getClass().getSimpleName(), var48);
            CrashReport var2 = null;

            if (var48 instanceof ReportedException)
            {
                var2 = this.func_71230_b(((ReportedException)var48).func_71575_a());
            }
            else
            {
                var2 = this.func_71230_b(new CrashReport("Exception in server tick loop", var48));
            }

            File var3 = new File(new File(this.func_71238_n(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

            if (var2.saveToFile(var3))
            {
                logger.severe("This crash report has been saved to: " + var3.getAbsolutePath());
            }
            else
            {
                logger.severe("We were unable to save this crash report to disk.");
            }

            this.func_71228_a(var2);
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
                this.func_71240_o();
            }
        }
    }

    protected File func_71238_n()
    {
        return new File(".");
    }

    protected void func_71228_a(CrashReport par1CrashReport) {}

    protected void func_71240_o() {}

    protected void func_71217_p()
    {
        long var1 = System.nanoTime();
        AxisAlignedBB.getAABBPool().cleanPool();
        Vec3.func_72437_a().func_72343_a();
        ++this.deathTime;

        if (this.field_71295_T)
        {
            this.field_71295_T = false;
            this.field_71304_b.profilingEnabled = true;
            this.field_71304_b.func_76317_a();
        }

        this.field_71304_b.startSection("root");
        this.func_71190_q();

        if (this.deathTime % 900 == 0)
        {
            this.field_71304_b.startSection("save");
            this.serverConfigManager.savePlayerStates();
            this.func_71267_a(true);
            this.field_71304_b.endSection();
        }

        this.field_71304_b.startSection("tallying");
        this.field_71311_j[this.deathTime % 100] = System.nanoTime() - var1;
        this.field_71300_f[this.deathTime % 100] = Packet.field_73290_p - this.field_71281_E;
        this.field_71281_E = Packet.field_73290_p;
        this.field_71301_g[this.deathTime % 100] = Packet.field_73289_q - this.field_71282_F;
        this.field_71282_F = Packet.field_73289_q;
        this.field_71313_h[this.deathTime % 100] = Packet.field_73292_n - this.field_71283_G;
        this.field_71283_G = Packet.field_73292_n;
        this.field_71314_i[this.deathTime % 100] = Packet.field_73293_o - this.field_71291_H;
        this.field_71291_H = Packet.field_73293_o;
        this.field_71304_b.endSection();
        this.field_71304_b.startSection("snooper");

        if (!this.usageSnooper.func_76468_d() && this.deathTime > 100)
        {
            this.usageSnooper.func_76463_a();
        }

        if (this.deathTime % 6000 == 0)
        {
            this.usageSnooper.func_76471_b();
        }

        this.field_71304_b.endSection();
        this.field_71304_b.endSection();
    }

    public void func_71190_q()
    {
        this.field_71304_b.startSection("levels");

        for (int var1 = 0; var1 < this.worldMngr.length; ++var1)
        {
            long var2 = System.nanoTime();

            if (var1 == 0 || this.func_71255_r())
            {
                WorldServer var4 = this.worldMngr[var1];
                this.field_71304_b.startSection(var4.getWorldInfo().func_76065_j());

                if (this.deathTime % 20 == 0)
                {
                    this.field_71304_b.startSection("timeSync");
                    this.serverConfigManager.sendPacketToAllPlayersInDimension(new Packet4UpdateTime(var4.getWorldTime()), var4.provider.worldType);
                    this.field_71304_b.endSection();
                }

                this.field_71304_b.startSection("tick");
                var4.tick();
                this.field_71304_b.endStartSection("lights");

                while (true)
                {
                    if (!var4.updatingLighting())
                    {
                        this.field_71304_b.endSection();

                        if (!var4.playerEntities.isEmpty())
                        {
                            var4.updateEntities();
                        }

                        this.field_71304_b.startSection("tracker");
                        var4.getEntityTracker().updateTrackedEntities();
                        this.field_71304_b.endSection();
                        this.field_71304_b.endSection();
                        break;
                    }
                }
            }

            this.field_71312_k[var1][this.deathTime % 100] = System.nanoTime() - var2;
        }

        this.field_71304_b.endStartSection("connection");
        this.func_71212_ac().handleNetworkListenThread();
        this.field_71304_b.endStartSection("players");
        this.serverConfigManager.onTick();
        this.field_71304_b.endStartSection("tickables");
        Iterator var5 = this.playersOnline.iterator();

        while (var5.hasNext())
        {
            IUpdatePlayerListBox var6 = (IUpdatePlayerListBox)var5.next();
            var6.update();
        }

        this.field_71304_b.endSection();
    }

    public boolean func_71255_r()
    {
        return true;
    }

    /**
     * Adds a player's name to the list of online players.
     */
    public void addToOnlinePlayerList(IUpdatePlayerListBox par1IUpdatePlayerListBox)
    {
        this.playersOnline.add(par1IUpdatePlayerListBox);
    }

    public static void main(String[] par0ArrayOfStr)
    {
        StatList.func_75919_a();

        try
        {
            boolean var1 = !GraphicsEnvironment.isHeadless();
            String var2 = null;
            String var3 = ".";
            String var4 = null;
            boolean var5 = false;
            boolean var6 = false;
            int var7 = -1;

            for (int var8 = 0; var8 < par0ArrayOfStr.length; ++var8)
            {
                String var9 = par0ArrayOfStr[var8];
                String var10 = var8 == par0ArrayOfStr.length - 1 ? null : par0ArrayOfStr[var8 + 1];
                boolean var11 = false;

                if (!var9.equals("nogui") && !var9.equals("--nogui"))
                {
                    if (var9.equals("--port") && var10 != null)
                    {
                        var11 = true;

                        try
                        {
                            var7 = Integer.parseInt(var10);
                        }
                        catch (NumberFormatException var13)
                        {
                            ;
                        }
                    }
                    else if (var9.equals("--singleplayer") && var10 != null)
                    {
                        var11 = true;
                        var2 = var10;
                    }
                    else if (var9.equals("--universe") && var10 != null)
                    {
                        var11 = true;
                        var3 = var10;
                    }
                    else if (var9.equals("--world") && var10 != null)
                    {
                        var11 = true;
                        var4 = var10;
                    }
                    else if (var9.equals("--demo"))
                    {
                        var5 = true;
                    }
                    else if (var9.equals("--bonusChest"))
                    {
                        var6 = true;
                    }
                }
                else
                {
                    var1 = false;
                }

                if (var11)
                {
                    ++var8;
                }
            }

            DedicatedServer var15 = new DedicatedServer(new File(var3));

            if (var2 != null)
            {
                var15.setServerOwner(var2);
            }

            if (var4 != null)
            {
                var15.func_71261_m(var4);
            }

            if (var7 >= 0)
            {
                var15.func_71208_b(var7);
            }

            if (var5)
            {
                var15.func_71204_b(true);
            }

            if (var6)
            {
                var15.func_71194_c(true);
            }

            if (var1)
            {
                var15.func_79001_aj();
            }

            var15.func_71256_s();
            Runtime.getRuntime().addShutdownHook(new ThreadDedicatedServer(var15));
        }
        catch (Exception var14)
        {
            logger.log(Level.SEVERE, "Failed to start the minecraft server", var14);
        }
    }

    public void func_71256_s()
    {
        (new ThreadServerApplication(this, "Server thread")).start();
    }

    /**
     * Returns a File object from the specified string.
     */
    public File getFile(String par1Str)
    {
        return new File(this.func_71238_n(), par1Str);
    }

    /**
     * Logs the message with a level of INFO.
     */
    public void log(String par1Str)
    {
        logger.info(par1Str);
    }

    /**
     * logs the warning same as: logger.warning(String);
     */
    public void logWarning(String par1Str)
    {
        logger.warning(par1Str);
    }

    /**
     * gets the worldServer by the given dimension
     */
    public WorldServer getWorldManager(int par1)
    {
        return par1 == -1 ? this.worldMngr[1] : (par1 == 1 ? this.worldMngr[2] : this.worldMngr[0]);
    }

    /**
     * Returns the server hostname
     */
    public String getHostname()
    {
        return this.hostname;
    }

    /**
     * Returns the server port
     */
    public int getPort()
    {
        return this.serverPort;
    }

    /**
     * Returns the server message of the day
     */
    public String getMotd()
    {
        return this.motd;
    }

    /**
     * Returns the server version string
     */
    public String getVersion()
    {
        return "1.3.1";
    }

    /**
     * Returns the number of players on the server
     */
    public int playersOnline()
    {
        return this.serverConfigManager.playersOnline();
    }

    /**
     * Returns the maximum number of players allowed on the server
     */
    public int getMaxPlayers()
    {
        return this.serverConfigManager.getMaxPlayers();
    }

    /**
     * Returns a list of usernames of all connected players
     */
    public String[] getPlayerNamesAsList()
    {
        return this.serverConfigManager.getPlayerNamesAsList();
    }

    public String getPlugin()
    {
        return "";
    }

    /**
     * Handle a command received by an RCon instance
     */
    public String handleRConCommand(String par1Str)
    {
        RConConsoleSource.instance.resetLog();
        this.field_71321_q.func_71556_a(RConConsoleSource.instance, par1Str);
        return RConConsoleSource.instance.getLogContents();
    }

    /**
     * Returns true if debugging is enabled, false otherwise
     */
    public boolean isDebuggingEnabled()
    {
        return false;
    }

    /**
     * Log severe error message
     */
    public void logSevere(String par1Str)
    {
        logger.log(Level.SEVERE, par1Str);
    }

    public void logIn(String par1Str)
    {
        if (this.isDebuggingEnabled())
        {
            logger.log(Level.INFO, par1Str);
        }
    }

    public String getServerModName()
    {
        return "vanilla";
    }

    public CrashReport func_71230_b(CrashReport par1CrashReport)
    {
        par1CrashReport.addCrashSectionCallable("Is Modded", new CallableIsServerModded(this));
        par1CrashReport.addCrashSectionCallable("Profiler Position", new CallableServerProfiler(this));

        if (this.serverConfigManager != null)
        {
            par1CrashReport.addCrashSectionCallable("Player Count", new CallablePlayers(this));
        }

        if (this.worldMngr != null)
        {
            WorldServer[] var2 = this.worldMngr;
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

    public List func_71248_a(ICommandSender par1ICommandSender, String par2Str)
    {
        ArrayList var3 = new ArrayList();

        if (par2Str.startsWith("/"))
        {
            par2Str = par2Str.substring(1);
            boolean var10 = !par2Str.contains(" ");
            List var11 = this.field_71321_q.func_71558_b(par1ICommandSender, par2Str);

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
            String[] var6 = this.serverConfigManager.getPlayerNamesAsList();
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

    public void func_70006_a(String par1Str)
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

    public ICommandManager func_71187_D()
    {
        return this.field_71321_q;
    }

    /**
     * Gets KeyPair instanced in MinecraftServer.
     */
    public KeyPair getKeyPair()
    {
        return this.field_71292_I;
    }

    /**
     * Gets serverPort.
     */
    public int getServerPort()
    {
        return this.serverPort;
    }

    public void func_71208_b(int par1)
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

    public boolean func_71264_H()
    {
        return this.serverOwner != null;
    }

    public String func_71270_I()
    {
        return this.field_71294_K;
    }

    public void func_71261_m(String par1Str)
    {
        this.field_71294_K = par1Str;
    }

    public void func_71253_a(KeyPair par1KeyPair)
    {
        this.field_71292_I = par1KeyPair;
    }

    public void func_71226_c(int par1)
    {
        for (int var2 = 0; var2 < this.worldMngr.length; ++var2)
        {
            WorldServer var3 = this.worldMngr[var2];

            if (var3 != null)
            {
                if (var3.getWorldInfo().isHardcoreModeEnabled())
                {
                    var3.difficultySetting = 3;
                    var3.setAllowedSpawnTypes(true, true);
                }
                else if (this.func_71264_H())
                {
                    var3.difficultySetting = par1;
                    var3.setAllowedSpawnTypes(var3.difficultySetting > 0, true);
                }
                else
                {
                    var3.difficultySetting = par1;
                    var3.setAllowedSpawnTypes(this.func_71193_K(), this.spawnPeacefulMobs);
                }
            }
        }
    }

    protected boolean func_71193_K()
    {
        return true;
    }

    public boolean func_71242_L()
    {
        return this.field_71288_M;
    }

    public void func_71204_b(boolean par1)
    {
        this.field_71288_M = par1;
    }

    public void func_71194_c(boolean par1)
    {
        this.field_71289_N = par1;
    }

    public ISaveFormat func_71254_M()
    {
        return this.field_71310_m;
    }

    public void func_71272_O()
    {
        this.field_71290_O = true;
        this.func_71254_M().func_75800_d();

        for (int var1 = 0; var1 < this.worldMngr.length; ++var1)
        {
            WorldServer var2 = this.worldMngr[var1];

            if (var2 != null)
            {
                var2.flush();
            }
        }

        this.func_71254_M().func_75802_e(this.worldMngr[0].getSaveHandler().func_75760_g());
        this.initiateShutdown();
    }

    public String func_71202_P()
    {
        return this.field_71297_P;
    }

    public void func_71269_o(String par1Str)
    {
        this.field_71297_P = par1Str;
    }

    public void func_70000_a(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        par1PlayerUsageSnooper.addData("whitelist_enabled", Boolean.valueOf(false));
        par1PlayerUsageSnooper.addData("whitelist_count", Integer.valueOf(0));
        par1PlayerUsageSnooper.addData("players_current", Integer.valueOf(this.playersOnline()));
        par1PlayerUsageSnooper.addData("players_max", Integer.valueOf(this.getMaxPlayers()));
        par1PlayerUsageSnooper.addData("players_seen", Integer.valueOf(this.serverConfigManager.func_72373_m().length));
        par1PlayerUsageSnooper.addData("uses_auth", Boolean.valueOf(this.onlineMode));
        par1PlayerUsageSnooper.addData("gui_state", this.func_71279_ae() ? "enabled" : "disabled");
        par1PlayerUsageSnooper.addData("avg_tick_ms", Integer.valueOf((int)(MathHelper.func_76127_a(this.field_71311_j) * 1.0E-6D)));
        par1PlayerUsageSnooper.addData("avg_sent_packet_count", Integer.valueOf((int)MathHelper.func_76127_a(this.field_71300_f)));
        par1PlayerUsageSnooper.addData("avg_sent_packet_size", Integer.valueOf((int)MathHelper.func_76127_a(this.field_71301_g)));
        par1PlayerUsageSnooper.addData("avg_rec_packet_count", Integer.valueOf((int)MathHelper.func_76127_a(this.field_71313_h)));
        par1PlayerUsageSnooper.addData("avg_rec_packet_size", Integer.valueOf((int)MathHelper.func_76127_a(this.field_71314_i)));
        int var2 = 0;

        for (int var3 = 0; var3 < this.worldMngr.length; ++var3)
        {
            if (this.worldMngr[var3] != null)
            {
                WorldServer var4 = this.worldMngr[var3];
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

    public void func_70001_b(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        par1PlayerUsageSnooper.addData("singleplayer", Boolean.valueOf(this.func_71264_H()));
        par1PlayerUsageSnooper.addData("server_brand", this.getServerModName());
        par1PlayerUsageSnooper.addData("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
        par1PlayerUsageSnooper.addData("dedicated", Boolean.valueOf(this.func_71262_S()));
    }

    public boolean func_70002_Q()
    {
        return true;
    }

    public int func_71227_R()
    {
        return 16;
    }

    public abstract boolean func_71262_S();

    public boolean func_71266_T()
    {
        return this.onlineMode;
    }

    public void func_71229_d(boolean par1)
    {
        this.onlineMode = par1;
    }

    public boolean func_71268_U()
    {
        return this.spawnPeacefulMobs;
    }

    public void func_71251_e(boolean par1)
    {
        this.spawnPeacefulMobs = par1;
    }

    public boolean func_71220_V()
    {
        return this.field_71323_z;
    }

    public void func_71257_f(boolean par1)
    {
        this.field_71323_z = par1;
    }

    public boolean func_71219_W()
    {
        return this.pvpEnabled;
    }

    public void func_71188_g(boolean par1)
    {
        this.pvpEnabled = par1;
    }

    public boolean func_71231_X()
    {
        return this.allowFlight;
    }

    public void func_71245_h(boolean par1)
    {
        this.allowFlight = par1;
    }

    public String func_71273_Y()
    {
        return this.motd;
    }

    public void func_71205_p(String par1Str)
    {
        this.motd = par1Str;
    }

    public int func_71207_Z()
    {
        return this.buildLimit;
    }

    public void func_71191_d(int par1)
    {
        this.buildLimit = par1;
    }

    public boolean func_71241_aa()
    {
        return this.serverStopped;
    }

    public ServerConfigurationManager func_71203_ab()
    {
        return this.serverConfigManager;
    }

    public void func_71210_a(ServerConfigurationManager par1ServerConfigurationManager)
    {
        this.serverConfigManager = par1ServerConfigurationManager;
    }

    public void func_71235_a(EnumGameType par1EnumGameType)
    {
        for (int var2 = 0; var2 < this.worldMngr.length; ++var2)
        {
            getServer().worldMngr[var2].getWorldInfo().setGameType(par1EnumGameType);
        }
    }

    public abstract NetworkListenThread func_71212_ac();

    public boolean func_71279_ae()
    {
        return false;
    }

    public abstract String func_71206_a(EnumGameType var1, boolean var2);

    public int func_71259_af()
    {
        return this.deathTime;
    }

    public void func_71223_ag()
    {
        this.field_71295_T = true;
    }

    public static ServerConfigurationManager func_71196_a(MinecraftServer par0MinecraftServer)
    {
        return par0MinecraftServer.serverConfigManager;
    }
}
