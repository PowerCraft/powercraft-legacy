package net.minecraft.src;

import java.io.File;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class IntegratedServer extends MinecraftServer
{
    /** The Minecraft instance. */
    private final Minecraft mc;
    private final WorldSettings field_71350_m;
    private IntegratedServerListenThread field_71347_n;
    private boolean field_71348_o = false;
    private boolean isPublic;
    private ThreadLanServerPing field_71345_q;

    public IntegratedServer(Minecraft par1Minecraft, String par2Str, String par3Str, WorldSettings par4WorldSettings)
    {
        super(new File(Minecraft.getMinecraftDir(), "saves"));
        this.setServerOwner(par1Minecraft.session.username);
        this.setFolderName(par2Str);
        this.setWorldName(par3Str);
        this.setDemo(par1Minecraft.isDemo());
        this.canCreateBonusChest(par4WorldSettings.isBonusChestEnabled());
        this.setBuildLimit(256);
        this.setConfigurationManager(new IntegratedPlayerList(this));
        this.mc = par1Minecraft;
        this.field_71350_m = par4WorldSettings;
        ModLoader.registerCommands(this);

        try
        {
            this.field_71347_n = new IntegratedServerListenThread(this);
        }
        catch (IOException var6)
        {
            throw new Error();
        }
    }

    protected void loadAllDimensions(String par1Str, String par2Str, long par3, WorldType par5WorldType)
    {
        this.convertMapIfNeeded(par1Str);
        this.theWorldServer = new WorldServer[3];
        this.timeOfLastDimenstionTick = new long[this.theWorldServer.length][100];
        ISaveHandler var6 = this.getActiveAnvilConverter().getSaveLoader(par1Str, true);

        for (int var7 = 0; var7 < this.theWorldServer.length; ++var7)
        {
            byte var8 = 0;

            if (var7 == 1)
            {
                var8 = -1;
            }

            if (var7 == 2)
            {
                var8 = 1;
            }

            if (var7 == 0)
            {
                if (this.isDemo())
                {
                    this.theWorldServer[var7] = new DemoWorldServer(this, var6, par2Str, var8, this.theProfiler);
                }
                else
                {
                    this.theWorldServer[var7] = new WorldServer(this, var6, par2Str, var8, this.field_71350_m, this.theProfiler);
                }
            }
            else
            {
                this.theWorldServer[var7] = new WorldServerMulti(this, var6, par2Str, var8, this.field_71350_m, this.theWorldServer[0], this.theProfiler);
            }

            this.theWorldServer[var7].addWorldAccess(new WorldManager(this, this.theWorldServer[var7]));
            this.getConfigurationManager().func_72364_a(this.theWorldServer);
        }

        this.setDifficultyForAllDimensions(this.getDifficulty());
        this.initialWorldChunkLoad();
    }

    /**
     * Initialises the server and starts it.
     */
    protected boolean startServer() throws IOException
    {
        logger.info("Starting integrated minecraft server version 1.3.2");
        this.setOnlineMode(false);
        this.setSpawnAnimals(true);
        this.setSpawnNpcs(true);
        this.setAllowPvp(true);
        this.setAllowFlight(true);
        logger.info("Generating keypair");
        this.setKeyPair(CryptManager.createNewKeyPair());
        this.loadAllDimensions(this.getFolderName(), this.getWorldName(), this.field_71350_m.getSeed(), this.field_71350_m.getTerrainType());
        this.setMOTD(this.getServerOwner() + " - " + this.theWorldServer[0].getWorldInfo().getWorldName());
        return true;
    }

    /**
     * main function called by run() every loop
     */
    public void tick()
    {
        boolean var1 = this.field_71348_o;
        this.field_71348_o = this.field_71347_n.func_71752_f();

        if (!var1 && this.field_71348_o)
        {
            logger.info("Saving and pausing game...");
            this.getConfigurationManager().saveAllPlayerData();
            this.saveAllDimensions(false);
        }

        if (!this.field_71348_o)
        {
            super.tick();
        }
    }

    public boolean canStructuresSpawn()
    {
        return false;
    }

    public EnumGameType getGameType()
    {
        return this.field_71350_m.getGameType();
    }

    /**
     * defaults to "1" for the dedicated server
     */
    public int getDifficulty()
    {
        return this.mc.gameSettings.difficulty;
    }

    /**
     * defaults to false
     */
    public boolean isHardcore()
    {
        return this.field_71350_m.getHardcoreEnabled();
    }

    protected File getDataDirectory()
    {
        return this.mc.mcDataDir;
    }

    public boolean isDedicatedServer()
    {
        return false;
    }

    public IntegratedServerListenThread func_71343_a()
    {
        return this.field_71347_n;
    }

    /**
     * called on exit from the main run loop
     */
    protected void finalTick(CrashReport par1CrashReport)
    {
        this.mc.crashed(par1CrashReport);
    }

    /**
     * iterates the worldServers and adds their info also
     */
    public CrashReport addServerInfoToCrashReport(CrashReport par1CrashReport)
    {
        par1CrashReport = super.addServerInfoToCrashReport(par1CrashReport);
        par1CrashReport.addCrashSectionCallable("Type", new CallableType3(this));
        par1CrashReport.addCrashSectionCallable("Is Modded", new CallableIsModded(this));
        return par1CrashReport;
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        super.addServerStatsToSnooper(par1PlayerUsageSnooper);
        par1PlayerUsageSnooper.addData("snooper_partner", this.mc.getPlayerUsageSnooper().func_80006_f());
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    public boolean isSnooperEnabled()
    {
        return Minecraft.getMinecraft().isSnooperEnabled();
    }

    /**
     * does nothing on dedicated. on integrated, sets commandsAllowedForAll and gameType and allows external connections
     */
    public String shareToLAN(EnumGameType par1EnumGameType, boolean par2)
    {
        try
        {
            String var3 = this.field_71347_n.func_71755_c();
            System.out.println("Started on " + var3);
            this.isPublic = true;
            this.field_71345_q = new ThreadLanServerPing(this.getMOTD(), var3);
            this.field_71345_q.start();
            this.getConfigurationManager().setGameType(par1EnumGameType);
            this.getConfigurationManager().setCommandsAllowedForAll(par2);
            return var3;
        }
        catch (IOException var4)
        {
            return null;
        }
    }

    /**
     * Saves all necessary data as preparation for stopping the server.
     */
    public void stopServer()
    {
        super.stopServer();

        if (this.field_71345_q != null)
        {
            this.field_71345_q.interrupt();
            this.field_71345_q = null;
        }
    }

    /**
     * sets serverRunning to false
     */
    public void setServerStopping()
    {
        super.setServerStopping();

        if (this.field_71345_q != null)
        {
            this.field_71345_q.interrupt();
            this.field_71345_q = null;
        }
    }

    public boolean func_71344_c()
    {
        return this.isPublic;
    }

    /**
     * sets the game type for all dimensions
     */
    public void setGameType(EnumGameType par1EnumGameType)
    {
        this.getConfigurationManager().setGameType(par1EnumGameType);
    }

    public NetworkListenThread getNetworkThread()
    {
        return this.func_71343_a();
    }
}
